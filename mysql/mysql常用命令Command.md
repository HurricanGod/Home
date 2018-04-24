## Mysql常用命令



+ **创建用户**

  + 用户名、密码

    ```mysql
    # 创建用户
    create user 'username'@'host' identified by 'password'
    # username为用户名，@'host' 指定主机或IP，password为用户密码
    # 查询创建的用户
    select  user,host from mysql.user;
    # 删除用户
    drop user username@host
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
    show grants for 'uername'@'host'

    # 查看用户拥有的授权（2）
    select * from mysql.db where user = 'username' \G
    ```


-----

+ **外键**

  ```mysql
  # 增加外键约束
  alter table tableName add constraint FK_tb1_tb2_name foreign key(columnName)  references keyTable(pkColumnName)
  #注意：columnName 要与 pkColumnName 类型完全一致，技术1个是int另一个是unsigned int 都是不允许的
  ```

  ​

