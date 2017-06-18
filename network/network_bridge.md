以太网上主机间的距离不能太远，否则主机发送的信号通过铜线的传输会衰减到使CSMA/CD协议无法正常工作。<br>
**早期使用转发器来扩展以太网的覆盖范围，现在使用光纤和光纤调制解调器来扩展以太网覆盖范围**

#### 网桥  
网桥工作在数据链路层，它根据MAC帧的目的地址对收到的帧进行**转发**和**过滤**   
1. 网桥收到1个帧首先检查帧的目的MAC地址，通过查找转发表查看从哪个接口转发出去    
2. 如果查找到的转发接口和进入网桥时的接口相同，则丢弃这个帧，如果查找的接口与进入网桥的接口不相同则按查找的接口转发出去


网桥是存储转发方式工作的，一定是把帧先收下来再进行处理，网桥会丢弃CRC检验有差错的帧或帧长过长和过短的无效帧。



**网桥自学习过程**

1. 每收到1个帧，记下其源地址和进入网桥的接口
2. 建立转发表时把帧首部中的源地址写在“地址”栏下面，转发帧时根据**目的地址**转发   

![网桥自学习](https://github.com/HurricanGod/Home/blob/master/img/networkbridge.png)
 
#### ARP
**ARP高速缓存**：ARP高效运行的关键在于每个主机上有一个ARP高速缓存。高速缓存里存放最近Internet地址到硬件地址之间的映射关系   
高速 缓存里的每一项的生存时间一般为20分钟，windows下可以使用`arp -a`命令检查ARP高速缓存    
>>C:\Users\NewObject>arp -a   
  接口: 192.168.123.230 --- 0x2   
  Internet 地址         物理地址                类型   
  192.168.123.1         d0-c7-c0-a0-f8-4a     动态   
  192.168.123.92        74-23-44-93-42-4b     动态   
  192.168.123.110       14-2d-27-ed-21-6b     动态   
  192.168.123.113       00-e0-66-e4-8e-58     动态    
  192.168.123.189       d4-97-0b-85-32-0b     动态     
 
##### Windows下有趣的cmd命令  
```shell
set ip=192.168.123 && for /l %j in (1,1,254) do ( ping %ip%.%j -n 1 -w 1000 |arp -a %ip%.%j |findstr dynamic >>2.txt )
set ip=192.168.123&& for /l %j in (1,1,100) do ( ping %ip%.%j -n 1 -w 1000 |arp -a %ip%.%j |findstr 动态 >>D:\1.txt )
```
``for /l %j in (1,1,254) do (commands)``这个是cmd下的for循环,官方帮助文档如下：<br>
FOR /L %variable IN (start,step,end) DO command    该集表示以增量形式从开始到结束的一个数字序列。<br>    
因此，(1,1,5)将产生序列1 2 3 4 5，(5,-1,1)将产生序列(5 4 3 2 1)<br>
在for循环里变量要用两个"%"包起来，所以刚才设置的变量ip这里表示为`%ip%`<br><br>
**ping**命令<br>
`-n count`表示要发送的回显请求数<br/>
`-w timeout`选项表示等待每次回复的超时时间(毫秒) <br>  
ping命令被读取时会被替换为``ping 192.168.123.n``(n为(1~254))  <br> <br>

`arp -a ip`用于获取指定ip的硬件地址，这里把arp命令运行结果通过**管道**传送给下一条命令``findstr``  <br> 
``findstr``命令查找包含字符串**"dynamic"**所在的行，把查找的结果以追加的方式重定向到2.txt文件里 <br>

-------

 ##### 批处理文件bat
 **find.bat**
 ```shell
 ::关闭回显
 @echo off
goto start
功能:查找局域网内指定范围内的ip地址和mac地址
运行脚本需要4个参数
%1% : 用于保存中间结果的文件名，可以任意
%2% : 网段，如192.168.1
%3% : 范围下限，表示要查找网段范围下限
%4% : 范围上限，表示要查找网段范围上限
比如脚本文件在D:\hello.bat
运行方式:D:\hello.bat D:\2.txt 192.168.1 1 110
****************************************
::cd .>%1% 表示清空txt文件
:start
::cd .>%1%
set ip= %2%
set left=%3%
set right=%4%
for /l %%j in (%left%,1,%right%) do (
  ping %ip%.%%j -n 1 -w 1000 >nul
  FOR /F "delims=_" %%i IN ('arp -a %ip%.%%j') DO (
    IF NOT "%%i" == "未找到 ARP 项。" (
      echo %%i >> %1%
    )
  )
)
FINDSTR /C:"动态" %1%
pause
 ```
 
 ##### 防止arp欺骗攻击，把网关ip地址与mac地址绑定
 **ip地址与mac地址绑定遇到的问题：**<br>
 >***windows10下管理员权限执行arp -d ip 命令出现arp项添加失败拒绝访问的问题***    
 
 <br>**解决方法：**<br><br>
 
 1. 使用`netsh`命令查看网络连接所在的接口号，具体操作为：``netsh i i show in`` <br>   
 执行结果如下：<br>
 
 >>
```
Idx         Met         MTU        状态                名称
---  ----------  ----------  ------------  ---------------------------
 18          35        1500  connected     VMware Network Adapter VMnet1
  7          35        1500  connected     VMware Network Adapter VMnet8
  2          55        1500  connected     WLAN
  1          75  4294967295  connected     Loopback Pseudo-Interface 1
  9           5        1500  disconnected  以太网
 17          25        1500  disconnected  本地连接* 4
```
<br>
 
 2. 
 
