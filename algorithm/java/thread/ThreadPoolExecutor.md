## Java线程池

**使用线程池的好处**：

+ **降低资源消耗**（可以重复利用已经创建的线程降低频繁创建销毁线程的消耗）
+ **提高响应速度**（任务到达时不用等待线程创建的时间，任务能够立刻执行）
+ **提高线程的可管理性**（使用线程池可以统一进行分配、调优和监控）

----

**线程池工作原理** ：

+ 判断核心线程池里的线程是否都在执行任务
  + 如果不是，则启动一个创建1个`Worker`线程来执行任务
  + 如果线程池中的线程数量超过了核心线程数量则执行下一步
+ 判断线程池有没有被关闭，如果线程池未关闭则判断是否可以往工作队列提交任务
  + 如果可以往工作队列提交任务，再次检测线程池有没有被关闭
    + 如果在这次检测线程池没有被关闭则将新提交的任务放进工作队列等待执行
    + 如果在这次检测中发现线程池已经被`shurdown`了则删除刚才提交的任务
  + **工作队列满了**，则执行下一步
+ 检查线程池的线程数量是否**超过线程池规定的最大值**
  + 未超过线程池规定的线程数量，启动一个**非核心线程**执行任务
  + **已经达到线程池规定的数量**，拒绝任务



![线程池类结构](https://github.com/HurricanGod/Home/blob/master/algorithm/java/thread/img/ThreadPoolExector.jpg)



----

**2017年10月16日 18:10:23补充**

----

## ThreadPoolExecutor类源码



`ThreadPoolExecutor`类有4个构造函数，分别为：

+ `public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize,long keepAliveTime, TimeUnit 		unit, BlockingQueue<Runnable> workQueue) `

  ​

+ `public ThreadPoolExecutor(int corePoolSize,int maximumPoolSize,long keepAliveTime,TimeUnit unit,                          BlockingQueue<Runnable> workQueue,ThreadFactory threadFactory)`


+ `public ThreadPoolExecutor(int corePoolSize,int maximumPoolSize,long keepAliveTime,TimeUnit unit,                          BlockingQueue<Runnable> workQueue,RejectedExecutionHandler handler)`

  ​

+ `public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize,long keepAliveTime,TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler)`



**线程池有4个构造函数**，前3个构造函数都是调用**第4个7个参数**的构造函数；如果未指定线程池的**线程工厂**则会通过`Executors`的**defaultThreadFactory()**方法直接生成一个线程工厂；如果未指定**拒绝执行处理器-RejectedExecutionHandler**，则会使用**ThreadPoolExecutor**的`defaultHandler`默认处理器，**默认处理器**处理不能提交任务的方法是抛出`RejectedExecutionException`异常

```java
	
    /**
     *
     * @param corePoolSize 
     * 保持在线程池中的核心线程数，即使在闲置状态，线程池也会保持这个线程数目
     * 如果设置了 allowCoreThreadTimeOut 的核心线程超时时间，闲置时将不会
     * 保持核心线程数
     *
     * @param maximumPoolSize 
     * 线程池允许的最大线程数目
     *
     * @param keepAliveTime 
     * 当线程池没有任务执行时，如果线程池数目超过核心线程数，闲置线程超过keepAliveTime
     * 会终止；直到线程池数目小于等于核心线程池数目
     *
     * @param unit 
     * 参数keepAliveTime的时间单位，有7种类型：
     * NANOSECONDS、MICROSECONDS、MILLISECONDS、SECONDS、MINUTES、HOURS、DAYS
     * 分别为纳秒、微秒、毫秒、秒、分钟、小时、天
     *
     * @param workQueue 
     * 用于存放未被执行的任务队列
     *
     * @param threadFactory 
     * 创建执行线程的核心工厂
     *
     * @param handler 
     * 用于处理当任务满时的处理器
     * 
     */
    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler) {
        if (corePoolSize < 0 ||
            maximumPoolSize <= 0 ||
            maximumPoolSize < corePoolSize ||
            keepAliveTime < 0)
            throw new IllegalArgumentException();
        if (workQueue == null || threadFactory == null || handler == null)
            throw new NullPointerException();
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.workQueue = workQueue;
        this.keepAliveTime = unit.toNanos(keepAliveTime);
        this.threadFactory = threadFactory;
        this.handler = handler;
    }

```

由于**ThreadPoolExecutor**的前3个构造函数都调用第4个7参数的构造函数，从这个构造函数中可以看出创建线程池时并没有创建线程.一般使用`execute()`执行任务时才会创建线程，如果想要创建线程池后就创建好线程可以调用`public boolean prestartCoreThread()` 或  `public int prestartAllCoreThreads()` 方法启动核心线程



参数`BlockingQueue<Runnable> workQueue`为阻塞队列，`BlockingQueue`是一个接口，常见的实现类有：

+ **ArrayBlockingQueue**（创建时必须指定队列长度，否则编译出错）
+ **LinkedBlockingQueue**（可以不指定队列长度，没有指定默认为整型最大值）
+ **SynchronousQueue**（不会保存提交的任务，而是直接创建一个线程执行新来的任务）

使用`Executors`创建的线程池时主要用的就是`SynchronousQueue` 和`LinkedBlockingQueue`两种队列

----

<a name="execute">**public void execute(Runnable command)**</a>

**execute**方法是线程池的核心方法，向线程池提交一个任务，任务执行完后没有返回结果；如果想要在任务结束后得到返回结果，可以使用`AbstractExecutorService`的<a href="#submit">`submit(Callable<T> task)`</a>方法向线程池提交任务，任务执行后可以得到执行后的结果

```java
 public void execute(Runnable command) {
        if (command == null)
            throw new NullPointerException();
        /*
         *
         * 1.如果运行的线程数小于corePoolSize，尝试启动一个新线程，并把提交的任务
         * 作为第一个任务；调用 addWorker方法原子的检查运行状态和工作线程数
         * 
         *
         * 2. 如果任务被成功添加到队列, 仍然需要两次检测是否需要添加1个线程
         * (可能存在上次检测了但线程现在已经处于死亡状态)或者执行这个方法时
         * 线程池已经关闭；我们需要重新检测状态，如果线程已经停止需要进行撤销
         *
         * 3. 如果不能往队列里添加任务, 尝试创建一个新的线程，如果创建失败
         * 	  说明线程池处于关闭或者任务队列已满，则拒绝执行任务
         */
   
   		// ctl 为 AtomicInteger 原子整型
        int c = ctl.get();
   		// 判断工作线程数是否小于核心线程数
        if (workerCountOf(c) < corePoolSize) {
          	// 工作线程数小于核心线程数，创建一个核心线程,并把任务交给新创建的线程执行
          	// 创建工作线程成功则直接返回
            if (addWorker(command, true))
                return;
          	//创建工作线程不成功重新获取线程池线程数目
            c = ctl.get();
        }
   		// 如果线程池是运行状态且可以往工作队列提交任务则进行下一步操作
        if (isRunning(c) && workQueue.offer(command)) {
            int recheck = ctl.get();
          	// 再次检测线程池中的线程是否都在运行，如果不是运行状态则把刚才提交的任务从任务队列移除
          	// 并且拒绝执行任务，抛出一个异常
            if (! isRunning(recheck) && remove(command))
                reject(command);
          	// 如果线程池中没有线程，则尝试创建一个新的线程来执行提交的任务
            else if (workerCountOf(recheck) == 0)
                addWorker(null, false);
        }
   		// 如果不能往工作队列里再提交任务，则尝试创建一个新的Worker线程来执行任务
        else if (!addWorker(command, false))
            reject(command);
    }
```



<a name="addWorker">`addWorker(Runnable firstTask, boolean core)`</a>

创建Worker线程失败的原因主要有：

+ 线程池已经被关闭，提交的任务又不为null且工作队列为空
+ 线程池中的线程数目大于核心线程数或者大于线程池线程数目最大值，具体是核心线程数还是最大线程数跟据参数`boolean core`有关
+ 先创建的线程无法启动也会导致创建一个新的工作线程失败

```java
 private boolean addWorker(Runnable firstTask, boolean core) {
        retry:
        for (;;) {
          	// ctl 为 AtomicInteger 原子整型
            int c = ctl.get();
            int rs = runStateOf(c);

            // 判断线程池是否处于运行状态
          	// 如果线程池不是运行状态状态且为shutdown状态，提交的任务不为null或队列为空将导致创建一个工作线程失败
          
            if (rs >= SHUTDOWN &&
                ! (rs == SHUTDOWN &&
                   firstTask == null &&
                   ! workQueue.isEmpty()))
                return false;

          	
            for (;;) {
              	// 获取线程池中线程数量
                int wc = workerCountOf(c);
                if (wc >= CAPACITY ||
                    wc >= (core ? corePoolSize : maximumPoolSize))
                    return false;
              	// 线程池数量未超过指定的值以CAS方式把线程数目加1并退出循环
                if (compareAndIncrementWorkerCount(c))
                    break retry;
                c = ctl.get();  // Re-read ctl
                if (runStateOf(c) != rs)
                    continue retry;
                // else CAS failed due to workerCount change; retry inner loop
            }
        }
		
   		// 添加线程池中线程计数器后开始创建Worker线程
        boolean workerStarted = false;
        boolean workerAdded = false;
        Worker w = null;
        try {
            w = new Worker(firstTask);
            final Thread t = w.thread;
            if (t != null) {
                final ReentrantLock mainLock = this.mainLock;
                mainLock.lock();
                try {
                    // Recheck while holding lock.
                    // Back out on ThreadFactory failure or if
                    // shut down before lock acquired.
                    int rs = runStateOf(ctl.get());

                  	// 判断线程池是否被shutdown并且提交的任务为null或者线程池还在运行状态
                    if (rs < SHUTDOWN ||
                        (rs == SHUTDOWN && firstTask == null)) {
                        if (t.isAlive()) // precheck that t is startable
                            throw new IllegalThreadStateException();
                      	//把新创建的线程添加到线程池里的线程集合中  
                      	workers.add(w);
                        int s = workers.size();
                        //修改线程池线程数量
                        if (s > largestPoolSize)
                            largestPoolSize = s;
                        workerAdded = true;
                    }
                } finally {
                    mainLock.unlock();
                }
              	// 如果新创建的Worker线程成功添加到线程池集合中则启动这个线程
                if (workerAdded) {
                    t.start();
                    workerStarted = true;
                }
            }
        } finally {
            if (! workerStarted)
                addWorkerFailed(w);
        }
        return workerStarted;
    }
```



<a name="setKeepAliveTime">`setKeepAliveTime(long time, TimeUnit unit)`</a>

**setKeepAliveTime**方法可以用于调整线程池中线程超时时间，当新设置的时间比原来超时时间短时时会让本来没有超时的闲置线程提前销毁

```java
    public void setKeepAliveTime(long time, TimeUnit unit) {
        if (time < 0)
            throw new IllegalArgumentException();
        if (time == 0 && allowsCoreThreadTimeOut())
            throw new IllegalArgumentException("Core threads must have nonzero keep alive times");
        long keepAliveTime = unit.toNanos(time);
        long delta = keepAliveTime - this.keepAliveTime;
        this.keepAliveTime = keepAliveTime;
        if (delta < 0)
            interruptIdleWorkers();
    }

```



<a name="submit">`submit(Callable<T> task)`</a>

```java
    public <T> Future<T> submit(Callable<T> task) {
        if (task == null) throw new NullPointerException();
        RunnableFuture<T> ftask = newTaskFor(task);
        execute(ftask);
        return ftask;
    }
```

<a href="#execute">back to execute</a>



-----

### 任务拒绝策略

+ 丢弃任务并抛出 `RejectedExecutionException` 异常
+ 丢弃任务不抛异常
+ 丢弃队列最前面的任务，尝试重新提交任务
+ 由调用线程处理提交的任务

