# <a name="top">RabbitMQ常用命令</a>

+ <a href="#run">**启动 & 关闭 服务**</a>


+ <a href="#plugins">**插件**</a>


+ <a href="#account">**帐号管理**</a>


+ <a href="#vhost">**虚拟机**</a>


+ <a href="#connect">`Channel & Queue & Binding`</a>


----

## <a name="run">启动 & 关闭 服务</a>



+ <a name="run">**启动 & 关闭 服务**</a>

  ```sh
  # 停止运行RabbitMQ的Erlang node并等待指定进程结束
  rabbitmqctl  stop [pid_file]

  # 停止 rabbitmq 服务
  rabbitmqctl stop

  # 开启 rabbitmq 服务
  service rabbitmq-server start

  # 终止 rabbitmq 服务
  service rabbitmq-server stop

  # 重启 rabbitmq 服务
  service rabbitmq-server restart

  # 查看节点状态
  rabbitmqctl status 
  ```




<p><a href="#run">返回</a>&nbsp&nbsp | &nbsp&nbsp<a href="#top">返回目录</a></p>

----

## <a name="plugins">**插件**</a>



- **开启web管理页面插件**

  ```sh
  # 开启web管理页面插件
  rabbitmq-plugins enable rabbitmq_management

  # 关闭web管理页面插件
  rabbitmq-plugins disable rabbitmq_management

  # 重启服务器后生效
  ```

  ​

  <p><a href="#plugins">返回</a>&nbsp&nbsp | &nbsp&nbsp<a href="#top">返回目录</a></p>


---

## <a name="account">**帐号管理**</a>

+ 添加用户

  ```sh
  # rabbitmqctl add_user 用户名   密码
  rabbitmqctl add_user   mqadmin hurrican
  ```

  ​


+ 分配用户标签

  ```sh
  # rabbitmqctl set_user_tags mqadmin tag
  # tag 可以是：administrator | monitoring | management
  # 将用户 mqadmin 设置为管理用户
  rabbitmqctl set_user_tags mqadmin administrator
  ```

  ​


+ 列出所有用户

  ```sh
  rabbitmqctl list_users
  ```

  ​


+ 删除用户

  ```sh
  # rabbitmqctl delete_user 用户名
  rabbitmqctl delete_user guest
  ```

  ​


+ 修改密码

  ```sh
  # rabbitmqctl change_password 用户名 新密码
  rabbitmqctl change_password mqadmin admin
  ```




<p><a href="#account">返回</a>&nbsp&nbsp | &nbsp&nbsp<a href="#top">返回目录</a></p>

-----

## <a name="vhost">**虚拟机**</a>

+ 创建虚拟机

  ```sh
  # rabbitmqctl add_vhost 虚拟机路径
  rabbitmqctl add_vhost hurrican_master
  ```

  ​


+ 删除虚拟机

  ```sh
  # rabbitmqctl delete_vhost  虚拟机路径
  rabbitmqctl delete_vhost hurrican_master
  ```

  ​


+ 查看所有虚拟机

  ```sh
  rabbitmqctl list_vhosts
  ```



+ 权限管理

  ```sh
  # 设置 rabbitmq 用户在某个虚拟机的访问权限
  # set_permissions [-p vhostpath] {user} {conf} {write} {read}
  rabbitmqctl set_permissions -p hurrican_master mqadmin ".*" ".*" ".*

  # 查看虚拟机下的用户访问权限
  # rabbitmqctl list_permissions -p 虚拟机路径
  rabbitmqctl list_permissions -p hurrican_master

  # 查看某个用户的访问权限
  rabbitmqctl list_user_permissions mqadmin

  # Listing permissions for user "mqadmin" ...
  # hurrican_master	.*	.*	.*
  # rpc_master	.*	.*	.*

  ```

  ​

  <p><a href="#vhost">返回</a>&nbsp&nbsp | &nbsp&nbsp<a href="#top">返回目录</a></p>


----

## <a name="connect">`Channel & Queue & Binding`</a>

+ 查看虚拟机下的队列

  ```sh
  # rabbitmqctl list_queues -p vhost_path
  rabbitmqctl list_queues -p rpc_master
  ```

  ​


+ 查看交换机

  ```sh
  rabbitmqctl list_exchanges -p vhost_path
  ```

  ​


+ 查看绑定

  ```sh
  rabbitmqctl list_bindings -p vhost_path
  ```

  ​


+ 查看已建立的连接

  ```sh
  rabbitmqctl list_connections -p vhost_path
  ```

  ​

<p><a href="#connect">返回</a>&nbsp&nbsp | &nbsp&nbsp<a href="#top">返回目录</a></p>