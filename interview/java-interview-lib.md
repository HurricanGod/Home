# <a name="top">Java面试题</a>



+ <a href="#shadow-deep-clone">深拷贝与浅拷贝</a>





----

+ `Java`中有几种数据类型，各自占多少字节？







+ `String`类能被继承吗？`String`类底层怎样实现的？与`StringBuffer`、`StringBuilder`有什么区别？









+ 类的实例化顺序，比如父类静态数据、构造函数、字段、子类静态数据、构造函数、字段执行顺序？







- 多线程顺序输出 `A1B2C3`





+ Collections.sort底层排序使用的算法？



+ Arrays.sort底层排序使用的算法？



- `ThreadLocal` 有没有内存泄漏的问题，为什么？



- 下列3种业务场景该如何选择线程池

  <details>

  - 高并发、任务执行时间短
  - 并发不高，任务执行时间长
  - 高并发，任务执行时间长

  </details>



+ 并发场景下使用的 `ConcurrentHashMap`实现原理是什么？创建时默认容量是多少？如何进行扩容？`JDK8`后为什么放弃分段锁机制？并发场景下如何实现Map中元素个数的计算的？`LinkedHashMap`是怎样保存有序的？



+ `HashSet`的实现原理？



+ 继承和聚合的区别？



+ 如何在字符流与字节流之间转换？







+ 常见的`IO`模型有哪些？各自有什么特点？`NIO`、`BIO`、`AIO`分别是什么？什么是`reactor模型`？









+ 反射创建实例的三种方式是什么？`Class.forName()`和`ClassLoader`





----

## <a name="dynamic-proxy">动态代理</a>



**动态代理** 常用的实现方式是反射，除此之外还可以使用 `CGLib`实现(`CGLib`基于ASM，操作字节码)。

`JDK Proxy` 与 `CGLib` 的区别：

+ `JDK Proxy` 是 Java 语言自带的功能，无需通过加载第三方类实现
+ Java 对 `JDK Proxy` 提供了稳定的支持，并且会持续的升级和更新 `JDK Proxy`
+ `JDK Proxy`是通过拦截器加反射的方式实现的， **只能代理继承接口的类**， 实现和调用起来比较简单
+ `CGLib` 是第三方提供的工具，基于 ASM 实现的，**无需通过接口来实现**，它是通过实现子类的方式来完成调用的









+ `cglib`代理可以对接口实现代理吗？





<p align="right"><a href="#dynamic-proxy">返回</a>&nbsp | &nbsp <a href="#top">返回目录</a></p>

-----



+ `final`的用途有哪些？





+ 单例模式4种实现方式？





+ 如何在父类中为子类自动完成所有的hashcode和equals实现





----

## <a name="shadow-deep-clone">深拷贝与浅拷贝</a>



### <a name="shadow-clone">浅克隆</a>

把原型对象中的成员变量为值类型的属性都复制给克隆对象，把原型对象中成员变量类型为**引用类型**的**引用地址也复制给克隆对象**，即原型对象中如果有成员变量为引用对象，则此引用对象的地址是共享给原型对象和克隆对象的。






### <a name="deep-clone">深克隆</a>

将原型对象中所有类型，无论值类型还是引用类型，都复制一份给克隆对象。



Java中要实现克隆需要实现 `Cloneable` 接口，并重写 `Object` 类的 `clone()`方法，`clone()`方法定义如下：

```java
protected native Object clone() throws CloneNotSupportedException;
```



**克隆**常见考点：

+ `Object` 中对 `clone()` 方法的约定有哪些？
+ `Arrays.copyOf()` 是深克隆还是浅克隆？
+ 深克隆的实现方式有几种？
+ Java 中的克隆为什么要设计成，既要实现空接口 Cloneable，还要重写 Object 的 clone() 方法？



`Object` 中对 `clone()` 方法的约定有哪些？

> 1. 对于所有对象来说，x.clone() !=x 应当返回 true，因为克隆对象与原对象不是同一个对象
> 2. 对于所有对象来说，x.clone().getClass() == x.getClass() 应当返回 true，因为克隆对象与原对象的类型是一样的
> 3. 对于所有对象来说，x.clone().equals(x) 应当返回 true，因为使用 equals 比较时，它们的值都是相同的



`Arrays.copyOf()` 是深克隆还是浅克隆？

>  Arrays.copyOf() 是浅克隆，只是把引用地址复制了一份给克隆对象，如果修改了它的引用对象，那么指向它的（引用地址）所有对象都会发生改变。



深克隆的实现方式有几种？

+ 所有对象都实现了克隆方法
+ 通过构造方法实现深克隆
+ 使用JDK自带的字节流实现深克隆
+ 使用第三方工具实现深克隆，比如：`Apache Commons Lang`
+ 使用JSON工具类实现深克隆（序列化&&反序列化）



为什么要在 Object 中添加一个 clone() 方法？

> 因为 clone() 方法语义的特殊性，因此最好有JVM的直接支持，最直接的做法就是将 clone() 方法添加到 Object 类中。



<p align="right"><a href="#shadow-deep-clone">返回</a>&nbsp | &nbsp <a href="#top">返回目录</a></p>

---



+ 列出5个常见的运行时异常？**error** 与 **exception** 的区别？





+ `Object`对象中常用的方法有哪些？分别在什么场景下使用？如何理解`hashcode()` 和 `equal()`方法？





+ 有没有可能存在2个不相等的对象但**`hashcode`相同**？为什么？



+ `hashcode`有哪些算法？



+ socket编程，服务端代码和客户端代码



----

+ **什么情况下会发生栈内存溢出** ？



+ `JVM`模型中哪些区域会发生`OOM` ？发生**内存溢出**时怎样排查原因？



+ `JVM`为什么要分**新生代** 、**老年代**、 **永久代** ？新生代中为什么要再分`Eden`和`Survivor`区？





+ 一次完整的`GC`流程是怎样的？`JVM`中采用什么方法判断一个对象是否可以被回收？





+ Java中能不能主动触发GC?







+ Java内存模型中什么是**重排序** 、 **内存屏障** 、**happen-before** 、 **主内存**、 **工作内存** ？ `volatile`关键字的用途？



+ 什么是**双亲委派**模型？可以打破**双亲委派**模型吗？如何打破？





+ **常用的垃圾回收算法有哪些** ？





+ 生产环境的常用的`JVM`参数有哪些？





+ `JVM`参数的含义

  ```sh
  -server -Xms512m -Xmx512m -Xss1024K
  -XX:PermSize=256m -XX:MaxPermSize=512m -
  XX:MaxTenuringThreshold=20XX:CMSInitiatingOccupancyFraction=80 -
  XX:+UseCMSInitiatingOccupancyOnly
  ```

  

**JVM调优的基本思路**

+ 减少`full gc`
+ 选择合适的垃圾收集器



----







+ `netty`线程模型？netty如何基于reactor模型上实现的？













+ `netty`的`fashwheeltimer`的用法，实现原理，是否出现过调用不够准时，怎么解决？`netty`的**心跳处理在弱网下**怎么办？





+ 



---

**参考文章** ：

+ <a href="https://mp.weixin.qq.com/s?__biz=MzI3ODcxMzQzMw==&mid=2247486906&idx=2&sn=9394dec358ec9130a4bbc9ac2b50c6e5&chksm=eb53888cdc24019a7e5a69086b5aff46973865681dd9dcf29a4b03d29036753150c86d16288f&scene=21#wechat_redirect">**阿里面试题**</a>


+ <a href="https://mp.weixin.qq.com/s?__biz=MzI3ODcxMzQzMw==&mid=2247486678&idx=1&sn=2a5e38e67c3d267d6c58d963adb24ccc&scene=21#wechat_redirect">**Spring面试题**</a>