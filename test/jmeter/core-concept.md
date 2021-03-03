# <a name="top">jmeter核心概念</a>









---

## <a name="thread-group">线程组</a>



###  <a name="setup-thread-group">setUp Thread Group</a>

特殊的 `ThreadGroup`，用于执行常规线程组之前的必要操作，它会在普通线程组**执行之前被触发**。应用场景：

+ 对于需要token的接口测试，可以在 `setUp Thread Group` 先获取token
+ 整个测试计划只需要执行一次的操作





<a name="tearDown-thread-group">tearDown Thread Group</a>

用于在执行常规线程组完成后执行一些必要的操作，它会在普通线程组**执行之后被触发**。



<p align="right"><a name="tearDown-thread-group">返回</a> &nbsp|&nbsp<a name="top">返回目录</a></p>



<a name="threadGroup">Thread Group</a>

一个线程组可以看做一个**虚拟用户组**，线程组中的每个线程都可以理解为一个虚拟用户。**每个线程之间都是隔离的**，互不影响的。一个线程的执行过程中，操作的变量，不会影响其他线程的变量值。





<p align="right"><a name="threadGroup">返回</a> &nbsp|&nbsp<a name="top">返回目录</a></p>

-----

## <a name="thread-attribute">线程属性</a>



### <a name="ramp-up">Ramp-Up</a>

`Ramp-Up` 用于**设置启动所有线程所需要的时间**。

+ 如果线程数设置为50，`Ramp-Up` 设置为1，表示1s内启动50个线程
+ 如果线程数设置得很大，`Ramp-Up` 设置得很小，实际情况下启动的线程数会小于设置的线程数，因为受硬件的限制，这种情况下相当于尽压测服务器最大能力启动线程。





### <a name="loop">循环</a>

每个线程执行请求的次数，上一个请求完成后才执行下一个请求。





<p align="right"><a name="thread-attribute">返回</a> &nbsp|&nbsp<a name="top">返回目录</a></p>

----



## <a name="component"> 元件与组件</a>

`Jmeter` 有6大组件：

+ 配置原件 —— 用于初始化变量，以便采样器使用。类似于框架的配置文件，参数化需要的配置都在配置元件中
+ 定时器 —— 一般用来指定请求发送的延时策略
+ 前置处理器 —— 在进行取样器请求之前执行一些操作，比如生成入参数据
+ 后置处理器 —— 在取样器请求完成后执行一些操作，通常用于处理响应数据，从中提取需要的值
+ 断言 —— 主要用于判断取样器请求或对应的响应是否返回了期望的结果
+ 监听器 —— 在 JMeter 执行测试的过程中搜集相关的数据，然后将这些数据在 JMeter 界面上以树、图、报告等形式呈现出来。**不过图形化的呈现非常消耗客户端性能，在正式性能测试中并不推荐使用**







