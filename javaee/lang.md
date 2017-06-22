#### java.lang包常用类     

![java.lang常用类](https://github.com/HurricanGod/Home/blob/master/img/java.lang.jpeg)

##### java.lang.Math需要注意的几个方法：        
![Math类需要注意的方法](https://github.com/HurricanGod/Home/blob/master/img/Math.jpg)     

```java
@Test
    public void testMathClass() {
        double c = 10.8;
        System.out.println("c = " + c);
        System.out.println("Math.floor(c) = " + Math.floor(c)); //向下取整,返回double类型 10.0
        System.out.println("Math.ceil(c) = " + Math.ceil(c));   //向上取整,返回double类型 11.0
        System.out.println("Math.round(c) = " + Math.round(c)); //四舍五入 11
        System.out.println("Math.rint(c) = " + Math.rint(c));   //求最接近c的数 11.0
        c  = 10.4;
        System.out.println("c = " + c);
        System.out.println("Math.round(c) = " + Math.round(c)); //四舍五入 10
        System.out.println("Math.rint(c) = " + Math.rint(c));   //求最接近c的数 10.0
        System.out.println(Math.random());
    }

```

>>
**运行结果：**
```
c = 10.8
Math.floor(c) = 10.0
Math.ceil(c) = 11.0
Math.round(c) = 11
Math.rint(c) = 11.0
c = 10.4
Math.round(c) = 10
Math.rint(c) = 10.0
0.4904361136150911
```  

**java中整型数据是以补码的形式保存在内存中的，所有的整型数据都是有符号，不像C++那样有无符号整型**<br><br>
对于1个字节，如果是无符号来说表示范围为**0x00~0xff**，与0~255一一对应。<br>
但对于有符号整型，1字节能表示的范围是`-128~127`。<br>
用补码表示整型时：<br>
0000 0000 ：0 <br>
0111 1111 ：127<br>
**1111 1111**表示什么？<br>
如果是原码当然是-127，但这里是补码，表示为-127显然是错的。 <br>

----

补码的求法为把**原码按位取反然后再加1**，要把补码变换为原码的方法有2种：<br>
1. 先把补码取反（***符号位除外***）然后再加1. <br>
   例： 1111 1111（*补码*） → 1000 0000 → 1000 0001（**原码**）  <br> 
2. 从右往左，找到第1个不为**0**的位，把该位左边的所有位取反（***符号位除外***），得到的结果即为原码.<br>
   例： 1111 1111（*补码*） → 1000 0001（**原码**）<br>

-----
那么-128该怎么表示？<br>
    在补码里并没有`-0`的存在，如果存在`-0`那么1000 0000和0000 0000岂不是同时表示0?这样做显然会浪费1个数字<br>
因此在补码里`1000 0000`并不表示`-0`，而是表示的是***-128***。需要注意的是原码是无法表示`-128`的<br>

**在补码表示法中**<br>
0000 0000 ：0    <br>
0111 1111 ：127  <br>
1000 0000 ：-128 <br>
1111 1111 ：-1   <br>
