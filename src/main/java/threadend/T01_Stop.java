package threadend;

import util.SleepHelper;

/**
 * 为什么不建议用stop方法？
 * 容易产生数据不一致的问题。
 * stop方法不管线程处于什么状态，线程在跑的过程中，直接干掉。
 * 比如线程正持有锁，stop二话不说释放所有的锁，并且不会做善后的工作
 * 上锁的过程一般都是要保证数据一致性，比放设置a=3，b=4，但是b=4还没来得及设置就被stop方法干掉了，非常容易产生数据不一致的问题。
 *
 */
public class T01_Stop {
    public static void main(String[] args) {
        Thread t = new Thread(()->{
            while (true){
                System.out.println("go on");
                SleepHelper.sleepSeconds(1);
            }
        });

        t.start();

        SleepHelper.sleepSeconds(5);

        t.stop();
    }
}
