# <a name="top">基准测试JMH</a>





JVM 在执行时，会对一些代码块，或者一些频繁执行的逻辑，进行 JIT 编译和内联优化，在得到一个稳定的测试结果之前，需要先循环上万次进行预热。预热前和预热后的性能差别非常大。



`JMH`（the Java Microbenchmark Harness）是一个能做基准测试的工具，JMH 已经在 JDK 12中被包含，其他版本的需要自行引入 maven

```xml
<dependencies> 
        <dependency> 
            <groupId>org.openjdk.jmh</groupId> 
            <artifactId>jmh-core</artifactId> 
            <version>1.23</version> 
        </dependency> 
        <dependency> 
            <groupId>org.openjdk.jmh</groupId> 
            <artifactId>jmh-generator-annprocess</artifactId> 
            <version>1.23</version> 
            <scope>provided</scope> 
        </dependency> 
</dependencies>
```

