## String

**String**的创建：

```java
String s = new String("hello")
```

JVM直接在堆上创建新的对象，只要使用`new`创建出来的对象都在**堆上**创建；`"hello"`本身就是一个对象，存放在**常量池**里，运行时执行`new String("hello")`，将会把`hello`从常量池复制一份放在**堆**中创建1个新的`String`对象，并把该**对象的引用**交给**s**

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

