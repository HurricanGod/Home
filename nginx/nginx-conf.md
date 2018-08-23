# <a name="top">Nginx基本配置</a>



-----

## <a name="conf">配置文件结构</a>



```nginx
main

events{
  
}

http{
  server{
  	location{
  
	}
  }
}
```

+ `main` —— 主要控制 **nginx** 子进程所属的用户和用户组、派生子进程数、错误日志位置与级别，`pid`位置、子进程优先级、进程对应的CPU、进程能够打开的文件描述符数目

<br/>

+ `events` —— 控制 **Nginx** 处理连接的方式

<br/>

+ `http` —— **Nginx** 处理 **`http` 请求的主要配置块**

<br/>

+ `server` ——  **Nginx** 中主机的配置块，**用于配置多个虚拟主机**

<br/>

+ `location` ——  `server` 中对应目录级别的控制块，可以有多个



<p align="right"><a href="#conf">返回</a> &nbsp&nbsp<a  href="#top">返回目录</a></p>

**最低配置** —— `nginx.conf`

```nginx
# worker_processes 是指令名称	1 是参数
worker_processes 1;
events{  
  worker_connections 1024;
}

http{  
  # include 相对路径引用，表示引入的 mime.types 文件是相对当前配置文件 nginx.conf 所在的目录
  # 绝对路径是以 Linux 根目录 "/" 为开始的文件目录
  # mime.types 文件的作用是存储文件扩展名与文件类型的映射表
  include   mime.types;    
  default_type  application/octet-stream;    
  sendfile  on;  
  keepalive_timeout 65;  
  
  server{        
    listen  80;        
    server_name 127.0.0.1 localhost;        
    location / {
      root	html;
      index	index.html index.htm;
    }
    
    # 500 502 503 504 指的是状态码
    # 当返回上面任何一个状态码时，都返回网站根目录下的50x.html
    error_page	500 502 503 504 /50x.html;
    
    location = /50x.html{
  		root	html;
	}
  }
}
```



+ `nginx 指令` —— **由指令名称和参数组成**，且每条指令以`;`结尾


+ 当1个指令包含多个子指令时使用`{ }` 进行包裹


+ 配置文件中的**文件路径**，可以是相对路径，也可以是绝对路径







### <a name="default_cmd">常用指令声明</a>



| 指令                   | 说明                                 |
| :------------------- | :--------------------------------- |
| `worker_processes`   | `Nginx`的工作进程数，一般为**CPU总核数**或总核数的2倍 |
| `worker_connections` | 允许单个进程并发连接的最大请求数                   |
| `include`            | 用于引入配置文件                           |
| `default_type`       | 设置默认文件类型                           |
| `sendfile`           | 是否开启高效文件传输模式，默认为**on**             |
| `keepalive_timeout`  | 设置长连接超时时间                          |
| `listen`             | 监听端口，默认监听**80**端口                  |
| `server_name`        | 设置主机域名                             |
| `root`               | **设置主机站点根目录地址**                    |
| `index`              | 指定默认索引文件                           |
| `error_page`         | 自定义错误页面                            |
| `user`               | 配置用户和组的指令                          |
|                      |                                    |
|                      |                                    |

+ `error_page`可以指定单个错误的处理页面，利用在线资源处理指定错误，更改网站响应状态码

  + **为每种类型的错误单独设置处理方式**

  ```nginx
  # 指定网站根目录下的 40x.html 页面处理403错误
  error_page 403 /40x.html
  ```

  <br/>

  + **利用在线资源进行错误处理**

  ```nginx
  error_page 500 502 504 http://www.baidu.com
  ```

  <br/>

  + **更改响应状态码**

  ```nginx
  # 当服务端响应状态码为502时，通过nginx转发后返回给浏览器的状态码为200
  error_page 502 = 200 /default.html

  # 当返回502状态码时返回 default.html，状态码由重定向后经过决定
  error_page 502 =  /default.html
  ```

  ​







<p align="right"><a href="#default_cmd">返回</a> &nbsp&nbsp<a  href="#conf">返回目录</a></p>

-----

## <a name="use_group">用户和组</a>

> Nginx 服务由一个主进程和多个工作进程组成，主进程以 root 权限运行，工作进程默认情况下以 nobody 用户运行。
>
> nobody 用户是一个不能登录的账号，有一个专门的ID，可以将每个工作进程进行隔离，保护服务器安全，工作进程设置的执行用户权限越低，服务器安全系数越高

**查看 Nginx 服务器中的主进程和 Worker进程**

```shell
ps -aux |grep nginx

# nginx 默认启动一个主进程和一个worker进程
# root 20035 1  0 23:04 ? 00:00:00 nginx: master process /usr/sbin/nginx -g daemon on; master_process on;
# www-data 20036 20035  0 23:04 ?        00:00:00 nginx: worker process

```











----

## <a name="control">访问控制</a>





