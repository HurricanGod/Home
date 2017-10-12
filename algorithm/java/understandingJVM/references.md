## <a name="ref">引用</a>

---

**按照引用强弱分为**：

* **强引用**，类似于`Object obj = new Object()` 的引用就是强引用，只要强引用还在，垃圾收集器永远不会回收被引用的对象
* **软引用** ，用于描述一些还有用但并非必需的对象，软引用关联的对象，系统将要发生**内存溢出异常**之前会将`软引用关联的对象`列入回收范围进行第二次回收
* **弱引用** ，用来描述非必需的对象，被弱引用关联的对象只能生存到下一次垃圾收集发生之前
* **虚引用** ，也称幻影引用，最弱的一种引用关系，**无法通过虚引用得到对象的实例** ，为1个对象设置**虚引用关联**的目的是：`在这个对象被垃圾收集器回收时收到一个系统通知`



**虚引用的使用**：

![]()

![]()

```java
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

/**
 * Created by NewObject on 2017/10/7.
 */
public class PhantomRef {

    public static void main(String[] args) throws InterruptedException {
        Byte[] obj = new Byte[1024*1024]; //申请1M空间
        // 创建虚引用对象，指向obj
        ReferenceQueue<Byte[]> refqueue = new ReferenceQueue<>();
        PhantomReference<Byte[]> phantomRef = new PhantomReference<Byte[]>(obj, refqueue);
        // 第1次建议进行垃圾回收
        System.gc();
        Thread.sleep(3000);
        System.out.println("\nphantomRef.isEnqueued() = " + phantomRef.isEnqueued());
        // 将 obj 的强引用清除再建议进行垃圾回收
        System.out.println("解除 obj 的强引用并建议 gc");
        obj = null;

        System.gc();
        Thread.sleep(3000);
        System.out.println("phantomRef.isEnqueued() = " + phantomRef.isEnqueued());
        // 把虚引用清除在建议垃圾回收
        System.out.println("\nrun phantomRef.clear()");
        phantomRef.clear();
        System.gc();
        Thread.sleep(3000);
        System.out.println();
    }
}

```

