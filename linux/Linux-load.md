# <a name="top">Linux系统负载相关命令</a>



+ <a href="#memory" >**Linux内存**</a>



+ <a href="#mem_top" >**查看Linux内存占用情况**</a>


+ <a href="#top_cmd">**Top命令**</a>


-----
## <a name="memory" >Linux内存</a>

+ ***虚拟内存***
> 虚拟内存就是为了满足物理内存的不足而提出的策略，使用磁盘空间虚拟出一块逻辑内存。用作虚拟内存的磁盘空间被称为交换空间，
> 当物理内存不足时，Linux就会将暂时不用的内存块信息写到虚拟内存里，以后需要用到时再从虚拟内存里读入到物理内存里即可。


+ ***cached & buffers***

> buffers —— 用于块设备做缓冲（块设备的读写缓冲区），只记录文件系统的metadata 以及 tracking in-flight pages
>
> cached —— 作为页面缓存的内存，文件系统的 cache。Linux内核为提高读写性能和速度，会将文件在内存中进行缓存，这部分内存称为缓存内存。
>
> 缓存内存（Cache Memory）不会自动释放，在需要使用是会自动释放



+ ***共享内存***

> 共享内存是指多个进程共享一段物理内存，将同一段物理内存映射到不同进程的虚拟空间实现的，进程间通信的最简单方式之一。
>
> 由于共享内存映射到了不同进程虚拟空间，所以不同进程里可以直接使用，不需要进行额外的复制，因此效率很高



+ `VSS(Virtual Set Size)` —— 虚拟耗用内存，包含共享库占用的内存
+ `RSS` —— 实际使用的物理内存
+ `PSS` —— 实际使用的物理内存
+ `USS` —— 进程独占的物理内存，**不包含共享库占用的内存**




<a href="https://raw.githubusercontent.com/pixelb/ps_mem/master/ps_mem.py">**Python脚本实现内存监控**</a>


<p align="right"><a href="#memory">返回</a>&nbsp&nbsp|&nbsp<a href="#top">返回顶部</a></p>

----

## <a name="mem_top" >**查看Linux内存占用情况**</a>



```shell
>> ps -auxw --sort=-rss
# 根据占用物理内存大小由高到低输出进程信息
#USER  PID      %CPU      %MEM     	  VSZ   	RSS 	   TTY STAT START TIME COMMAND
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
  ![](https://github.com/HurricanGod/Home/blob/master/linux/img/ps-auxw.png)

<p align="right"><a href="#mem_top">返回</a>&nbsp&nbsp|&nbsp<a href="#top">返回顶部</a></p>

----
## <a name="top_cmd">Top命令</a>
对 `top` 输出结果指定某列进行排序：
+ `top` → `shift + f` 
+ 使用 `↑` 或 `↓` 选择排序列，使用 `s` 进行确定
+ `q` 退出生效



**Top常用参数**：

+ `-d`：设置延迟间隔，用法示例：`top -d 1 `——表示以1s的间隔启动top
+ `-n`： 指定top命令迭代的次数，用法示例：`top -n 3`——表示迭代3次后退出
+ `-p`： 监控指定进程pid



```sh
# 组合用法
# 查看指定pid进程的线程占用cpu、内存等信息
top -Hp pid

top -d 1 -n 10
```





<p align="right"><a href="#top_cmd">返回</a>&nbsp&nbsp|&nbsp<a href="#top">返回顶部</a></p>



