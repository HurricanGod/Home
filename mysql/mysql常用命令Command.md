#  <a name="top">Mysql常用命令</a>



+ <a href="#user">**用户相关命令**</a>


+ <a href="#character_code">**字符集编码**</a>


+ <a href="#problem">**MySQL进程CPU占用率高排查**</a>




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



