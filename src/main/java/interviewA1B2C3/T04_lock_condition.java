package interviewA1B2C3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * notify和notifyAll没法叫醒某个线程或某一类线程
 * condition本质上是等待队列，现在使用两个condition，可以精确的指定哪个等待队列里的线程醒过来。
 *
 */
public class T04_lock_condition {
    public static void main(String[] args) {
        char[] aI = "1234567".toCharArray();
        char[] aC = "ABCDEFG".toCharArray();

        Lock lock = new ReentrantLock();
        Condition conditionT1 = lock.newCondition();
        Condition conditionT2 = lock.newCondition();

        new Thread(() -> {
            try {
                lock.lock();
                for (char c : aI) {
                    System.out.print(c);
                    conditionT2.signal();
                    conditionT1.await();
                }
                conditionT2.signal();
            }catch (InterruptedException e){
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "t1").start();

        new Thread(() -> {
            try {
                lock.lock();
                for (char c : aC) {
                    System.out.print(c);
                    conditionT1.signal();
                    conditionT2.await();
                }
                conditionT1.signal();
            }catch (Exception e){
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "t2").start();
    }
}
