# <a name="top">Nginx基本配置与常用指令</a>



+ <a href="#conf">**配置文件结构**</a>
  + <a href="#minimum_conf">`nginx.conf最低配置`</a>
  + <a href="#default_cmd">`常用指令声明`</a>


+ <a href="#use_group">**用户和组**</a>


+ <a href="#control">**访问控制**</a>
  + <a href="#location">`location前缀`</a>
  + <a href="#precise_match">`精确匹配`</a>


+ <a href="#log">**日志**</a>


+ <a href="#virtual_host">**虚拟主机**</a>


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



###  <a name="minimum_conf">`nginx.conf最低配置`</a>

```nginx
user root;
# worker_processes 是指令名称	1 是参数
worker_processes 1;
events{  
  # 允许单个进程并发连接的最大请求数
  worker_connections 1024;
}

http{  
  # include 相对路径引用，表示引入的 mime.types 文件是相对当前配置文件 nginx.conf 所在的目录
  # 绝对路径是以 Linux 根目录 "/" 为开始的文件目录
  # mime.types 文件的作用是存储文件扩展名与文件类型的映射表
  include   mime.types;    
  default_type  application/octet-stream;    
  
  # 开启高效文件传输模式
  sendfile  on;
  
  # 设置长连接超时时间
  keepalive_timeout 65;  
  
  server{    
    # 监听端口
    listen  80;  
    
    # 设置主机域名
    server_name 127.0.0.1 localhost;    
    
    location / {
      # 设置主机站点根目录地址
      root	html;
      # 指定默认索引文件
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




<p align="right"><a href="#conf">返回</a> &nbsp&nbsp<a  href="#top">返回目录</a></p>

----


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





<p align="right"><a  href="#top">返回目录</a></p>



----

## <a name="control">访问控制</a>

`nginx`用于配置访问权限控制的指令有：

+ `allow` —— 用于设置**允许访问**的权限
+ `deny` —— 用于设置**禁止访问**的权限
+ 权限指令后面可以跟上：
  + 允许访问的IP、IP段
  + 禁止访问的IP、IP段
  + **all** —— 表示所有的
+ 单个`IP`指定的作用范围最小，`all`指定的作用范围最大
+ 同一块下，若同时出现多个权限指令，**先出现的访问权限设置生效**，并且会对后出现的设置进行覆盖，**未覆盖的范围依然生效**
+ 当多个块（`http`、`server`、`location`）都出现了权限设置指令，**内层块**中的**权限级别**要比外层块中设置的权限级别**高**





```nginx
# 所有客户端将被禁止访问
allow 192.168.10.123;
deny all;
```

<p align="right"><a href="#control">返回</a> &nbsp &nbsp<a  href="#top">返回目录</a></p>

-----

### <center><a name="location">**location前缀**</a></center>

| 前缀   | 说明                                       |
| :--- | :--------------------------------------- |
| `=`  | 根据其后的指定模式进行<a href="#precise_match">精确匹配</a>，路径完全一致时才会执行其后的指令块 |
| `~`  | 使用正则表达式完成`location`指令的匹配，区分大小写           |
| `~*` | 使用正则表达式完成`location`指令的匹配，**不区分大小写**      |
| `^~` | **不使用正则表达式**，完成以指定模式开头的 `location` 匹配    |
| `@`  | 用于定义 `location` 块，**不能被外部客户端访问**，只能被 `nginx` 内部配置指令访问 |

+ 配置文件中同时出现多个`location`时，普通 `location` 之间遵循**最大匹配原则**，匹配度最高的`location`将会被执行


+ 最大前缀 `location` 与正则 `location` 同时存在时，若正则匹配 `location` **先匹配成功将不会匹配最大前缀** `location`


+ `location / {}` —— 遵循普通 `location` 最大前缀匹配，任何URI中都必然以`/`跟开头，一个URI若配置中有更合适的匹配将会被替代，`location / {}` **相当于站点默认配置**


+ `location = /{}` —— 遵循的是<a href="#precise_match">**精确匹配**</a>，**只能匹配站点根目录**，同时会禁止搜索正则 `location`，效率比`location / {}`高



多种类型的`location`同时出现时，**优先级如下**：

精准匹配(`=`)   >  最大前缀匹配(``^~``)   >  正则匹配  >  普通最大前缀匹配

<p align="right"><a href="#control">返回</a> &nbsp&nbsp<a  href="#top">返回目录</a></p>

----

<a name="precise_match">**精确匹配**</a>

> 精确匹配指用户访问的URI与指定的URI完全一致的情况，才会执行其后的指令。



```nginx
server{
  listen 80;
  server_name localhost;
  root /home/admin/website;
  index index.html index.htm;
  # location 配合 "=" 前缀的精确匹配
  location = /img{
  		allow 127.0.0.1;
	}
  location = /js{
  		allow 119.123.64.135;
	}
  deny all;
}
```



设`nginx`服务器ip为`138.89.57.129`，**精确匹配测试结果如下** ：

| URL                                   | 主机`119.123.64.135`的响应结果 | 主机`127.0.0.1`的响应结果 |
| :------------------------------------ | :---------------------- | :----------------- |
| `http://138.89.57.129/img/qrcode.png` | 403 Forbidden           | 正常访问               |
| `http://138.89.57.129/js/index.js`    | 正常访问                    | 403 Forbidden      |
|                                       |                         |                    |
|                                       |                         |                    |





<a name="regex_match">**正则匹配**</a>

+ 存在多个正则`location`情况下，只要前面的正则`location`匹配成功后面的`location`就不会被匹配到



**root与alias的区别** ：

```nginx
location /img/ {
  alias /var/hurrican/website
}

location /img/ {
  root /var/hurrican/website
}
```

当请求路径为`/img/hello.png`时：

+ `root` 将请求路径映射为 `/var/hurrican/website/img/hello.png`
+ `alias` 将请求路径映射为 `/var/hurrican/website/hello.png`



<p align="right"><a href="#control">返回</a> &nbsp&nbsp<a  href="#top">返回目录</a></p>



----

## <a name="log">**日志**</a>

配置日志相关指令：

+ `log_format` —— 自定义日志格式
+ `access_log` —— 设置日志存储指令，缓存大小



**日志指令示例** ：

```nginx
# main 表示日志格式名称
log_format  main 'remote-address = $remote_user "$request" $status';

# 第1个参数：指定 access log 存放的路径
# 第2个参数：指定日志文件格式
access_log  /var/logs/nginx/access.log main;

```





**日志格式相关的内置变量** ：

| 内置变量                    | 含义                             |
| :---------------------- | :----------------------------- |
| `$remote_addr`          | 客户端IP地址                        |
| `$remote_user`          | 客户端用户名，**用于记录浏览者进行身份验证时提供的名称** |
| `$time_local`           |                                |
| `$request`              | 请求的URI和HTTP协议                  |
| `$status`               | http状态码                        |
| `$body_bytes_sent`      | 发送给客户端文件主体内容的大小                |
| `$http_referer`         |                                |
| `$http_user_agent`      |                                |
| `$http_x_forwarded_for` | 客户端IP地址列表（**包括中间经过的代理**）       |



### <a name="off_log">**关闭日志**</a>

+ **关闭 access 日志**

```nginx
access_log  off; 
```

+ **关闭error 日志**

```nginx
error_log  /dev/null;
```







<p align="right"><a href="#log">返回</a> &nbsp&nbsp<a  href="#top">返回目录</a></p>

------

## <a name="virtual_host">**虚拟主机**</a>

> 一个物理服务器上划分多个磁盘空间，每个空间对应一个虚拟主机，每台主机可以对外提供Web服务。



+ <a href="#base_port">**基于端口号配置虚拟主机**</a>


+ <a href="#">**基于IP配置虚拟主机**</a>


+ <a href="#base_domain">**基于域名配置虚拟主机**</a>





<a name="base_port">**基于端口号配置虚拟主机**</a>

+ 基于 **端口** 配置虚拟主机原理是让 `nginx` 监听多个端口，根据端口号区分不同的网站
+ `nginx` 配置文件中，**http** 块每个 `server` 块都是一个虚拟主机



<br/>

<a name="base_domain">**基于域名配置虚拟主机**</a>

**虚拟域名** —— 使用操作系统里的`hosts`文件配置虚拟域名：

+ `Windows下hosts文件路径` ——  `C:\Windows\System32\drivers\etc`


+ `Linux下hosts文件路径` —— `/etc/hosts`


+ 配置虚拟域名：

  ```nginx
  127.0.0.1 www.hurrican.cn
  127.0.0.1 hurrican.cn
  ```

+ `server` 块配置：

  ```nginx
  server{
    listen 80;
    server_name www.hurrican.cn;
    root /home/hurican/web;
    index index.html;
  }
  ```



**备注** ：

+ 可以使用通配符 `*` 与 `正则表达式` 设置域名，实现域名的泛解析
+ `server_name` 有多种设置方式，只要有一种配置成功就会停止继续匹配
+ 匹配优先级顺序为： `精确匹配` >  `以通配符开始的字符串` >  `以通配符结束的字符串`  >  `正则表达式`


<p align="right"><a href="#virtual_host">返回</a> &nbsp&nbsp<a  href="#top">返回目录</a></p>



-----

## <a name="ref_website">**参考资料**</a>

+ <a href="http://nginx.org/en/docs/http/ngx_http_proxy_module.html">**nginx官方文档**</a>