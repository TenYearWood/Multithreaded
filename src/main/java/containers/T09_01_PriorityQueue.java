package containers;

import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * PriorityQueue
 * 内部实现的结构是一个二叉树。
 * DelayQueue本质上是用PriorityQueue实现的
 */
public class T09_01_PriorityQueue {
    public static void main(String[] args) {
        PriorityQueue<String> q = new PriorityQueue<>();
        q.add("c");
        q.add("e");
        q.add("a");
        q.add("d");
        q.add("z");

//        for (int i = q.size(); i > 0; i--) {
//            System.out.println(q.poll());
//        }

        Iterator<String> it = q.iterator();
        while(it.hasNext()){
            System.out.println(it.next());
        }
    }
}
