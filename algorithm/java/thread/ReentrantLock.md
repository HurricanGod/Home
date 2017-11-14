## 重入锁ReentrantLock

-----

+ `ReentrantLock` 的构造函数可以接收一个可选的 `fairness` **boolean**型参数，如果这个参数值为**true** ，则创建的是公平锁，在竞态环境下等待时间最长的线程会优先获得锁；使用**公平锁会降低系统的吞吐量** ；


+ 在多个线程中，1个线程在其它活动线程没有进展并且未持有锁时可能多次获得公平锁；


+ 不指定时间的`tryLock()` 方法不遵循公平锁设置，即使其它线程在等待，如果锁可用也会获得锁


----

`ReentrantLock` 构造器

```java
private final Sync sync;

// 无参函数默认构造非公平锁
// NonfairSync、FairSync  都是Sync的子类
public ReentrantLock() {
        sync = new NonfairSync();
    }

public ReentrantLock(boolean fair) {
        sync = fair ? new FairSync() : new NonfairSync();
    }
```



`ReentrantLock` 的常用方法：

+ `public void lock()` 

  + 如果锁没有被其它线程占有，则会立刻返回并设置持有锁的数量为1

  + 如果当前线程持有锁，则持有锁的数量会加1并立刻返回

  + 如果锁被其它线程持有，那么本线程会被阻塞直到获得锁，获得锁后锁计数置为1

    ​

+ `public void lockInterruptibly() throws InterruptedException`

  + 如果其它线程持有锁导致本线程被阻塞，本线程将继续执行直到发生下面任意情况
    1. 当前线程获得锁
    2. 其它线程打断当前线程
  + 如果当前线程被打断，将会抛出`InterruptedException`

  ​



+ `public boolean tryLock()`
  + 如果其它线程没有持有锁将立刻获得锁并返回**true** ，设置锁计数器为1
  + 如果当前线程持有锁，持有锁计数加1后返回**true**
  + 如果其它线程持有锁，这个方法会**立刻返回false**

  ​



+ `public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException`
  + 如果锁没有被其它线程持有并且**当前线程没有被打断**，将在给定时间范围内获得锁
  + 如果锁已经被**设置为公平锁** ，锁可用时，当有其它线程等待锁的情况下调用`tryLock(long timeout, TimeUnit unit)` 将不会得到锁
  + 当锁被其它线程持有时，当前线程会一直处于休眠状态直到发生下面任意事件：
    + 当前线程获得锁
    + 其它线程打断当前线程
    + 指定等待的时间过去了，方法返回**false**
  + 当指定的 `time` 参数不是正数，线程将不会等待

  ​



+ `public void unlock()`





+ `public Condition newCondition()`
  + 返回这个锁对象的`Condition` 实例
  + 当前线程未获得锁时，任意一个 `Condition` 对象调用`await()` 或者 `signal()`方法都会抛出 `IllegalMonitorStateException`异常
  + 如果线程在等待过程中被打断等待会结束并且抛出`InterruptedException`
  + 被通知的线程是按照 `FIFO` 顺序的，线程从等待方法返回重新获得锁的顺序与最初时请求锁的顺序一样

  ​



+ `public int getHoldCount()`
  + 获取被当前线程持有这把锁的数量，及调用`lock()`的次数减去`unlock()`的次数

  ​



+ `public boolean isHeldByCurrentThread()`
  + 获取锁是否被当前线程持有

  ​



+ `public boolean isLocked()`
  + 判断锁是否被线程持有

  ​



+ `public final boolean hasQueuedThreads()`
  + 获取是否有线程等待获取锁

  ​



+ `public final boolean hasQueuedThread(Thread thread)`
  + 判断给定的线程是否在等待获取锁

  ​



+ `public final int getQueueLength()`
  + 获取等待获取锁的线程的估计数，该方法不是用于同步控制

  ​



+ `public boolean hasWaiters(Condition condition)`
  + 判断是否有线程在等待与该锁关联的给定 `Condition` 

  ​



+ `public int getWaitQueueLength(Condition condition)`
  + 返回等待给定 `Condition` 的线程数量
  + 如果给定的 `Condition` 与这个锁不相关将会出现`IllegalArgumentException`异常
  + 如果未持有锁调用本方法将会出现`IllegalMonitorStateException`异常
  + `Condition` 为null 将抛出空指针异常

  ​



![ReentrantLock类图]()







-----

调用`ReentrantLock`对象的`lock()`方法获取锁，调用`unlock()`方法释放锁

```java
private ReentrantLock lock = new ReentrantLock();
public void service(){
  try {
    
    lock.lock();
    // 同步操作
  } catch (Exception e) {
    
    System.out.println("异常信息：\n" + e.getMessage());
    
  }finally {
    
    lock.unlock();
    
  }
}         
			

```





----

## Condition实现等待通知



`Condition` 是一个接口，该接口有以下方法：

+ `void await() throws InterruptedException`
+ `void awaitUninterruptibly()`
+ `long awaitNanos(long nanosTimeout) throws InterruptedException`
+ `boolean await(long time, TimeUnit unit) throws InterruptedException`
+ `boolean awaitUntil(Date deadline) throws InterruptedException`
+ `void signal()`
+ `void signalAll()`


----

`void await()` 方法：

+ 调用此方法将会导致本线程阻塞直到被通知或者线程被打断，与`Condition`相关的锁被原子的释放，当前线程进入睡眠状态
+ 此方法返回时保证线程是持有锁的



`void awaitUninterruptibly()`方法：

+ 当进入此方法时线程被设置为`interrupted`状态或者线程在`waiting`时被中断，该方法也会一直等待直到被`signalled`



`long awaitNanos(long nanosTimeout)`方法：

+ 调用此方法将导致线程阻塞直到被通知或被打断或者指定的时间过去
+ 该方法返回给定时间内等待的纳秒数估计值，如果超时则返回小于等于0的数



`void signal()`

+ 唤醒一个等待的线程，线程在`await()`方法返回前会重新获得锁



`void signalAll()`

+ 唤醒所有线程







```java
 /**
     *  添加因 Condition 被阻塞的线程 
     * @return
     */
    private Node addConditionWaiter() {
        // 获取最后一个被阻塞的结点
        Node t = lastWaiter;
        // If lastWaiter is cancelled, clean out.
        // 最后一个被等待线程如果被取消，从头开始清除阻塞队列中取消等待的线程
        if (t != null && t.waitStatus != Node.CONDITION) {
            unlinkCancelledWaiters();
            t = lastWaiter;
        }
        Node node = new Node(Thread.currentThread(), Node.CONDITION);
        // 如果最后一个等待线程节点为 null，则把新添加的节点置为第一个等待线程
        if (t == null)
            firstWaiter = node;
        //否则把新添加的节点链接到最后并重新设置最后一个等待线程节点
        else
            t.nextWaiter = node;
        lastWaiter = node;
        return node;
    }

```



-----

## 实战

**4个线程a,b,c,d对一个整数（初值为0）进行操作，线程a,b打印整数后加1；线程c,d打印整数后减1，线程执行顺序为a → b → c → d；打印顺序为0 → 1 → 2 → 1 → 0**



### 使用Condition实现线程的顺序执行

**思路** ：让每个线程持有两个`Condition` 实例，一个 `Condition` 实例用于唤醒下一个要被执行的线程，另一个 `Condition` 用于在对整数进行操作（**加1或者减1**）后阻塞自己直到被其它线程唤醒

**注意** ：每个线程应该先唤醒其它线程再执行**Condition** 的`await()`方法



**实现代码如下** ：

```java
public class ConditionProducerComsumer {

    private static ReentrantLock lock = new ReentrantLock();

    private static Condition conditionA = lock.newCondition();
    private static Condition conditionB = lock.newCondition();
    private static Condition conditionC = lock.newCondition();
    private static Condition conditionD = lock.newCondition();

    private static volatile Integer counter = 0;

    private static class MyThread extends Thread{

        public MyThread(String name, Condition condition0, Condition condition1,
                        int number, int operand) {
            super(name);
            this.condition0 = condition0;
            this.condition1 = condition1;
            this.number = number;
            this.operand = operand;
        }

        private Condition condition0;
        private Condition condition1;
        private int number;
        private int operand;

        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            while (true) {
                lock.lock();
                try {

                    while (counter == number) {
                        System.out.println(name + "\t" + counter);
                        counter += operand;
                        Thread.sleep(1000);
                        condition1.signal();
                        condition0.await();
                    }

                } catch (Exception e) {

                    System.out.println("异常信息：\n" + e.getMessage());
                }finally {
                    lock.unlock();
                }
            }
        }
    }

    public static void main(String[] args)  {

        MyThread producer1 = new MyThread("A", conditionA, conditionB, 0, 1);
        MyThread producer2 = new MyThread("B", conditionB, conditionC, 1, 1);

        MyThread consumer1 = new MyThread("C", conditionC, conditionD, 2, -1);
        MyThread consumer2 = new MyThread("D", conditionD, conditionA, 1, -1);

        producer2.start();
        producer1.start();

        consumer2.start();
        consumer1.start();

    }
}

```





### 使用阻塞队列实现线程的顺序执行

**思路** ：把线程对整数的操作抽象成不同的状态，比如初值为0，把线程a对整数加1看做一种状态，设这个状态为0；整数由1 → 2又看做一种状态，把这个状态定义为状态1；把这些状态放入到一个阻塞队列里，每次读取队列首部的状态，根据状态让对应的线程做相应的操作，把每次从队列取出的状态值有添加到队列的尾部，从而实现线程顺序循环执行



**代码实现如下** ：

```java
package mutithread;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by NewObject on 2017/11/10.
 */
public class BolckQueueProducerConsumer {


    private static volatile Integer counter = 0;

    private static class OperateThread extends Thread{

        private ArrayBlockingQueue<Integer> queue;
        private int id; // 状态id，跟队列里的状态对应
        private int operand; // 根据状态对整数进行操作的操作数


        /**
         * @decription: 
         * @param name 线程名
         * @param queue 线程间共享的阻塞队列
         * @param id  用于标记本线程应该进行的操作，与队列里的值关联，
         *            如果取出的队首元素为1，本线程的id值为1则对整数进行相应的操作
         * @param operand 用于控制整数加 1 或者减 1 的变量
         * @return:
         */
        public OperateThread(String name, ArrayBlockingQueue<Integer> queue, int id, int operand) {
            
            super(name);
            this.queue = queue;
            this.id = id;
            this.operand = operand;
        }

        @Override
        public void run() {
            String name = Thread.currentThread().getName();

            while (true) {
                try {
                    while (queue.peek() == id) {
                        synchronized (counter){
                            System.out.println(name + "\t" + counter);
                            counter += operand;
                            queue.offer(queue.take());
                        }
                        Thread.sleep(3000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(4);
        /**
         *  4个线程，两个线程先对整数加1，两个线程对整数进行减1操作，
         *  整数值状态变化为： 0 → 1 → 2 → 1 → 0
         *  因此可以抽象为4个状态
         */
        queue.put(0);
        queue.put(1);
        queue.put(2);
        queue.put(3);

        /**
         *  第3个参数的含义为线程对应的操作，跟队列中的值关联
         *  第4个参数 1 表示对整数进行加 1 操作，若为 -1 则表示进行减 1 操作
         */
        OperateThread producer1 = new OperateThread("A", queue, 0, 1);
        OperateThread producer2 = new OperateThread("B", queue, 1, 1);

        OperateThread consumer1 = new OperateThread("C", queue, 2, -1);
        OperateThread consumer2 = new OperateThread("D", queue, 3, -1);

        producer1.start();
        producer2.start();

        consumer1.start();
        consumer2.start();

    }
}
```



----

使用**Condition** 与**ReentrantLock** 实现**生产者——消费者**模式

```java
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by NewObject on 2017/11/13.
 *  使用 Condition 与 Reentrant 实现 生产者——消费者模式
 */
public class ConditionProducerConsumer1 {
    private static volatile LinkedList<Integer> list = new LinkedList<>();

    private static class Producer extends Thread{

        private Condition condition;
        private ReentrantLock lock;

        public Producer(Condition condition, ReentrantLock lock) {
            this.condition = condition;
            this.lock = lock;
        }

        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            Random random = new Random();
            while (true) {
                lock.lock();
                if (list.size() == 1) {
                    try {
                        System.out.println(name + "\t阻塞");
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    int i = random.nextInt(100);
                    System.out.println(name + "\tproduce\t" + i);
                    list.offer(i);
                    System.out.println("list.size() = " + list.size());
                    condition.signalAll();
//                    condition.signal();
                }
                lock.unlock();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class Consumer extends Thread{
        private Condition condition;
        private ReentrantLock lock;

        public Consumer(Condition condition, ReentrantLock lock) {
            this.condition = condition;
            this.lock = lock;
        }

        @Override
        public void run() {

            String name = Thread.currentThread().getName();
            while (true) {
                lock.lock();
                if (list.size() > 0) {
                    System.out.printf("\n%s消费产品:\t%d\n", name, list.poll());
                    condition.signalAll();
//                    condition.signal();

                } else {
                    try {
                        System.out.printf("\n消费者 %s 将被阻塞\n", name);
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                lock.unlock();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        for (int i = 0; i < 4; i++) {
            new Producer(condition, lock).start();
            new Consumer(condition,lock).start();
        }
    }
}
```

