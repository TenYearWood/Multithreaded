package volatilea;

import util.SleepHelper;

/**
 * volatile修饰的引用类型(包括数组)，只能保证引用本身的可见性，不能保证内部字段的可见性
 *
 * volatile修饰引用类型相对少
 */
public class T02_VolatileReference {

    private static class A {
        boolean running = true;

        void m() {
            System.out.println("m start");
            while (running) { }
            System.out.println("m end!");
        }
    }

    private static volatile A a = new A();

    public static void main(String[] args) {
        new Thread(a::m, "t1").start();
        SleepHelper.sleepSeconds(1);
        a.running = false;
    }

    /**
     * 上面程序要怎么改：
     * volatile 修饰boolean running = true;加载这句话前面就行了
     */
}
