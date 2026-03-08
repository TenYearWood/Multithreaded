package interview;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 面试题：写一个固定容量同步容器，拥有put和get方法，以及getCount方法，
 * 能够支持2个生产者线程以及10个消费者线程的阻塞调用
 * <p>
 * 使用lock的condition的await/signalAll来实现
 * lock condition的本质：在synchronized的时候只有一个等待队列，lock.newCondition()了之后变成了多个等待队列。
 * condition的本质就是等待队列的个数。
 * producer = lock.newCondition()； 一个等待队列出来了，当调用producer.await()的时候，是说这个线程进入producer队列等待。
 * consumer = lock.newCondition()；另外一个等待队列，当调用consumer.await()的时候，是说线程进入consumer等待队列里等待。
 * 生产者叫醒消费者线程时，就可以只叫醒consumer队列里等待的线程了。反之也是。
 * <p>
 * condition的本质就是不同的等待队列。
 */
public class T09_MyContainer2<T> {
    final private LinkedList<T> lists = new LinkedList<>();
    final private int MAX = 10; //最多10个元素
    private int count = 0;

    private final Lock lock = new ReentrantLock();
    private final Condition producer = lock.newCondition();
    private final Condition consumer = lock.newCondition();

    public void put(T t) {
        try {
            lock.lock();
            while (count == MAX) { //想想为什么用while而不是用if？-- 因为一旦被叫醒之后还需要检查count是否=max? 小于max才可以add，否则就不对了。所以用while，叫醒之后还需要再次检查count的数量
                producer.await();
            }

            lists.add(t);
            ++count;
            consumer.signalAll();  //通知消费者线程进行消费
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public T get() {
        try {
            lock.lock();
            while (count == 0) {
                consumer.await();
            }
            T t = lists.removeFirst();
            count--;
            producer.signalAll(); //通知生产者进行生产
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return null;
    }

    public static void main(String[] args) {
        T09_MyContainer2<String> c = new T09_MyContainer2<>();
        //启动消费者线程
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    String name = c.get();
                    System.out.println("吃掉了" + name);
                }
            }, "consumer" + i).start();
        }

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //启动生产者线程
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                for (int j = 0; j < 25; j++) {
                    String name = "馒头" + j;
                    c.put(name);
                    System.out.println("生产了" + name);
                }
            }, "producer" + i).start();
        }
    }
}