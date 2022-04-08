package containers;

/**
 * 有N张火车票，每张票都有一个编号
 * 同时有10个窗口对外售票
 * 请写一个模拟程序
 *
 * 使用Vector
 * 所谓的线程安全是指，调tickets.size()的时候，它加锁了，调remove的时候也加锁了，
 * 但是在size()和remove()之间没有加锁。
 * 虽然用了线程安全的容器，但是整个的这段代码不是原子的。
 * 卖最后一张的时候会出问题。
 */
import java.util.Vector;
import java.util.concurrent.TimeUnit;

public class TicketSeller2 {
    static Vector<String> tickets = new Vector<>();

    static {
        for(int i=0; i<1000; i++) tickets.add("票编号：" + i);
    }

    public static void main(String[] args) {
        for(int i=0; i<10; i++) {
            new Thread(()->{
                while(tickets.size() > 0) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("销售了--" + tickets.remove(0));
                }
            }).start();
        }
    }
}
