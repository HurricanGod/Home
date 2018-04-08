# <a name="top">启动Mysql多实例</a>



+ <a href="#findCnf">找到mysql的配置文件`my.cnf`</a>
+ <a href="#cpCnf">复制mysql配置文件`my.cnf`</a>
+ <a href="#createDir">创建mysql实例的文件夹并添加权限</a>
+ <a href="#alterCnf">修改`my.cnf`配置文件</a>
+ <a href="#initDb">用`mysql_install_db`初始化数据库</a>
+ <a href="#startService">启动mysql服务器</a>
+ <a href="#loginService">登录`Mysql3307`实例</a>
+ <a href="#restart">**再次启动mysql3307服务**</a>
+ <a href="#alterPassword">修改mysql中root用户登录密码</a>
+ **<a href="#startAndStop">重新登录mysql3307服务器</a>**

----

## Mysql5.7创建启动多个实例



+ <a name="findCnf">找到mysql的配置文件`my.cnf`</a>，一般在`/etc/mysql/my.cnf`，不在也没关系，可以使用一下命令进行查找：

  ```shell
  find / -name 'my.cnf'
  ```



<a href="#top">**返回顶部**</a>

-----

+ <a name="cpCnf">复制mysql配置文件`my.cnf`</a>，名字任意

  ```shell
  cp my.cnf my_3307.cnf
  ```




------

+ 鉴于mysql5.7里面的`my.cnf`没有多少配置内容，需要把`mysqld.cnf`文件里的配置内容复制到`my.cnf`里。

  ​

  下面操作假设当前路径有` my_3307.cnf`文件和`mysql.conf.d`**目录**，且该目录下有`mysqld.cnf`文件

  ```shell
  # 将 mysqld.cnf 里的配置内容添加到 my_3307.cnf 里
  grep -v '^#' mysql.conf.d/mysqld.cnf >> my_3307.cnf 
  ```

  ​

<a href="#top">**返回顶部**</a>

-----

+ <a name="createDir">创建mysql实例的文件夹并添加权限</a>

  ```shell
  cd /var/lib
  mkdir mysql_3307
  chown -R mysql:mysql mysql_3307

  vim /etc/apparmor.d/usr.sbin.mysqld
  # 添加 /var/lib/mysql_3307/ r
  # 添加 /var/lib/mysql_3307/** rwk
  # mysql_3307即为刚才创建的mysql新实例的文件夹
  service apparmor restart
  ```

  ![mysql-apparmor](https://github.com/HurricanGod/Home/blob/master/mysql/img/manyInstance1.png)



<a href="#top">**返回顶部**</a>

------

+ <a name="alterCnf">修改`my.cnf`配置文件</a>

  ```shell
  vim my_3307.cnf
  ```

  修改后的配置文件如下所示：

  ```ini
  [mysqld]
  general_log_file = /var/lib/mysql_3307/op_mysql_3307.log
  general_log = 1

  !includedir /etc/mysql/conf.d/
  !includedir /etc/mysql/mysql.conf.d/

  [mysqld_safe]
  socket		= /var/lib/mysql_3307/mysqld.sock
  #nice		= 0

  [mysqld]
  user		= mysql
  pid-file	= /var/lib/mysql_3307/mysqld.pid
  socket		= /var/lib/mysql_3307/mysqld.sock
  port		= 3307
  basedir		= /usr
  datadir		= /var/lib/mysql_3307
  tmpdir		= /tmp
  lc-messages-dir	= /usr/share/mysql
  skip-external-locking
  bind-address		= 0.0.0.0
  key_buffer_size		= 16M
  max_allowed_packet	= 16M
  thread_stack		= 192K
  thread_cache_size       = 8
  myisam-recover-options  = BACKUP
  query_cache_limit	= 1M
  query_cache_size        = 16M
  log_error = /var/lib/mysql_3307/error.log
  expire_logs_days	= 10
  max_binlog_size   = 100M
  ```

  ![rollback2](https://github.com/HurricanGod/Home/blob/master/mysql/img/manyInstance2.png)



<a href="#top">**返回顶部**</a>

-----

+ <a name="initDb">切换到`mysql_install_db`初始化数据库</a>

  ```shell
  find / -name mysql_install_db
  # 或者使用如下两条命令找出 mysql_install_db 所在目录
  find / -name mysql_install_db >>tmp.txt
  grep -n 'mysql_install' tmp.txt 
  # 我的 mysql_install_db 在 /usr/bin/目录下
  #################################################
  cd /usr/bin/
  # 初始化数据库实例
  mysql_install_db --defaults-file=/etc/mysql/my_3307.cnf --basedir=/usr/ --datadir=/var/lib/mysql_3307 --user=mysql
  ```



<a href="#top">**返回顶部**</a>

-----

+ <a name="startService">启动mysql服务器</a>

  ```shell
  # 后台运行 mysql 实例
  mysqld_safe --defaults-file=/etc/mysql/my_3307.cnf --user=mysql&

  # 查看新的 mysql 实例是否成功启动
  netstat -nlt|grep 330[67]	#如果3306端口的数据库有启动的话此时应该有两个数据库实例
  ps -ef|grep mysql			#ps命令也可以查看新创建的 mysql 实例有没有成功启动
  ```



   <a href="#top">**返回顶部**</a>

+ <a name="loginService">登录`Mysql3307`实例</a>

  ```shell
  mysql -S /var/lib/mysql_3307/mysqld.sock -P 3307
  # 若出现 Error 1045(28000):Access denied for user 'root'@'localhost' (using password:No)
  #需要把刚启动的mysql实例停掉
  ps -ef|grep mysql
  # 找出要停掉的mysql的pid
  kill 9 pid
  ```

​       **修改my_3307.cnf配置文件** ——  `vim /etc/mysql/my_3307.cnf`

+ ​

  ```ini
  # 在my_3307.cnf中添加一行
  skip-grant-tables
  ```

  ![rollback4](https://github.com/HurricanGod/Home/blob/master/mysql/img/manyInstance4.png)

+ <a href="#top">**返回顶部**</a>

  ----

  <a name="restart">**再次启动mysql3307服务**</a>

  ```shell
  # 启动mysql3307服务
  mysqld_safe --defaults-file=/etc/mysql/my_3307.cnf --user=mysql&
  # 登录mysql3307服务器，在配置文件加了 skip-grant-tables 可以不用密码登录
  mysql -S /var/lib/mysql_3307/mysqld.sock -P 3307
  ```

  --------

  **<a name="alterPassword">修改mysql中root用户登录密码</a>**

  ```mysql
  update mysql.user 
  set authentication_string=password('YourNewPassword') 
  where user='root' and Host ='localhost';

  flush privileges;
  quit;
  ```

  ​

  **停止mysql3307服务**

  ```shell
  mysqladmin -S -u root -p YourNewPassword /var/lib/mysql_3307/mysqld.sock shutdown
  ```

  ----

  **修改my_3307.cnf配置文件**

  ```shell
  vim /etc/mysql/my_3307.cnf
  # 注释掉 skip-grant-tables
  ```

  ----

  **<a name="startAndStop">重新登录mysql3307服务器</a>**

  ```shell
  mysql -u root -S /var/lib/mysql_3307/mysqld.sock -p
  # 接着就会进入输密码的提示，输入刚才修改的密码就可以登录进去
  ```






```shell
 # 停止mysql3307服务用上面的命令
 mysqladmin -u root -S /var/lib/mysql_3307/mysqld.sock -p shutdown
 # 接着在 “Enter password:” 输入密码
```

> 停止mysql服务时若出现：
>
> mysqladmin:connect to server at localhost failed, error:"Your password has expired. To log in you must change it using a client that supports expired passwords."
>
> 可按下面操作解决：

```mysql
/*进入mysql命令行，执行一下命令*/
set PASSWORD = PASSWORD('YourNewPassword');
alter USER 'root'@'localhost' PASSWORD expire never;
flush privileges;
```





  <a href="#top">**返回顶部**</a>

**参考博客**：

+ <a href="http://www.jb51.net/article/108786.htm">**登录时出现ERROR 1045 (28000): Access denied for user 'root'@'localhost'问题**</a>
+ <a href="https://blog.csdn.net/supercrsky/article/details/64443618">**mysqladmin:connect to server at localhost failed, error:"Your password has expired. To log in you must change it using a client that supports expired passwords."**</a>

