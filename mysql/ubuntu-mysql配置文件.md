# Ubuntu使用apt-get安装mysql配置详解



----

+ 主要配置文件：`/etc/mysql/my.cnf`(全局global文件)


+ 经常修改的配置文件：`/etc/mysql/mysql.conf.d/mysqld.cnf`


+ 默认数据文件目录：`/var/lib/mysql`


+ 安装位置： `/usr/share/mysql`



**mysql读取默认配置文件顺序如下**：

+ `/etc/my.cnf`
+ `/etc/mysql/my.cnf`
+ `~/.my.cnf`



