package disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 等待策略
 * ring buffer满了之后采用什么样的策略进行等待，可自定义，已经定义好的有8种：
 * 1.BlockingWaitStrategy: 通过线程阻塞的方式，等待生产者唤醒，被唤醒后，在循环检查依赖的sequence是否已经消费。
 * 2.BusySpinWaitStrategy：线程一直自旋等待，可能比较消耗cpu
 */
public class Main05_WaitStrategy {

    public static void main(String[] args) throws InterruptedException {

        //The factory for the event
        LongEventFactory factory = new LongEventFactory();

        //Specify the size of the ring buffer, must be power of 2
        int bufferSize = 1024;

        Disruptor<LongEvent> disruptor = new Disruptor<>(factory, bufferSize, Executors.defaultThreadFactory(),
                ProducerType.MULTI, new SleepingWaitStrategy());

        //Connect the handler
        disruptor.handleEventsWith(new LongEventHandler());

        //Start the Disruptor, Starts all threads running
        disruptor.start();

        //Get the ring buffer form the Disruptor to be used for publishing
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        //==================================================================
        final int threadCount = 50;
        CyclicBarrier barrier = new CyclicBarrier(threadCount);
        ExecutorService service = Executors.newCachedThreadPool();
        for (int i = 0; i < threadCount; i++) {
            final long threadNum = i;
            service.submit(() -> {
                System.out.printf("Thread %s ready to start !", threadNum);
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }

                for (int j = 0; j < 100; j++) {
                    ringBuffer.publishEvent(((event, sequence) -> {
                        event.set(threadNum);
                        System.out.println("生产了 " + threadNum);
                    }));
                }
            });
        }

        service.shutdown();
        TimeUnit.SECONDS.sleep(3);
        System.out.println(LongEventHandler.count);
    }
}
