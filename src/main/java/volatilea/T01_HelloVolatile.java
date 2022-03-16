package volatilea;

import util.SleepHelper;

/**
 * 线程可见性的概念：
 * running位于主内存里，有两个线程都会用到它，t1和main。
 * t1线程会一直读running，t1读的实际是running的拷贝，每个线程在运行的时候，都会把这个数据从内存里读出一份
 * 放在线程本地缓存，以后每次循环while(running)读的这个值并不是去主内存读而是读的本地缓存。只要本地缓存中的这份
 * copy没有被改过，或者说没有从主内存重新读取过，这个running值就一直为true。t1它不会每次去主内存读，只是读t1本地缓存的。
 * main这个线程，1s钟之后，把自己本地缓存的running拷贝值改成了false。但是没用，t1读的还是自己的那份拷贝。
 * 所以，t1停止不了。
 *
 * 可见性问题：
 * 一个线程改了值，另外一个线程是不是能见到最新修改的值。默认的情况下一个线程改了值，另外线程是看不见的。
 *
 *
 * Volatile作用：
 * 一、保障可见性
 * volatile所修饰的这块内存，对于它的任何修改，另外一个线程立马可见。
 * 可见的意思是说，对于volatile修饰的这块内存，每次线程都要从主内存去读。
 * main修改了running之后，马上刷新到主内存。t1的while(running)每次都去主内存读一遍。
 *
 */
public class T01_HelloVolatile {
    private static volatile boolean running = true;

    private static void m(){
        System.out.println("m start");
        while (running){
            //System.out.println("hello");

            /**
             * 如果放开System.out.println("hello");的话，会发现不加volatile也能结束，为什么？
             * 因为println方法里面有synchronized关键字，触发了可见性
             * synchronized也是可以保障可见性的，可以简单这么理解。触发了内存缓存同步刷新。
             */
        }
        System.out.println("m end!");
    }

    public static void main(String[] args) {

        new Thread(()-> T01_HelloVolatile.m(), "t1").start();

        SleepHelper.sleepSeconds(1);

        running = false;
    }
}

