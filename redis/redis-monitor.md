# <a name="top">Redis监控</a>


## <a name="throughput">**吞吐量**</a>

```shell
# 每秒输入量，单位是kb/s
redis-cli -p 6380 -a password -c info stats |grep instantaneous_input_kbps

# 每秒输出量，单位是kb/s
redis-cli -p 6380 -a password -c info stats |grep instantaneous_output_kbps

# 当前Redis实例的OPS
redis-cli -p 6380 -a password -c info stats |grep instantaneous_ops_per_sec

```


-----


## <a name="memory">**内存利用率**</a>

```shell
# Redis可用的最大物理内存，默认为0，表示不限制内存大小
redis-cli -h 127.0.0.1 -p 6380 -a password -c info memory |grep maxmemory_human

# Redis物理内存达到上限后的写入策略，默认为：noeviction，表示达到最大内存时不删除任何数据，写入操作直接返回失败
redis-cli -h 127.0.0.1 -p 6380 -a password -c info memory |grep maxmemory_policy


# Redis进程内部开销和数据占用的内存
redis-cli -h 127.0.0.1 -p 6380 -a password -c info memory |grep used_memory_human

# Redis的内存消耗峰值
redis-cli -h 127.0.0.1 -p 6380 -a password -c info memory |grep used_memory_peak_human

# Redis数据占用的内存大小
redis-cli -h 127.0.0.1 -p 6380 -a password -c info memory |grep used_memory_dataset


```

-----
## <a name="hit-rate">**命中率**</a>

```shell
# 命中的次数
redis-cli -h 127.0.0.1 -p 6380 -a password -c info memory | grep keyspace_hits

# 未命中缓存的次数
redis-cli -h 127.0.0.1 -p 6380 -a password -c info memory | grep keyspace_misses

```



-----

## <a name="clients-server">**客户端连接&服务器信息**</a>

```shell

# 查看客户端连接数
redis-cli -h 127.0.0.1 -p 6380 -a password -c info clients | grep connected_clients

# 查看被阻塞的连接数
redis-cli -h 127.0.0.1 -p 6380 -a password -c info clients | grep blocked_clients

# Redis服务器监听的端口
redis-cli -h 127.0.0.1 -p 6380 -a password -c info server | grep tcp_port

# Redis启动时使用的配置文件
redis-cli -h 127.0.0.1 -p 6380 -a password -c info server | grep config_file

# Redis服务器进程ID
redis-cli -h 127.0.0.1 -p 6380 -a password -c info server | grep process_id

```





