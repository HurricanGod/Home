# <a name="top">Linux系统线上故障定位</a>

+ <a href="#reason">原因</a>


+ ​






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




`sar(System Activity Reporter)`：是Linux上系统性能分析工具，可用于查看网络设备的吞吐率，通过网络设备的吞吐量，判断网络设备是否饱和

**命令格式**  ——  `sar [ 选项 ] [ <时间间隔> [ <次数> ] ]`，常用选项如下：
+ `-B`： 显示换页状态
+ `-b`： 显示I/O和传递速率的统计信息
+ `-i`： 设置状态信息刷新的间隔时间
+ `–u`： 输出cpu使用情况和统计信息
+ `-R`： 显示内存状态
+ `-d`： 磁盘使用详情统计
+ `-n`： 统计网络信息，后面跟的可选参数有：`DEV`、`EDEV`、`TCP`、`SOCK`、`ALL`...
  + `DEV` —— 网络接口信息
  + `EDEV` —— 网络错误的统计数据
  + `SOCK` —— 套接字信息
  + `TCP` —— TCP统计信息
  + `ALL` —— 所有信息


```sh
sar -n DEV 1
```

+ `rxpck/s` —— 每秒钟接受的数据包
+ `txpck/s ` —— 每秒钟发送的数据库
+ `rxKB/S` —— 每秒钟接受的数据包大小，单位为KB
+ `txKB/S` —— 每秒钟发送的数据包大小，单位为KB

![]()



```sh
# 查看tcp连接状态
# active/s —— 每秒主动发起的TCP连接数
# passive/s —— 每秒被动发起的TCP连接数
# retrans/s —— 每秒重传的数量，反映网络状况和丢包情况
sar -n TCP,ETCP 1
```

![sar-n](https://github.com/HurricanGod/Home/blob/master/linux/img/sar-n-TCP.png)

<p align="right"><a href="#reason">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

-----

`iostat`：查看磁盘IO情况

```sh
# await(ms) —— IO操作的平均等待时间，是应用查询和磁盘交互时需要消耗的时间，包括IO等待时间和实际操作的耗时，该值过大可能是硬件设备遇到瓶颈或出现故障，一般超过20ms说明磁盘压力过大
# avgqu-sz —— 向设备发出的平均请求数量，大于1可能是硬件设备已经饱和
# %util —— 设备利用率，越大表示越繁忙
iostat -xz 1
```
![](https://github.com/HurricanGod/Home/blob/master/linux/img/iostat-xz1.png)



<p align="right"><a href="#top">返回目录</a></p>

----

## <a name="jvm">JVM诊断及配置优化</a>



+ `jstack`

  > Java堆栈跟踪工具，主要用于打印指定Java进程Java线程的堆栈跟踪信息
  >
  > 命令格式：jstack  <pid> 

  + ​






+ `jmap(Java Memory Map)`

  > Java内存映射工具，由于打印Java进程共享对象内存或堆内存详情

  + `jmap -heap PID` —— 查看JVM堆内存空间

    ​

  + `jmap -histo PID` —— 查看JVM对象占用情况

    ​

  + `jmap -histo:live PID` —— 查看JVM内存存活的对象

    ​

    ​


+ `jmap -dump:format=b,file=/var/log/dump.log` —— 导出JVM内存信息

  ​

+ `jmap -dump:format=b,live, file=/var/log/dump.log` —— 先做一次`Full GC`再Dump JVM中存活的对象信息




​	



+ `jhat(Java Heap Analysis Tool)`

> Java堆分析工具，用于分析Java堆中的对象信息





+ `jinfo(Java Configuration Information)`

> 实时查看和动态修改JVM参数配置

**命令格式** ：`jinfo [option] pid`

***常用选项*** ：

+ `-v`： 查看虚拟机启动时显式指定的参数列表
+ `-flag`： 查询**未被显式指定的虚拟机参数的默认值**





+ `jstat(JVM Statistic Monitoring Tool)`

> 监控JVM各种运行状态信息的命令行工具，可以显示虚拟机进程中类装载、内存、垃圾收集、JIT编译等运行时数据

**命令格式** ：`jstat [ option vmid [ interval [s|ms]  [count] ] ]`

***常用选项*** ：

+ `-class`： 监视类装载、卸载数量、总空间以及类装载所耗费的时间
+ `-gc`： 监视Java堆状况，包括`Eden`、`Survivor`、 老年代、方法区、已用空间、GC时间合计等信息
+ `-gccapacity`： 输出Java各个堆各个区域使用的最大、最小空间
+ `-gcutil`： 输出主要关注已使用空间占总空间的百分比
+ `-gcnew`： 监视新生代GC状况



**案例** ：

```sh
# 每500ms监控一次Java堆状况，一共监控20次
jstat -gc pid 500ms 20
```



-----

+ `jcmd`




+ `jps` —— 虚拟机进程状况工具

  **命令格式** ：`jps [options] [hostid]`

  + `-l`： 输出主类全名
  + `-v`： 输出虚拟机进程的启动时JVM参数






+ `arthas`

> 阿里Java诊断工具箱



<p align="right"><a href="#jvm">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

----

### <a name="jvm_config">JVM配置</a>

**优化建议** ：

+ JVM内存设置经验法则：完成`Full GC`后，应该释放出70%的内存
+ 老年代优先使用 `Parallel GC (-XX:+UseParallel[Old]GC)`，可以保证最大吞吐量
+ 开启GC日志


-----

## 网络IO

使用`netstat -napt` 查看网络连接状况。`TIME_WAIT`或`CLOSE_WAIT`连接过多会影响应用的响应速度。前者需要优化内核参数，后者可能是代码BUG导致的网络连接没有释放