# log4j日志

-----

`log4j`主要由3部分组成：

+ `loggers` —— 负责采集日志信息


+ `appenders` —— 负责将日志信息发布到不同的地方


+ `layouts` —— 负责以各种风格格式化日志信息


----

## log4j的功能

+ 线程安全
+ 速度优化
+ 基于命名的`logger`层次
+ 设计之初就考虑了**处理Java异常**
+ 不受限于预定义好的设施
+ 可以通过 `Layout`类轻松改变日志的格式


-----

## 架构

> log4j API 采用分层架构，每一层有不同的架构，完成不同的任务



![log4j架构]()

`log4j`两种类型的对象：

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




`FileAppender`配置

|        属性        |              描述              |
| :--------------: | :--------------------------: |
| `immediateFlush` | 默认为true，`每次日志追加操作都将输出流刷新至文件` |
|    `encoding`    |            日志文件编码            |
|   `threshold`    |      **appender 对象的阀值**      |
|     Filename     |            日志文件名             |
|   `fileAppend`   |      默认为true，将日志追加到文件末尾      |
|   `bufferedIO`   |      是否开启缓冲区写，缺省为false       |
|   `bufferSize`   |   若开启缓冲区I/O，用于指定缓冲区大小，默认8k   |

-----

> Layout对象从`Appender`对象接收一个`LoggingEvent`对象，然后从`LoggingEvent`对象那里获取信息，并使用`ObjectRenderer`对象获取该信息的字符串形式



**可用的Layout包括**：

+ `DateLayout`
+ `PatternLayout`
+ `SimpleLayout`
+ `XMLLayout`
+ `HTMLLayout`




-----

**日志级别**：

+ fatal(致命)


+ error


+ warn


+ info


+ debug


+ trace(堆栈)


+ all

**标准级别顺序为**：

> all < debug < info < warn < error < fatal < off



假设日志级别设置为`warn`，低于 `warn`级别的日志都不会打印出来（即debug、info级别的日志会被忽略）



**Demo**

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE Log4j:configuration SYSTEM "Log4j.dtd">
<Log4j:configuration>

<appender name="FILE" class="org.apache.Log4j.FileAppender">
	<!--日志文件输出位置-->
   <param name="file" value="${log}/log.out"/>
  	<!--将输出流立刻添加到文件-->
   <param name="immediateFlush" value="true"/>
    <!--FileAppender的级别，低于debug的日志不输出-->
   <param name="threshold" value="debug"/>
  <!--false指将消息覆盖指定的文件内容-->
   <param name="append" value="false"/>

   <layout class="org.apache.Log4j.PatternLayout">
     <!--输出日志信息后换行-->
      <param name="conversionPattern" value="%m%n"/>
   </layout>
</appender>

<logger name="Log4j.rootLogger" additivity="false">
   <level value="DEBUG"/>
   <appender-ref ref="FILE"/>
</logger>

</Log4j:configuration>
```



-----

|   符号   |                    说明                    |
| :----: | :--------------------------------------: |
|  `-X`  |                 信息输出时左对齐                 |
|  `%p`  |                输出日志信息优先级                 |
|  `%d`  | 输出日志时间点，使用Demo：`%d{yyyy MM dd HH:mm:ss}` |
|  `%c`  |           输出日志信息所属类目，通常是所在类全名            |
|  `%t`  |              输出产生该日志事件的线程名               |
|  `%l`  |             **输出日志事件发生的位置**              |
|  `%L`  |                 输出代码中的行号                 |
|  `%m`  |               输出产生的具体日志信息                |
|  `%n`  |                **输出换行符**                 |
|  `%F`  |             输出日志消息产生时所在的文件名              |
| `%20`  |            最小宽度为20，小于20默认右对齐             |
| `%-20` |           最小宽度为20，小于20**左对齐**            |
|        |                                          |