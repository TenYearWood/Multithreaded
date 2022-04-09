package containers;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * offer和add方法的区别：
 * 如果加不进去，add会抛异常。一般使用offer，给你一个返回值
 */
public class T06_ConcurrentQueue {
    public static void main(String[] args) {
        Queue<String> strs = new ConcurrentLinkedQueue<>();

        for(int i=0; i<10; i++) {
            strs.offer("a" + i);  //add
        }

        System.out.println(strs);

        System.out.println(strs.size());

        System.out.println(strs.poll());
        System.out.println(strs.size());

        System.out.println(strs.peek());
        System.out.println(strs.size());

        //双端队列Deque
    }
}
