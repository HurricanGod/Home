# <a name="top">wrk</a>



<a href="https://github.com/wg/wrk">`wrk`</a> 是一款针对 Http 协议的基准测试工具，它能够在单机多核 CPU 的条件下，使用系统自带的高性能 I/O 机制，如 epoll，kqueue 等，通过多线程和事件模式，对目标机器产生大量的负载。


## Ubuntu安装

```sh

# 安装编译依赖
apt-get install build-essential libssl-dev -y

# 拉取代码
git clone https://github.com/wg/wrk.git wrk


# 编译
cd wrk && make

# 复制可执行文件到 /usr/local/bin 目录
cp wrk /usr/local/bin


# 查看 wrk 是否安装成功
wrk -v

```



## 使用

**命令格式**： `wrk <options> <url>`

options:
+ `-c <N>` —— 与服务器保持连接的TCP数
+ `-d <T>` —— 测试时长
+ `-t <N>` —— 使用的线程数
+ `-s` —— Lua脚本路径
+ `-h` —— http请求头
+ `--latency` —— 压测结束后，打印延迟统计信息
+ `--timeout <T>` —— 请求超时

`<N>` 表示数字参数，支持国际单位（1k/1M/1G）
`<T>` 表示时间参数，支持时间单位（1s/2m/2h）

### 样例1——指定lua脚本发送POST请求

```sh
# -T15s：指定socket超时时间为15s
wrk -t8 -c40 -d100s -T15s -s /home/app/lua/GoodsDetail.lua --latency  http://goods-test.kt3.pagoda.com.cn/goods/detail

```

**lua脚本内容**
```lua
wrk.method = "POST"                                                 
wrk.headers["Content-Type"] = "application/json"
wrk.headers["Accept"] = "*/*"                                                       
wrk.body = '{ "body": {"goodsSn": "1006311001","organizationCode": "szpszx", "channelId": 2},"head": {}}'  

```



-----
## <a name="wrk-lifecycle">wrk生命周期</a>

[![ruI9qx.png](https://s3.ax1x.com/2020/12/14/ruI9qx.png)](https://imgchr.com/i/ruI9qx)



### <a name="setup">setup</a>

`wrk` 会在测试线程已经初始化，但还没有启动的时候调用该方法，为每一个测试线程调用一次 `setup` 方法，并传入代表测试线程的对象`thread` 作为参数。

`setup` 方法中可操作该`thread` 对象，获取信息、存储信息、甚至关闭该线程

+ `thread.addr`
+ `thread:get(name)`
+ `thread:set(name,value)`
+ `thread:stop()`






<p align="right"><a href="#wrk-lifecycle">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

-----
### <a name="init">init</a>

由测试线程调用，**只会在进入运行阶段时调用一次**。支持从启动 wrk 的命令中，获取命令行参数。









<p align="right"><a href="#wrk-lifecycle">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

----
### <a name="delay">delay</a>

在每次发送请求之前调用，如果需要定制延迟时间，可以在这个方法中设置









<p align="right"><a href="#wrk-lifecycle">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

-----
### <a name="request">request</a>

用来生成请求, 每一次请求都会调用该方法，**注意不要在该方法中做耗时的操作**







<p align="right"><a href="#wrk-lifecycle">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

----
### <a name="response">response</a>

在每次收到一个响应时被调用，为提升性能，如果没有定义该方法，那么wrk不会解析 `headers` 和 `body`



<p align="right"><a href="#wrk-lifecycle">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

----
### <a name="done">done</a>

done() 方法在整个测试过程中只会被调用一次，我们可以从给定的参数中，获取压测结果，生成定制化的测试报告。

```sh
function done(summary, latency, requests)
```



<p align="right"><a href="#wrk-lifecycle">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>