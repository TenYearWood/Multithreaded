package atomicxxx;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 这里就是一种CAS机制
 * incrementAndGet每次往回写的时候都要比较一下，看下是否原值是我期望的那个值。
 * incrementAndGet操作自带原子性：
 * unsafe.getAndAddInt->this.compareAndSwapInt->native boolean compareAndSwapInt
 * 得益于CPU在汇编级别上支持指令：cmpxchg，但是cmpxchg不是原子性的，最终实现：
 * lock cmpxchg
 * 所以你会发现，CAS在宏观上我们叫做自旋锁，乐观锁，但它在底层上的实现，微观上的实现实际上是一个悲观锁。
 */
public class T01_AtomicInteger {

    AtomicInteger count = new AtomicInteger(0);

    void m(){
        for(int i=0; i<10000; i++){
            count.incrementAndGet();
        }
    }

    public static void main(String[] args) {
        T01_AtomicInteger t = new T01_AtomicInteger();

        List<Thread> threads = new ArrayList<>();

        for(int i=0; i<100; i++){
            threads.add(new Thread(t::m, "thread-" + i));
        }

        threads.forEach(o -> o.start());
        threads.forEach(o -> {
            try {
                o.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println(t.count);
    }
}
