package threadend;

import util.SleepHelper;

/**
 * interrupt是设定标志位
 *
 * 比volatile更优雅一些
 */
public class T04_Interrupt_and_NormalThread {
    public static void main(String[] args) {
        Thread t = new Thread(()->{
            while(!Thread.interrupted()){
                //sleep wait

                /**
                 * 如果使用volatile的话，这里如果wait()等方法阻塞住，volatile改变值，是无法进入while的下一个循环的，
                 * 是没有办法结束线程的。
                 *
                 * 但是如果使用interrupt的话，你只要在sleep或者wait里面处理InterruptException可以结束线程。
                 */
            }
            System.out.println("t1 end!");
        });

        t.start();

        SleepHelper.sleepSeconds(1);

        t.interrupt();
    }
}
