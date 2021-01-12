# <a name="top">AQS</a>

`AQS` 是 `AbstractQueuedSynchronizer`的简称。是Java并发框架的底层，AQS的主要实现类有：

- `java.util.concurrent.locks.ReentrantLock.FairSync`
- `java.util.concurrent.locks.ReentrantLock.NonfairSync`
- `java.util.concurrent.locks.ReentrantReadWriteLock.FairSync`
- `java.util.concurrent.locks.ReentrantReadWriteLock.NonfairSync`
- `java.util.concurrent.Semaphore.FairSync`
- `java.util.concurrent.Semaphore.NonfairSync`
- `java.util.concurrent.CountDownLatch.Sync`
- `java.util.concurrent.ThreadPoolExecutor.Worker`



`AQS`中定义的成员变量 `state` 用于表示是否获取到锁。`state=0`表示没有线程占有锁，`state>0`表示锁被线程占用。



----

## <a name="reentrantLock">ReentrantLock</a>

`ReentrantLock`有两个构造函数，默认创建的为**非公平锁**，想要创建公平锁需要在构造函数中指定参数 `fair` 为 true。



### <a name="reentrantLock-lock">`ReentrantLock#lock`源码解析</a>

```java
public void lock(){
    sync.lock();
}
```

`sync` 是 `AQS` 的实现类，在  `ReentrantLock` 中有两个实现，分别是 `FairSync` 和 `NonfairSync`，`sync` 的类型由创建 `ReentrantLock`  时是否指定公平锁决定的。以下是两种实现：

+ <a href="#fairSync-lock">`ReentrantLock.FairSync#lock`</a>
+ <a href="#nonfairSync-lock">`ReentrantLock.NonfairSync#lock`</a>





<br/>

#### <a name="fairSync-lock">`ReentrantLock.FairSync#lock`</a>

```java
final void lock(){
    acquire(1);
}
```





#### <a name="nonfairSync-lock">`ReentrantLock.NonfairSync#lock`</a>

```java
final void lock(){
    // cas设置 state, 成功表示当前线程获取到锁
    if (compareAndSetState(0, 1))
        setExclusiveOwnerThread(Thread.currentThread());
    else
        // cas 失败
        acquire(1);
}
```

`NonfairSync#lock`相比公平锁的实现多了一个 `cas` 尝试获取锁的逻辑，此处是非公平锁实现的关键代码。



<br/>

#### <a name="AQS-acquire">`AbstractQueuedSynchronizer#acquire`</a>

```java
public final void acquire(int arg){
    // tryAcquire(arg) 这里用到了模版方法，具体的实现在子类
    if (!tryAcquire(arg) &&
        acquireQueued(
            addWaiter(Node.EXCLUSIVE), arg) ){
        selfInterrupt();
    }     
}
```

`tryAcquire(arg)`的实现在子类，该方法主要是通过 `cas` 方式尝试修改 `state` 状态，公平锁与非公平锁对应的实现如下：

+ <a href="#FairSync-tryAcquire">`ReentrantLock.FairSync#tryAcquire`</a>
  + <a href="#AQS-hasQueuedPredecessors">`AbstractQueuedSynchronizer#hasQueuedPredecessors`</a>
+ <a href="#NonfairSync-tryAcquire">`ReentrantLock.NonfairSync#tryAcquire`</a>



<br/>

<a href="#AQS-acquireQueued">`acquireQueued()`</a>的功能：

+ 把进入等待队列的线程挂起
+ 等待队列中当前节点被唤醒时，将当前节点设置为等待队列的头节点
+ 该方法返回当前线程是否被打断，在**线程阻塞**期间打断线程不会响应，









----

<a name="FairSync-tryAcquire">`ReentrantLock.FairSync#tryAcquire`</a>

```java
protected final boolean tryAcquire(int acquires){
    final Thread current = Thread.currentThread();
    int c = getState();
    if (c == 0) {
        // hasQueuedPredecessors() 是公平锁实现的关键点
        if (!hasQueuedPredecessors() &&
            compareAndSetState(0, acquires)){
            setExclusiveOwnerThread(current);
            return true;
        }
    }
    
    else if (current == getExclusiveOwnerThread()){
        int nextc = c + acquires;
        if (nextc < 0) throw new Error("Maximum lock count exceeded");
        setState(nextc);
        return true;
    }
    return false;
}
```



<p align="right"><a href="#AQS-acquire">AQS-acquire</a>&nbsp|&nbsp<a href="#reentrantLock-lock">ReentrantLock-lock</a></p>



<a name="NonfairSync-tryAcquire">`ReentrantLock.NonfairSync#tryAcquire`</a>

```java
protected final boolean tryAcquire(int acquires){
    final Thread current = Thread.currentThread();
    int c = getState();
    if (c == 0) {
        if (compareAndSetState(0, acquires)) {
            setExclusiveOwnerThread(current);
            return true;
        }
    }
    else if (current == getExclusiveOwnerThread()) {
        int nextc = c + acquires;
        if (nextc < 0) // overflow
            throw new Error("Maximum lock count exceeded");
        setState(nextc);
        return true;
    }
    return false;
}
```



<p align="right"><a href="#AQS-acquire">AQS-acquire</a>&nbsp|&nbsp<a href="#reentrantLock-lock">ReentrantLock-lock</a></p>







----

<a name="AQS-hasQueuedPredecessors">`AbstractQueuedSynchronizer#hasQueuedPredecessors`</a>

```java
public final boolean hasQueuedPredecessors() {
    Node t = tail;
    Node h = head;
    Node s;
    return h != t && (
        (s = h.next) == null || 
        s.thread != Thread.currentThread()
    );
}
```

`hasQueuedPredecessors()`有几个值得思考的问题：

+ 什么情况下会出现`h == t`？
+ 什么场景下会满足 `(s = h.next) == null `





<p align="right"><a href="#AQS-acquire">AQS-acquire</a>&nbsp|&nbsp<a href="#FairSync-tryAcquire">FairSync-tryAcquire</a></p>



----

<a name="AQS-acquireQueued">`AbstractQueuedSynchronizer#acquireQueued`</a>

```java
final boolean acquireQueued(final Node node, int arg) {
    boolean failed = true;
    try {
        boolean interrupted = false;
        for (;;) {
            // 获取等待队列中当前节点的前驱节点
            final Node p = node.predecessor();
            // head 表示当前持有锁的线程
            // 当前线程如果cas设置state成功表示获取到锁，把head设置为当前节点
            if (p == head && tryAcquire(arg)) {
                setHead(node);
                p.next = null; // help GC
                failed = false;
                return interrupted;
            }
            if (shouldParkAfterFailedAcquire(p, node) &&
                parkAndCheckInterrupt()
               )
                interrupted = true;
        }
    }finally {
        if (failed)	cancelAcquire(node);
    }
}
```





<a name="AQS-shouldParkAfterFailedAcquire">`AbstractQueuedSynchronizer#shouldParkAfterFailedAcquire`</a>

```java
private static boolean shouldParkAfterFailedAcquire(Node pred, Node node){
    int ws = pred.waitStatus;
    // This node has already set status asking a release to signal it, so it can safely park.
    if (ws == Node.SIGNAL)	return true;
    if (ws > 0) {
        do {
            node.prev = pred = pred.prev;
        }while (pred.waitStatus > 0);
        pred.next = node;
    }
    else{
        compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
    }
    return false;
}
```

+ `Node#waitStatus > 0` 表示等待队列中的节点已经取消了，一般发生在线程被 `interrupt`的场景， 该节点需要从等待队列中移除。从 `prev` 节点一直往前找，找到第一个 `Node#waitStatus <= 0` 的节点，重新整理链表并返回 `false`(**表示不用把当前线程阻塞**) 
+ `else`分支表示 `Node#waitStatus` 为0(默认值)或为-2，采用cas的方式将 `Node#waitStatus`改为 -1(`Node.SIGNAL`)



这里就有以下几个问题值得思考：

+ 什么情况下会执行`else`分支？换句话说什么时候 `Node#waitStatus` 为0(默认值)或-2



<p align="right"><a href="#AQS-acquireQueued">AQS-acquireQueued</a>&nbsp|&nbsp<a href="#AQS-acquire">AQS-acquire</a></p>

---

<a name="AQS-parkAndCheckInterrupt">`AbstractQueuedSynchronizer#parkAndCheckInterrupt`</a>

```java
private final boolean parkAndCheckInterrupt() {
    LockSupport.park(this);
    return Thread.interrupted();
}
```





<p align="right"><a href="#AQS-acquire">AQS-acquire</a>&nbsp|&nbsp<a href="#reentrantLock-lock">ReentrantLock-lock</a></p>



-----

`AbstractQueuedSynchronizer#addWaiter`

```java
private Node addWaiter(Node mode) {
    Node node = new Node(Thread.currentThread(), mode);
    // Try the fast path of enq; backup to full enq on failure
    Node pred = tail;
    if (pred != null) {
        node.prev = pred;
        if (compareAndSetTail(pred, node)) {
            pred.next = node;
            return node;
        }
    }
    enq(node);
    return node;
}

private Node enq(final Node node) {
    for (;;) {
        Node t = tail;
        if (t == null) { // Must initialize
        	if (compareAndSetHead(new Node()))
                tail = head;
        }else{
            node.prev = t;
            if (compareAndSetTail(t, node)) {
                t.next = node;
                return t;
            }
        }
    }
}
```



<p align="right"><a href="#AQS-acquire">AQS-acquire</a>&nbsp|&nbsp<a href="#reentrantLock-lock">ReentrantLock-lock</a></p>



----

### <a name="reentrantLock-unlock">`ReentrantLock#unlock`源码解析</a>



<a name="ReentrantLock-unlock">`ReentrantLock#unlock`</a>

```java
public void unlock() {
    sync.release(1);
}
```





<a name="AQS-release">`AbstractQueuedSynchronizer#release`</a>

+ <a href="#Sync-tryRelease">`ReentrantLock.Sync#tryRelease`</a>
+ <a href="#AQS-unparkSuccessor">`AbstractQueuedSynchronizer#unparkSuccessor`</a>

```java
public final boolean release(int arg) {
    if (tryRelease(arg)) {
        Node h = head;
        if (h != null && h.waitStatus != 0)// ① 
            unparkSuccessor(h);
        return true;
    }
    return false;
}
```

 ① 处 `h == null` 表示等待队列的头节点为null，说明压根就没有其它线程等待锁，因此可以return



 ① 处 `h.waitStatus == 0` 什么情况下会出现？

尝试加锁<a href="#AQS-shouldParkAfterFailedAcquire">`AbstractQueuedSynchronizer#shouldParkAfterFailedAcquire`</a>时，每个进入等待队列的节点会把前驱节点的 `waitStatus ` 改为 -1，只有**等待队列的最后一个节点**的`waitStatus` 是没有其它线程去设置的，因此最后一个节点的`waitStatus` 就是`int`的默认值0



<br/>

<a name="Sync-tryRelease">`ReentrantLock.Sync#tryRelease`</a>

```java
protected final boolean tryRelease(int releases) {
    int c = getState() - releases;
    if (Thread.currentThread() != getExclusiveOwnerThread())
        throw new IllegalMonitorStateException();
    boolean free = false;
    if (c == 0) {
        free = true;
        setExclusiveOwnerThread(null);
    }
    setState(c);
    return free;
}
```



<p align="right"><a href="#Sync-tryRelease">Sync-tryRelease</a>&nbsp|&nbsp<a href="#AQS-release">AQS-release</a></p>

<br/>

<a name="AQS-unparkSuccessor">`AbstractQueuedSynchronizer#unparkSuccessor`</a>

```java
private void unparkSuccessor(Node node) {
    int ws = node.waitStatus;
    if (ws < 0)
        compareAndSetWaitStatus(node, ws, 0); //① 这行代码的意义？
    Node s = node.next;
    if (s == null // ②
        || s.waitStatus > 0 // ③
       ) {
        s = null;
        for (Node t = tail; t != null && t != node; t = t.prev)// ④
            if (t.waitStatus <= 0)
                s = t;
    }
    if (s != null)
        LockSupport.unpark(s.thread);
}
```

`unparkSuccessor()`的入参是等待队列的**头节点**，该方法主要是唤醒被阻塞的线程。

代码①：

代码②：等待队列头节点的后一个节点为null的情况：head的后继节点阻塞指定了时间后获取到CPU执行权，恰好<a href="#Sync-tryRelease">`ReentrantLock.Sync#tryRelease`</a> `state` 状态值改为0了，线程获取锁并设置了等待队列头节点，即执行了以下代码

```java
if (p == head && tryAcquire(arg)){
    setHead(node);
    p.next = null; // help GC
    failed = false;
    return true;
}
```



代码③：等待队列头节点的后一个节点的 `waitStatus>0`，说明该线程在等待锁时被打断了，不需要唤醒该线程

代码④：从队列尾部往前找，找最靠近头节点且`waitStatus<=0`的节点




<p align="right"><a href="#AQS-unparkSuccessor">AQS-unparkSuccessor</a>&nbsp|&nbsp<a href="#AQS-release">AQS-release</a></p>



----

### <a name="lockInterruptibly">`ReentrantLock#lockInterruptibly`</a>

`lockInterruptibly()`方法在等待锁期间如果被其它线程 `interrupt`，当前线程并不会立即响应中断，只有等当前线程获取到锁时才会抛出 `InterruptedException`



```java
public void lockInterruptibly() throws InterruptedException {
    sync.acquireInterruptibly(1);
}
```





<br/>

<a name="acquireInterruptibly">`AbstractQueuedSynchronizer#acquireInterruptibly`</a>

```java
public final void acquireInterruptibly(int arg) throws InterruptedException {
    // 如果当前线程被打断了直接抛出一个 InterruptedException
    if (Thread.interrupted())
        throw new InterruptedException();
    if (!tryAcquire(arg)) // ① 尝试cas方式获取锁
        doAcquireInterruptibly(arg); // ②
}
```

① 具体实现在<a href="#FairSync-tryAcquire">`ReentrantLock.FairSync#tryAcquire`</a>或<a href="#NonfairSync-tryAcquire">`ReentrantLock.NonfairSync#tryAcquire`</a>



② <a href="#doAcquireInterruptibly">`doAcquireInterruptibly()`</a> 跟前面的<a href="#AQS-acquireQueued">`acquireQueued()`</a>比较相似，不同点在于前者如果被中断直接**抛出异常**。





-----

<a name="doAcquireInterruptibly">`AbstractQueuedSynchronizer#doAcquireInterruptibly`</a>

```java
private void doAcquireInterruptibly(int arg) throws InterruptedException{
    final Node node = addWaiter(Node.EXCLUSIVE);
    boolean failed = true;
    try {
        for (;;) {
            final Node p = node.predecessor();
            if (p == head && tryAcquire(arg)) {
                setHead(node);
                p.next = null; // help GC
                failed = false;
                return;
            }
            if (shouldParkAfterFailedAcquire(p, node) &&
                 parkAndCheckInterrupt()){
                throw new InterruptedException();
            }
        }
    }finally {
        if (failed)
            cancelAcquire(node);
    }
    
}
```

