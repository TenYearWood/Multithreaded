package atomicxxx;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * LongAdder为什么要比其他的两种方式要快？
 * 要加的值特别高，在LongAdder的内部会把这些值放到一个数组里，以1000个线程为例，
 * 分为4个数组，每个数组的初始值long都为0，将1000个线程分为4组，每组250个线程，对应前面的4个数组，
 * 250个线程锁在第1个数组，第二个250线程锁在第2个数组...每一个数组段进行递增，最后将所有数加在一起。
 * 所以在LongAdder并发特别高的情况下，尤其是在线程数量特别多的时候，就有优势了。这就是分段锁的概念。
 * 如果线程数量特别少，LongAdder没什么优势。
 * 分段锁也是CAS操作。
 */
public class T02_AtomicVsSyncVsLongAdder {
    static long count2 = 0L;
    static AtomicLong count1 = new AtomicLong(0L);
    static LongAdder count3 = new LongAdder();

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[1000];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int k = 0; k < 100000; k++) {
                    count1.incrementAndGet();
                }
            });
        }

        long start = System.currentTimeMillis();
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
        long end = System.currentTimeMillis();
        System.out.println("Atomic: " + count1.get() + " time: " + (end - start));

        //------------------------------------------------------------------------------
        final Object lock = new Object();

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int k = 0; k < 100000; k++) {
                    synchronized (lock) {
                        count2++;
                    }
                }
            });
        }

        start = System.currentTimeMillis();
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
        end = System.currentTimeMillis();
        System.out.println("Sync: " + count2 + " time: " + (end - start));

        //------------------------------------------------------------------------------
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int k = 0; k < 100000; k++) count3.increment();
            });
        }
        start = System.currentTimeMillis();
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
        end = System.currentTimeMillis();
        System.out.println("LongAdder: " + count3.longValue() + " time: " + (end - start));
    }
}
