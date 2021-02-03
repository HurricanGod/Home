# <a name="top">Linux常用命令</a> 

+ <a href="#quick_key">**快捷键**</a>
+ <a href="#netstat">`netstat`</a>
+ <a href="#ps">**进程**</a>
+ <a href="#echo">`echo`</a>
+ <a href="#disk">**磁盘**</a>
+ <a href="#userManage">**用户管理**</a>
+ <a href="#head_tail">`head & tail`</a>
+ <a href="#network_monitor">**网络监控**</a>
+ <a href="#iptables">**防火墙**</a>
+ <a href="#tar">**打包&压缩**</a>
+ <a href="#nslookup">**域名解析nslookup**</a>
+ <a href="#lsof">**lsof**</a>








----

## <a name="quick_key">快捷键</a>
+ `ctrl + u`：删除光标到行首的内容
  ![ctrl_u用法示例](https://github.com/HurricanGod/Home/blob/master/linux/img/ctrl_u.gif)




----


## <a name="netstat">netstat</a>


### Linux


+ `netstat` **-ntlp**  —— 显示**TCP相关**的进程监听的端口信息
  + `-a (all)`：显示所有选项，默认不显示LISTEN相关
  + `-t (tcp)`：仅显示tcp相关选项
  + `-u (udp)`：仅显示udp相关选项
  + `-n`：拒绝显示别名，能显示数字的全部转化成数字
  + `-l `：仅列出有在 Listen (监听) 的服務状态
  + `-p`：显示建立相关链接的程序名
  + `-s`：按各个协议进行统计
  + `-T (show threads)`：显示线程





+  `netstat -nlt | grep 330[67]` —— 查看`3306、3307`端口监听情况
         ![netstat-nlt](https://github.com/HurricanGod/Home/blob/master/linux/img/netstat-nlt.png)

+  `netstat -tulp n` —— 显示对应进程**pid**的网络端口


+ `nbtstat -A x.x.x.x` —— `x.x.x.x`为ip地址，该命令用于获取ip地址对应的域名


+ `lsof -i:端口号` —— 根据端口号查看进程
  ![](https://github.com/HurricanGod/Home/blob/master/linux/img/lsof.png)


+ `ps -T -p pid` —— 显示进程 `pid` 包含的线程
  ![thread_in_pid](https://github.com/HurricanGod/Home/blob/master/linux/img/ps-T-p.png)


### Windows

**查看端口对应的进程信息**

```sh
netstat -ano|findstr 8082
```


<p align="right"><a href="#netstat">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a><p>

----

## <a name="ps">进程</a>

***命令格式***  ：`ps [options]`

**常用options** ：

+ `-a` ： 显示终端上的所有进程，包括其他用户的进程
+ `-u`： 以用户为主的格式来显示程序状况
+ `-x`： 显示没有控制终端的进程
+ `-aux`： 显示所有包含其他使用者的行程
+ `-e` ： 显示所有进程,环境变量
+ `-f`： 全格式输出
+ `-w`： 宽格式输出
+ `-l`：长格式输出
+ `--sort`—— 根据`sort`后面的key进行排序，其中`-`表示逆序，`+`表示正序



**常用命令** ：

```sh
# 根据进程名搜索进程信息
ps -ef|grep 搜索串

# 列出进程表中的僵尸进程
ps aux|grep Z

# 杀掉僵尸进程
kill -s SIGCHLD pid

# 根据占用物理内存大小由高到低输出进程信息
# VSZ：占用的虚拟内存
# RSS：占用的物理内存
ps -auxw --sort=-rss

#  查看所有进程信息，按照物理内存占用由高到低，cpu使用率由低到高排序
ps -auxw --sort=-rss,+%cpu
```








<p align="right"><a href="#ps">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a><p>

----

## <a name="echo">echo命令</a>



+ **清空文件**  →  `echo '' > 文件名`


+ **控制输出** —— `-e`选项后面的参数(**字符串**)可以有转义字符
  + `\c` —— 出现在参数最后的位置，在`\c`之前的参数被显示后，光标不换行，新输出的内容接在该行的后面

    ```shell
    echo -e "Hello\t\c" && echo "World"
    # Hello	World
    ```

    ​
+ `-n换行输出`




<p align="right"><a href="#echo">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a><p>

----

## <a name="type">**type** —— 找出给定命令的信息</a>

+ `type -p command` —— 找出给定命令的绝对路径
+ `type -a command` —— 找出并显示命令的所有信息

![type](https://github.com/HurricanGod/Home/blob/master/linux/img/type.png)



------

## <a name="disk">磁盘</a>

### <a name="du">磁盘使用情况 —— **du**</a>

> du(disk usage)——用于查找文件或目录的磁盘使用情况

```shell
#  显示 `/home`目录下的所有文件和目录以及显式块大小
du -h /home

# 目录的总磁盘大小
du -sh /home

# 查看当前目录及其子目录磁盘空间占用情况
du -h  --max-depth=1
du -h -d 1 
```

![du-h](https://github.com/HurricanGod/Home/blob/master/linux/img/du-h.png)



------

### <a name="df">磁盘利用率 —— **df**</a>

```shell
# 显示设备名称、总块数、总磁盘空间、已用磁盘空间、可用磁盘空间和文件系统上的挂载点
df -h

# 显示特定分区信息
df -hT /home
```


![](https://github.com/HurricanGod/Home/blob/master/linux/img/dfh.png)

<p align="right"><a href="#disk">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a><p>

------

<a name="calulateFileCount">**统计当前目录的文件**</a>

 `ls -l .|egrep -c '^-'`

+ `-l` —— 以列表形式显式
+ `.` —— 指定 `ls` 命令操作的路径，`.`表示当前路径
+ `-c` —— 通用输出控制
+ `^-` —— 以`-`开头的行，`ls -l`命令的结果**若行首为 - 代表普通文件**




----

<a name="kill">**kill**命令</a>

```shell
kill [-alpsu] pid
a: 当处理当前进程时，不限制命令名和进程号的对应关系
l: l参数会列出全部的信息名称
p: kill 命令只打印相关进程的进程号，而不发送任何信号
s: 指定要送出的信息
```



<a href="http://man.linuxde.net/kill">参考博客</a>

-----

<a name="history">**history**</a>

> 显示用户之前执行的bash脚本历史记录



-----

## <a name="userManage">用户管理</a>

| 命令                           | 描述            |
| :--------------------------- | :------------ |
| `useradd new-user`           | 创建一个先的linux用户 |
| `passwd username`            | 重置Linux用户密码   |
| `deluser username`           | 删除一个用户        |
| `chgrp username filename -R` | 改变文件所属用户组     |
| `chown username filename -R` | 改变文件所属用户      |



<p align="right"><a href="#userManage">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a><p>

------

## <a name="head_tail">head & tail</a>

**通过管道截取上一条命令的前n行**
```shell
head -n 10 file
head -10 file
# file 为文件名
# 管道组合使用：
ll |head -2	# 输出ll命令结果的前2行
```

**通过管道截取上一条命令的最后n行**

```shell
tail -n 10 file
tail -10 file
# file 为文件名
ll |tail -2	# 输出ll命令结果的最后两行
```

<p align="right"><a href="#head_tail">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a><p>

-----
## <a name="network_monitor">网络监控</a>
```shell
# 安装 iftop 工具
apt install iftop

# 查看网卡信息
ifconfig

# 监控网络
iftop -i eth0
```

![iftop相关参数](https://github.com/HurricanGod/Home/blob/master/linux/img/iftop-1.jpg)

+ `TX` —— 发送流量
+ `RX`—— 接收流量
+ `TOTAL`—— 总流量
+ `peak` —— 流量峰值
+ `rates`—— 表示过去 `2s`、`10s`、 `40s`的平均流量

**可选参数**：

+ `iftop -n -i eth0` —— host信息默认直接都显示IP
+ `iftop -F 113.116.29.0/24 -i eth0` —— 显示特定网段的进出流量



`iftop`界面操作命令：

+ `n`：切换显示本机的IP或主机名
+ `N`：切换显示端口号或端口服务名称
+ `t`：切换显示格式为2行/1行/只显示发送流量/只显示接收流量
+ `T`： 切换是否显示每个连接的总流量

<p align="right"><a href="#network_monitor">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a><p>

----

## <a name="iptables">防火墙</a>

### iptables

+ 添加过滤规则

  ```shell
  iptables -t filter -A INPUT -p tcp -m tcp --dport 8080 -s localhost -j ACCEPT

  iptables -t filter -A INPUT -p tcp -m tcp --dport 8080 -j REJECT
  ```

  + `-p` 参数：用于指定协议
  + `-dport`参数： 指定目标端口，指数据从外网访问服务器使用的端口号
  + `-sport`参数：数据源端口，指从服务器出去的端口
  + `-j`参数：**ACCEPT**表示接收


### Ubuntu防火墙配置工具 —— ufw

+ 启用命令：
```shell
# ufw 默认是没有启用的
ufw enable
```

+ 查看防火墙状态：
```shell
ufw status
```

+ 开放&关闭端口
```shell
# 开放80端口
ufw allow 80

# 开放的22端口只允许使用tcp协议访问
ufw allow 22/tcp

# 删除开放的80端口
ufw delete allow 80 

```

+ 开放ip
```shell
ufw allow from 192.168.168.10

# 删除开放的ip规则
ufw delete allow from 192.168.168.10
```

<p align="right"><a href="#iptables">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a><p> 

------

<a name="tar">**打包&压缩**</a>

### 打包压缩

```sh
# 将/home/ubuntu打包并压缩
tar -zcvf filename.tar.tgz /home/ubuntu
```



### 解压

| 文件后缀      | 解压命令                      | 说明                                       |
| :-------- | :------------------------ | :--------------------------------------- |
| `7z`      | 7z x filename.7z          | 需要先安装`7z`命令：`apt-get install p7zip-full` |
| `tar.tgz` | tar zxvf filename.tar.tgz |                                          |
|           |                           |                                          |
|           |                           |                                          |



<p align="right"><a href="#tar">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a><p> 

----


### <a name="nslookup">查看域名解析 —— nslookup</a>

命令格式：`nslookup ip/domain`

<p align="right"><a href="#iptables">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a><p> 



----

## <a name="cut">分割文件</a>

`cut -d "分隔符" -f${index} 文件`

+ `-d`： 指定分隔符
+ `-f`： 选择要提取哪些字段值






<p align="right"><a href="#cut">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a><p> 



-----

## <a name="lsof">lsof</a>

Linux下的主要文件包括：

+ 普通文件
+ 目录
+ 符号链接
+ 设备文件
+ 管道和命名管道
+ 套接字



`lsof`即**list open files**，该命令的默认行为是对结果进行***或*** 运算，使用`-a`选项可以将多个组合的条件变为 **与** 的关系

**命令格式**： `lsof [option] filename`

**常用功能** ：

+ 恢复打开但被删除的文件（**进程还在运行，但可执行文件被删除了**）

  ```sh
  # 找到文件被删除，但进程还运行着的进程pid
  lsof |grep keyword
  # 查看该进程打开的文件描述符
  ll /proc/${pid}/fd

  # 恢复被删除的文件，通过重定向的方式恢复
  cat /proc/${pid}/fd/${待恢复文件描述符id} > recover-file
  ```

  ![lsof-1](https://github.com/HurricanGod/Home/blob/master/linux/img/lsof-1.png)

+ 查看当前文件被哪些进程打开：`lsof filename`

+ 查看进程打开了哪些文件：`lsof -c 进程名` 或 `lsof -p ${pid}`

+ 查看端口被占用情况：`lsof -i :${port}`

+ 查看`TCP/UDP`连接情况

  ```sh
  # 查看tcp连接情况
  lsof -i tcp

  # 查看udp连接情况
  lsof -i udp

  # 查看处于 LISTEN 状态的tcp连接
  lsof -i  -sTCP:LISTEN

  # 查看处于 ESTABLISHED 状态的tcp连接
  lsof -i  -sTCP:ESTABLISHED
  ```

  ​

  ​

<p align="right"><a href="#lsof">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a><p> 

