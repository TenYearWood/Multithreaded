package juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 没有LockSupport之前我们想让当前线程阻塞，必须是得有一把锁的，
 * 要锁定某个对象才可以，LockSupport是不需要的，想什么时候停就什么时候停。
 * 原来想要叫醒一个线程要用notify，但是被叫醒的线程往往是放在一个队列里面的，
 * 你想叫醒一个指定线程是很难的，现在只需要LockSupport.unpark指定线程就可以。
 *
 * unpark可以先于park之前调用，不像之前的wait和notify，在没有调用wait之前调用notify是无效的，不起作用的，必须先wait调notify才起作用
 * 但是unpark上来就可以先调，先调了之后即便后面调park也停不住了。
 */
public class T13_TestLockSupport {
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(i);
                if(i == 5){
                    LockSupport.park();
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();

        try {
            TimeUnit.SECONDS.sleep(8);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("after 8 seconds!");
        LockSupport.unpark(t);
    }
}
