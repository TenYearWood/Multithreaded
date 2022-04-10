package containers;

import util.SleepHelper;

import java.util.concurrent.LinkedTransferQueue;

/**
 * transfer方法和put方法区别在于：
 * put方法，一个线程put装完就走了，结束了。(只有有界队列的时候，满了，put才会等着)
 * transfer：线程装完等着，阻塞，等有人把它取走后，这个线程再继续干自己的事情。
 * <p>
 * 应用场景：
 */
public class T11_TransferQueue {
    public static void main(String[] args) throws InterruptedException {
        LinkedTransferQueue<String> strs = new LinkedTransferQueue<>();

        new Thread(() -> {
            try {
                SleepHelper.sleepSeconds(10);
                System.out.println(strs.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        strs.transfer("aaa");
    }
}
