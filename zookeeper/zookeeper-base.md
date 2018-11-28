# Zookeeper





----









----


## 常用命令
+ **启动zookeeper服务**

  ```sh
  # 进入 zookeeper 安装目录下的 /bin 目录
  ./zkServer.sh start
  ```



+ **停止zookeeper服务**

  ```sh
  ./zkServer.sh stop
  ```

  ​


+ 客户端脚本

  ```sh
  # 启动客户端，未显式指定 zookeeper 服务器默认连接本地的 zookeeper 服务器
  ./zkCli.sh [-server ip:port]
  ```



+ **创建**

  ```shell
  create [-s] [-e] path data_value acl
  ```

  + `-s` ——指定节点特性，表明创建的是**顺序节点**

  + `-e`——指定节点特性，表明创建的是**临时节点**

  + 不指定任何参数情况下创建的是**持久节点**

  + `acl` ——进行权限控制的参数，缺省情况下**不做权限控制**

  + 不支持递归创建节点，即当要创建的节点的父节点不存在时无法成功创建

    ​

  + `Java`客户端创建节点示例：

    ```java

    ```

    ​







+ **读取**

  ```sh
  # 列出 ZooKeeper 指定节点下的所有子节点(即不能查看子节点的子节点...)
  ls path [watch]

  # 如：列出根节点下的所有子节点
  ls /

  # 获取 ZooKeeper 指定节点的数据内容和属性值
  get path [watch]

  # 如：获取 /zookeeper 节点的内容及属性
  get /zookeeper
  ```

  ![zk-get]()

  ​


+ **修改** & **删除**

  ```sh
  # 修改指定节点的值
  set path new_value [version]
  ```

  + 修改操作完成后节点属性 `dataVersion` 会发生改变

    ​

  ```shell
  delete path [version]
  ```

  + **要删除的节点必须在满足没有子节点的情况下才可以成功删除**