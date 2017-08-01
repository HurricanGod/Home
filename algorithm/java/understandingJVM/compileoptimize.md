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

在java中***包装类***的<font size=5 color='#c6af22'>"=="</font>在不遇到算术运算符的情况下不会自动拆箱，`equals()`方法不处理数据转换的关系。

- `System.out.println("(c==d) = " + (c == d));`结果为**true**，原因是`Integer`对部分整数有缓存，范围为`-128~127`，c的引用和d的引用指向同一个对象，因此返回true
- `System.out.println("(e==f) = " + (e == f));`结果为**false**的原因为e和f的值超出`Integer`整数缓存范围，把321赋值给变量时分别创建了两个对象，因此它们的地址肯定不相同，所以返回false
- `System.out.println("(c==a+b) = " + (c==(a+b)));`这里**==**比较时遇到了***算术运算符***，会进行自动拆箱，c从`Integer`拆箱为 `int`，a+b的值刚好等于c，所以返回true 
- `System.out.println("c.equals(a+b) = " + (c.equals(a+b)));`因为a+b结果为3，进行`equals()`比较时自动**装箱**，3在`Integer`整数缓存里，只有1个实例，肯定属于`Integer`类的实例并且值相等，所以返回true
- `System.out.println("g.equals(c) = " + g.equals(c)); `结果为false的原因为`equals()`不处理数据转型关系，c的类型为`Integer`,而g的类型为`Long`，因为两个实例类型不同，equals方法直接返回false
- `System.out.println("g.equals(3) = " + g.equals(3));`返回false的原因为常量3自动装箱后的类型为`Integer`，与g类型不一致直接返回false；如果把3换为**3L**自动装箱后为`Long`，结果将会返回**true**

