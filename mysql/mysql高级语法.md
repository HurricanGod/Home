# <a name="top">高级特性</a>

+ <a href="#insert">**insert高级语法**</a>


+ <a href="#query">**查询**</a>


+ <a href="#processlist">**查看MySQL进程状态**</a>


+ <a href="#transaction">**事务**</a>


+ <a href="#table_preserve">**表维护**</a>


----
<a name="table_define">**样例中的表结构定义**</a>
```mysql
CREATE TABLE `exception_log` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `business_name` varchar(128) DEFAULT NULL,
  `exception_name` varchar(128) DEFAULT 'unknow exception' COMMENT '异常名',
  `exception_stack` varchar(2048) DEFAULT NULL COMMENT '异常堆栈',
  `detail` varchar(512) DEFAULT NULL COMMENT '异常描述',
  `last_update_time` datetime DEFAULT NULL COMMENT '最近更改时间',
  PRIMARY KEY (`id`),
  KEY `exception_name__index` (`exception_name`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='异常日志表'
```

------
## <a name="insert">insert高级语法</a>
+ 使用 `set` 语法进行插入

  ```mysql
  # DEFAULT 表示字段定义时的默认值
  insert into exception_log 
  set last_update_time=now(),
  exception_name=DEFAULT;
  ```

  ​


+ 一次性插入多个值

  ```mysql
  insert into 
  exception_log(detail,last_update_time) values
  ('test2',now()),
  ('test3', now()),
  ('test4', now());
  ```



+ 将查询结果作为插入的值

  **语法** ：

  ```mysql
  insert into table_name(field0, field1) 
  select field0, field1 
  from table_name 
  [ where condition]
  ```

  **样例** ：

  ```mysql
  insert into 
  exception_log(exception_name,last_update_time) 
  select exception_name,last_update_time  
  from exception_log 
  where id=10;
  ```

  ​

+ 插入前判断记录是否存在

  ```mysql
  insert into table_name(field0, field1) 
  select fieldVal0, fieldVal1
  from tmp
  where not exists (
  	select *
    	from  table_name
    	where field0 = fieldVal0 and field1 = fieldVal1
  )
  ```

  + 表`tmp`为临时表
  + 上面sql如果数据库里已有`where`条件的记录将不会插入

  ​

+ ​


<p align="right"><a href="#insert">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回顶部</a></p>

----
## <a name="query">**查询**</a>
> 查询时使用 for update 可以进行加锁查询。当表结构采用的存储引擎为Innodb时，若查询条件为主键，所加的锁为行锁

+ 使用 `for update` 加锁方式查询

  ```sql
  SELECT id, last_update_time
  FROM exception_log
  WHERE exception_name = 'npe'
  LIMIT 0, 1
  FOR UPDATE
  ```

  ​



+ `left join`多张表

  ```sql
  # 同时join三张表
  select t1.field1, t1.field2, t2.field1, t2.field2, t3.field1
  from t1 
  left join t2 on t1.field_x = t2.field_x
  left join t3 on t1.field_y = t3.field_y
  where t1.field = ? [and t2.field = ?]
  ```

  ​

+ 查询结果输出到文件

  ```mysql
  # 如果有写文件的权限
  select t1.field1, t1.field2
  from t1 
  into outfile '/tmp/result.log'
  ;

  # 直接在shell命令行执行查询
  # -p：指定数据库名称
  # -P：指定端口号
  # -e：指定要执行的sql
  mysql -h 10.8.63.115  -u root -p testdb -P 3306 -e "select t1.field1, t1.field2 from t1 where id < 10" > /tmp/result.log
  ```

  ​

+ ​







<p align="right"><a href="#lock_query">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回顶部</a></p>

----

## <a name="processlist">查看MySQL进程状态</a>

```mysql
show processlist;
```
![processlist.png](https://github.com/HurricanGod/Home/blob/master/mysql/img/show-processlist.png)

<p align="right"><a href="#processlist">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回顶部</a></p>

----

## <a name="transaction">事务</a>

+ 普通的MySQL执行语句后，当前的数据提交操作均可被其他客户端可见；事务则暂时**关闭自动提交机制**，需要 `手动 commit` 提交持久化数据操作

+ 数据定义语言（DDL）语句不能被回滚，比如创建或删除数据库的语句，修改表结构语句

+ `set autocommit = 0|1` 用于设置当前会话是否自动提交事务，其中**1**表示自动提交事务

  ```mysql
  begin(或者 start transaction)

  commit

  rollback
  ```

  ​




<p align="right"><a href="#transaction">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回顶部</a></p>

-----

## <a name="table_preserve">**表维护**</a>

+ `analyze table table_name`


+ `optimize table table_name`







<p align="right"><a href="#table_preserve">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回顶部</a></p>
