# <a name="top">BigDecimal类</a>





```java
System.out.println(0.05 + 0.01);
// 结果：0.060000000000000005
System.out.println(1.0 - 0.42);
// 结果：0.5800000000000001
System.out.println(4.015 * 100);
// 结果：401.49999999999994
System.out.println(123.3 / 100);
// 结果：1.2329999999999999
```

Java中进行浮点数运算会出现精度丢失的问题，`float`的精度的有效数字为**6~7**位，`double`的精度为**15~16**位。



`BigDecimal`构造方法：

+ `BigDecimal(int)` 
+ `BigDecimal(double)`
+ `BigDecimal(long)`
+  `BigDecimal(string)` —— 开发中一般要使用这个构造函数



`BigDecimal`工具类

```java
/**
 *  v1 + v2
 */
public static BigDecimal add(double v1, double v2) {
	BigDecimal b1 = new BigDecimal(Double.toString(v1));
    BigDecimal b2 = new BigDecimal(Double.toString(v2));
    return b1.add(b2);
}


/**
 *  v1 - v2
 */
public static BigDecimal sub(double v1, double v2) {
	BigDecimal b1 = new BigDecimal(Double.toString(v1));
    BigDecimal b2 = new BigDecimal(Double.toString(v2));
    return b1.subtract(b2);
}



public static BigDecimal mul(double v1, double v2) {
	BigDecimal b1 = new BigDecimal(Double.toString(v1));
    BigDecimal b2 = new BigDecimal(Double.toString(v2));
    return b1.multiply(b2);
}


public static BigDecimal div(double v1, double v2) {
	BigDecimal b1 = new BigDecimal(Double.toString(v1));
    BigDecimal b2 = new BigDecimal(Double.toString(v2));
    // 2 = 保留小数点后两位   ROUND_HALF_UP = 四舍五入,应对除不尽的情况
    return b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP);
}
```

