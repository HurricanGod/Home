## <a name="top">查询优化</a>

+ <a href="#explain">explain的使用</a>


+ <a href="#join">连接</a>


+  <a href="#optimize_suggest">查询优化建议</a>


+ <a href="#big_table_optimize">大表优化</a>

----

##  <a name="explain">explain的使用</a>



**查询性能低下的最基本原因就是访问了太多的数据** 。使用`explain`查看SQL执行计划时，我们需要关注的列有：

+ `type` —— SQL查询目标行的方式，生产环境至少要达到`range`级别。性能由差到好的顺序为：
  + `all` —— 全表扫描
  + `index` —— 按索引顺序全表扫描
  + `range` —— 有限制的索引扫描
  + `ref` —— 索引查找
  + `eq_ref` —— 索引查找，最多返回1条记录
  + `const|system` —— 常量
  + `null`
+ `key` —— 使用到的索引名，**没有选择索引值为null**
+ `rows` —— 扫描行数
+ `extra` —— 详细说明，重点关注`using filesort`和`using temporary(对查询结果排序使用了临时表)`

```sql
 explain select * from testindex where id<3;
```

|  id  | select_type |   table   | partitions | type  | possible_keys |   key   | key_len | ref  | rows | filtered | Extra       |
| :--: | :---------: | :-------: | :--------: | :---: | :-----------: | :-----: | :-----: | :--: | :--: | :------: | ----------- |
|  1   |   SIMPLE    | testindex |    NULL    | range |    PRIMARY    | PRIMARY |    4    | NULL |  2   |  100.0   | Using where |

![explain查询分析](https://github.com/HurricanGod/Home/blob/master/img/mysql-explain-command.png)

**MySql会在3中情况使用Where子句，效率依次降低：**

+ 对索引查找使用where子句来消除不匹配的行，发生在存储引擎层
+ 使用覆盖索引(Extra列是'using index')来避免访问行并且从索引取得数据后过滤掉不匹配的行，发生在服务器层，不需要从表中读取行
+ 从表中检索数据，然后过滤掉不匹配的行(Extra列是'using where')，发生在服务器端并且需要从表中读取行后再过滤掉

**MySql执行查询的一般性过程：**

+ 客户端将查询请求发送到服务器
+ 服务器检查查询缓存，如果找到了就从缓存中返回结果，否则进行下一步
+ 服务器解析，预处理和查询优化，生成执行计划
+ 执行引擎调用存储引擎API执行查询
+ 服务器将结果发送会客户端



解析：MySql解析器将查询分解成一个个标识，构造“解析树”，对sql语句进行语法分析

预处理：检查解析器生成的结果树，进行语义分析，语义分析完成后如果没有错误进行**权限检查**

查询优化：找到最优的查询执行方式

----

### MySql能够处理的优化类型

1. 将外连接转换成内连接
2. 代数等价法则
3. 优化count()、min()、max()
4. 计算和减少常量表达式
5. 覆盖索引
6. 子查询优化

------
## <a name="join">连接</a>



**外连接**：

+ 左外连接`left outer join` —— 两个表使用某一字段进行条件连接，连接结果为：左边表内容全部输出，右边表符合**连接条件**的输出，不符合的则为`null`
+ 右外连接`right outer join`—— 两个表使用某一字段进行条件连接，连接结果为：右边表内容全部输出，左边表符合**连接条件**的输出，不符合的则为`null`

**内连接**：

+ `inner join` —— 两个表使用某一字段进行条件连接，对于符合条件的都输出，不符合条件的都不输出



<p align="right"><a href="top">返回目录</a></p>

----

## <a name="optimize_suggest">查询优化建议</a>

+ `sql`中`in`包含的值不应该过多，能够使用 `between`的代替`in`的就不要使用`in`
+ `or` 条件优化，可以考虑使用`union all` 进行替代


+ 尽量使用`union all` 代替 `union`，前提条件是：查询出来的结果集**没有重复数据**

  >union和union all的区别：union需要将结果集合并后再进行唯一性过滤操作，会涉及到排序，增加CPU运算，加大资源消耗及延迟。
  >
  >​

+ `in`用于***外表大内表小***的情况，`exists`适用于**外表小内标大**的情况


+ 避免在 where 子句中对索引字段进行表达式操作


+ 对于联合索引来说，要遵守最左前缀法则


+ sql中若对普通索引列使用了`!=` 或 `<>` 查询，查询引擎将会放弃索引进行**全表扫描**


+ 同样`not in` 操作也会使普通索引失效进行**全表扫描**


+ 禁止使用`select *`，`select`时必须指明字段名称，原因如下：
  + `select *`增加了不必要的开销（CPU、IO、内存、网络带宽）
  + 使用`select *`基本不可能使用**覆盖索引**
  + 当表结构发生改变时，增加了修改业务代码可能性


+ 尽量避免在  `where` 子句中进行 `null` 值判断，使用`null` 值判断将导致放弃索引进行**全表扫描**


+ 排序字段尽量加上索引，没有索引大部分情况下排序需要借助**文件**进行排序


+ 大表若条件允许建议使用自增主键进行分页查询


+ 大表禁止使用 `%` **前缀模糊查询**，原因：将导致全表扫描


+ 尽量使用**数字字段**，数字字段比较比字符串的比较速度快


+ 尽量避免大事务操作，提高系统并发能力


<p align="right"><a href="top">返回目录</a></p>

-----

### <a name="big_table_optimize">大表优化</a>

+ **字段**
  + 尽量使用`tinyint`、`smallint`，如果非负加上`unsigned`
  + 尽量使用`timestamp`替代`datetime`
  + 避免使用`null`字段
+ **索引**
  + 值分布很稀少的字段不适合建立索引
  + ......
+ **查询SQL**
+ **系统调优参数**
  + 基准测试工具：
    + `sysbench`：跨平台的多线程性能测试工具
    + `tpcc-mysql`：TPC-C测试工具
  + 常见的重要调优参数：
    + `back_log`：指在MySQL暂时停止回答新请求之前的短时间内多少个请求可以被存在堆栈中，也就是说，如果MySQL的连接数达到了`max_connection`时，新来的请求将会被存放在堆栈中，以等待释放连接后的资源，如果待连接的数达到`back_log`后，新来的连接将不会授予资源
    + `wait_timeout`：数据库连接闲置时间，限制连接会占用内存资源，默认8小时
    + `max_user_connection`： 最大连接数，默认为0，即无上限
    + `thread_concurrency`：并发线程数，一般设为CPU核数的2倍
    + `skip_name_resolve`：禁止对外部连接进行DNS解析，消除DNS解析时间
    + `key_buffer_size`：索引块的缓存大小，增加可以提升索引处理速度。对于4G内存，可设置为256M或384M。`show status like key_read%`查看索引访问状态，保证`key_reads / key_read_requests`在0.1%以下最好
    + `innodb_buffer_pool_size`：缓存数据块和索引块，对`innodb`表性能影响最大
    + `innodb_log_buffer_size`：`InnoDB`存储引擎的事务日志所使用的缓冲区，一般来说不建议超过32M
    + `query_cache_size`：缓存MySQL的SQL的查询结果，仅针对select有效，当某个表的数据有任何变化都会导致所有引用该表的查询缓存失效。
      + 该值可以根据`( Qcache_hits / (Qcache_hits + Qcache_inserts) * 100% )`进行调整
      + 可以通过 `show status like 'Qcache%'` 查看MySQL Query cache的使用情况
      + 一般设置在256M左右
    + `read_buffer_size`：MySQL读入缓冲区大小。对表进行顺序扫描的请求将分配一个读入缓冲区，MySQL将会为它分配一段内存缓冲区。如果对表的顺序扫描请求非常频繁，可以增加该变量的值以及内存缓冲区大小以提升性能
    + `sort_buffer_size`：MySQL执行排序使用的缓冲区大小。可以尝试调整该值提升`order by`的速度
    + `read_rnd_buffer_size`：MySQL随机读缓冲区大小。当按任意顺序读取行时，将分配一个随机读缓存区。进行排序查询时，MySQL会先扫描该缓冲区以避免磁盘搜索。MySQL为每个客户端连接分配缓冲空间，**应恰当设置该值以避免内存开销过大**
    + `thread_cache_size`：缓存线程数量，新连接进来可以快速响应
+ **读写分离**
+ **垂直拆分**
  + 垂直分库：把相关性高发表拆分到同一个库
  + 垂直分表： 常见的是把一个多字段的表按照是否常用拆分
  + 垂直拆分的优点：
    + 可以使得行数据变小，一个数据块可以存放更多的数据，查询是可以减少`I/O`次数
    + 利于做Cache管理
    + 数据维护简单
+ **水平拆分**
  + 分库
  + 分库内分表
+ ​









<p align="right"><a href="top">返回目录</a></p>

-------

### B+树索引存取方法的选择

1. 如果1个或1组属性经常在查询条件中出现，则考虑在这个属性或这组属性上建立索引
2. 经常作为聚集函数参数的1个或1组属性可以考虑建立索引
3. 经常作为连接操作的连接条件的属性可以建立索引



