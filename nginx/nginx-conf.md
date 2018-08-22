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













<p align="right"><a href="#default_cmd">返回</a> &nbsp&nbsp<a  href="#conf">返回目录</a></p>

-----

