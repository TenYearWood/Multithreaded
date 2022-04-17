package disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * 需要一个EventFactory，怎么产生这些事件。
 * 为什么要用Event工厂，这里牵扯到效率问题：disruptor初始化的时候，会调用Event工厂，对ringBuffer进行内存的提前分配。
 * gc产生频率会降低。
 */
public class LongEventFactory implements EventFactory<LongEvent> {
    @Override
    public LongEvent newInstance() {
        return new LongEvent();
    }
}
