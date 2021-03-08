# <a name="top">字符串</a>

+ <a href="#String">String</a>

+ StringBuffer

+ StringBuilder







----

## <a name="String">String</a>

`String`类定义：

```java
public final class String  implements java.io.Serializable, 
		Comparable<String>, CharSequence{
            
	private final char value[];
            
    private int hash;
    
    private static final long serialVersionUID = -6849794470754667710L;
    
    // ......
}
```



如何计算 `String` 占用的内存空间？

对象在内存中的布局可以分为3块区域：

+ 对象头 —— 对象头分为2部分，分别为 `Mark Word` 和 **类型指针**
  + `Mark Word`里存放着对象自身的运行时数据，比如对象哈希码、GC分代年龄、锁标志位、线程持有的锁、偏向线程ID、偏向时间戳等
  + **类型指针** —— 指向类元数据信息的指针
+ 实例数据
+ 直接填充

![Java对象内存布局](https://github.com/HurricanGod/Home/blob/master/javase/img/%E5%AF%B9%E8%B1%A1%E5%86%85%E5%AD%98%E5%B8%83%E5%B1%80.png)
![Java对象内存布局](https://s3.ax1x.com/2021/01/05/sFSimF.png)


空 `String` 的内存大小：

```
字符串引用指针（4字节） + 对象头（8字节） + long静态变量 （8字节）+ int成员变量（4字节） 
+ 字符数组对象头（8字节）+ 数组长度（4字节） + 字符数组引用指针（4字节） = 40字节
```



非空 `String` 的内存大小为： 40 + 2n （n为字符串长度）



-----

### 常见面试题

+ 以下语句创建了多少个 `String` 对象？

```java
String s = new String("hello")
```

> 可能1个，也可能2个

通过`new`关键字一定会创建出一个；

如果字符串常量池中不存在`"hello"`，则会在常量池中创建字符串`"hello"`对象，否则会直接使用常量池的`"hello"`对象。



+ `String`为什么被设计为 `final`?

  > 1. 基于安全的考虑，字符串内容不可变，多线程场景下不用考虑字符串内容是否改变
  > 2. 更高效，设计为不可变可以缓存字符串（字符串常量池）









```java
String s1 = new String("hello");
String s2 = new String("hello");
```

`s1 == s2` 的比较结果肯定是`false`，因为它们分别是堆中两个不同地址的对象

----

**StringBuffer**

+ `append()`方法的效率并不会好于`String`的`+`
+ 尽量不要使用`new`创建字符串
+ 在**编译期间**能够确认字符串值的情况下使用`+`的效率最高
+ 避免使用`+=`来构造字符串
+ 在声明`StringBuffer`对象的时候指定合适的容量，**避免默认容量不够时复制数组开销**

