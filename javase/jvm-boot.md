# <a name="top">Java命令行参数</a>



+ `-` ——表示JVM标准参数，向后兼容

+ `-X` ——非标准参数，默认JVM实现了这些参数功能，不保证向后兼容

+ `-XX` ——表示非 `Stable` 参数，***需谨慎使用***

-----

## <a name="stard_args">JVM标准参数</a>



`-D` 

+ 用以设置系统属性，程序中**任何地方都可以访问到**
+ 如果要设置的属性值**含有空格**，需要使用`双引号`括起来

示例用法：`-Dzookeeper.log.dir=.`
```java
// 获取虚拟机参数 .
System.getProperty("zookeeper.log.dir");
```

----

`-classpath` 

+ 告知 `JVM` 启动目标类所依赖的其它类路径
  + 若有多个路径，**Linux系统使用 `:` 进行分隔**，Windows系统用 `;` 进行分隔
  + 支持通配符：`.` 、`*`
+ 可以简写为 `-cp`
+ 使用 `-classpath` 后 `JVM` 将不再使用**CLASSPATH**中的类搜索路径
+ 若未设置 `-classpath` 和 `CLASSPATH`，默认使用当前路径`.`作为类搜索路径
+ `JVM`搜索类的方式和顺序为：
  + → `Bootstrap` —— 可以使用` System.getProperty("sun.boot.class.path")`获取搜索路径
  + → `Extension` —— 位于 `JRE_HOME/lib/ext`目录下的**jar文件**， 可以使用 `System.getProperty("java.ext.dirs")` 获取搜索路径
  + → `User` —— 搜索java文件顺序为：**当前路径**  → `CLASSPATH` → **-classpath**指定的路径，可以使用`System.getProperty("java.class.path")` 获取搜索路径




```java
public class JvmEntryTest {

    public static void main(String[] args) {
    	String bootstrapDir = System.getProperty("sun.boot.class.path");
        System.out.println("bootstrapDir = " + bootstrapDir);
		
        String logDir = System.getProperty("zookeeper.log.dir");
        System.out.println("logDir = " + logDir);

        String classPath = System.getProperty("java.class.path");
        System.out.println("classPath = " + classPath);
    }
}
```



以`zookeeper-server`启动参数为例：

```sh
/mnt/jdk/jdk1.8.0_181/bin/java 
-Dzookeeper.log.dir=. 
-Dzookeeper.root.logger=INFO,CONSOLE 
-cp 
/mnt/zookeeper-3.4.5/bin/../build/classes:
/mnt/zookeeper-3.4.5/bin/../build/lib/*.jar:
/mnt/zookeeper-3.4.5/bin/../share/zookeeper/zookeeper-3.4.5-cdh5.15.1.jar:
/mnt/zookeeper-3.4.5/bin/../share/zookeeper/slf4j-log4j12-1.7.5.jar:
/mnt/zookeeper-3.4.5/bin/../share/zookeeper/slf4j-api-1.7.5.jar:
/mnt/zookeeper-3.4.5/bin/../share/zookeeper/netty-3.10.5.Final.jar:
/mnt/zookeeper-3.4.5/bin/../share/zookeeper/log4j-1.2.16.jar:
/mnt/zookeeper-3.4.5/bin/../share/zookeeper/jline-2.11.jar:
/mnt/zookeeper-3.4.5/bin/../src/java/lib/*.jar:
/mnt/zookeeper-3.4.5/bin/../conf:
.:
/mnt/jdk/jdk1.8.0_181/lib 
-Dcom.sun.management.jmxremote 
-Dcom.sun.management.jmxremote.local.only=false org.apache.zookeeper.server.quorum.QuorumPeerMain 
/mnt/zookeeper-3.4.5/bin/../conf/zoo.cfg
```

+ 以上运行的`Java`主程序名字为：`org.apache.zookeeper.server.quorum.QuorumPeerMain`
+ **命令行参数**指定的配置文件的路径为： `/mnt/zookeeper-3.4.5/bin/../conf/zoo.cfg`
+ 设置的系统属性有：
  + `zookeeper.log.dir` —— `zookeeper`日志文件路径
  + `zookeeper.root.logger` —— `zookeeper`日志级别
  + `com.sun.management.jmxremote`
  + `com.sun.management.jmxremote.local.only`





<p align="right"><a href="#stard_args">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a></p>

-----

## <a name="unstard">非标准JVM参数</a>

| 参数       | 用途                                       | 说明   |
| :------- | :--------------------------------------- | :--- |
| `-Xmsn`  | 指定 `JVM` 堆初始大小，最小为**1M**，默认为物理内存的`1/64` |可选的单位：`k`、`m`(***不指定默认为字节***)|
| `-Xmxn`  | 指定 `JVM` 堆最大值，默认为物理内存的`1/4`或`1G`         |可选的单位：`k`、`m`(***不指定默认为字节***)|
| `-Xprof` | 跟踪正运行的程序，并将跟踪数据在标准输出输出|      |
| `-Xssn`   | 设置单个线程栈的大小，一般默认为512k |      |
|          |                                          |      |







<p align="right"><a href="#stard_args">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a></p>

------

## <a name="un_stable_args">非 `Stable` 参数</a>

> -XX作为前缀的参数列表在jvm中可能是不健壮的，SUN也不推荐使用，后续可能会在没有通知的情况下就直接取消了。

| 参数                                | 用途                      | 类型           |
| :-------------------------------- | :---------------------- | :----------- |
| `-XX:+MaxFDLimit`                 | 最大化文件描述符的数量限制           | **改变行为参数**   |
| `-XX:+ScavengeBeforeFullGC`       | 新生代GC优先于Full GC执行       |              |
| `-XX:-UseConcMarkSweepGC`         | 对老生代采用并发标记交换算法进行GC      |              |
| `-XX:-UseParallelGC`              | 启用并行GC                  |              |
| -                                 |                         |              |
| **-XX:MaxHeapFreeRatio=70**       | **GC后java堆中空闲量占的最大比例**  | ***性能调优参数*** |
| **-XX:MaxNewSize=10m**            | **新生成对象能占用内存的最大值**      |              |
| **-XX:MaxPermSize=64m**           | **老生代对象能占用内存的最大值**      |              |
| **-XX:NewRatio=2**                | **新生代内存容量与老生代内存容量的比例**  |              |
| **-XX:ThreadStackSize=512**       | **设置线程栈大小，若为0则使用系统默认值** |              |
| **-XX:NewSize=2.125m**            | **新生代对象生成时占用内存的默认值**    |              |
| -                                 |                         |              |
| *-XX:-HeapDumpOnOutOfMemoryError* | *当首次遭遇OOM时导出此时堆中相关信息*   | *调试参数*       |
| *-XX:-PrintGC Details*            | *每次GC时打印详细信息*           |              |
| *-XX:-PrintGCTimeStamps*          | *打印每次GC的时间戳*            |              |
| *-XX:-TraceClassLoading*          | *跟踪类的加载信息*              |              |
| *-XX:-TraceClassResolution*       | *跟踪常量池*                 |              |
| *-XX:-TraceClassUnloading*        | *跟踪类的卸载信息*              |              |







<p align="right"><a href="#un_stable_args">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a></p>

