package ordering;

public class T01_NoVisibility {
    private static volatile boolean ready = false;
    private static int number;

    private static class ReadThread extends Thread {

        @Override
        public void run() {
            while (!ready){
                Thread.yield();
            }
            System.out.println(number);
        }
    }

    /**
     * number = 42和ready = true这两句话的执行有可能换顺序，所以上面程序可能打印为0
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Thread t = new ReadThread();
        t.start();
        number = 42;
        ready = true;
        t.join();
    }
}
