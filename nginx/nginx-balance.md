# <a name="top">Nginx + Tomcat 负载均衡</a>



+ <a href="#simple_web">**单实例Tomcat + Nginx**</a>


+ <a href="#proxy">**代理**</a>


+ <a href="#balance">**负载均衡**</a>


+ <a href="#cache">**缓存**</a>


+ <a href="#gzip">**网络压缩传输**</a>


+ <a href="#rewrite">**重写与重定向**</a>


+ <a href="#config_optimize">**配置优化**</a>


+ <a href="#cdn">**CDN**</a>



-----

<a name="simple_web">**单实例Tomcat + Nginx**</a>

```nginx
server{
  listen 80;
  server_name 118.89.59.66;
  root /home/hurrican/tomcat8/instance1;
  index index.jsp index.html index.htm;
  
  # 禁止访问 WEB-INFO 和 META-INF 下的文件
  location ~/(WEB-INFO|META-INF) {
  		deny all;
	}
  
  # 以 .jsp 或 .do 结尾的请求都会被转发到 8080 端口中的 Tomcat服务器中
  location ~\.(jsp|do){
  		proxy_pass http://127.0.0.1:8080;
    		proxy_set_header X-Client-IP $remote_addr;
	}
  
  # 
  location ~^/(docs|expamples)(/.*)*${
  		root /home/hurrican/default;
	}
}
```







<p><a href="#simple_web">返回</a>&nbsp&nbsp |&nbsp&nbsp<a href="#top">返回目录</a></p>

----

## <a name="proxy">**代理**</a>

### 正向代理

> 代理被称为正向代理，是一个位于客户端和目标服务器之间的代理服务器，客户端将发送的请求和指定的目标服务器提交给代理服务器，代理服务器向目标服务器发送发起请求，将获得的响应结果返回给客户端的过程。



### 反向代理

> 反向代理对于客户端而言就是目标服务器，客户端向反向代理服务器发送请求后，反向代理服务器将请求转发给内网其它服务器，从后端服务器得到响应结果返回给用户。



### <a name="proxy_pass">反向代理服务配置相关指令 —— `proxy_pass`</a>

+ `proxy_pass` 通常在 `location` 块中进行设置


+ 常用的反向代理指令

  | 指令                      | 说明                                       |
  | :---------------------- | :--------------------------------------- |
  | `proxy_set_header`      | 在将客户端请求发送到后端服务器之前，更改来自客户端的请求头信息          |
  | `proxy_connect_timeout` | 配置 `Nginx` 与后端服务器**尝试连接的超时时间**           |
  | `proxy_read_timeout`    | 配置 `Nginx` 向后端服务器组发送`read`请求后，等待响应的超时时间  |
  | `proxy_send_timeout`    | 配置 `Nginx` 向后端服务器组发送`write`请求后，**等待响应的超时时间** |
  | `proxy_redirect`        | 用于修改后端服务器返回到响应头中的`Location` 和 `Refresh`  |





<p><a href="#proxy">返回</a>&nbsp&nbsp |&nbsp&nbsp<a href="#top">返回目录</a></p>

---

## <a nname="balance">**负载均衡**</a>

`Nginx`中使用`upstream`指令实现负载均衡，负载均衡典型的配置方式有：

+ 轮询方式
+ 权重方式
+ `ip_hash`方式
+ 第三方模块方式





**轮询方式配置方法如下** ：

```nginx
server{
  listen 80;
  server_name 192.168.123.100;
  location / {
  		proxy_pass http://192.168.123.10;
	}
}

upstream 192.168.123.10{
  server http://192.168.123.10:8080;
  server http://192.168.123.10:8090;
}
```

+ `Nginx`服务器检测到后端服务器宕机后会**自动删除该服务器**







**加权轮询负载均衡**

+ 使用`weight`参数设置权重大小，`weight`越大被分配到的概率就越大
+ 主要用于服务器硬件配置不一致的场景

```nginx
upstream 192.168.123.10{
  server http://192.168.123.10:8080 weight=1;
  server http://192.168.123.10:8090 weight=3;
  # 设置 http://192.168.123.10:8070 为备用服务器，只有上面两台服务器都出故障才会请求 backup 服务器
  server http://192.168.123.10:8070 backup;
}
```







`ip_hash`**负载均衡**

+ 将每个请求按照访问IP的`hash`结果进行分配，同个IP的客户端固定访问一台Web服务器
+ 可以解决`Session`共享问题
+ **ip_hash负载均衡的方式无法保证Web服务器的均衡负载**



<p><a href="#balance">返回</a>&nbsp&nbsp |&nbsp&nbsp<a href="#top">返回目录</a></p>

-----

## <a name="cache">**缓存**</a>

`Nginx` 提供两种Web缓存：

+ <a href="#forever_cache">**永久性缓存**</a>
+ <a href="#temporary_cache">**临时性缓存**</a>




### <a name="forever_cache">**永久性缓存**</a>

`Nginx`提供`proxy_store`指令将**内容源服务器**的响应缓存到本地，若不手动删除缓存，缓存一直生效，配置方法如下：

```nginx
server{
  listen 80;
  server_name 192.168.123.10;
  location / {
    	# 配置缓存目录，该目录需要与 nginx 工作进程有相同的权限
  		root cache;
    
    	# 开启本地缓存
    	proxy_store on;
    
    	# 设置缓存的读写规则
    	proxy_store_access user:rw group:rw all:r;
    	
    	# 设置反向代理时接收的数据临时存储文件的目录
    	proxy_temp_path cache_temp;
    
    	# 配置要代理的服务器, $request_filename 为 nginx 内置变量，表示当前请求的文件路径
    	# -e 检测一个文件是否存在
    	if(!-e $request_filename){
  			proxy_pass http://192.168.123.100;
		}
	}
}
```





### <a name="temporary_cache">**临时性缓存**</a>





<p><a href="#cache">返回</a>&nbsp&nbsp |&nbsp&nbsp<a href="#top">返回目录</a></p>

---

## <a name="gzip">网络压缩传输</a>

`gzip`是一种压缩技术，经过 `gzip` 压缩后，页面大小可以变为原来的 30% 或者更小。



**gzip模块相关配置指令** ：

| 指令                | 说明                                       |
| :---------------- | :--------------------------------------- |
| `gzip`            | 用于开启或关闭 `gzip` 模块                        |
| `gzip_buffers`    | 设置系统获取几个单位的缓存用于存储**gzip**压缩结果的数据流        |
| `gzip_comp_level` | `gzip` 压缩比，范围为`1-9`，值越大压缩级别越高，压缩过程越耗时    |
| `gzip_disable`    |                                          |
| `gzip_min_length` |                                          |
| `gzip_types`      | 配置 `MIME` 类型进行压缩。无论是否指定，**text/html** 类型一定会被压缩 |
|                   |                                          |
|                   |                                          |
|                   |                                          |


<br/>
<p><a href="#gzip">返回</a>&nbsp&nbsp |&nbsp&nbsp<a href="#top">返回目录</a></p>

-----

## <a name="rewrite">重写与重定向</a>

**rewrite重写指令语法** ： `rewrite regex replacement [flag];`

+ `regex`： 正则表达式
+ `replacement `：符合正则规则的替换算法
+ `flag` ：可选参数，可选的值如下：
  + `last` —— 终止`rewrite`，**继续匹配其它规则**
  + `break` ——  终止`rewrite`，**不再继续匹配**
  + `redirect` —— 临时重定向，返回 `302` 状态码
  + `permanent` —— **永久重定向**，返回 `301` 状态码



**rewrite实现重定向**




<br/>
<p><a href="#rewrite">返回</a>&nbsp&nbsp |&nbsp&nbsp<a href="#top">返回目录</a></p>

-------

## <a name="config_optimize">**配置优化**</a>

+ 连接数优化


+ 客户端请求限制


+ 浏览器缓存优化




<br/>
<p><a href="#config_optimize">返回</a>&nbsp&nbsp |&nbsp&nbsp<a href="#top">返回目录</a></p>

------

## <a name="cdn">**CDN**</a>

> CDN 是一种新型的网络构建方式，主要用于网站加速。



<p><a href="#cdn">返回</a>&nbsp&nbsp |&nbsp&nbsp<a href="#top">返回目录</a></p>

------

## 

