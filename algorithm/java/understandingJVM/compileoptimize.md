## 编译器优化

**javac编译过程为：**

+ 解析与填充符号表
+ 插入式注解处理器的注解处理过程
+ 分析与字节码生成过程

----

`词法分析：` 将源代码的字符流转换为**标记**集合，单个字符是程序编写过程中的最小元素。



`符号表：` 一组符号地址和符号信息构成的表格，符号表用于语义检查和中间代码生成，在目标代码生成阶段，为符号名分配地址提供依据

**字节码生成**

完成了对语法树遍历和调整后，把填充了所需信息的符号表输出字节码，生成Class文件。



-----

**自动装箱、拆箱、遍历循环编译前代码：**

```java
public static void main(String[] args) {
	List<Integer> list = Arrays.asList(1,2,3,4);
	int sum = 0;
	for(int i : list){
		sum += i;
	}
	System.out.println(sum);
}
```

------

**经过编译后代码会被还原**

```java
public static void main(String[] args) {
	List<Integer> list = Arrays.asList(new Integer[]{
			Integer.valueOf(1),
			Integer.valueOf(2),
			Integer.valueOf(3),
			Integer.valueOf(4)});
	int sum = 0;
	for(Iterator localIterator = list.iterator();localIterator.hasNext();){
		int i = (Integer)localIterator.next().intValue();
		sum += i;
	}
	System.out.println(sum);
}
```

因此**遍历循环**要求被遍历的对象实现了`Iterable`接口

<br>

### 自动装箱的陷阱

```java
public static void main(String[] args) {
        Integer a = 1;
        Integer b = 2;
        Integer c = 3;
        Integer d = 3;
        Integer e = 321;
        Integer f = 321;
        Long g = 3l;
        System.out.println("(c==d) = " + (c == d));
        System.out.println("(e==f) = " + (e == f));
        System.out.println("(c==a+b) = " + (c==(a+b)));
        System.out.println("c.equals(a+b) = " + (c.equals(a+b)));
        System.out.println("(g == a+b) = " + (g == a+b));
        System.out.println("g.equals(a+b) = " + (g.equals(a+b)));
        System.out.println("g.equals(c) = " + g.equals(c));
        System.out.println("g.equals(3) = " + g.equals(3));
        System.out.println("g.equals(3L) = " + g.equals(3L));

    }
```

<font size=5 color='green'>运行结果：</font>

```
(c==d) = true
(e==f) = false
(c==a+b) = true
c.equals(a+b) = true
(g == a+b) = true
g.equals(a+b) = false
g.equals(c) = false
g.equals(3) = false
g.equals(3L) = true

```

在java中**包装类**的<font size=5 color='#c6af22'>"=="</font>在不遇到算术运算符的情况下不会自动拆箱，`equals()`方法**不处理数据转换**的关系。

- `System.out.println("(c==d) = " + (c == d));`结果为**true**，原因是`Integer`对部分整数有缓存，范围为`-128~127`，c的引用和d的引用指向同一个对象，因此返回true
- `System.out.println("(e==f) = " + (e == f));`结果为**false**的原因为e和f的值超出`Integer`整数缓存范围，把321赋值给变量时分别创建了两个对象，因此它们的地址肯定不相同，所以返回false
- `System.out.println("(c==a+b) = " + (c==(a+b)));`这里**==**比较时遇到了**算术运算符**，会进行自动拆箱，c从`Integer`拆箱为 `int`，a+b的值刚好等于c，所以返回true 
- `System.out.println("c.equals(a+b) = " + (c.equals(a+b)));`因为a+b结果为3，进行`equals()`比较时自动**装箱**，3在`Integer`整数缓存里，只有1个实例，肯定属于`Integer`类的实例并且值相等，所以返回true
- `System.out.println("g.equals(c) = " + g.equals(c)); `结果为false的原因为`equals()`不处理数据转型关系，c的类型为`Integer`,而g的类型为`Long`，因为两个实例类型不同，equals方法直接返回false
- `System.out.println("g.equals(3) = " + g.equals(3));`返回false的原因为常量3自动装箱后的类型为`Integer`，与g类型不一致直接返回false；如果把3换为**3L**自动装箱后为`Long`，结果将会返回**true**




----

**再看一个例子** ：

+ `Integer.valueOf()` 的返回类型为`Integer`  ，当参数（**int类型或String类型**）对应的整数值在**-128~127**之间时，返回缓存里的`Integer对象`，这个对象与用`Integer i= 80`类似的方法得到的对象一样，都是从缓存里取出来的；**用** `new Integer(m)`**创建出来的** `Integer` **对象，不管参数的值是不是在 -128~127，都不是从缓存里返回得到的**
+ `Integer.parseInt(String)` 的返回类型为`int`，是基本数据类型，不是对象；如果用`parseInt(String)`的结果与等值的`Integer`类型变量比较，无论`Integer`的值为多少，结果都为**true**



```java
package hurrican.cvte;

public class Main2 {

    public static void main(String[] args) {
        Integer n = 80;
        System.out.println();
        System.out.println(n + " == new Integer(80) = " + (n == new Integer(80)));
        System.out.println(n + " == Integer.valueOf(80) = " + (n == Integer.valueOf(80)));
        System.out.println(n + " == Integer.valueOf(\"80\") = " + (n == Integer.valueOf("80")));
        System.out.println(n + " == Integer.parseInt(\"80\") = " + (n == Integer.parseInt("80")));
        System.out.println("Integer.parseInt(\"80\") == new Integer(80) = " + (Integer.parseInt("80") == new Integer(80)));


        Integer m = 300;
        System.out.println();
        System.out.println(m + " == new Integer(300) = " + (n == new Integer(300)));
        System.out.println(m + " == Integer.valueOf(300) = " + (n == Integer.valueOf(300)));
        System.out.println(m + " == Integer.valueOf(\"300\") = " + (n == Integer.valueOf("300")));
        System.out.println(m + " == Integer.parseInt(\"300\") = " + (n == Integer.parseInt("300")));
        System.out.println("Integer.parseInt(\"300\") == new Integer(300) = " + (Integer.parseInt("300") == new Integer(300)));

    }
}
```

```
### 运行结果：
80 == new Integer(80) = false
80 == Integer.valueOf(80) = true
80 == Integer.valueOf("80") = true
80 == Integer.parseInt("80") = true
Integer.parseInt("80") == new Integer(80) = true

300 == new Integer(300) = false
300 == Integer.valueOf(300) = false
300 == Integer.valueOf("300") = false
300 == Integer.parseInt("300") = false
Integer.parseInt("300") == new Integer(300) = true
```



-----

## 运行时优化

<a name="hot_spot_code">**热点代码**</a> ：当虚拟机发现某个方法或代码块运行特别频繁，就会把这些代码认定为热点代码



<a name="JIT">**JIT编译器**</a>  ：为了提高<a href="#hot_spot_code">**热点代码**</a> 的执行效率，虚拟机将会把这些代码编译成与本地平台相关的机器码，并进行各种层次的优化，完成这个任务的编译器称为**即时编译器** ——`Just In Time Compiler`



---

### 逃逸分析

当一个对象在方法中被定义后，可能被外部方法所引用，比如**作为调用参数传递到其它方法中**，这种逃匿称为**方法逃逸**； 如果还能被其它线程访问到，比如赋值给可以在其它线程中访问的实例变量，称为**线程逃逸**

如果一个对象不会发生**逃逸**，则可以进行一些高效的优化：

+ **栈上分配** ，对象占有的空间可以随着栈帧的出栈而销毁，大量的对象会随着方法的调用结束而销毁，减轻垃圾收集系统的负担
+ **同步消除** ，如果逃逸分析能够确认一个变量不会发生**线程逃逸** ，就可以对变量的同步措施进行消除
+ **标量替换** ，逃逸分析证明一个对象不会被外部访问并且这个对象可以拆散，那么程序真正执行时可以不创建这个对象，直接创建若干个被方法使用到的成员变量代替





