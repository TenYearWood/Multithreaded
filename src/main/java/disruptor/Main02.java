package disruptor;

import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.EventTranslatorThreeArg;
import com.lmax.disruptor.EventTranslatorTwoArg;
import com.lmax.disruptor.EventTranslatorVararg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.util.concurrent.Executors;

public class Main02 {
    public static void main(String[] args) {
        //The factory for the event
        LongEventFactory factory = new LongEventFactory();

        //Specify the size of the ring buffer, must be power of 2
        int bufferSize = 1024;

        /**
         * Construct the Disruptor
         */
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(factory, bufferSize, DaemonThreadFactory.INSTANCE);

        //Connect the handler
        disruptor.handleEventsWith(new LongEventHandler());

        //Start the Disruptor, Starts all threads running
        disruptor.start();

        //Get the ring buffer form the Disruptor to be used for publishing
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        //====================================================================
        EventTranslator<LongEvent> translator1 = new EventTranslator<LongEvent>() {
            @Override
            public void translateTo(LongEvent event, long sequence) {
                event.set(8888L);
            }
        };
        ringBuffer.publishEvent(translator1);

        //====================================================================
        EventTranslatorOneArg<LongEvent, Long> translator2 = new EventTranslatorOneArg<LongEvent, Long>() {
            @Override
            public void translateTo(LongEvent event, long sequence, Long l) {
                event.set(l);
            }
        };
        ringBuffer.publishEvent(translator2, 7777L);

        //====================================================================
        EventTranslatorTwoArg<LongEvent, Long, Long> translator3 = new EventTranslatorTwoArg<LongEvent, Long, Long>() {
            @Override
            public void translateTo(LongEvent event, long sequence, Long l1, Long l2) {
                event.set(l1 + l2);
            }
        };
        ringBuffer.publishEvent(translator3, 10000L, 10000L);

        //====================================================================
        EventTranslatorThreeArg<LongEvent, Long, Long, Long> translator4 = new EventTranslatorThreeArg<LongEvent, Long, Long, Long>() {
            @Override
            public void translateTo(LongEvent event, long sequence, Long l1, Long l2, Long l3) {
                event.set(l1 + l2 + l3);
            }
        };
        ringBuffer.publishEvent(translator4, 10000L, 10000L, 10000L);

        //====================================================================
        EventTranslatorVararg<LongEvent> translator5 = new EventTranslatorVararg<LongEvent>() {
            @Override
            public void translateTo(LongEvent event, long sequence, Object... args) {
                long result = 0;
                for(Object o : args){
                    long l = (Long) o;
                    result += l;
                }
                event.set(result);
            }
        };
        ringBuffer.publishEvent(translator5, 10000L, 10000L, 10000L, 10000L);

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
