package threadbasic;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class T01_HowToCreateThread {
    static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("Hello MyThread!");
        }
    }

    static class MyRun implements Runnable {
        public void run() {
            System.out.println("Hello MyRun!");
        }
    }

    /**
     * 带返回值的线程执行
     */
    static class MyCall implements Callable<String> {

        public String call() throws Exception {
            System.out.println("Hello MyCall!");
            return "success";
        }
    }

    /**
     * 启动线程的5种方式
     * @param args
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        new MyThread().start();
        new Thread(new MyRun()).start();
        new Thread(()-> System.out.println("Hello Lambda!")).start();

        /**
         * FutureTask，将来会产生返回值的任务
         *
         */
        FutureTask<String> task = new FutureTask<>(new MyCall());
        Thread t = new Thread(task);
        t.start();
        System.out.println(task.get());

        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(()-> System.out.println("Hello ThreadPool"));

        /**
         * Future异步的概念，扔给线程池一个任务，是带有返回值的任务，
         * 然后程序继续执行，不会等submit里面的Callable执行完，是异步的。
         * 当Callable执行完后，将返回值装在Future里面，将来有可能拿到的一个值。
         * future的get()方法是阻塞的，程序运行到这就等着，什么时候执行完
         * Callable，装在future，我拿到这个值为止，程序才能继续往下执行。
         */
        Future<String> f = service.submit(new MyCall());
        String s = f.get();
        System.out.println(s);

        service.shutdown();
    }
}
