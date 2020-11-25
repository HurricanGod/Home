# <a name="top">MySQL监控要点</a>



+ 服务器监控

+ MySQL监控







----

## 服务器监控

基础指标：`网络`、`CPU`、`内存`、`磁盘`、`系统负载`、`文件 fd 数量`

+ **监控CPU使用情况**：通过系统文件` /proc/stat `计算获取监控项：user、idle、iowait、sys
+ **监控Load信息**：通过系统文件 /proc/loadavg 计算获取 load1、load5、load15
+ **监控磁盘空间信息**：
+ **监控内存使用情况**： 通过系统文件 /proc/meminfo 计算获取 used 、free、buffer、cache、total
+ **监控虚拟内存信息**： 通过 ` free –m|grep swap` 即可采集
+ **监控 TCP 信息**：通过系统文件 /proc/net/tcp 计算获取 close、connected、listen、syn
+ **监控 Net 信息**：通过系统文件 /proc/net/dev 计算获取，recv 接收数据速度，send 发送数据速度







----

## MySQL监控要点

MySQL 大部分的监控取值来源于 MySQL 运行状态值

+ **根据状态值 Querys 计算 QPS**：取值方式为 `show global status like '%Queries%';`
+ 根据采集数据库状态值 com_select、com_delete、com_insert、com_update、com_update_multi、com_insert_select 来监控数据库每秒操作次数
+ 根据 IBP 读写来计算 IBP 的缓存命中率、连接数和慢查询数的使用情况



**MySQL常见监控指标**

