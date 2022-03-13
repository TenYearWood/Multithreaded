package threadbasic;

import util.SleepHelper;

/**
 * interrupt() 与 interrupted()
 */
public class T04_Interrupt_and_interrupted {
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
           for(;;){
               if(Thread.interrupted()){
                   System.out.println("Thread is interrupted!");
                   System.out.println(Thread.interrupted());
               }
           }
        });

        t.start();

        SleepHelper.sleepSeconds(2);

        t.interrupt();

        //思考一下，如果在这里写
        t.interrupted();
        //输出的是哪个线程的中断状态  --- main线程
    }
}
