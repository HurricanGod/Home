#  <a name="top">canal</a>



工作原理：

+ `canal` 模拟MySQL slave的交互协议，把自己伪装成MySQL slave，向MySQL发送 `dump`协议
+ MySQL master 收到 dump 请求，开始推送 binary log 给 `canal`
+ `canal` 解析 binary log 对象（byte流）



