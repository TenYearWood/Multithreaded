package containers;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class T08_ArrayBlockingQueue {

    static BlockingQueue<String> strs = new ArrayBlockingQueue<>(10); //有界队列，最多装10个元素

    static Random r = new Random();

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            strs.put("a" + i);
        }

//        strs.put("aaa"); //满了就会等待，程序阻塞,无限制的阻塞下去
//        strs.add("aaa");  //报异常，Queue full
//        strs.offer("aaa"); //不会报异常，但是加不进去；boolean带表是否加成功；这是add和offer的区别
//        strs.offer("aaa", 1, TimeUnit.SECONDS); //1s钟之后加不进去就加不进了；按时间段阻塞

        System.out.println(strs);
    }
}