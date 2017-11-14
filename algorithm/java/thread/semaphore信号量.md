## 信号量Semaphore



信号量包含**公平策略** 和**非公平策略** 的两种信号量，**非公平策略**的信号量意味着允许抢占式地获取；**公平策略的信号量** 能保证调用 `acquire()` 的方法的任意线程能够按照方法被调用的顺序获取**许可证** .



**信号量** 主要用于资源访问，应该初始化为公平的，从而避免出现**饥饿**现象；但非公平的信号量能够带来更高的吞吐量的好处



----

`Semaphore` 将信号量概念化一个维护一组许可证的对象，构造函数如下：

+ `public Semaphore(int permits)`—— 默认创建**非公平策略**的信号量
+ `public Semaphore(int permits, boolean fair)` —— 用于创建1个**公平策略**的信号量



**常用方法** ：

+ `public void accquire() throws InterruptedException `
  + 从信号量获取1个许可证，如果有则立刻返回，如果不可获得将会一直阻塞直到有**许可证**可以获取或者线程被中断
  + 成功获取 **许可证** 后将可获取的许可证减1



+ `public void acquireUninterruptibly()`
  + 与 `accquire()` 类似，不过线程被打断并不会返回，而是一直等待直到可以获取**许可证**



+ `public boolean tryAcquire()`
  + 如果可以获取**许可证** 则立刻返回，返回值为 `true` 并把可获取的许可证数量减1
  + 如果没有可获取的**许可证**， 方法将立刻返回**false**
  + 即使信号量`semaphore`被设置为公平策略的，当调用此方法时，如果有可获取的**许可证** 也将立刻获取而**不管有没有其它线程在等待许可证**



+ `public boolean tryAcquire(long timeout, TimeUnit unit) throws InterruptedException`
  + 当前线程没有被打断下，在给定时间内获得**许可证**
  + 如果有**许可证**可获取将立刻获取并返回**true**，可获取的许可证数量减1
  + 如果没有可获取的**许可证** ，线程将会阻塞直到发生下面任一件事发生
    + 其它线程里调用了这个信号量的`release()`方法并且当前线程获取到了**许可证**，将返回**true**
    + 其它线程打断本线程，线程将被唤醒并且抛出异常，线程的`interrupted` 标志位被清除
    + 指定等待的时间过去，如果线程未获取**许可证**则返回**false**，否则返回**true**



+ `public void release()`
  + 还回1个**许可证**给信号量，信号量可被获取的**许可证**数量加1



+ `public void acquire(int permits) throws InterruptedException`
  + 获取信号量给定数量的**许可证**，如果有足够的许可证可被获取则立刻返回否则会一直阻塞直到有**可获取的许可证** 或者线程被中断



+ `public void acquireUninterruptibly(int permits)`
  + 功能同上，但线程被中断时不会抛出异常



+ `public int availablePermits()`
  + 获取这个信号量可被获取的**许可证**数目



+ `public int drainPermits()`
  + 获取并返回立刻可用的**许可证**



+ `public boolean isFair()`
  + 如果信号量是公平的则返回**true** ，否则返回**false**



+ `public final boolean hasQueuedThreads()`
  + 判断有没有线程正在等待获取**许可证**，该方法不是用于同步控制



+ `public final int getQueueLength()`
  + 返回等待获取**通行证**的线程数目（**估计值**）



+ `protected Collection<Thread> getQueuedThreads()`



-----

使用**Semaphore**实现生产者——消费者模式代码如下：

```java
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * Created by NewObject on 2017/11/12.
 *  使用 Semaphore 实现生产者——消费者模式
 */
public class SemaphoreTest {

    //　创建供生产者、消费者共享的集合
    private static volatile LinkedList<Integer> list = new LinkedList<>();

    /**
     * 　生产者线程，把生产的产品放到　list 集合里
     */
    private static class Producer extends Thread{
        private Semaphore semaphore;

        public Producer(Semaphore semaphore) {
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            Random random = new Random();
            String name = Thread.currentThread().getName();

            while (true) {
                try {
                    semaphore.acquire();
                    if (list.size() <= 10) {

                        int data = random.nextInt(100);
                        System.out.printf("%s 往 list 中添加 %d\n", name, data);
                        list.add(data);
                    }
                    semaphore.release();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 　消费者线程，从集合 list　取数据消费
     */
    private static class Consumer extends Thread{
        private Semaphore semaphore;

        public Consumer(Semaphore semaphore) {
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            while (true) {
                try {
                    semaphore.acquire();
                    if (list.size() > 0) {
                        Integer data = list.poll();
                        System.out.println(name + "\t消费产品 " + data + "\n");
                    }
                    semaphore.release();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        //　创建互斥访问的信号量
        Semaphore semaphore = new Semaphore(1);

        int count = 5;

        for (int i = 0; i < count; i++) {
            new Producer(semaphore).start();
        }

        for (int i = 0; i < count; i++) {
            new Consumer(semaphore).start();
        }
    }
}
```

