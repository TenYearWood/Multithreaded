package threadlocal;

import java.lang.ref.SoftReference;

/**
 * 软引用，当有一个对象被一个软引用指向的时候，只有系统内存不够用的时候，
 * 才会回收这个对象。内存够用就不会回收它。
 *
 * 主要用作缓存，比如从数据库里读一大堆数据出来，放在缓存里下次还可以用，可以用
 * 软引用，需要新的空间再干掉，下次从数据库再取就行了。空间够使的时候就不用从数据
 * 库读而是从内存直接拿。
 */
public class T02_SoftReference {
    public static void main(String[] args) {
        SoftReference<byte[]> m = new SoftReference<>(new byte[1024*1024*10]);
        System.out.println(m.get());
        System.gc();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(m.get());

        /**
         * 再分配一个数组。heap将装不下，这时候系统会垃圾回收，先回收一次，如果不够，会把软引用干掉
         * 需要设置参数：VM options：-Xms20M -Xmx20M
         * 堆内存最少20M 最多20M 堆内存直接分配20M
         */
        byte[] b = new byte[1024*1024*15];
        System.out.println(m.get());
    }
}
