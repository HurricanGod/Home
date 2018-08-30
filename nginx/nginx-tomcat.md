# <a name="top">Nginx + Tomcat 负载均衡</a>



+ <a href="#simple_web">**单实例Tomcat + Nginx**</a>


+ <a href="#proxy">**代理**</a>


+ <a href="#balance">**负载均衡**</a>


+ <a href="#cache">**缓存**</a>





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







<p><a href="#balance">返回</a>&nbsp&nbsp |&nbsp&nbsp<a href="#top">返回目录</a></p>

-----

## <a name="cache">**缓存**</a>

`Nginx` 提供两种Web缓存：

+ <a href="#forever_cache">**永久性缓存**</a>
+ <a href="#temporary_cache">**临时性缓存**</a>



### <a name="forever_cache">**永久性缓存**</a>





### <a name="temporary_cache">**临时性缓存**</a>





<p><a href="#cache">返回</a>&nbsp&nbsp |&nbsp&nbsp<a href="#top">返回目录</a></p>