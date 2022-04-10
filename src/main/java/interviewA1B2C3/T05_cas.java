package interviewA1B2C3;

/**
 * cas的写法
 * 自旋的写法会占用cpu。而之前的wait、await不会占用cpu
 */
public class T05_cas {

    enum ReadyToRun {T1, T2}

    public volatile static ReadyToRun t = ReadyToRun.T1;

    public static void main(String[] args) {
        char[] aI = "1234567".toCharArray();
        char[] aC = "ABCDEFG".toCharArray();

        new Thread(() -> {
            for (char c : aI) {
                while (t != ReadyToRun.T1) {}
                System.out.print(c);
                t = ReadyToRun.T2;
            }
        }, "t1").start();

        new Thread(() -> {
            for (char c : aC) {
                while (t != ReadyToRun.T2) {}
                System.out.print(c);
                t = ReadyToRun.T1;
            }
        }, "t2").start();
    }
}
