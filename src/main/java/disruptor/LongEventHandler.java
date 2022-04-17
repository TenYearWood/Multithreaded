package disruptor;


import com.lmax.disruptor.EventHandler;

/**
 * 拿到事件之后该怎样进行处理，消息的消费者。
 */
public class LongEventHandler implements EventHandler<LongEvent> {
    public static long count = 0;

    /**
     * @param event      要处理的是哪个消息
     * @param sequence   RingBuffer的序号
     * @param endOfBatch 是否为最后一个元素
     * @throws Exception
     */
    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
        count++;
        System.out.println("[" + Thread.currentThread().getName() + "] " + event + " 序号：" + sequence);
    }
}
