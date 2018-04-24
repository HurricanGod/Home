# <a name="top">查看Linux内存、CPU相关命令</a>



+ <a href="#mem_top" >**查看Linux内存占用情况**</a>





-----

<a name="mem_top" >**查看Linux内存占用情况**</a>



```shell
>> ps -auxw --sort=-rss
# 根据占用物理内存大小由高到低输出进程信息
#USER  PID      %CPU      %MEM     	  VSZ   		RSS 	   TTY STAT START TIME COMMAND
#用户  进程号  CPU占用率  内存占用率  占用的虚拟内存  占用的物理内存  
>> ps -auxw --sort=-rss,+%cpu
# 查看所有进程信息，按照物理内存占用由高到低，cpu使用率由低到高排序
```


+ `a` —— 显示终端上的所有进程，包括其他用户的进程
+ `-l` —— 长格式
+ `-w`—— 宽输出
+ `-e`—— 显示所有进程
+ `-f`—— 全格式
+ `--sort`—— 根据`sort`后面的key进行排序，其中`-`表示逆序，`+`表示正序

![](https://github.com/HurricanGod/Home/blob/master/linux/img/ps-auxw.png)

**备注** ：

+ `STAT` —— 进程状态，`+`表示位于后台的进程组，主要有如下几种：
  + `R(runtime)`：正在运行或准备运行
  + `S(sleep)`：睡眠状态
  + `s`：小写的`s`，表示包含子进程
  + `I(idle)`：空闲状态
  + `Z`：僵死状态
  + `D`：不可中断的睡眠，常发生于I/O
  + `P`：等待交换页
  + `<` 表示高优先级，`N`表示低优先级
  + `L` ：表示有些页被锁进内存，`l(小写的l)`：多线程
  + `T`：终止
+ `START` —— **进程启动的时间和日期**
+ `TIME` —— **进程使用的总CPU时间**


+ `COMMAND` —— 正在执行的命令行命令
