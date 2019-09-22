#  <a name="top">Mysql常用命令</a>



+ <a href="#user">**用户相关命令**</a>


+ <a href="#character_code">**字符集编码**</a>


+ <a href="#problem">**MySQL进程CPU占用率高排查**</a>


+ <a href="#showIndex">**查看表中的索引使用情况**</a>





------

## <a name="user">**用户相关命令**</a>



+ 用户创建、查询与删除

  ```mysql
  # 创建用户，username为用户名，@'host' 指定主机或IP，password为用户密码
  create user 'username'@'host' identified by 'password';

  # 查询创建的用户
  select user,host from mysql.user;

  # 删除用户
  drop user username@host;
  ```

  ​

+ 指定来访者

  ```mysql
  grant privilege_name on *.* to 'username'@'host' identified by 'password';
  # privilege_name 为权限名
  ```

  ![privileges](https://github.com/HurricanGod/Home/blob/master/mysql/img/mysql-user-privileges.png)

  ​

+ 查看用户权限

  ```mysql
  # 查看用户拥有的授权（1）
  show grants for 'uername'@'host';

  # 查看用户拥有的授权（2）
  select * from mysql.db where user = 'username' \G;
  ```




<p align="right"><a href="#user">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a></p>

----

## <a name="character_code">字符集编码</a>



```mysql
show variables where variable_name like 'character_set_%';
```





<p align="right"><a href="#character_code">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a></p>

-----

## <a name="problem">MySQL进程CPU占用率高排查</a>

+ `show processlist` —— 默认只列出100条





+ `show full processlist`




-----

## <a name="query_log">查询日志</a>





----
## <a name="queryTableInfo">查询数据库里的表数量</a>



```mysql
select count(*) tables, table_schema 
from information_schema.TABLES 
where table_schema = 'database_name' 
group by table_schema;
```





<p align="right"><a href="#queryTableInfo">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a></p>



-----

##  <a name="showInnodbStatus">查看InnoDB状态</a>



```mysql
show engine innodb status \G;
```







## <a name="showIndex">查看表中的索引使用情况</a>

```shell
show index from t_cmb_openuser_0;
```

![showIndex.png](https://github.com/HurricanGod/Home/blob/master/mysql/img/showIndex.png)

+ `Non_unique`：是否为非唯一的索引，0表示唯一索引
+ `Key_name`：索引的名字
+ `Seq_in_index`：索引中该列的位置
+ `Column_name`：索引列的名称
+ `Collation`：列以什么方式存储在索引中，可以是`A`或`NULL`，B+树索引总是A，即排序的，如果使用了Heap存储引擎，并且建立了Hash索引，就会显示为NULL
+ `Cardinality`：表示索引中唯一值的数目的估计值，**Cardinality表的行数应该尽可能接近1**，优化器会根据这个值来判断是否使用该索引，这个值并非实时更新，需要更新索引的`Cardinality`的值可以使用`analyze table `命令
+ `Sub_part`：是否表示对列的部分进行索引
+ `Packed`：关键字被压缩的方式，没有被压缩则表示为null
+ `Null`：是否索引的列包含null值
+ `Index_type`：索引的类型，一般为B+树索引





<p align="right"><a href="#showIndex">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a></p>
