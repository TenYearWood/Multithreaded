package threadbasic;

import util.SleepHelper;

/**
 * interrupt() 与 isInterrupted()
 * 设置标志位 + 查询标志位
 */
public class T03_Interrupt_and_isInterrupted {
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
           for(;;){
               if(Thread.currentThread().isInterrupted()){
                   System.out.println("Thread is interrupted!");
                   System.out.println(Thread.currentThread().isInterrupted());

                   /**
                    * 如何结束一个线程，这里就可以根据isInterrupted判断，如果为true，break结束循环，线程运行结束
                    * 是一种比较优雅的结束线程的方案
                    */
                   //break;
               }
           }
        });

        t.start();

        SleepHelper.sleepSeconds(2);

        t.interrupt();
    }
}
