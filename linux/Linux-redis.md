# <a name="top">Redis</a>



----

## Redis服务端

+ 启动服务端

  ```sh
  # 启动 redis 服务端：redis-server 配置文件名
  redis-server /etc/redis/redis.conf
  ```

+ 停止服务

  ```sh
  # 停止 redis 服务
  redis-cli -h 127.0.0.1 -p 6379 shutdown
  ```



---

## Redis客户端



+ 连接单机redis

  ```sh
  redis-cli -h 127.0.0.1 -p 6379 -a auth
  ```

  

+ 连接redis集群

  ```sh
  # 登录有密码的 redis-cluster 
  redis-cli -c -h 127.0.0.1 -p 6379 -a auth
  ```

  

+ 批量删除key

  ```sh
  # 删除 db0 指定模式的key
  redis-cli -h 127.0.0.1 -p 6379 keys "pattern*" | xargs redis-cli -h 127.0.0.1 -p 6379 del
  
  # 删除 db2 匹配某个模式的key
  redis-cli -n 2  keys "pattern*" | xargs redis-cli -n 2 del
  ```

  

----

## Redis监控指标



+ 监控大key

  ```sh
  redis-cli -h 127.0.0.1 -p 6379  --bigkeys
  ```

  





+ 



