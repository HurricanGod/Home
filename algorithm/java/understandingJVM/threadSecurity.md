## 线程安全

----

**什么是线程安全** ：

当多个线程访问一个对象时，如果不用考虑这些线程在运行时环境下的调度和交替执行，也不需要进行额外的同步，或者在调用方进行任何其它的协调操作，调用这个对象的行为都可以获得正确的结果，那这个对象就是线程安全的。

另一种线程安全的说法：多线程环境下每次运行的结果与单个线程运行的结果是一样的。

----

按照线程安全程度由强到弱排序为：

+ 不可变
+ 绝对线程安全
+ 相对线程安全
+ 线程兼容
+ 线程对立

<hr> 

**相对线程安全** ：就是通常意义上的线程安全，保证对这个对象单独操作是线程安全的。例如：Hashtable、Vector等都是相对线程安全的。

**线程兼容** ：指对象本身不是线程安全的，通过调用端正确地使用同步手段保证对象在并发环境下可以安全地使用

----

## 线程安全的实现方法

1. 互斥同步
2. 非阻塞同步
3. 无同步方案

### 互斥同步

**同步**指多个线程并发访问共享数据，保证共享数据在1个时刻只被1个线程使用。

互斥是实现同步的一种手段，``临界区、互斥量、信号量``是互斥的主要实现方式。



Java中最基本实现互斥的手段 —— `synchronized`关键字

+ 经过编译后`synchronized`同步块前后分别形成`monitorenter`和`monitorexit`两个字节码指令
+ `monitorenter`和`monitorexit`字节码指令都***需要一个references类型**来指明要锁定和解锁的对象
+ 如果`synchronized`是加在类方法或对象方法上的，则通过该方法找到类对象或Class对象对其加锁。



synchronized同步块对同一个线程来说是可重入的，不会出现自己把自己锁死的问题，同步块在已进入线程执行完之前会阻塞其它线程进入，Java线程的实现是通过轻量级进程实现的，线程的阻塞、唤醒调度都要依靠系统调用，需要在用户态与内核态间切换，代价大，因此`synchronized`是Java里的**重量级锁**。



实现同步的其它方法：使用`java.util.concurrent`包中的**重入锁（ReentrantLock）**,使用``lock()和unlock()方法配合try/finally``语句块完成，`ReentrantLock`增加了高级功能：

- 等待可中断（一直得不到锁可放弃等待）
- 可实现公平锁（按照申请锁的时间顺序获得锁，synchronized为非公平锁）
- 锁可以绑定多个条件（同时对多个对象加锁）



### 非阻塞同步

***采用乐观并发策略遇到竞争时不必把线程挂起，采用其它补救措施的方法称为非阻塞同步***。<br/>

------

`悲观并发策略：`只要不做正确的同步措施，并发肯定会出问题，无论共享数据是否真的会出现竞争，都要进行加锁、用户态核心态转换、维持锁计数器和检查是否有阻塞的线程需要唤醒。

`乐观并发策略：` 先进行操作，如果没有出现其它线程争用共享数据，操作成功；若出现其它线程争用共享数据则采用**补救措施**（常用的是不断等待，直到成功为止）



### 无同步方案

**可重入代码：** 可以在代码任意时刻中断，转去执行另一段代码（包括递归调用），而在控制权返回后程序不会出现任何错误。



**注**

- 所有可重入代码都是线程安全的，但并非所有线程安全的代码都是可重入的
- 判断代码具备可重入性的方法：
  - 如果个方法的返回结果是可预测的，只要输入了相同的数据就能返回相同的结果，那就是可满足可重入的要求，也是线程安全的

---

**线程本地存储**

可以通过`java.lang.ThreadLocal`类实现线程本地存储功能。

> ThreadLocal对象里有ThreadLocalMap对象，该对象存储了一组以threadLocalHashCode为键，本地线程变量为值的K-V值对，ThreadLocal对象就是当前线程ThreadLocalMap的访问入口，每个ThreadLocal对象都有唯一的threadLocalHashCode值，通过这个值可以找到本地线程变量



## 锁优化

## 自旋锁和自适应自旋

互斥同步对性能最大的影响为当存在竞争时需要把线程阻塞，通过系统调用挂起线程和恢复线程都需要在内核态实现。

在多处理机中多个线程并行执行，如果存在竞争可以让后面加锁的线程“稍微”等待一下，等前面那个线程把锁释放，**在等待的这段时间后加锁的线程不放弃cpu的执行权，而是执行空操作**，这就是`自旋锁`。等待时间有一定的限度，超过等待时间未获得锁则挂起线程



``自适应自旋锁``： 自旋锁等待时间不固定由前一次在同一个锁上的自旋时间及锁的拥有者的状态决定

----

### 锁消除

锁消除指编译器在运行时，**对一些代码要求同步的但检测到不存在数据竞争**的锁进行消除。

例如：

```java
String str = str1 + str2 + str3;
```

在单线程环境下上面语句不存在资源竞争关系所以`javac`会将其用`StringBuilder`的`append()`方法进行优化。

------

### 锁粗化

加锁操作出现在循环体内，即使没有竞争频繁**加锁**和**释放锁**会导致不必要的性能开销，虚拟机探测到对同一个对象进行频繁的同步互斥操作会把加锁的同步范围扩大（粗化）到整个序列的外部。

-----

### 轻量级锁

进入同步块时如果同步对象没有被锁定，虚拟机在**当前线程栈帧**中建立`锁记录Lock Record `空间，用于存储对象当前的**标记字Mark Word**的拷贝，然后使用`CAS(比较并交换)`操作将**Mark Word**更新指向`Lock Record`的指针：

- 如果更新成功，线程拥有该对象的**轻量级锁**
- 更新失败，检测对象的**Mark Word**是否指向当前线程的栈帧
  - **Mark Word**若指向当前线程的栈帧，则直接进入同步块
  - 否则说明对象已经被其它线程抢占，**如果两个以上线程争用同一个锁，轻量级锁失效，膨胀为重量级锁** 



**解锁过程**

+ 若**Mark Word**指向线程的锁记录，用`CAS`操作把当前的**Mark Word**替换为线程锁记录中保存的**对象当前的Mark Word拷贝**
  + 若替换成功，同步过程完成
  + 替换失败，说明还有其它线程尝试获得该锁，释放锁的同时唤醒其它线程



如果没有竞争，轻量级锁使用`CAS`操作避免了互斥量的开销；如果存在竞争，除了互斥量的开销还有额外的`CAS`操作，有竞争情况下**轻量级锁**比重量级锁还慢。

------

### 偏向锁

偏向锁是在无竞争情况下**把整个同步都消除**，连`CAS`操作都不做。



如果1个线程在执行过程中，该锁没有被其它线程获取，则持有偏向锁的线程将不在进行同步；当另一个线程尝试去获取这个锁时，**偏向锁**宣告结束。根据锁状态撤销偏向锁后恢复到**轻量级锁定**或**未锁定状态**



----

一个线程发生异常时会怎样？

​	如果异常没有被捕获该线程将会停止执行。`Thread.UncaughtExceptionHandler`是用于处理未捕获异常造成线程突然中断情况的一个内嵌接口。当一个未捕获异常将造成线程中断的时候JVM会使用`Thread.getUncaughtExceptionHandler()`来查询线程的`UncaughtExceptionHandler`并将线程和异常作为参数传递给**handler**的`uncaughtException()`方法进行处理。



