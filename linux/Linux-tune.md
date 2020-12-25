# <a name="top">Linux服务器高并发调优</a>



----
## <a name="process-max-open-file">单进程最大打开文件限制</a>

Linux系统单进程默认最大允许打开的文件数为1024个，在高并发场景下是远远不够的
```sh
# 查看单个进程允许打开的最大文件数
ulimit -n

# 单个进程允许打开的最大文件数设置为65535
ulimit -n 65535
```

若修改不生效可能受Linux系统用户打开文件的数的软限制或硬限制。
```
# 在 /etc/security/limits.conf 添加如下配置

# 
* soft nofile 65535

* hard nofile 65535

```




<p align="right"><a href="#process-max-open-file">返回</a> &nbsp| &nbsp <a href="#top">返回目录</a></p>

-----

## <a name="kernel-tcp">内核Tcp参数</a>

Linux系统下，TCP连接断开后，会以`TIME_WAIT`状态保留一定的时间，然后才会释放端口。当并发请求过多的时候，就会产生大量的`TIME_WAIT`状态的连接，无法及时断开的话，会占用大量的端口资源和服务器资源。

可以通过调整下Linux的TCP内核参数，让系统更快的释放TIME_WAIT连接。在 `/etc/sysctl.conf` 文件添加如下配置：

```sh
# 开启SYNCookies,当出现SYN 等待队列溢出时，启用cookies来处理，可防范少量SYN攻击，默认为0，表示关闭
net.ipv4.tcp_syncookies = 1

# 允许重用 TIME-WAIT 状态的TCP连接
net.ipv4.tcp_tw_reuse = 1

# 快速回收 TIME-WAIT 状态的TCP连接
net.ipv4.tcp_tw_recycle = 1

# 保持在FIN-WAIT-2状态的时间
net.ipv4.tcp_fin_timeout = 30

# TCP keepalive 时间，默认2小时
net.ipv4.tcp_keepalive_time = 1200

# SYN队列的长度，用于存放等待连接的网络连接，默认1024
net.ipv4.tcp_max_syn_backlog= 8192

# 端口范围
ip_local_port_range= 1024 65535

```
保存后使用 `sysctl -p` 命令使配置生效


<p align="right"><a href="#kernel-tcp">返回</a> &nbsp| &nbsp <a href="#top">返回目录</a></p>
