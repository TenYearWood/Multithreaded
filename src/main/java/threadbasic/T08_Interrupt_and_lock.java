package threadbasic;

import util.SleepHelper;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 前面我们知道interrupt是不能打算synchronized这种线程竞争锁的过程的，
 * 那么interrupt能否打断lock这种锁?
 * ----不能
 * 通过interrupt和synchronized，interrupt和lock这两个小实验，发现
 * 如果有一个线程持有一把锁，不释放，另外一个线程去申请这把锁的时候，中间过程是干扰不到的。
 *
 * 如果想要干扰到怎么做呢？见T09
 */
public class T08_Interrupt_and_lock {

    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            lock.lock();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
            System.out.println("t1 end!");
        });

        t1.start();

        SleepHelper.sleepSeconds(1);

        Thread t2 = new Thread(() -> {
            lock.lock();
            try {

            } finally {
                lock.unlock();
            }
            System.out.println("t2 end!");
        });

        t2.start();

        SleepHelper.sleepSeconds(1);

        t2.interrupt();
    }
}
