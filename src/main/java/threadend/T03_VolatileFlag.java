package threadend;

import util.SleepHelper;

/**
 * 使用volatile的这种方式
 *
 * 虽然可以让线程结束，但是不能精确控制循环了多少次
 */
public class T03_VolatileFlag {

    private static volatile boolean running = true;

    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            long i = 0L;
            while (running){
                //wait recv accept
                i++;
            }
            System.out.println("end and i = " + i); //3259107266 3305218089
        });

        t.start();

        SleepHelper.sleepSeconds(1);

        running = false;
    }
}
