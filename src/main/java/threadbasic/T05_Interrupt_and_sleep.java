package threadbasic;

import util.SleepHelper;

/**
 * interrupt 与 sleep wait join
 */
public class T05_Interrupt_and_sleep {
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.out.println("Thread is interrupted!");
                //false
                //一旦抛出InterruptedException之后，java会自动将标志位复位。
                System.out.println(Thread.currentThread().isInterrupted());
            }
        });

        t.start();

        SleepHelper.sleepSeconds(5);

        t.interrupt();
    }
}
