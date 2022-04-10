package threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description threadpool
 * @author: chengyu
 * @date: 2022-04-10 10:41
 */
public class T02_ExecutorService {
    public static void main(String[] args) {
        ExecutorService e = Executors.newFixedThreadPool(1);

    }
}
