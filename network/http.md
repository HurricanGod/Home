## Http协议

### Http请求报文
+ **请求行**：以**请求方法**开头，以空格分开，后面跟着请求的URI和协议的版本
+ **消息报头**（可选）
+ **请求正文**（可选）

----
**请求报文**
+ `Accept`——请求报头域用于指定客户端接受哪些类型的信息，例如：`Accept：text/html`，表明客户端希望接受html文本
+ `Accept-Encoding`——请求报头域用于指定客户端接受的字符集
+ `Accept-language`
+ `Connection`
+ `Cookie`
+ `Host`
+ `User-Agent`


----

### Http响应报文

+ **状态行**，格式：`协议版本 状态码 状态代码文本描述`
+ **消息报头**
+ **响应正文**
+ `Connection`
+ `Content-Encoding`
+ `Content-type`
+ `Date`
+ `Set-Cookie`
