package disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.concurrent.Executors;

public class Main01 {
    public static void main(String[] args) {
        //The factory for the event
        LongEventFactory factory = new LongEventFactory();

        //Specify the size of the ring buffer, must be power of 2
        int bufferSize = 1024;

        /**
         * Construct the Disruptor
         * Executors.defaultThreadFactory(): 这个线程工厂指的是当产生消费者的时候，它是在一个特定
         * 的线程里执行，这个线程是怎么产生的，通过ThreadFactory产生。ThreadFactory里产生一个线程，调用
         * LongEventHandler里的onEvent方法。
         */
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(factory, bufferSize, Executors.defaultThreadFactory());

        //Connect the handler
        disruptor.handleEventsWith(new LongEventHandler());

        //Start the Disruptor, Starts all threads running
        disruptor.start();

        //Get the ring buffer form the Disruptor to be used for publishing
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        //官方例子程序
        long sequence = ringBuffer.next();  //Grab the next sequence
        try {
            LongEvent event = ringBuffer.get(sequence); //Get the entry in the Disruptor
            //for the sequence
            event.set(8888L);   //Fill with data
        } finally {
            //这个位置上的消息被发布出来了，等待着被消费
            ringBuffer.publish(sequence);
        }

    }
}
