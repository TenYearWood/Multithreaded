package juc;

import java.util.concurrent.Semaphore;

/**
 * Semaphore：信号量，信号灯
 * permits：允许的数量
 * 灯亮的时候可以执行，不亮不可以执行，大概这么个概念
 * 最多允许多少个线程同时运行
 * fair: 是否公平，默认非公平
 *
 * 比喻场景：
 * 机动车道很多，8条机动车道，前面有1个收费站，更多的车同时往收费站冲，不好意思，谁
 * acquire得到这个收费站通道，谁过去。限流。如果fair为true时，车道上后面来新车的时候，
 * 它不会超到前面去，它也必须在后面排着。
 */
public class T11_TestSemaphore {
    public static void main(String[] args) {
//        Semaphore s = new Semaphore(1);
        Semaphore s = new Semaphore(1, true);
        //允许一个线程同时执行

        new Thread(() -> {
            try{
                /**
                 * s.acquire 阻塞方法
                 * acquire一下permits就会减1变为0，别人是acquire不到的
                 * s.release后permits就会加1变为1，别人可以继续得到了
                 *
                 * Semaphore是控制可以有好多个线程同时acquire，但是acquire到的只有permits个，只有permits个线程在同时运行
                 *
                 */
                s.acquire();
                System.out.println("T1 running...");
                Thread.sleep(200);
                System.out.println("T1 running...");
            }catch (InterruptedException e){
                e.printStackTrace();
            } finally {
                s.release();
            }
        }).start();

        new Thread(() -> {
            try{
                s.acquire();
                System.out.println("T2 running...");
                Thread.sleep(200);
                System.out.println("T2 running...");
            }catch (InterruptedException e){
                e.printStackTrace();
            } finally {
                s.release();
            }
        }).start();
    }
}
