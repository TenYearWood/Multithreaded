package disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 下面程序中使用ProducerType.SINGLE指定单线程模式，但是例子使用却是多线程，
 * 因此会出问题，sequence没有加锁，多个线程同时去访问的话，可能会造成这个线程设置的值被另一个线程覆盖了。
 * 但是程序并不会报错。
 */
public class Main04_ProducerType {

    public static void main(String[] args) throws InterruptedException {

        //The factory for the event
        LongEventFactory factory = new LongEventFactory();

        //Specify the size of the ring buffer, must be power of 2
        int bufferSize = 1024;

        /**
         * Construct the Disruptor
         * ProducerType有两种模式：Producer.MULTI 和 Producer.SINGLE
         * 默认是MULTI，表示在多线程模式下产生sequence
         * 如果确认是单线程生产者，那么可以指定SINGLE，效率会提升。
         */
        Disruptor<LongEvent> disruptor = new Disruptor<>(factory, bufferSize, Executors.defaultThreadFactory(),
                ProducerType.SINGLE, new BlockingWaitStrategy());

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
                try{
                    barrier.await();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }catch (BrokenBarrierException e){
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
