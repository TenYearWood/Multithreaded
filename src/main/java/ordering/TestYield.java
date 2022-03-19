package ordering;

/**
 * yield:
 * 从cpu上先离开，别的线程开始有机会执行。当然别的线程没有执行，我还可以回来继续执行。
 * 所谓的离开，就是进入一个等待队列里头，yield之后回到等待队列里，在操作系统OS的调度算法里面，
 * 还是依然有可能把你刚回去的线程拿出来继续执行，当然更大的可能是将其他等待的线程拿出来执行。
 *
 * yield的本质上就是让出一下cpu，至于后面其他线程能不能抢到，不管。
 * yield之后返回到就绪状态。
 */
public class TestYield {
    public static void main(String[] args) {
        //可以使用同一个线程类，new两个线程。
        MyThread3 t1 = new MyThread3("t1");
        MyThread3 t2 = new MyThread3("t2");
        t1.start();
        t2.start();
    }
}

class MyThread3 extends Thread{
    MyThread3(String s){
        super(s);
    }

    @Override
    public void run() {
        for(int i=0; i<=100;i++){
            System.out.println(getName() + ": " +i);
            if(i%10 == 0){
                yield();
            }
        }
    }
}