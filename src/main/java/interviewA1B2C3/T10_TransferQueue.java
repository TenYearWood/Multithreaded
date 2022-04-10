package interviewA1B2C3;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

/**
 * 使用TransferQueue来完成这道面试题
 */
public class T10_TransferQueue {
    public static void main(String[] args) {
        char[] aI = "1234567".toCharArray();
        char[] aC = "ABCDEFG".toCharArray();
        TransferQueue<Character> queue = new LinkedTransferQueue<>();

        new Thread(() -> {
            try {
                for (int i = 0; i < aI.length; i++) {
                    queue.transfer(aI[i]);
                    System.out.print(queue.take());
                }
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1").start();

        new Thread(() -> {
            try {
                for (int i = 0; i < aC.length; i++) {
                    System.out.print(queue.take());
                    queue.transfer(aC[i]);
                }
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t2").start();
    }
}
