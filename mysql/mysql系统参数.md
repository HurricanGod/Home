# <a name="top">MySQL系统参数配置</a>



+ <a href="#log">**日志相关配置**</a>


+ <a href="#queryCache">**查询缓存配置**</a>


+ <a href="#filesort">**文件排序**</a>


+ <a href="#connection">**连接相关配置**</a>


+ <a href="#innodb">**innodb存储引擎相关配置**</a>





----

## <a name="log">**日志相关配置**</a>

+ 配置错误日志文件存放的位置

  ```sh
  log-error	= /var/log/mysql/error.log
  ```

  ​


+ **慢查询日志相关的配置**

  ```sh
  # 开启慢查询
  slow_query_log = ON

  # 慢查询日志存放的位置
  slow_query_log_file = /var/log/mysql/mysql-slow.log

  # 慢查询时间阀值
  long_query_time = 2

  # 记录没有使用索引的查询语句
  log_queries_not_using_indexes = 1
  log_queries_not_using_indexes = on

  # 查看没有使用索引查询配置参数
  show variables like 'log_queries_not_using_indexes';

  # 查看慢查询相关的配置
  show variables like '%slow_query%';

  # 查看慢查询日志格式，默认为file，为table时慢查询日志将记录在mysql.slow_log表
  show variables like '%log_output%';

  # 扫描全表少于min_examined_row_limit的记录将不会被记录至慢查询日志
  show variables like  'min_examined_row_limit';


  # 将慢查询日志输出方式改为table
  set global log_output = 'TABLE';

  # MySQL数据库支持同时两种(FILE和TABLE)日志存储方式
  set global log_output = 'TABLE,FILE';

  # 动态修改 log_queries_not_using_indexes 配置
  set global log_queries_not_using_indexes = 1; 

  ```

  ​


+ **查询日志相关配置**

  ```sh
  # 开启sql执行日志记录是很耗性能，一般只用于debug
  # 开启sql执行日志记录
  general_log = ON

  # 配置sql执行日志存放的路径
  general_log_file=/var/log/mysql/mysql-general.log

  # 查看 general 相关配置
  show variables like '%general%';

  # 动态修改 general 日志开关
  set global general_log = on;

  ```

  ​


+ 日志自动过期清理相关配置

  ```mysql
  # 日志自动过期清理天数
  expire_logs_days = 30
  ```

  ​







<p align="right"><a href="#log">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a></p>

----

## <a name="queryCache">**查询缓存配置**</a>

+ 查询缓存开关

  ```sh
  # query_cache_type可选的值有：on、off、demand
  # on表示开启查询缓存
  # off表示关闭查询缓存
  # demand表示在sql中声明使用查询缓存才会缓存查询结果
  # 开启查询缓存
  query_cache_type = on

  # 查询查询缓存相关的配置
  show variables like '%query_cache%';
  ```

  ​


+ 查询缓存大小相关的配置

  ```sh
  # 配置查询缓存使用的总内存空间
  query_cache_size = 2048M

  # 配置查询结果能缓存的最大值，如果查询结果超过配置值将不会被缓存
  query_cache_limit = 8M

  # 配置查询缓存分配内存的最小单位
  query_cache_min_res_unit = 4k
  ```

  ​


+ **查询缓存使用情况**

  ```sh
  show status like '%qcache%';
  ```

  ![query-cache-qcache]()

  + `Qcache_free_blocks`：查询缓存内空闲内存块的数量
  + `Qcache_free_memory`：查询缓存内空闲的内存大小
  + `Qcache_hits`：缓存的命中次数
  + `Qcache_inserts`： 缓存的写入次数
  + `Qcache_total_blocks`： 缓存中的块的数量
  + `Qcache_lowmem_prunes`： 由于查询缓存内存不足，删除旧缓存结果的次数
  + `Qcache_queries_in_cache`： 当前缓存中的查询数量
  + `Qcache_not_cached`： 查询结果没有被缓存的次数，查询结果不被缓存的原因主要有：









<p align="right"><a href="#queryCache">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a></p>

---

**参考**：

+ <a href="https://www.cnblogs.com/zping/p/10797498.html">mysql 5.7配置项最详细的解释</a>