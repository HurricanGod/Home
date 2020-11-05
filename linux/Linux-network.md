# <a name="top">Linux网络相关的命令</a>













----

## <a name="nc">网络工具nc</a>

**命令格式** ：

```sh
nc [-46DdhklnrStUuvzC] [-i interval] [-p source_port] [-s source_ip_address] [-w timeout] [hostname] [port[s]]
```

+ `-4`：只使用 IPv4 地址
+ `-6`：只使用 IPv6 地址
+ `-l`：启动本地监听
+ `-n`：不使用 DNS 解析
+ `-p`：指定源端口
+ `-s`：指定源 IP 地址
+ `-u`：使用 UDP，默认是 TCP
+ `-v`：显示详细信息
+ `-w`：设定超时时间（只适合用在 Client 端）
+ `-d`：禁止从标准输入读取数据，也就是客户端输入数据不会发送到服务端
+ `-k`：让服务端保持连接，不断开



**安装** ：

```sh
# centos
yum install nc

# ubuntu
apt-get install netcat
```





**常用功能** ：

+ `nc -l port`： 查看指定的端口号是否被占用


+ `nc -n ip -v -z num1-num2`： 端口扫描，扫描目标服务器num1到num2范围的端口是否开放，使用案例：`nc -n 192.168.168.26 -v -z 22-80`
  - `-n`： 直接使用ip地址，不使用域名
  - `-z`：扫描时不发送任何数据，连接成功后立即关闭连接
  - `-v`：输出详细信息


+ `nc -l port`： 创建一个socket，监听给定的端口号port


+ `nc -lvv port`： `-v`选项用于输出交互或出错信息

+ 发送文件

  ```shell
  # 服务端接收文件
  nc  127.0.0.1 8000 > data.log
  
  # 客户端发送文件
  nc 127.0.0.1 8000 < info.log
  
  
  ```

  



