package disruptor;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.ExceptionHandler;
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
 * 定义多个消费者
 * 多个消费者是位于不同的线程里面的。
 */
public class Main07_ExceptionHandler {
    public static void main(String[] args) throws InterruptedException {
        //The factory for the event
        LongEventFactory factory = new LongEventFactory();

        //Specify the size of the ring buffer, must be power of 2
        int bufferSize = 1024;

        //Construct the Disruptor
        Disruptor<LongEvent> disruptor = new Disruptor<>(factory, bufferSize, Executors.defaultThreadFactory(),
                ProducerType.MULTI, new SleepingWaitStrategy());

        //Connect the handlers
        EventHandler h1 = (event, sequence, end) -> {
            System.out.println(event);
            throw new Exception("消费者出现异常");
        };
        disruptor.handleEventsWith(h1);

        disruptor.handleExceptionsFor(h1).with(new ExceptionHandler() {
            @Override
            public void handleEventException(Throwable ex, long sequence, Object event) {
                ex.printStackTrace();
            }

            @Override
            public void handleOnStartException(Throwable ex) {
                System.out.println("Exception start to handle!");
            }

            @Override
            public void handleOnShutdownException(Throwable ex) {
                System.out.println("Exception shutdown to handle");
            }
        });

        //Start the Disruptor, Starts all threads running
        disruptor.start();

        //Get the ring buffer form the Disruptor to be used for publishing
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        //==================================================================
        final int threadCount = 1;
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

                for (int j = 0; j < 1; j++) {
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
