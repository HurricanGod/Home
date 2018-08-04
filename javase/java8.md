## <a name="top">Java8新特性</a>



-----

### 常用函数接口

+ <a href="#Predicate">`Predicate`</a>


+ <a href="#Consumer">`Consumer`</a>


+ <a href="#Function">`Function`</a>


+ <a href="#BiFunction">`BiFunction`</a>


---

<a name="Predicate">`Predicate`</a>

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

```

> 上面Demo中使用Predicate筛选字符串集合中以"Bi"开头，"java"结尾或"py"结尾的字符串

`Console`：

> BiConsumer.java
> BiFunction.java
> BiConsumer.py

<p align="right"><a href="#top">返回</a></p>



<hr/>

<a name="Function">`Function`</a>

`Function`源码如下所示：

```java
@FunctionalInterface
public interface Function<T, R> {

    /**
     * 接收泛型参数 T 并返回 R
     */
    R apply(T t);

    /**
     * 返回1个组合函数，参数 before 执行的结果将作为本对象的入参
     * @param before 本实例执行 apply(T t) 方法前先执行的 Function 实例
     * @throws NullPointerException if before is null
     */
    default <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the {@code after} function
     * @throws NullPointerException if after is null
     *
     * @see #compose(Function)
     */
    default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

    /**
     * Returns a function that always returns its input argument.
     *
     * @param <T> the type of the input and output objects to the function
     * @return a function that always returns its input argument
     */
    static <T> Function<T, T> identity() {
        return t -> t;
    }
}

```

+ `apply(T t)` —— 将类型 T 对象转换为 类型 R 的方法
+ `compose(Function<? super V, ? extends T> before)` —— 接收`Function`实例**before**参数并返回新的`Function`实例，**before**实例的`apply(T t)`方法将作为返回`Function`实例的入参
+ `andThen(Function<? super R, ? extends V> after)` —— 接收`Function`实例**after**参数并返回新的`Function`实例，本实例的`apply(T t)`方法的返回值将作为**after**实例的入参



**Demo** ：

```java

    @Test
    public void testMethod11() {
        String hello = ((Function<String, String>) s1 -> s1 + s1)
                .compose((Function<String, String>) String::toUpperCase).apply("Hello ");
        System.out.println(hello);

        String line = ((Function<String, String>) s1 -> Joiner.on(" = ").join(s1, 10))
                .compose(s2 -> "int " + s2)
                .andThen(str -> str + " ;").apply("a");
        System.out.println(line);
    }

```

+ 第1个例子`compose()`方法将字符串转换为大写，然后再相加
+ 第1个例子是将 `s1` 与 `10`用 "=" 相连接，得到 ` s1 = 10`，`compose()`方法将字符串 `int` 与  ` s1 = 10` 相加，`andThen()` 方法最后把之前得到的字符串加上一个'";"



**Console** ：

>HELLO HELLO 
>int a = 10 ;
>



<p align="right"><a href="#top">返回</a></p>

<hr/>

<a name="BiFunction">`BiFunction`</a>

`BiFunction` 其实就是 `Function` 接口的升级版，只是将 `Function`接口的 `apply()`方法由1个参数变为**2个参数**





<p align="right"><a href="#top">返回</a></p>

----

### Collectors







<p align="right"><a href="#top">返回顶部</a></p>

------
