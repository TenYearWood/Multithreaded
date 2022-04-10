package interviewA1B2C3;

import util.SleepHelper;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Exchanger;

/**
 * 使用Exchanger来完成这道面试题
 * 有问题，不行，不知道为什么
 */
public class T09_Exchanger_Not_Work {
    public static Exchanger<String> exchanger = new Exchanger<>();

    public static void main(String[] args) {
        char[] aI = "1234567".toCharArray();
        char[] aC = "ABCDEFG".toCharArray();

        new Thread(() -> {
            for (int i = 0; i < aI.length; i++) {
                System.out.print(aI[i]);
                try {
                    exchanger.exchange("T1");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t1").start();

        new Thread(() -> {
            for (int i = 0; i < aC.length; i++) {
                try {
                    exchanger.exchange("T2");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print(aC[i]);
            }
        }, "t2").start();
    }
}
