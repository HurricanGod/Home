## <a name="top">Java8新特性</a>



-----

### 常用函数接口

+ <a name="Predicate">`Predicate`</a>


+ <a name="Consumer">`Consumer`</a>


+ <a name="Function">`Function`</a>


+ <a name="BiFunction">`BiFunction`</a>



---

<a href="#Predicate">`Predicate`</a>

**Predicate** 接口定义如下：

```java
@FunctionalInterface
public interface Predicate<T> {

    /**
     * 计算给定 Predicate 参数的表达式的 Boolean 值
     *
     */
    boolean test(T t);

    /**
     * @param 参数 other 与接口本身的 test() 方法进行逻辑与
     * @return predicate 对象
     * @throws NullPointerException if other is null
     */
    default Predicate<T> and(Predicate<? super T> other) {
        Objects.requireNonNull(other);
        return (t) -> test(t) && other.test(t);
    }

    /**
     * 返回 predicate 的逻辑非对象
     */
    default Predicate<T> negate() {
        return (t) -> !test(t);
    }

    /**
     * this.test() 方法与参数 other.test() 方法进行短路或操作，
     * 如果 this.test() 结果为 true 后面的表达式将不会再自行
     *
     * @param other 执行逻辑或的第二个操作数对象
     * @throws NullPointerException if other is null
     */
    default Predicate<T> or(Predicate<? super T> other) {
        Objects.requireNonNull(other);
        return (t) -> test(t) || other.test(t);
    }

    /**
     * Returns a predicate that tests if two arguments are equal according
     * to {@link Objects#equals(Object, Object)}.
     *
     * @param <T> the type of arguments to the predicate
     * @param targetRef the object reference with which to compare for equality,
     *               which may be {@code null}
     * @return a predicate that tests if two arguments are equal according
     * to {@link Objects#equals(Object, Object)}
     */
    static <T> Predicate<T> isEqual(Object targetRef) {
        return (null == targetRef)
                ? Objects::isNull
                : object -> targetRef.equals(object);
    }
}

```

+ `test(T t)` —— 将参数T转换为Boolean类型
+ `and(Predicate<? super T> other)` —— 与参数 other 进行**短路逻辑与**操作并返回`Predicate`对象
+ `or(Predicate<? super T> other)` —— 与参数 other 进行**短路逻辑或**操作并返回`Predicate`对象



**Demo** ：

```java

    @Test
    public void testMethod3(){
        List<String> list = Arrays.asList("BiConsumer.java", "BiFunction.java",
                "Consumer.java", "Function.py","BiConsumer.py", "Hello.py");
        Predicate<String> predicate = (String s) -> s.startsWith("Bi");
        predicate.and((String s) -> s.endsWith("java")).or((String s) -> s.endsWith("py"));
        List<String> collect = list.stream().filter(predicate).collect(Collectors.toList());
        collect.forEach(System.out::println);
    }
}
```

> 上面Demo中使用Predicate筛选字符串集合中以"Bi"开头，"java"结尾或"py"结尾的字符串

<p align="right"><a href="#top">返回</a></p>



<hr/>

<a href="#Function">`Function`</a>



----

### Collectors







<p align="right"><a href="#top">返回顶部</a></p>

------