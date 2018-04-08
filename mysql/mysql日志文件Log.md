## 日志文件

`Mysql`常见的日志文件有：

+ <a href="#errorLog">错误日志</a>
+ <a href="#binaryLog">二进制日志</a>
+ <a href="#slowLog">慢查询日志</a>
+ <a href="#queryLog">查询日志</a>




<a name="errorLog">**错误日志**</a>

+ **错误日志文件** 对`Mysql`的启动、运行、关闭过程进行了记录，不仅记录所有错误信息，也记录了警告信息或正确信息，`Linux`下通过**Mysql命令行**的`show variables like 'log_error'`来定位错误日志文件的位置
+ 当出现**Mysql不能正常启动**时，首先应该查找的文件应该是错误日志文件




<a name="slowLog">**慢查询日志**</a>

+ 可以定位可能存在问题的sql语句，从而进行sql语句层面的优化。可以在`Mysql`启动时设一个阀值，将运行时间超过该阀值的所有`Sql语句`都记录到慢查询日志文件中，方便后续优化操作
+ `阀值`可以使用`long_query_time`来设置，默认值为10s；在默认情况下，Mysql并不启用慢查询日志，需要手工设置为`ON`
+ `Linux`下查看`long_query_time`值的方法：在`Mysql`命令行下使用`show variables like 'long_query_time'`命令
+ `Mysql5.1`开始将慢查询日志记录放入一张表，慢查询在`mysql`架构下表名为`show_log`，慢查询默认输出格式为文件，将其设置为**table**形式输出的方法为`set global log_output = 'TABLE'`




另一个与慢查询相关的日志参数是——**log_queries_not_using_indexes** (表示执行的sql语句若未使用索引，将会被记录到慢查询日志里)

![NotUsingIndex](https://github.com/HurricanGod/Home/blob/master/mysql/img/NotUsingIndex.png)





![ShowLongQueryTime](https://github.com/HurricanGod/Home/blob/master/mysql/img/ShowLongQueryTime.png)



<a name="queryLog">**查询日志**</a>

+ 记录了所有对`Mysql`数据库请求的信息，无论这些请求是否得到正确执行都会被记录下来
+ `Mysql5.1`开始将查询日志放入`mysql架构下`的**general_log**表中




![QueryLog](https://github.com/HurricanGod/Home/blob/master/mysql/img/MysqlQueryLog.png)



`mysql.general_log`表定义如下：

```mysql

```



![mysql.general_log](https://github.com/HurricanGod/Home/blob/master/mysql/img/GeneralLog.png)



---

<a name="binaryLog">**二进制日志**</a>

+ 二进制日志文件默认情况下没有启动，需要使用手动指定参数来启动



**查看mysql的binlog命令的方法** ：

```shell
mysqlbinlog mysql-bin.0000001.log
```

```mysql
# mysql 命令行下
show binlog events;
show binlog events in 'filename';
```



### binlog的操作

+ 每次服务器重启，都会调用 ` flush logs` 操作创建1个新的的`binlog`日志


+ `show master status` —— 查看当前日志的状态


+ `flush logs` —— 刷新日志文件，会产生一个新的日志文件


+ `show master logs` —— 查看当前的所有日志文件


+ `reset master` —— 清空日志



------

### binlog恢复数据

+ `mysqlbinlog mysql-bin-log-name | mysql -u root -p`


+ `mysqlbinlog mysql-bin-log-name --start-position  数字 --stop-position 数字 | mysql -u root -p`

