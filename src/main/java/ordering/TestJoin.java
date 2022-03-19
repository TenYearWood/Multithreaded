package ordering;

/**
 * join:
 * t1和t2线程都在执行，如果在t1的某个点上调用t2.join，麻烦你跑到t2去运行，
 * t1在等着，什么时候t2运行完了，继续t1运行。
 * 相当于把t2线程加入了我当前线程里面，经常用来等待另外一个线程的结束。
 *
 * 比如，起了t1，t2，t3三个线程，怎么保证按照顺序执行完？
 * 答：main线程里调用t1.join、t2.join、t3.join.也可以t1里面调用t2.join，t2里调t3.join，
 * 保证t3完了t2才能完成，t2完成了t1才能完成。
 */
public class TestJoin {
    public static void main(String[] args) {
        MyThread2 t1 = new MyThread2("t1");
        t1.start();
        try {
            /**
             * 将t1线程合并到main线程，和main线程一块执行。
             */
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(int i=0; i<=5; i++){
            System.out.println("i am main thread");
        }
    }
}

class MyThread2 extends Thread{
    MyThread2(String s){
        super(s);
    }

    @Override
    public void run() {
        for(int i=0; i<=5; i++){
            System.out.println("i am " + getName());
            try{
                sleep(1000);
            }catch(InterruptedException e){
                return;
            }
        }
    }

}
