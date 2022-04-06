package threadlocal;

import java.io.IOException;

/**
 * 普通引用，默认的这种引用，只要有引用指向一个对象，垃圾回收器一定不会回收它。
 * 这种是强引用。没有引用指向它的时候，它才会被回收。
 */
public class T01_NormalReference {
    public static void main(String[] args) throws IOException {
        M m = new M();
        m = null;
        System.gc();    //显示的调用垃圾回收

        System.in.read();   //阻塞住当前线程，因为gc是跑在别的线程里的，如果main主线程退出了就看不到gc了。
    }
}
