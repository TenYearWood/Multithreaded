package threadbasic;

import util.SleepHelper;

/**
 * interrupt这个方法能否将一个正在争抢锁、正在竞争锁的状态打断？
 * 正在竞争锁的线程是否会抛异常？
 * ----不会
 * interrupt只是设标志位，正在争抢锁的过程，锁竞争的过程是不可能被interrupt干扰的。
 */
public class T07_Interrupt_and_sync {

    private static Object o = new Object();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            synchronized (o) {
                SleepHelper.sleepSeconds(10);       //sleep方法是不会释放锁的，在这10s内锁o归t1所有
            }
        });

        t1.start();

        SleepHelper.sleepSeconds(1);

        Thread t2 = new Thread(() -> {
            synchronized (o) {

            }
            System.out.println("t2 end!");
        });

        t2.start();

        SleepHelper.sleepSeconds(1);

        t2.interrupt();
    }
}
