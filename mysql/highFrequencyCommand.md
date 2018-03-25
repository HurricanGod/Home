## Mysql常用命令



+ **创建用户**

  + 用户名、密码

    ```mysql
    # 创建用户
    create user 'username'@'host' identified by 'password'
    # 查询创建的用户
    select  user,host from mysql.user;
    # 删除用户
    drop user username@host
    ```

    ​

  + 指定来访者

    ```mysql
    grant privilege_name on *.* to 'username'@'host' identified by 'password';
    ```

    ​

  + 设置用户权限