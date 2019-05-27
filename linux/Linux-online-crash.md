# <a name="top">Linux系统线上故障定位</a>









-----

## <a name="reason">原因</a>

+ 代码Bug


+ 代码性能


+ 内存泄漏


+ 异常流量


+ 外部系统问题




**关注指标**：

+ CPU
+ 内存
+ IO
+ 系统负载






`uptime`：查看系统运行时间、平均负荷

```sh
uptime
# 23:58:28 up 242 days, 23:12,  1 user,  load average: 0.07, 0.04, 0.01
```

`load average`后面3个数字的含义：

+ 过去**1分钟**内运行进程队列中的平均进程数
+ 过去**5分钟**内运行进程队列中的平均进程数
+ 过去**15分钟**内运行进程队列中的平均进程数

没有等待IO，没有WAIT，没有KILL的进程都进**运行进程队列**。



<p align="right"><a href="#reason">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

----

`vmstat`：实时性能检测工具，可以展现给定时间间隔的服务器状态值

+ 服务器的CPU使用率
+ 内存使用
+ 虚拟内存交换情况
+ IO读写情况




`sar`：查看网络设备的吞吐率，通过网络设备的吞吐量，判断网络设备是否饱和

```sh
# 
sar -n DEV 1

# 查看tcp连接状态
# active/s —— 每秒主动发起的TCP连接数
# passive/s —— 每秒被动发起的TCP连接数
# retrans/s —— 每秒重传的数量，反映网络状况和丢包情况
sar -n TCP,ETCP 1

```

![sar-n]()



`iostat`：查看磁盘IO情况

```sh
# await(ms) —— IO操作的平均等待时间，是应用查询和磁盘交互时需要消耗的时间，包括IO等待时间和实际操作的耗时
# avgqu-sz —— 向设备发出的平均请求数量
# %util —— 设备利用率
iostat -xz 1
```



<p align="right"><a href="#top">返回目录</a></p>

----

## <a name="jvm">JVM诊断</a>



+ `jstack`

> Java堆栈跟踪工具，主要用于打印指定Java进程Java线程的堆栈跟踪信息





+ `jmap(Java Memory Map)`

> Java内存映射工具，由于打印Java进程共享对象内存或堆内存详情





+ `jhat(Java Heap Analysis Tool)`

> Java堆分析工具，用于分析Java堆中的对象信息





+ `jinfo(Java Configuration Information)`

> 用于打印Java进程的配置信息，动态修改JVM参数配置





+ `jstat(JVM Statistic Monitoring Tool)`

> 监控JVM性能统计信息



+ `jcmd`





+ `arthas`

> 阿里Java诊断工具箱



<p align="right"><a href="#jvm">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

-----

