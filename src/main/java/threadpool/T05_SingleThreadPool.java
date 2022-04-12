package threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 类似Arrays对数组工具类的使用，Collections对集合
 * 对线程执行的工具类叫Executors
 *
 * Executors - 线程池的工厂
 * newSingleThreadExecutor：
 * 保证扔进去的任务是按顺序执行的
 * QA：
 * 1.为什么要有单线程的线程池？
 * 答：虽然是单线程但是有任务队列；生命周期的管理线程池来提供；
 */
public class T05_SingleThreadPool {
    public static void main(String[] args) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 5; i++) {
            int j = i;
            service.execute(() -> {
                System.out.println(j + " " + Thread.currentThread().getName());
            });
        }
    }
}
