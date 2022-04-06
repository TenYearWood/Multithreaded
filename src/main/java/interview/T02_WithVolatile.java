package interview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 曾经的面试题：(淘宝)
 * 实现一个容器，提供两个方法，add size
 * 写两个线程，线程1添加10个元素到容器中，线程2实现监控元素的个数，当个数到5个时，线程2给出提示并结束
 *
 * 分析下面程序，能完成这个功能吗？
 * volatile其实没有起作用，将t1线程里面的睡眠1s去掉后，运行，t2还是无法检测到size为5.
 * 为什么呢，因为这里volatile修饰的是一个引用，我们改的是引用里面的值，但是这个引用的值本身没变，所以加volatile不起作用。
 * 1.volatile如果你没有把握就不要用
 * 2.volatile尽量修饰简单的值，不要修饰引用值。volatile只是关注的是引用的值，引用指向的对象里面的内容改变了，它是观察不到的
 */
public class T02_WithVolatile {

    volatile List lists = Collections.synchronizedList(new ArrayList());

    public void add(Object o) {
        lists.add(o);
    }

    public int size(){
        return lists.size();
    }

    public static void main(String[] args) {
        T02_WithVolatile c = new T02_WithVolatile();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                c.add(new Object());
                System.out.println("add " + i);
            }
        }, "t1").start();

        new Thread(() -> {
            while (true){
                if(c.size() == 5){
                    break;
                }
            }
            System.out.println("t2 结束");
        }, "t2").start();
    }
}
