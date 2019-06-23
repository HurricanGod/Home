# <a name="top">渗透工具</a>







----

## <a name="nmap">nmap</a>

**命令格式**： `nmap -A -v -T4 目标主机ip`

+ `-A`： 开启全面扫描
+ `-T4`：扫描过程使用的时序模版，等级越高扫描速度越快，越容易被防火墙屏蔽
+ `-v`： 显示扫描细节



**命令格式**： `nmap -sS 目标主机ip` —— 使用`TCP SYN`方式扫描TCP端口

**命令格式**： `nmap -sU 目标主机ip` —— 扫描UDP端口



侦测到的端口状态分为 6 类：

+ `open` —— 端口开放
+ `closed` —— 端口关闭
+ `filtered` —— 被防火墙屏蔽，无法进一步确认状态
+ `unfiltered`—— 没有被屏蔽，状态未知
+ `open|filtered` —— 状态不确定
+ `closed|filtered` —— 状态不确定





----

## <a name="curl">curl工具</a>

简介： ***用于通过URL传输数据的命令行工具和库***，支持`cookies`、`forms`

支持的协议：

+ `FTP`
+ `HTTP`
+ `HTTPS`
+ `IMAPS`
+ `SCP`
+ ...



**测试目标网站支持的http方法**

```sh
curl -v -X OPTIONS  url
```

+ `-v`： 显示请求的信息 
+ `-X`： 指定请求方法



模拟post方法请求

```sh
curl -d "args" url
# curl -d "name=admin&password=2333" http://www.a.com

# 添加请求头
curl -H "Content-Type:application/json" -X POST --data '{"key":"value"}' url
```



模拟put方法

```sh
curl -v -X PUT url -d "args"
```







---

## <a name="web_shell">Shell反弹</a>





