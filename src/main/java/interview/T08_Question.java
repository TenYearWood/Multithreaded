package interview;

import java.util.concurrent.CountDownLatch;

/**
 * 要求用线程顺序打印A1B2C3....Z26
 *
 * 博客园：https://www.cnblogs.com/kiwi-deng/tag/%E5%A4%9A%E7%BA%BF%E7%A8%8B%E4%B8%8E%E9%AB%98%E5%B9%B6%E5%8F%91/
 */
public class T08_Question {
    public static void main(String[] args) {
        String[] numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        String[] words = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

        final Object lock = new Object();
        StringBuilder sb = new StringBuilder();
        CountDownLatch latch = new CountDownLatch(2);

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < words.length; i++) {
                synchronized (lock) {
                    sb.append(words[i]);
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    lock.notify();
                }
            }
            latch.countDown();
        });

        Thread t2 = new Thread(() -> {
            for (int j = 0; j < numbers.length; j++) {
                synchronized (lock) {
                    sb.append(numbers[j]);
                    lock.notify();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            latch.countDown();
        });

        t1.start();
        t2.start();

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(sb.toString());
    }
}
