package ordering;

import java.io.IOException;

/**
 * 下面程序的问题：有可能输出中间状态0
 *
 * T03_ThisEscape的构造方法分三步：
 * 1.new一半的对象，对象的半初始化状态，num=0
 * 2.调用构造方法，将num设置为8
 * 3.建立关联，和this建立关联
 * 但是在指令1完成之后，很有可能指令2和3换顺序，指令可以重排序，于是先建立了关联，然后才调用构造方法。
 * 结果在调用构造方法的过程中，起了线程打印this对象的num，num还没有初始化完成，所以有可能输出的是中间
 * 状态0，这种现象称为this的中间状态逸出。
 *
 * 因此，不要再构造方法中new线程启动。可以使用成员变量保存线程new出来，但是不要start。确保构造方法完成之
 * 后再调用start。
 *
 */
public class T02_ThisEscape {

    private int num = 8;

    public T02_ThisEscape() {
        new Thread(() -> System.out.println(this.num)
        ).start();
    }

    public static void main(String[] args) throws IOException {
        new T02_ThisEscape();
        System.in.read();
    }
}
