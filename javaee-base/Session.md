Session对应的类为javax.servlet.http.HttpSession，Session对象是在客户端第一次请求服务器时创建，session也是一种key-value属性对。

Servlet里通过request.getSession(boolean  create)方法获取客户的Session.

可以使用getMaxInactiveInterval()获取Session的超时时间，

setMaxInactiveInterval(long  interval)修改Session的超时属性，Tomcat默认的超时时间为20分钟.

修改web.xml文件改变Session默认的超时时间

```xml
<session-config>
  	<session-timeout>60</session-timeout>
</session-config>
```

Session需要使用Cookie作为识别标志，依据Cookie来识别是否为同一用户，新开的浏览器窗口会生成新的Session，但子窗口除外，子窗口会共用父窗口的Session.

**注意**

对于wap应用，常规的Cookie不能派上用场