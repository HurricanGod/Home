# <a name="top">Web应用程序的部署</a>

+ <a href="#jdk">**JDK安装**</a>


+ <a href="#mysql_install">**MySQL安装与配置**</a>
  + <a href="#mysql-install">安装</a>
  + <a href="#create-user">创建外网访问用户</a>
  + <a href="#config-mysql">配置MysQL服务器</a>




+ <a href="#redis">**Redis编译安装**</a>


+ <a href="#kafka">**Kafka安装与使用**</a>


-----

## <a name="jdk">JDK安装</a>

+ 解压 `jdk` 到指定目录

  ```shell
  mkdir -p /home/hurrican/jdk
  cd /home/hurrican/jdk

  # 将 jdk 压缩包解压到 /home/hurrican/jdk 目录
  tar -zxvf jdk-8u144-linux-x64.tar.gz -C ./
  ```

+ 配置 `java` 环境变量

  ```shell
  vim /etc/profile

  # 在最后面追加以下内容，假设jdk解压路径(/home/hurrican/jdk)
  export JAVA_HOME=/home/hurrican/jdk/jdk1.8.0_181
  export JRE_HOME=${JAVA_HOME}/jre
  export CLASSPATH=.:$JAVA_HOME/lib:${JRE_HOME}/lib
  export PATH=JAVA_HOME/bin:$PATH

  ```

+ **使配置的环境变量生效**

  ```shell
  source /etc/profile

  # 查看是否成功配置 java 环境变量
  java -version
  ```

+ 配置加快 `Tomcat` 启动的**Java**系统属性

  ```shell
  # 查找 java.security 所在的文件路径
  locate java.security

  # 若未找到 java.security 文件，先检查是否安装了jdk
  # 安装 jdk 还是没有找到可以使用 updatedb 命令刷新一下 Linux 文件数据库
  updatedb

  # 在运行 locate java.security 命令后输出内容示例如下：
  # /home/hurrican/jdk/jdk1.8.0_181/jre/lib/security/java.security

  vim /home/hurrican/jdk/jdk1.8.0_181/jre/lib/security/java.security

  # 搜索 securerandom.source
  #  将 securerandom.source=file:/dev/random 改为 securerandom.source=file:/dev/./urandom
  ```

  ​


<p align="right"><a href="#jdk">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a></p>

-----

## <a name="mysql_install">**MySQL5.7安装与配置**</a>

### <a name="mysql-install">安装</a>
```shell
apt update

# 查看 apt-get install mysql-server 可以安装的 MySQL 版本
apt-cache search mysql|grep mysql-server

# 获取 MySQL5.7 版本相关文件
wget -P /home/mysql http://dev.mysql.com/get/mysql-apt-config_0.8.0-1_all.deb
dpkg -i /home/mysql/mysql-apt-config_0.8.0-1_all.deb

apt-get update

# 安装 MySQL 服务器，过程略
apt-get install mysql-server

```




### <a name="create-user">创建MySQL外网访问用户</a>
登录 `MySQL` 服务器
```mysql
# mysql -u root -p${password} 直接一步登录MySQL
mysql -u root -p
Enter password: 

# 以 root 身份登录进去后创建1个允许远程访问的用户
# 语法： create user '用户名'@'IP' identified by '密码';
# IP 选项中 % 是通配符，表示允许所有用户登录
create user 'Hurrican'@'%' identified by 'Xmx256Xms128';

# 给 Hurrican 用户授予数据库的所有权限
grant all privileges on *.* to 'Hurrican'@'%' identified by 'Xmx256Xms128';

# 刷新用户权限
flush privileges;


# 回收 Hurrican 用户的 drop、 grant option权限
revoke drop, grant option on *.* from 'Hurrican'@'%';


# 查询 Hurrican用户的所有权限
select * from mysql.user where user='Hurrican'\G;
```
**注意点**:
+ **安装过程中有个输入 root 账户密码的步骤，需要记住这个密码**
+ 若进行上述操作任**无法使用外网访问服务器**上的 `MySQL` 时可以参考<a href="https://www.cnblogs.com/funnyboy0128/p/7966531.html">这篇博客</a>


------

### <a name="config-mysql">配置MysQL服务器</a>

若**修改了MySQL日志文件存放路径**，则需要进行如下操作：
+ 修改`/etc/apparmor.d`目录下的usr.sbin.mysqld
```sh
vim /etc/apparmor.d/usr.sbin.mysqld
# 假设现在把 log_error 配置为：/mnt/log/mysql/error.log
# 在usr.sbin.mysqld 最后面添加如下内容：
/mnt/log/mysql r,
/mnt/log/mysql/** rw, 
```

+ 重启apparmor
```sh
/etc/init.d/apparmor restart
```

<p align="right"><a href="#mysql_install">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a></p>

----

<a name="mysql-connections-conf">MySQL连接数配置</a>

+ 查看MySQL最大连接数

  ```mysql
  # 查看MySQL最大连接数配置
  select @@global.max_connections;

  show variables like '%connections%';
  ```

  ![connections]()

> max_user_connections —— 配置一个用户session的连接数，若max_user_connections = 1，任何用户与MySQL服务器的session个数只能为1

+ 修改MySLQ最大连接数

  ```mysql
  set @@global.max_connections=10000;
  ```

  **注意**： 

  1. 上面命令是在不重启MySQL服务器情况下动态调整MySQL最大连接数，服务器重启后失效。
  2. `max_connections`配置的值过小会造成`ERROR 1040 (HY000): Too many connections`错误



+ 收回非`root`用户的`super`权限

  ```mysql
  # 收回非root用户的super的权限，当发生 Too many connections 错误时数据库管理员可以通过 root 用户登录MySQL服务器进行处理
  revoke super on *.* from 'hurrican'@'%';
  ```

  ​



<p align="right"><a href="#mysql_install">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a></p>

----
## <a name="redis">Redis的安装</a>

<a href="http://download.redis.io/releases/">**Redis源码压缩包下载地址**</a>

需要装 `redis-4.0.0` 可以使用 ` wget http://download.redis.io/releases/redis-4.0.0.tar.gz` 将资源下载到服务器，然后再**编译安装**

+ 解压下载后的 `redis` 压缩包 —— `tar -zxvf redis-4.0.0.tar.gz -C /usr/redis`

+ 进入解压后的目录，执行 `make` 命令

+ 接着执行 `make test`

+ 继续执行 `make install`

+ **启动** & **停止** Redis服务
```sh
# 启动 redis 服务端：redis-server 配置文件名
redis-server /etc/redis/redis.conf

# 停止 redis 服务
redis-cli -h 127.0.0.1 -p 6379 shutdown

# 登录有密码的 redis-cluster 
redis-cli -c -h 127.0.0.1 -p 6379 -a auth
```


**make test** 过程中若出现 `You need tcl 8.5 or newer in order to run the Redis test` 错误需要先安装 `tcl`
```shell
wget http://downloads.sourceforge.net/tcl/tcl8.6.1-src.tar.gz 
tar xzvf tcl8.6.1-src.tar.gz  -C /usr/local/
cd  /usr/local/tcl8.6.1/unix/  
./configure  
make  
make install   
```





<p align="right"><a href="#redis">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a></p>

## <a name="kafka">Kafka安装</a>
假设`Kafka`目录安装在`/home/hurrican/kafka`

+ **启动Kafka Broker** ：
```shell
cd /home/hurrican/kafka/bin/
# 如果不使用nohup，终端关闭后启动的 Kafka Broker 就会退出
nohup ./kafka-server-start.sh  ../config/server.properties & 
```

+ **关闭Kafka Broker** ：
```shell
cd /home/hurrican/kafka/bin/
./kafka-server-stop.sh
```

+ **查看Topic列表** ：
```shell
cd /home/hurrican/kafka/bin/
./kafka-topics.sh --zookeeper 127.0.0.1:2181 --list
```


+ **往Topic发消息** ：
```shell
cd /home/hurrican/kafka/bin/
./kafka-console-producer.sh --broker-list localhost:9092 --topic day_topic
```

+ **消费消息** ：
```shell
cd /home/hurrican/kafka/bin/
# 消费Topic名为day_topic的消息
./kafka-console-consumer.sh --zookeeper 127.0.0.1:2181 --topic day_topic --from-beginning
```

+ **查看Kafka消息消费情况** ：
```shell
cd /home/hurrican/kafka/bin/
./kafka-run-class.sh kafka.tools.ConsumerOffsetChecker --zookeeper 127.0.0.1:2181 --group group_day_topic --topic day_topic
# --group：指定消费者组
# --topic：指定topic
```
![Topic消费情况](https://github.com/HurricanGod/Home/blob/master/linux/img/kafka-consumer-detail.png)


