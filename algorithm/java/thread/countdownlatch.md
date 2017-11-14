## 倒计时门闩（CountDownLatch）



-----

`CountDownLatch` 会导致一条或多条线程在**门口**一直等待，直到另一条线程打开这扇门线程才得于继续运行



**应用** ：

+ 让一个线程同时等待多个线程完成操作，相比`join()`方法，它在等待线程完成某项操作时可以同时等待多个线程，不仅仅等待一个线程；另外使用 `CountDownLatch` 可以在任意时刻等待其它线程完成某项操作，不一定是等待线程运行结束， 在一个线程里调用`join()`方法是等待线程结束后才返回


+ 另外可以使用多个`CountDownLatch`互相等待从而使线程按照预期的顺序执行下去



`CountDownLatch` 的使用方法：

```java
/**
 * Created by NewObject on 2017/11/11.
 *  首先创建 1个 包含4个线程的线程池，并且往线程池提交4个任务
 *  任务提交后被执行，然后所有任务在 startLatch.await() 处被阻塞
 *
 *  主线程调用 startLatch 实例的 countDown() 方法后所有任务被唤醒并执行
 *  当主线程里运行到 processLatch.await() 处时主线程会被阻塞
 *  直到所有任务都执行完 processLatch.countDown() 主线程才会被唤醒然后进行执行
 */
public class CountDownLatchTest {

    private static int count = 4;

    private static CountDownLatch startLatch = new CountDownLatch(1);
    private static CountDownLatch processLatch = new CountDownLatch(count);


    public static void main(String[] args) throws InterruptedException {

        ExecutorService pool = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 4; i++) {
            pool.submit(() -> {

                String name = Thread.currentThread().getName();
                System.out.println(name + " is start executing");

                try {
                    Thread.sleep(1000);
                    startLatch.await();
                    System.out.println(name + " is processing");

                    processLatch.countDown();
                    System.out.println(name + " is exit!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        pool.shutdown();

        System.out.println("main is executing!");
        Thread.sleep(1000);
        System.out.println("调用 countDown() 前提交到线程池的任务会一直停在  is processing前\n");
        startLatch.countDown();
        Thread.sleep(2000);
        processLatch.await();
        System.out.println("主线程等待线程池的任务执行完再退出!");

    }
}

```



----

## 同步屏障

**同步屏障** 允许一组线程彼此等待，直到抵达某个公共的屏障点



**应用** ：

+ 对于数量固定，线程间需要不时等待彼此的多线程应用场景；
+ 同步屏障在并行分解的场合下很有用，在这里长时间的任务被分割为多个子任务，单独执行后的结果随后被合并在整个任务的结果中



与同步屏障相关的类为`CyclicBarrier` ，构造函数如下：

+ `public CyclicBarrier(int parties, Runnable barrierAction)`

  + 初始化一个同步屏障，包含 `parties` 数量的线程以及当线程一旦**跨越同步屏障** 将执行第二个参数里的任务；即当 `parties - 1` 个线程正在等待最后一个线程到达**同步屏障**时，最后一个线程到达**同步屏障**将会执行 `barrierAction` 任务

  ​

+ `public CyclicBarrier(int parties)`

  + 初始化一个包含指定 `parties` 数目的 `CyclicBarrier` 实例



+ `public int await() throws InterruptedException, BrokenBarrierException`
  + 强制调用线程一直等待，直到所有的`parties`都已经在同步屏障上调用了`await()`方法
  + 当调用线程自己或者其它等待线程被中断、有线程在等待中超时或者线程在同步屏障上调用`reset()`方法，该调用线程会停止等待
  + 一旦有线程在等待时被中断，其它所有等待的线程都会抛出`BrokenBarrierException`异常并且同步屏障也会被设置为打破状态
  + 如果调用线程是最后一个到达**同步屏障**的线程并且创建 `CyclicBarrier` 时提供了非空的 `barrierAction`任务 ，会先执行 `barrierAction`任务后再让其它线程继续执行



+ `public int getParties()`
  + 返回需要跨越同步屏障的线程数目



+ `public void reset()`
  + 把同步屏障重置到原始状态，如果此时有任意线程等待在这个屏障上，将会抛出 `BrokenBarrierException`


+ `public int getNumberWaiting()`
  + 返回当前在同步屏障上等待的线程数目





`CyclicBarrier` 同步屏障的使用方法1：

```java
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by NewObject on 2017/11/11.
 *  使用同步屏障对二维数组中的每一行进行排序，排序完成后打印排序好的数组
 *
 */
public class CyclicBarrierTest {

    /**
     * @decription: 打印二维数组所有元素
     * @param array 二维数组
     */
    public static void showArray(int[][] array){


        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                System.out.print(array[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }


    /**
     * @decription: 对二维数组中的指定行进行归并排序
     * @param array 待排序的二维数组
     * @param row 数组的某一行
     * @param left array[row] 的起始下标
     * @param right array[row] 的结束下标
     */
    public static void sort(int[][] array, int row, int left, int right){

        if (left >= right) {
            return;
        }
        if (right - left == 1) {
            if (array[row][left] > array[row][right]) {
                int t = array[row][left];
                array[row][left] = array[row][right];
                array[row][right] = t;
            }
            return;
        }
        int mid = (left + right) / 2;
        sort(array, row, left, mid);
        sort(array, row, mid+1, right);

        int[] p = new int[right - left + 1];

        int index1 = left;
        int index2 = mid+1;
        int index = 0;
        while (index1 <= mid && index2 <= right){
            if (array[row][index1] < array[row][index2]) {
                p[index++] = array[row][index1++];

            } else {
                p[index++] = array[row][index2++];

            }
        }
        while (index1 <= mid){
            p[index++] = array[row][index1++];
        }

        while (index2 <= right){
            p[index++] = array[row][index2++];
        }

        for (int i = left; i < right + 1; i++) {
            array[row][i] = p[i - left];
        }
    }


    /**
     *  排序线程，负责对二维数组中的每一行进行归并排序
     */
    private static class SortThread extends Thread{
        private int row;
        private int[][] data;
        private CyclicBarrier barrier;

        public SortThread(int row, int[][] data, CyclicBarrier barrier) {
            this.row = row;
            this.data = data;
            this.barrier = barrier;
        }

        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            try {
                long millis = (long) (Math.random() * 1000);
                System.out.println(name + " 将花费 " + millis + " ms 准备排序操作");
                Thread.sleep(millis);
                sort(data, row, 0, 4);
                System.out.println(name + " 排序完成，正等待其它线程排序完!");
                /**
                 *  这里调用同步屏障的 await() 将导致本线程一直阻塞
                 *  正常情况下，直到构造函数里指定的 parties 个线程中最后一个线程
                 *  也调用 await() 后才继续执行；
                 *  如果在构造函数中指定了所有线程到达同步屏障后要执行的动作(一个 Runnable参数)
                 *  那将先执行这个 Runnable 后再让所有线程继续运行下去
                 */
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.printf("排序线程  %s  结束!\n", name);
        }
    }


    public static void main(String[] args) {

        int[][] digits = new int[6][5];
        CyclicBarrier barrier = new CyclicBarrier(digits.length, () -> {
            System.out.println("\n所有排序线程到达同步屏障，将执行到达屏障后的显示操作");
            showArray(digits);
        });
        Random random = new Random();

        for (int i = 0; i < digits.length; i++) {
            for (int j = 0; j < digits[i].length; j++) {
                digits[i][j] = random.nextInt(200);
            }
        }

        showArray(digits);

        for (int i = 0; i < digits.length; i++) {
            SortThread sortThread = new SortThread(i, digits, barrier);
            sortThread.start();
        }

    }
}

```



`CyclicBarrier` 同步屏障的使用方法2

```java
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by NewObject on 2017/11/12.
 *
 *  使用同步屏障 CyclicBarrier 实现循环生产消费产品(类似于生产者消费者模式)
 *  创建有 parties 个线程伙伴的同步屏障 CyclicBarrier 实例，
 *  并为该实例指定一个所有线程到达同步屏障后要执行的动作，
 *  该动作主要是消费掉 parties 个线程生产的所有产品
 *
 *  创建parties 个线程，每个线程往 list 里添加元素，当 list 元素达到 10 时，
 *  调用同步屏障的 await() 阻塞线程，直到所有线程得到同步屏障再让一个消费掉所有产品
 */
public class ProducerConsumerCyclicBarrier {

    private static volatile LinkedList<Integer> list = new LinkedList<>();

    private static synchronized boolean add(Integer data){
        if (list.size() >= 10) {
            return false;
        }
        list.add(data);
        return true;
    }

    private static class Producer extends Thread{

        private CyclicBarrier barrier;

        public Producer(CyclicBarrier barrier) {
            this.barrier = barrier;
        }

        @Override
        public void run() {
            String name = Thread.currentThread().getName();


            while (true) {
                int data = new Random().nextInt(100);

                if (add(data)) {

                    System.out.printf("%s 生产产品\t%d\n", name, data);

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        barrier.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public static void main(String[] args) {
        int parties = 4;
        CyclicBarrier barrier = new CyclicBarrier(parties, () ->{

            System.out.println();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                Integer val = list.poll();
                System.out.printf("消费者消费产品：\t%d\n", val);
            }
            System.out.println("消费者消费完所有产品\n");
        });


        for (int i = 0; i < parties; i++) {
            new Producer(barrier).start();
        }
    }
}

```

