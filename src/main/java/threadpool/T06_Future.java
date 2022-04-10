package threadpool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * Callable：类似Runnable接口，只不过有返回值
 * Future: Callable执行完了之后怎么才能拿到这个结果，会封装到Future里面。Future代表未来的一个结果
 * FutureTask: 既是一个Future，又是一个task，执行完的结果存在自己里
 *
 */
public class T06_Future {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Integer> task = new FutureTask<>(() -> {
            TimeUnit.MILLISECONDS.sleep(500);
            return 1000;
        });

        new Thread(task).start();

        System.out.println(task.get());     //阻塞
        System.out.println(task.isDone());

        /**
         * submit：异步的提交任务，提交完之后原线程继续运行。submit的返回值是Future，
         *
         */
        ExecutorService service = Executors.newFixedThreadPool(5);
        Future<Integer> f = service.submit(() -> {
            TimeUnit.MILLISECONDS.sleep(500);
            return 1;
        });
        System.out.println(f.get());
        System.out.println(f.isDone());

        service.shutdown();
    }
}
