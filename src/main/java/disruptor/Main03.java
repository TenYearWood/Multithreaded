package disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.io.IOException;
import java.util.concurrent.Executors;

/**
 * 将Disruptor例子程序改成lambda的写法，省略掉了LongEventFactory和LongEventHandler
 * 其实并没有什么变化，只是写法改动了一下而已，使用lambda写法省略掉了很多类。
 */
public class Main03 {
    public static void main(String[] args) throws IOException {
        //Specify the size of the ring buffer, must be power of 2
        int bufferSize = 1024;

        //Construct the Disruptor
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(LongEvent::new, bufferSize, DaemonThreadFactory.INSTANCE);

        //Connect the handler
        disruptor.handleEventsWith(((event, sequence, endOfBatch) -> System.out.println("Event：" + event)));

        //Start the Disruptor, Starts all threads running
        disruptor.start();

        //Get the ring buffer form the Disruptor to be used for publishing
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        ringBuffer.publishEvent(((event, sequence) -> event.set(10000L)));

        System.in.read();
    }
}
