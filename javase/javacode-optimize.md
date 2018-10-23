# <a name="top">**代码优化**</a>







----

## <a name="multhread">**多线程**</a>



+ ***一台服务器上启动多少线程合适？***

  > 启动线程数 = CPU内核数 *  [ 任务执行时间 / (任务执行时间 - IO等待时间) ]

  如果是 `CPU密集型任务`，线程数最多不超过CPU内核数，因为启动再多线程，CPU也来不及调度。





<p align="right"><a href="#multhread">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a></p>