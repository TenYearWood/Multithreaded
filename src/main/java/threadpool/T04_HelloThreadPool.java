package threadpool;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ThreadPool线程池维护着两个集合：
 * a.线程的集合
 * b.任务的集合
 */
public class T04_HelloThreadPool {

    static class Task implements Runnable {
        private int i;

        public Task(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " Task" + i);
            try {
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return "Task{" +
                    "i=" + i +
                    '}';
        }
    }

    /**
     * new ThreadPoolExecutor的入参7个参数：
     * corePoolSize: 核心线程数
     * maximumPoolSize：最大线程数
     * keepAliveTime：生存时间
     * unit：生存时间的单位
     * workQueue：任务队列，
     * threadFactory：线程工厂，产生的到底是什么样的线程，线程名叫什么等等。
     * RejectedExecutionHandler：拒绝策略。
     * 线程池里最开始的时候是有一些核心线程的（corePoolSize），线程数不够了能扩展到最多是多少（maximumPoolSize），下例中
     * 核心线程+非核心线程最多4个。如果某个线程长时间不干活了，请你把它归还给OS，因为启的线程数太多的话，消耗的资源也非常大，
     * 所以没事空闲的时候应该归还给OS，60秒(keepAliveTime+unit)没活干就归还给OS。剩到核心线程的时候就不再归还了，核心线程永远活着。
     * 不过有个参数是指定核心线程参不参与空闲归还，一般不设置。
     * <p>
     * workQueue是各种各样的BlockingQueue,下例中使用ArrayBlockingQueue，且最多装4个任务。LinkedBlockingQueue无界队列，最大值是Integer.MAX_VALUE
     * 还有其他的例如TransferQueue，SynchronousQueue等，这些BlockingQueue的不同随之产生各种各样的线程池：
     * ArrayBlockingQueue：固定长度的线程池
     * SynchronousQueue：来一个就得马上执行，不然新任务加不进来
     * <p>
     * threadFactory：产生线程的方式。可以自定义ThreadFactory
     * 拒绝策略：线程池里刚开始为0个线程，来了一个任务启一个线程。第3个任务来的时候，核心线程正忙，放进任务队列。现在两个核心线程忙着处理任务，
     * 任务队列里存满了4个任务。假如第7个任务来的时候怎么办，启新线程去处理它，线程满了到达4个时，再来新任务时，所有线程都忙着，任务队列也满了，
     * 这时执行拒绝策略。拒绝策略可以自定义。
     * <p>
     * JDK默认提供了4种拒绝策略：
     * Abort: 抛异常
     * Discard：扔掉，不抛异常
     * DiscardOldest：扔掉排队时间最久的
     * CallerRuns：调用者处理任务
     */
    public static void main(String[] args) {
        ThreadPoolExecutor tpe = new ThreadPoolExecutor(2, 4, 60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(4),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardOldestPolicy()
                /*new ThreadPoolExecutor.CallerRunsPolicy()*/);
        /**
         * 任务队列4 + maximumPoolSize 4 = 8，总共就能处理8个任务
         * 每个任务的run里面System.in.read()都是阻塞的，一直占用线程
         */
        for (int i = 0; i < 8; i++) {
            tpe.execute(new Task(i));
        }

        System.out.println(tpe.getQueue()); //[Task{i=2}, Task{i=3}, Task{i=4}, Task{i=5}]  0、1被core线程处理了，2345扔到队列里了，6、7也被执行中

        tpe.execute(new Task(100));

        System.out.println(tpe.getQueue());  //[Task{i=3}, Task{i=4}, Task{i=5}, Task{i=100}]  由于拒绝策略，丢弃了task2，task100被加进来了

        tpe.shutdown();
    }

}
