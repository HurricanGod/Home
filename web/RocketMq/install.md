## Ubuntu下安装RocketMQ



+ <a href="#envCheck">**环境检查**</a>


+ <a href="#unzip">**下载并解压rocketmq-all-4.2.0-bin-release.zip**</a>






-----

<a name="envCheck">**环境检查**</a>

**环境要求** ：

+ ***64位*** 操作系统
+ ***64 位*** JDK 1.6+

```shell
# 查看 Ubuntu 系统位数
uname -m
# 若为包含 x86_64 则表示系统为64位的
# 若为只有 86 没有 64 字样表明系统是 32 位的

# 略过 JDK 安装教程
java - version
# 若输出类似 "Java HotSpot(TM) 64-Bit Server VM" 表明jdk是64位的
```



----

<a name="unzip">**下载并解压rocketmq-all-4.2.0-bin-release.zip**</a>

+ 下载`rocketmq`发行版，<a href="https://www.apache.org/dyn/closer.cgi?path=rocketmq/4.2.0/rocketmq-all-4.2.0-bin-release.zip">下载地址</a>


+ 自己想办法把下载好的`rocketmq-all-4.2.0-bin-release.zip`上传到服务器


+ 解压`rocketmq-all-4.2.0-bin-release.zip` 并修改内存大小配置

```shell
unzip rocketmq-all-4.2.0-bin-release.zip 

# 假设解压后 rocketmq 目录为 /home/hurrican/rocketmq
# 进入 rocketmq 解压目录下的 bin 文件夹
cd /home/hurrican/rocketmq/bin

# 修改 runserver.sh
vim runserver.sh

# 修改 JVM 配置中的 -server 部分
# 将 -Xms4g 改为 -Xms512m 初始堆内存大小
# 将 -Xmx4g 改为 -Xmx512m JVM允许的最大堆内存大小，当剩余堆内存小于40%时会扩展到此值
# 将 -Xmn2g 改为 -Xmn128m -Xmn参数用来来指定新生代的大小，
# 也可以通过-XX:SurvivorRation参数来调整Eden Space与Survivor Space比例来设置新生代大小
# 修改结果如下：
JAVA_OPT="${JAVA_OPT} -server -Xms512m -Xmx512m -Xmn128m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m"

vim runbroker.sh

# 修改 JVM 配置中的 -server 部分
# 修改结果如下：
JAVA_OPT="${JAVA_OPT} -server -Xms256m -Xmx256m -Xmn128m"
```

+ 启动 mqnamesrv 服务

```shell
# 启动 name server
nohup sh mqnamesrv &

# 查看 name server 的启动日志，启动 name server 后默认会生成 nohup.out
tail -2 nohup.out

# 若日志输出含有 The Name Server boot success. serializeType=JSON 表名启动 name server 成功
# 若日志输出 Java HotSpot(TM) 64-Bit Server VM warning: INFO: os::commit_memory(0x00000006ec800000, 2147483648, 0) failed; 
# error='Cannot allocate memory' (errno=12) 一般是因为启动时虚拟机内存设置太大导致内存不足无法启动
```

**使用 ps 命令查看mqnamesrv进程**，成功启动后如下图所示：

![ps_mqnamesrv](https://github.com/HurricanGod/Home/blob/master/web/RocketMq/img/CheckMqNameSrv.png)
![mqnamesrv_success_start](https://github.com/HurricanGod/Home/blob/master/web/RocketMq/img/NameServer.png)



+ 启动 `mqbroker` 服务

+ ```shell
  nohup sh mqbroker -n localhost:9876 autoCreateTopicEnable=true > broker.log 2>&1 &
  # command > broker.log 2>&1 表示命令的标准输出重定向到broker.log，标准错误输出也重定向到broker.log
  ```

  ​


+ 关闭 `rocketmq` 相关服务

+ ```shell
  # 停止 broker 服务
  sh mqshutdown broker
  # 停止 nameserver 服务
  sh mqshutdown namesrv
  ```

  ​
