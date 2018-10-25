# <a name="top">Java面试题</a>





----

+ `Java`中有几种数据类型，各自占多少字节？







+ `String`类能被继承吗？`String`类底层怎样实现的？与`StringBuffer`、`StringBuilder`有什么区别？









+ 类的实例化顺序，比如父类静态数据、构造函数、字段、子类静态数据、构造函数、字段执行顺序？









+ 并发场景下使用的 `ConcurrentHashMap`实现原理是什么？创建时默认容量是多少？如何进行扩容？`JDK8`后为什么放弃分段锁机制？并发场景下如何实现Map中元素个数的计算的？`LinkedHashMap`是怎样保存有序的？



+ `HashSet`的实现原理？



+ 继承和聚合的区别？







+ 常见的`IO`模型有哪些？各自有什么特点？`NIO`、`BIO`、`AIO`分别是什么？什么是`reactor模型`？









+ 反射创建实例的三种方式是什么？`Class.forName()`和`ClassLoader`







+ 动态代理的几种实现方式？各自有什么特点？





+ `cglib`代理可以对接口实现代理吗？



+ `final`的用途有哪些？



+ 单例模式4种实现方式？





+ 如何在父类中为子类自动完成所有的hashcode和equals实现





+ **深拷贝** 与 **浅拷贝** 的区别？





+ 列出5个常见的运行时异常？**error** 与 **exception** 的区别？





+ `Object`对象中常用的方法有哪些？分别在什么场景下使用？如何理解`hashcode()` 和 `equal()`方法？





+ 有没有可能存在2个不相等的对象但**`hashcode`相同**？为什么？



----

+ **什么情况下会发生栈内存溢出** ？



+ `JVM`模型中哪些区域会发生`OOM` ？发生**内存溢出**时怎样排查原因？



+ `JVM`为什么要分**新生代** 、**老年代**、 **永久代** ？新生代中为什么要再分`Eden`和`Survivor`区？





+ 一次完整的`GC`流程是怎样的？`JVM`中采用什么方法判断一个对象是否可以被回收？





+ Java内存模型中什么是**重排序** 、 **内存屏障** 、**happen-before** 、 **主内存**、 **工作内存** ？ `volatile`关键字的用途？



+ 可以打破**双亲委派**模型吗？如何打破？





+ **常用的垃圾回收算法有哪些** ？





+ 生产环境的常用的`JVM`参数有哪些？





+ `JVM`参数的含义

  ```sh
  -server -Xms512m -Xmx512m -Xss1024K
  -XX:PermSize=256m -XX:MaxPermSize=512m -
  XX:MaxTenuringThreshold=20XX:CMSInitiatingOccupancyFraction=80 -
  XX:+UseCMSInitiatingOccupancyOnly
  ```

  ​



----

+ `Spring`加载流程？





+ `Tomcat`线程模型是怎样的？进行`Tomcat`调优涉及哪些参数？





+ `Spring AOP`实现原理？





+ `Spring`事务**传播属性**有哪些？





+ `SpringMVC`中的`DispatcherServlet`初始化过程？





+ `netty`线程模型？netty如何基于reactor模型上实现的？









+ 什么是**TCP粘包**，**拆包** ，如何解决？





+ `netty`的`fashwheeltimer`的用法，实现原理，是否出现过调用不够准时，怎么解决？`netty`的**心跳处理在弱网下**怎么办？









---

**参考文章** ：

+ <a href="https://mp.weixin.qq.com/s?__biz=MzI3ODcxMzQzMw==&mid=2247486906&idx=2&sn=9394dec358ec9130a4bbc9ac2b50c6e5&chksm=eb53888cdc24019a7e5a69086b5aff46973865681dd9dcf29a4b03d29036753150c86d16288f&scene=21#wechat_redirect">**阿里面试题**</a>


+ <a href="https://mp.weixin.qq.com/s?__biz=MzI3ODcxMzQzMw==&mid=2247486678&idx=1&sn=2a5e38e67c3d267d6c58d963adb24ccc&scene=21#wechat_redirect">**Spring面试题**</a>