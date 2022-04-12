package threadpool;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 假设你能提供一个服务
 * 这个服务查询各大电商网站同一类产品的价格并汇总展示
 */
public class T07_CompletableFuture {
    public static void main(String[] args) {
        long start, end;

        start = System.currentTimeMillis();

        CompletableFuture<Double> futureTM = CompletableFuture.supplyAsync(() -> priceOfTM());
        CompletableFuture<Double> futureTB = CompletableFuture.supplyAsync(() -> priceOfTB());
        CompletableFuture<Double> futureJD = CompletableFuture.supplyAsync(() -> priceOfJD());

        /**
         * CompletableFuture：各种任务的管理类
         * allOf: 等futureTM、futureTB、futureJD三个都完成才继续往下运行
         * anyOf: 只要其中有一个完成就可以
         */
        CompletableFuture.allOf(futureTM, futureTB, futureJD).join();
        /*
        * supplyAsync: 执行一个任务，异步的。
        * thenApply： 对结果执行，结果是Double类型的，这里转成String类型
        *
        CompletableFuture.supplyAsync(() -> priceOfTM())
                .thenApply(String::valueOf)
                .thenApply(str -> "price " + str)
                .thenAccept(System.out::println);*/

        end = System.currentTimeMillis();
        System.out.println("use completableFuture! " + (end - start));
    }

    private static double priceOfTM(){
        delay();
        return 1.00;
    }

    private static double priceOfTB(){
        delay();
        return 2.00;
    }

    private static double priceOfJD(){
        delay();
        return 3.00;
    }

    private static void delay(){
        int time = new Random().nextInt(500);
        try{
            TimeUnit.MILLISECONDS.sleep(time);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.printf("After %s sleep!", time);
        System.out.println();
    }
}
