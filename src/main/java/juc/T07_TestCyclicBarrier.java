package juc;

import java.sql.Time;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * 循环栅栏
 * parties：20的意思是满20人发车
 * barrierAction：满人之后的动作action。不传也是可以的，满人不做任何事
 */
public class T07_TestCyclicBarrier {
    public static void main(String[] args) {
        /**
         * 所有线程到20个之后，栅栏才会被放倒，才能继续。
         */
        //CyclicBarrier barrier = new CyclicBarrier(20);

        CyclicBarrier barrier = new CyclicBarrier(5, new Runnable() {
            @Override
            public void run() {
                System.out.println("满人，发车");
            }
        });

        Thread[] threads = new Thread[100];
        for(int i=0; i<threads.length; i++){
            threads[i] = new Thread(() -> {
                try {
                    /**
                     * 第1个线程来了之后在barrier前等着，第2个线程来了也等着...
                     * 什么时候barrier会被放倒？什么时候等够20个线程了
                     */
                    System.out.println(Thread.currentThread().getName() + " start");
                    barrier.await();
                    System.out.println(Thread.currentThread().getName() + " end");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }, "thread"+i);
        }

        for(Thread t : threads){
            t.start();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
