## <a name="cookie">Cookie</a>

----



在java中把`Cookie`封装成了`javax.servlet.http.Cookie类`，`Cookie`对象使用**key-value**属性对保存用户状态

+ `resquest.getCookie()`用于获取客户端提交的所有Cookie（以Cookie[]数组形式返回）
+ `response.addCookie(Cookie cookie)`用于向客户端设置Cookie
+ 各客户端的**Cookie**彼此独立，`互不可见`，并且具有`不可跨域名性`
+ **Cookie**使用**Unicode编码**，**Cookie**里有中文需要用`java.net.URLEncoder`类的`encode(String str,String encoding)`方法对中文字符进行**编码**，使用`java.net.URLDecoder类的decode(String str,String encoding)`方法进行解码
+ **Cookie不支持修改和删除操作** ，如需修改只需新建一个同名Cookie添加到response中覆盖即可，**删除Cookie**只需将Cookie的maxAge设置为负数



----

**Session**的实现是建立在**Cookie**上的，当调用`HttpServletRequest`对象的**getSession()**方法时才被创建，**并不是客户端访问服务器时就创建了Session**



当需要为某个客户端的请求创建一个**session**的时候，服务器首先检查这个客户端的请求里是否包含了一个**session标识**——称为`session id`：

+ 如果已经包含一个`session id`则说明以前已经为此客户创建过**session**，服务器就按照`session id`把这个**session**检索出来使用
+ 如果客户端请求里不包含`session id`，则为此客户创建一个**session**并且生成一个与此session相关联的`session id`，这个`session id`将在本次响应中返回给客户端



---

客户端存储`session id`的方式：

- 如果浏览器**没有禁用**`cookie`，则默认<a href="#cookiesave">采用`cookie`来保存</a>服务器发送的`session id`，并且在交互过程中浏览器可以自动的按照规则把标识发给服务器
- 如果客户端禁止了`cookie`，则会通过**URL重写**的方式把`session id`附在URL后面



![](https://github.com/HurricanGod/Home/blob/master/spring-mvc/img/cookie.png)

![](https://github.com/HurricanGod/Home/blob/master/spring-mvc/img/cookie1.png)
