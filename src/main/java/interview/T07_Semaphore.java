package interview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 曾经的面试题：（淘宝？）
 * 实现一个容器，提供两个方法，add，size
 * 写两个线程，线程1添加10个元素到容器中，线程2实现监控元素的个数，当个数到5个时，线程2给出提示并结束
 * <p>
 * 给lists添加volatile之后，t2能够接到通知，但是，t2线程的死循环很浪费cpu，如果不用死循环，该怎么做呢？
 * <p>
 * 这里使用wait和notify做到，wait会释放锁，而notify不会释放锁
 * 需要注意的是，运用这种方法，必须要保证t2先执行，也就是首先让t2监听才可以
 * <p>
 * 阅读下面的程序，并分析输出结果
 * 可以读到输出结果并不是size=5时t2退出，而是t1结束时t2才接收到通知而退出
 * 想想这是为什么？
 * <p>
 * notify之后，t1必须释放锁，t2退出后，也必须notify，通知t1继续执行
 * 整个通信过程比较繁琐
 * <p>
 * 使用Semaphore来写这个程序
 *
 * @author mashibing
 */
public class T07_Semaphore {

    List lists = Collections.synchronizedList(new ArrayList());

    public void add(Object o) {
        lists.add(o);
    }

    public int size() {
        return lists.size();
    }

    public static void main(String[] args) {
        T07_Semaphore c = new T07_Semaphore();

        Semaphore s = new Semaphore(1);

        Thread t2 = new Thread(() -> {
            System.out.println("t2启动");
            try {
                s.acquire();
                System.out.println("t2 结束");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                s.release();
            }

        }, "t2");


        new Thread(() -> {
            System.out.println("t1启动");
            try{
                s.acquire();
                t2.start();
                for (int i = 0; i < 10; i++) {
                    c.add(new Object());
                    System.out.println("add " + i);

                    if (c.size() == 5) {
                        s.release();
                        t2.join();
                        s.acquire();
                    }
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }finally {
                s.release();
            }

        }, "t1").start();

    }
}