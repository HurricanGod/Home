## <a name="object">Object类方法使用</a>

-----
![](https://github.com/HurricanGod/Home/blob/master/img/uml-object.png)

+ <a href="#wait">**wait(long timeout)**</a>
+ <a href="#notify">**notify()**</a>
+ <a href="#finalize">**finalize()**</a>
+ <a href="#clone">**clone()**</a>



<br/>

### <a name="wait">wait(long timeout)</a>

```java

public final native void wait(long timeout) throws InterruptedException;

public final void wait() throws InterruptedException {
        wait(0);
    }
```

在线程中调用此方法将导致此线程`进入等待状态`直到其它线程调用`notify()`方法或`notifyAll()`方法或者超过指定的时间；**调用**  `wait()`  方法时**当前线程必须拥有此对象的监视器，即当前线程要有当前对象的对象锁** ，调用后当前线程会放弃**同步操作** ，即放弃对象锁，可供其它线程获取该对象的**对象锁** ；如果当前线程在等待的时候**被打断**，将会抛出一个`InterruptedException` 异常；不过这个异常不是马上抛出，而是等到**当前线程重新获得该对象的锁**时抛出！



-----

### <a name="notify">notify( )</a>

```java
public final native void notify();
```

唤起一个等待这个对象监视器的线程，如果有多个线程等待这个对象的监视器，选择其中一个线程；**被唤醒的线程不会马上执行，需要等到当前线程放弃这个对象的锁，即执行完synchronized同步块的内容** ，被唤醒的线程以常规的方式与其它线程对此对象进行同步竞争。

调用`notify()`方法时，当前线程必须拥有对象的**监测器**，即拥有**对象锁**，线程拥有对象锁的方式有3种：

+  通过执行这个对象的同步实例方法
+  通过执行`synchronized`块语句
+  对于类型对象，通过执行该类的同步静态方法获得对象锁



如果当前线程没有对象的**监视器** 执行`wait()` 或 `notify()` 或 `notifyAll()` 方法将会抛出`java.lang.IllegalMonitorStateException` 异常



-----

###  <a name="finalize">finalize()</a>

```java
protected void finalize() throws Throwable { }
```

如果对象进行因此可达性分析后发现没有与`GC Roots`相连接的引用链，该对象将会被第一次标记并且进行一次筛选，筛选的条件是**对象是否调用finalize()方法** ；

- 当对象没有覆写`finalize()`方法或者`finalize()` 已经被虚拟机调用过，则认为`finalize()`没有必要执行
- 当认为有必要执行`finalize()`方法时，对象将会被放置在`F-Queue`队列中，并在稍后由虚拟机自动建立的，低优先级的**Finalizer**线程去执行，该线程会触发`finalize()`方法，但不承诺会等待它运行到结束
- `finalize()`方法是对象逃脱死亡命运的最后一次机会，**GC**会对**F-Queue**中的对象进行第二次小规模标记，如果对象在`finalize()`方法中成功拯救了自己，**即当前对象与引用链上的任一对象建立关联** ，**GC**在第二次标记时就会**把成功拯救自己的对象移除即时回收集合**
- 任何一个对象的`finalize()`方法都**只会被系统自动调用一次**


-----

先看一段代码：

```java
public class ObjectWaitMethod {

    private static final Object obj = new Object();
    public static void main(String[] args) {
        Thread thread1 = new Thread(){
            @Override
            public void run() {
                super.run();
                System.out.println(Thread.currentThread().getName() + " start!");
                try {
                    obj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " end!");
            }
        };
        thread1.setName("线程1");

        Thread thread2 = new Thread(){
            @Override
            public void run() {
                super.run();
                System.out.println(Thread.currentThread().getName() + " start!");
                obj.notify();
                System.out.println(Thread.currentThread().getName() + " end!");
            }
        };
        thread2.setName("线程2");

        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main ending");
    }
}
```

这段代码虽然语法上能通过检查，但运行时会抛出`java.lang.IllegalMonitorStateException` 异常，因为在线程**run方法**里`wait()`和`notify()`方法没有进行**同步机制**获取对象的**监测器**

![](https://github.com/HurricanGod/Home/blob/master/img/wait-pro1.png)

**修改版本1**

![](https://github.com/HurricanGod/Home/blob/master/img/wait-pro2.png)



上面代码会出现`线程1`一直处于**等待**状态的情况，当`线程2`先于`线程1`执行时，`wait()`还没有调用而`线程2`的`notify()`已经调用完毕，因而会出现**线程1一直等待的状态**

**最终版本**
![](https://github.com/HurricanGod/Home/blob/master/img/wait-pro3.png)

------

### <a name="clone">clone(  )</a>

```java
protected native Object clone() throws CloneNotSupportedException;
```

`clone()`方法是**Object**的保护方法，没有实现`Cloneable` 接口的类不能直接调用；调用`clone()`方法时，该方法会先检查class是否实现了`Cloneable` 接口，只有实现了这个接口才能调用`clone`方法，否则会抛出`CloneNotSupportedException` 异常

+ **Object**类中的默认的实现是**浅复制**，也就是表面复制
+ 如果需要实现**深复制** ，必须对类中可变域生成新实例



**不实现** `Cloneable` 接口直接调用`clone()方法`示例 ：

![](https://github.com/HurricanGod/Home/blob/master/img/clone(%20).png)



**实现接口再调用** `clone()`方法

```java
public class CloneDemo implements Cloneable{

    public User user;

    public CloneDemo() {
        this.user = new User(1, "hurrican", "acachqu66@163.com");
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public static void main(String[] args) {
        CloneDemo demo = new CloneDemo();
        try {
            Object bk = demo.clone();
            System.out.println(bk.getClass().getCanonicalName());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

    }
}

class User{
    public Integer id;
    public String name;
    public String email;

    public User(Integer id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public User() {
    }
}
```

![](https://github.com/HurricanGod/Home/blob/master/img/clone(1).png)



-----

### hashCode()

`hashCode()`方法会返回一个对象的哈希码，`String`类对象的值如果相等则会返回相同的哈希码