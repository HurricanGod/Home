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
