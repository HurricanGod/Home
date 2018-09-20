# <a name="top">Log4j2</a>



## maven引用

```
<!--日志相关配置......start-->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.0</version>
        </dependency>

        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-slf4j-impl</artifactId>
            <version>2.0.2</version>
        </dependency>

        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-api</artifactId>
            <version>2.5</version>
        </dependency>

        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>2.5</version>
        </dependency>

        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-web</artifactId>
            <version>2.5</version>
        </dependency>
<!--日志相关配置......end-->

```





<p align="right"><a href="#tpop" >**返回顶部**</a></p>

----

## log4j2.xml



```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="trace" monitorInterval="30">
    <!-- Dev -->
    <Properties>
        <Property name="DETAIL_LOG_NAME">info</Property>
        <Property name="ERROR_LOG_NAME">error</Property>
        <Property name="LOG_HOME">target/logs</Property>
    </Properties>

    <Appenders>
        <Console name="console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %-5level %class{36}#%M [line:%L] - %msg%xEx%n"/>
        </Console>

        <RollingFile name="detailLog" filename="${LOG_HOME}/${DETAIL_LOG_NAME}.log"
                     filepattern="${LOG_HOME}/${DETAIL_LOG_NAME}-%d{yyyy-MM-dd}-%i.log">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %-5level %class{36}#%M [line:%L] - %msg%xEx%n"/>
            <Policies>
                <SizeBasedTriggeringPolicy size="10 MB"/>
            </Policies>
            <DefaultRolloverStrategy max="100"/>
        </RollingFile>

        <RollingFile name="errorLog" fileName="${LOG_HOME}/${ERROR_LOG_NAME}.log"
                     filePattern="${LOG_HOME}/${ERROR_LOG_NAME}.%d{yyyy-MM-dd}.%i.log">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %-5level %class{36}#%M [line:%L] - %msg%xEx%n"/>
            <Policies>
                <SizeBasedTriggeringPolicy size="10 MB"/>
            </Policies>
            <DefaultRolloverStrategy max="60"/>
        </RollingFile>
    </Appenders>

    <Loggers>
        <!-- root标签为log的默认输出形式 -->
        <Root level="trace">
            <AppenderRef ref="console" level="trace"/>
            <AppenderRef ref="detailLog" level="debug"/>
            <AppenderRef ref="errorLog" level="error"/>
        </Root>

    </Loggers>
</Configuration>
```

要使用`Log4j2`的API，需要从`LogManager`中获取有明确名称的`Logger`。

+ `Appenders` 标签里包含了所有`appender`
+ ​