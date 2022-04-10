package containers;

import util.SleepHelper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class T10_SynchronousQueue { //容量为0
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> strs = new SynchronousQueue<>();

        new Thread(()->{
            try {
                System.out.println(strs.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        strs.put("aaa"); //阻塞等待消费者消费
        //strs.add("aaa");  //报异常 queue full
        System.out.println(strs.size());
    }
}