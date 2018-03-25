# log4j日志级别

1. fatal(致命)
2. error
3. warn
4. info
5. debug
6. trace(堆栈)
7. all




-----

`Log4j`主要由3部分组成：

+ `loggers` —— 负责采集日志信息


+ `appenders` —— 负责将日志信息发布到不同的地方


+ `layouts` —— 负责以各种风格格式化日志信息



----

## Log4j的功能

+ 线程安全
+ 速度优化
+ 基于命名的`logger`层次
+ 设计之初就考虑了**处理Java异常**
+ 不受限于预定义好的设施
+ 可以通过 `Layout`类轻松改变日志的格式



-----

## 架构

> Log4j API 采用分层架构，每一层有不同的架构，完成不同的任务

`Log4j`两种类型的对象：

+ 核心对象——架构必须的对象，使用架构必需用到它们，主要包括：

  + `Logger`对象 —— 负责获取日志信息，并存储到一个分层的命名空间中

    ​

  + `Layout`对象 —— 用于各种风格格式化日志信息的对象，在发布日志信息之前，为`appender`对象提供支持

    ​

  + `Appender`对象 —— 该对象位于架构中较低层，负责将日志信息发布到不同目的地

    ​

+ 支持对象 —— 框架可选对象，支持核心对象做一些额外的，但并不重要的任务

  + `Level` —— 定义了日志信息的粒度和优先级，参考上面的7种级别

    ​

  + `Filter` —— 用来分析日志信息，进而决定该日志是否被记录，1个`Appender`对象可以对应多个`Filter`对象，当日志信息传给`Appender`对象时，与其关联的所有`Filter`对象需要判断是否将日志信息发布到目的地

    ​

  + `ObjectRenderer` —— 负责为传入的日志架构的不同对象提供字符串形式的表示

  + `LogManager` —— 对象管理日志框架，负责从系统级的配置文件或类中**读取初始配置参数**



-----

> 默认情况下LogManager会到`classpath`下寻找`log4j.properties`文件



```properties
Log4j.rootLogger = DEBUG, X
#根日志级别定义为Debug，将名为X的appender添加其上

Log4j.appender.X=org.apache.Log4j.FileAppender
Log4j.appender.X.File=${log}/log.txt
#将名为X的appender设置为FileAppender,将日志写入到log目录下的log.txt中

Log4j.appender.X.layout=org.apache.Log4j.PatternLayout
Log4j.appender.X.layout.conversionPattern=%m%n
#设置appender X的layout为 %m%n，打印的日志信息末尾加入换行
```



|    属性     |                    描述                    |
| :-------: | :--------------------------------------: |
|  layout   | `Appender`对象使用`layout`属性和与之关联的模式来格式化日志信息 |
|  target   |              目的地，可以是控制台、文件               |
|   level   |                用来控制过滤日志信息                |
| threshold | `Appender` 可脱离于日志级别定义一个阀值级别，`Appender` 对象会忽略所有级别低于阀值级别的日志 |
|  filter   | `Filter` 对象可在级别基础之上分析日志信息，来决定 `Appender` 对象是否处理或忽略一条日志记录 |



在配置文件中将`Appender`对象添加到`Logger`对象的方法：

**properties**添加方式：

```
Log4j.logger.[logger-name]=level,appender1,appender2...
```



**xml**添加方式：

```xml
<logger name="com.apress.logging.Log4j" additivity="false">
   <appender-ref ref="appender1"/>
   <appender-ref ref="appender2"/>
</logger>
```



----

**所有可用的appender包括** ：

+ `AppenderSkeleton`
+ `AsyncAppender`
+ `ConsoleAppender`
+ `JDBCAppender`
+ `JMSAppender`
+ `FileAppender`
+ `DailyRollingFileAppender`
+ `ExternallyRolledFileAppender`
+ `RollingFileAppender`
+ `SMTPAppender`
+ `SocketAppender`
+ `SyslogAppender`



**可用的Layout包括**：

+ `DateLayout`
+ `PatternLayout`
+ `SimpleLayout`
+ `XMLLayout`
+ `HTMLLayout`



