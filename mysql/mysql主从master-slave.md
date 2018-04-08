# Mysql搭建主从步骤




## 配置主节点

-----

### 创建用户并赋予权限



```mysql
# 给用户授予权限
grant replication slave on *.* to 'uername'@'host' identified by 'YourPassword';


```

![privileges](https://github.com/HurricanGod/Home/blob/master/mysql/img/mysql-user-privileges.png)







----

### 开启binlog日志

**查看是否开启binlog**：

```mysql
show variables like '%log_bin%'
# 或者使用下面命令：
show master logs;
```
![](https://github.com/HurricanGod/Home/blob/master/mysql/img/mysql-master-open-binlog.png)
![show-open-binlog](https://github.com/HurricanGod/Home/blob/master/mysql/img/show-open-binlog.png)







----

## 配置从节点



### 配置同步日志

![mysql-slave-config1](https://github.com/HurricanGod/Home/blob/master/mysql/img/mysql-slave-config-1.png)



### 指定主节点ip、端口、用户

```mysql
change master to master_host='master_ip', master_port=3306,
master_user='主数据库有被授予了replacation slave权限的用户',
master_password = '对应的密码',master_log_file='mysql-bin.000001',
master_log_pos=0;
```





### 启动从节点

```mysql
start slave;
show slave status \G;
```

>                Slave_IO_State: Waiting for master to send event
>                   Master_Host: localhost
>                   Master_User: admin
>                   Master_Port: 3306
>                 Connect_Retry: 60
>               Master_Log_File: mysql-bin.000001
>           Read_Master_Log_Pos: 154
>                Relay_Log_File: mysql-relay-bin.000002
>                 Relay_Log_Pos: 367
>         Relay_Master_Log_File: mysql-bin.000001
>              Slave_IO_Running: Yes
>             Slave_SQL_Running: Yes
>               Replicate_Do_DB: 
>           Replicate_Ignore_DB: 
>            Replicate_Do_Table: 
>        Replicate_Ignore_Table: 
>       Replicate_Wild_Do_Table: 
>   Replicate_Wild_Ignore_Table: 
>                    Last_Errno: 0
>                    Last_Error: 
>                  Skip_Counter: 0
>           Exec_Master_Log_Pos: 154
>               Relay_Log_Space: 574
>               Until_Condition: None
>                Until_Log_File: 
>                 Until_Log_Pos: 0
>            Master_SSL_Allowed: No
>            Master_SSL_CA_File: 
>            Master_SSL_CA_Path: 
>               Master_SSL_Cert: 
>             Master_SSL_Cipher: 
>                Master_SSL_Key: 
>         Seconds_Behind_Master: 0
> Master_SSL_Verify_Server_Cert: No
>                 Last_IO_Errno: 0
>                 Last_IO_Error: 
>                Last_SQL_Errno: 0
>                Last_SQL_Error: 
>   Replicate_Ignore_Server_Ids: 
>              Master_Server_Id: 2
>                   Master_UUID: 690fc5b5-5e7d-11e7-881f-5254007eddfd
>              Master_Info_File: /var/lib/mysql_3307/master.info
>                     SQL_Delay: 0
>           SQL_Remaining_Delay: NULL
>       Slave_SQL_Running_State: Slave has read all relay log; waiting for more updates
>            Master_Retry_Count: 86400
>                   Master_Bind: 
>       Last_IO_Error_Timestamp: 
>      Last_SQL_Error_Timestamp: 
>                Master_SSL_Crl: 
>            Master_SSL_Crlpath: 
>            Retrieved_Gtid_Set: 
>             Executed_Gtid_Set: 
>                 Auto_Position: 0
>          Replicate_Rewrite_DB: 
>                  Channel_Name: 
>            Master_TLS_Version: 
> 1 row in set (0.00 sec)
>

**成功配置mysql主从**：

![mysql-master-slave-ok](https://github.com/HurricanGod/Home/blob/master/mysql/img/mysql-master-slave-ok.png)
