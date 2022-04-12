package threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * newCachedThreadPool:
 * 它里面的BlockingQueue使用的是SynchronousQueue，容量为0
 * 它是来一个任务如果池子里有空闲的线程在KeepAliveTime时间内，就用这个原来的线程。
 * 如果所有线程都忙着，那就新启一个线程。本来新来一个任务coreSize满的话是要扔进任务队列里的，但是这里使用的是SynchronousQueue，
 * SynchronousQueue是一个手递手的，容量为0的queue
 * 一般情况下不会使用newCachedThreadPool，因为maximumPoolSize=Integer.MAX_VALUE，线程数达到这个接近无限级别的话，CPU都忙着切换线程了，不干别的了，崩溃。
 */
public class T08_CachedPool {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newCachedThreadPool();
        System.out.println(service);

        for (int i = 0; i < 2; i++) {
            service.execute(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName());
            });
        }

        System.out.println(service);

        TimeUnit.SECONDS.sleep(80); //cachedthreadPool里面的线程空闲状态默认60s后销毁，这里保险起见

        System.out.println(service);


    }
}
