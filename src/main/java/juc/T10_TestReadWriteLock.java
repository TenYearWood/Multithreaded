package juc;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReadWriteLock读写锁
 * readLock共享锁
 * writeLock排它锁
 *
 * 比如公司的组织结构，经常会被访问，会读，但是很少更改。
 * 如果很多线程都来访问这个组织结构，有的是读线程，有的是写线程，
 * 但是加锁效率会很低，尤其是读线程特别多的时候。
 * 所以我们可以给读线程加锁，这把锁允许其他读线程可以继续读，但是不允许写线程访问，要等我读完你才能写。readLock
 * 但是写线程加锁后，读线程没法访问，要等我写完才能读。writeLock
 */
public class T10_TestReadWriteLock {
    static Lock lock = new ReentrantLock();
    private static int value;

    static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    static Lock readLock = readWriteLock.readLock();
    static Lock writeLock = readWriteLock.writeLock();

    public static void read(Lock lock){
        try{
            lock.lock();
            Thread.sleep(1000);
            System.out.println("read over!");
            //模拟读取操作
        }catch (InterruptedException e){
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void write(Lock lock, int v){
        try{
            lock.lock();
            Thread.sleep(1000);
            value = v;
            System.out.println("write over!");
            //模拟写操作
        } catch (InterruptedException e){
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        //Runnable readR = () -> read(lock);
        Runnable readR = () -> read(readLock);

        //Runnable writeR = () -> write(lock, new Random().nextInt());
        Runnable writeR = () -> write(writeLock, new Random().nextInt());

        for(int i=0; i<18; i++) new Thread(readR).start();
        for(int i=0; i<2; i++) new Thread(writeR).start();
    }
}
