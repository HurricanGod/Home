## 脏读与锁

对实例变量的值**写操作**进行的同步并不意味着对该实例变量的操作是线程安全的，如果对写操作同步而**读操作未同步**在并发环境下可能出现**脏读**的现象。

以下面代码为例：

```java
package mutithread;

/**
 * Created by NewObject on 2017/10/7.
 */
public class DirtyRead {

    public static void main(String[] args) throws InterruptedException {
        Account account = new Account("张三", 1000d);
        MyThread thread = new MyThread(account, 100d);
        thread.start();
        Thread.sleep(1000);
        System.out.println(account.getAccountInfo());
        thread.join();
        System.out.println(account.getAccountInfo());

    }


}

class MyThread extends Thread{

    private Account account;
    private Double money;

    public MyThread(Account account, Double money) {
        this.account = account;
        this.money = money;
    }

    @Override
    public void run() {
        try {
            this.account.transfer(this.money);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Account{

    private String name;
    private Double money;

    public Account(String name, Double money) {
        this.name = name;
        this.money = money;
    }

    public synchronized void transfer(Double number) throws InterruptedException {
        System.out.println(name + " 账户里还剩余 " + money);
        System.out.println("开始执行转账业务");
        Thread.sleep(3000);
        this.money -= number;
        System.out.println("转账完成");
    }


    public String getAccountInfo(){
        String info;
        info = "name:\t" + this.name + "\nmoney:\t" + this.money;
        return info;
    }
}
```

运行结果：

![脏读现象]()



**对读操作也进行同步**解决**脏读**问题

![]()

-----

## synchronized 锁重入

关键字 `synchronized` 拥有锁重入功能，当一个线程得到一个对象锁后，再次请求此对象锁时可以再次获得该对象的锁。

**理解**： 如1条线程获得了某个对象的锁，这个线程还未释放对象锁，当这个线程**调用该对象的其它同步方法**或**进入该对象的同步块** 时可以**再次获得该对象的锁**，这便是锁重入的概念。



+ 存在父子继承关系时，子类可以通过 `可重入锁` 调用父类的同步方法
+ 当一个线程执行的代码**出现异常**时，其**持有的锁会自动释放**
+ **同步不可以被继承**——即子类如果重写了父类的方法，子类方法如果未用`synchronized` 修饰，那么子类中该方法就不是同步的
+ 同步块尽量不要使用 `String` 对象作为加锁对象



**以String对象为加锁的例子**：

```java
/**
 * Created by NewObject on 2017/10/7.
 */
public class StringLock {

    private static class MyThread extends Thread{

        String lock = "000";
        public MyThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            synchronized (lock){
                System.out.println(currentThread().getName() + " enter synchronized block");
                lock = "001";
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(currentThread().getName() + "----> lock = " + lock);
            }
            System.out.println(currentThread().getName() + " end!");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MyThread threadA = new MyThread("A");
        MyThread threadB = new MyThread("B");
        threadA.start();
        Thread.sleep(200);
        threadB.start();
}
}
```

**运行结果**：

```
A enter synchronized block
A----> lock = 001
B enter synchronized block
A end!
B----> lock = 001
B end!

```

程序中使用**String** 对象作为加锁对象，一开始**lock**指向字符常量**"000"**，当有1个线程进入同步块后把**lock**指向另一字符常量**"001"**后睡眠2s，线程在同步块内睡眠，按照常理其它线程是不能进入同步块的，但字符串是比较特殊的对象，进入睡眠的线程进入同步块加锁的对象是**"000"**，但进入同步块后**lock**指向的内容发生了改变，指向了**"001"**，没有线程对字符串**"001"**对象加锁，因此第二个线程可以进入同步块，从而出现了两个线程交替打印的实验结果

