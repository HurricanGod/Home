## <a name="top">Linux常用命令</a> 



---







----

### <a name="netstat">netstat</a>

+ `netstat` **-ntlp**  —— 显示**TCP相关**的进程监听的端口信息

![netstat-nlt](https://github.com/HurricanGod/Home/blob/master/linux/img/netstat-nlt.png)

> `netstat -nlt | grep 330[67]` —— 查看`3306、3307`端口监听情况





+ `netstat -tulp n` —— 显示对应进程**pid**的网络端口



+ `nbtstat -A x.x.x.x` —— `x.x.x.x`为ip地址，该命令用于获取ip地址对应的域名


+ `lsof -i:端口号` —— 根据端口号查看进程
  ![](https://github.com/HurricanGod/Home/blob/master/linux/img/lsof.png)

----

### <a name="ps">ps</a>

`ps -ef|grep 搜索串` —— 查看含有**搜索串**的进程信息



----

### echo命令



+ **清空文件**  →  `echo '' > 文件名`


+ **控制输出** —— `-e`选项后面的参数(**字符串**)可以有转义字符
  + `\c` —— 出现在参数最后的位置，在`\c`之前的参数被显示后，光标不换行，新输出的内容接在该行的后面
+ `-n换行输出`



----

<a name="type">**type** —— 找出给定命令的信息</a>

+ `type -p command` —— 找出给定命令的绝对路径
+ `type -a command` —— 找出并显示命令的所有信息

![type](https://github.com/HurricanGod/Home/blob/master/linux/img/type.png)



------

<a name="du">文件系统磁盘使用情况 —— **du**</a>

> du(disk usage)——用于查找文件或目录的磁盘使用情况

+ `du -h /home` —— 显示 `/home`目录下的所有文件和目录以及显式块大小


+ `du -sh /home` —— 目录的总磁盘大小

![du-h](https://github.com/HurricanGod/Home/blob/master/linux/img/du-h.png)



------

<a name="df">显式linux系统的磁盘利用率 —— **df**</a>

+ `df -h` —— 显示设备名称、总块数、总磁盘空间、已用磁盘空间、可用磁盘空间和文件系统上的挂载点



+ `df -hT /home` —— **显示特定分区信息**


![](https://github.com/HurricanGod/Home/blob/master/linux/img/dfh.png)

------

<a name="calulateFileCount">**统计当前目录的文件**</a>

 `ls -l .|egrep -c '^-'`

+ `-l` —— 以列表形式显式
+ `.` —— 指定 `ls` 命令操作的路径，`.`表示当前路径
+ `-c` —— 通用输出控制
+ `^-` —— 以`-`开头的行，`ls -l`命令的结果**若行首为 - 代表普通文件**




----

<a name="showZProcess">**找出僵尸进程**</a>

+  `ps aux|grep Z` —— 列出进程表中的僵尸进程


+ `kill -s SIGCHLD pid` —— 杀掉僵尸进程






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

<a name="userManage">**Linux用户管理**</a>

|         命令         |      描述       |
| :----------------: | :-----------: |
| `useradd new-user` | 创建一个先的linux用户 |
| `passwd username`  |  重置Linux用户密码  |
| `deluser username` |    删除一个用户     |





------

### <a name="head">通过管道截取上一条命令的前n行</a>

```shell
head -n 10 file
head -10 file
# file 为文件名
# 管道组合使用：
ll |head -2	# 输出ll命令结果的前2行
```





----

<a name="tail">通过管道截取上一条命令的最后n行</a>

```shell
tail -n 10 file
tail -10 file
# file 为文件名
ll |tail -2	# 输出ll命令结果的最后两行
```

-----
<a name="iftop">实时网络监控</a>
```shell
# 安装 iftop 工具
apt install iftop

# 查看网卡信息
ifconfig

# 监控网络
iftop -i eth0
```

