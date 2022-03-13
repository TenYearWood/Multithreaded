package threadbasic;

import util.SleepHelper;

/**
 * interrupt 与 sleep wait join
 */
public class T06_Interrupt_and_wait {

    private static Object o = new Object();

    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            synchronized (o) {
                try {
                    o.wait();
                } catch (InterruptedException e) {
                    System.out.println("Thread is interrupted!");
                    System.out.println(Thread.currentThread().isInterrupted());
                }
            }
        });

        t.start();

        SleepHelper.sleepSeconds(5);

        t.interrupt();
    }
}
