## 生产者 —— 消费者 模式

+ 1个生产者对应1个消费者
+ 1个生产者对应多个消费者
+ 多个生产者对应1个消费者
+ 多个生产者对应多个消费者



----

### 1个生产者对应多个消费者

**1个生产者对应多个消费者使用notify可能导致假死问题**

出现假死现象代码如下：

```java
package mutithread;

import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by NewObject on 2017/11/6.
 */
public class OneProducerConsumerModel {

    /**
     *  生产者消费者共享的队列，队列容量为1，
     *  生产者往队列生产1个产品后阻塞自己直到被消费掉
     */
    public static final ArrayDeque<Integer> queue = new ArrayDeque<>();


    /**
     * 生产者线程，总共生产100个产品
     */
    private static class Producer extends Thread{

        private Integer index = 0;

        public Producer(String name) {
            super(name);
        }


        @Override
        public void run() {
            while (index < 100) {
                synchronized (queue){
                    if (queue.size() == 0) {
                        queue.add(index++);
                        System.out.println("producer " + (index - 1));

                        queue.notify();
//                        queue.notifyAll();
                    } else {
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }
    }

    private static class Consumer extends Thread{

      	// 消费者消费计数器
        private static volatile AtomicInteger counter = new AtomicInteger(0);

        public Consumer(String name) {
            super(name);
        }

        private Integer index = 0;


        @Override
        public void run() {
            while (counter.intValue() < 100) {
                synchronized (queue){
                    if (queue.size() == 1) {
                        System.out.println(Thread.currentThread().getName()
                                + " consume " + queue.poll());
                        counter.getAndIncrement();

                        // 注意这里使用的是 notify()，
                        // 在1对多生产者消费者模式下可以导致线程假死
                        // 因被阻塞的线程包括其它消费者，如果被唤醒的线程是其它消费者，
                        // 将会导致被唤醒的消费者再次阻塞，最终可能会导致所有消费者被阻塞
                        // 因为所有消费者都被阻塞了并且没有唤醒生产者，最后程序会出现假死现象
                        queue.notify();
//                        queue.notifyAll();
                    } else {

                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("main start");

        Producer producer = new Producer("producer");
        Consumer consumer1 = new Consumer("consumer1");
        Consumer consumer2 = new Consumer("consumer2");

        producer.start();
        consumer1.start();
        consumer2.start();
        System.out.println("main exit");
    }
}

```

![假死现象图]()



出现这种假死现象的原因是：

消费者把产品消费完后队列调用`notify()`方法唤醒1个被阻塞的线程，但是具体唤醒哪个线程是不确定的，这些被阻塞的线程包括生产者和消费者。如果唤醒的线程是消费者，由于队列里的产品数为0，被唤醒的消费者线程又会进入阻塞状态，最糟糕的情况是所有消费者线程被阻塞，将导致没有线程唤醒生产者，从而出现**消费者、生产者都被阻塞**的现象



在消费者中使用**notifyAll()**方法唤醒所有被阻塞的线程可以解决这种假死现象。

![notifyAll()解决假死问题]()



------

### 多个生产者对应多个消费者

