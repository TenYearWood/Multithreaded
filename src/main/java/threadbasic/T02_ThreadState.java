package threadbasic;

import util.SleepHelper;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 关于线程状态的实验
 */
public class T02_ThreadState {
    public static void main(String[] args) throws InterruptedException {
        //===============================================
        Thread t1 = new Thread(() -> {
            System.out.println("2: " + Thread.currentThread().getState());
            for (int i = 0; i < 3; i++) {
                SleepHelper.sleepSeconds(1);
                System.out.print(i + " ");
            }
            System.out.println();
        });
        System.out.println("1: " + t1.getState());
        t1.start();
        t1.join();  //等待t1线程执行结束
        System.out.println("3: " + t1.getState());

        //=================================================
        Thread t2 = new Thread(() -> {
            try {
                LockSupport.park();     //让t2阻塞住，等待被别人叫醒
                System.out.println("t2 go on!");
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t2.start();
        TimeUnit.SECONDS.sleep(1);          //main主线程睡1秒，保证t2启动
        System.out.println("4：" + t2.getState());

        LockSupport.unpark(t2);
        TimeUnit.SECONDS.sleep(1);
        System.out.println("5: " + t2.getState());

        //==================================================
        final Object o = new Object();
        Thread t3 = new Thread(() -> {
            synchronized (o) {
                System.out.println("t3得到了锁o");
            }
        });

        new Thread(() -> {
            synchronized (o) {
                SleepHelper.sleepSeconds(5);
            }
        }).start();

        SleepHelper.sleepSeconds(1);

        t3.start();
        SleepHelper.sleepSeconds(1);
        System.out.println("6: " + t3.getState());

        //==================================================
        /**
         * lock.lock()也是申请锁，只不过使用的是JUC的锁，juc的锁一般使用cas实现
         * CAS，忙等待，不会进入blocked的状态，而是进入waiting状态
         *
         * 只有synchronized的锁，才会进入blocked状态
         */
        final Lock lock = new ReentrantLock();
        Thread t4 = new Thread(() -> {
            lock.lock();    //省略try finally
            System.out.println("t4得到了锁o");
            lock.unlock();
        });

        new Thread(() -> {
            lock.lock();
            SleepHelper.sleepSeconds(5);
            lock.unlock();
        }).start();

        SleepHelper.sleepSeconds(1);

        t4.start();
        SleepHelper.sleepSeconds(1);
        System.out.println("7: " + t4.getState());

        //==================================================
        Thread t5 = new Thread(()->{
            LockSupport.park();
        });

        t5.start();
        SleepHelper.sleepSeconds(1);
        System.out.println("8: " + t5.getState());
        LockSupport.unpark(t5);
    }
}
