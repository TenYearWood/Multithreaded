package juc;

import java.util.concurrent.CountDownLatch;

public class T06_TestCountDownLatch {
    public static void main(String[] args) {
        usingJoin();
        usingCountDownLatch();
    }

    private static void usingCountDownLatch(){
        Thread[] threads = new Thread[100];
        CountDownLatch latch = new CountDownLatch(threads.length);

        for(int i=0; i<threads.length; i++){
            threads[i] = new Thread(() -> {
                int result = 0;
                for(int j=0; j<10000; j++) result += j;
                latch.countDown();      //每个线程结束时调用latch.countDown
            });
        }

        for(Thread t : threads) t.start();

        try{
            /**
             * 在这闩住门，等着，上面计数100，每个线程运行结束的时候调countDown，计数减1
             * 什么时候闩打开呢，等计数变为0的时候
             */
            latch.await();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        System.out.println("end latch");
    }

    private static void usingJoin(){
        Thread[] threads = new Thread[100];

        for(int i=0; i<threads.length; i++){
            threads[i] = new Thread(() -> {
                int result = 0;
                for(int j=0; j<10000; j++) result += j;
            });
        }

        for(Thread t: threads) t.start();

        for(Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("end join");
    }
}
