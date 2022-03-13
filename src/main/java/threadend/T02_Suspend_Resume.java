package threadend;

import util.SleepHelper;

/**
 * suspend:暂停
 * resume：恢复
 * 这两个方法被废弃不建议使用的原因和stop类似，
 */
public class T02_Suspend_Resume {
    public static void main(String[] args) {
        Thread t = new Thread(()->{
            while (true){
                System.out.println("go on");
                SleepHelper.sleepSeconds(1);
            }
        });

        t.start();

        SleepHelper.sleepSeconds(5);

        t.suspend();
        SleepHelper.sleepSeconds(3);
        t.resume();
    }
}
