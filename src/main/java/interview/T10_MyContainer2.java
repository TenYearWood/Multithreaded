package interview;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * 面试题：写一个固定容量同步容器，拥有put和get方法，以及getCount方法，
 * 能够支持2个生产者线程以及10个消费者线程的阻塞调用
 *
 * 使用CAS实现
 * 没写完，写的不对，不知道怎么写...
 *
 * @author mashibing
 */
public class T10_MyContainer2<T> {
    final private LinkedList<T> lists = new LinkedList<>();
    private volatile int MAX = 10; //最多10个元素
    private volatile int count = 0;

    public void put(T t) {
        while(count < MAX){
            lists.add(t);
            ++count;
            break;
        }
    }

    public T get() {
        while(count > 0){
            T t = lists.removeFirst();
            count --;
            return t;
        }
        return null;
    }

    public static void main(String[] args) {
        T10_MyContainer2<String> c = new T10_MyContainer2<>();

    }
}