package juc;

import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock还可以指定为公平锁
 * 公平锁的概念：
 * 很多线程都在等待一把锁，这些线程都位于队列里，当又有一个新的线程来的时候，它会不会检查队列里面
 * 有内容是公平锁的关键，如果是非公平锁，新线程上来就抢锁，它是有可能抢到的。如果锁是一个公平锁，
 * 新线程上来会先检查队列里原来有没有等待的线程，如果有，它就先进队列里等着，等别人先运行。
 *
 * ReentrantLock默认是非公平锁。
 */
public class T05_ReentrantLock5 extends Thread{
    private static ReentrantLock lock = new ReentrantLock(); //参数为true表示为公平锁，请对比输出结果

    public void run() {
        for (int i = 0; i < 100; i++) {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "获得锁");
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        T05_ReentrantLock5 rl = new T05_ReentrantLock5();
        Thread th1 = new Thread(rl);
        Thread th2 = new Thread(rl);
        th1.start();
        th2.start();
    }
}