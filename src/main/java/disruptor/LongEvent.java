package disruptor;

/**
 * 这个事件/消息里面装的什么值，可以装任意值
 */
public class LongEvent {
    private long value;

    public void set(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "LongEvent{" +
                "value=" + value +
                '}';
    }
}
