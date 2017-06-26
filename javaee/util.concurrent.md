### concurrent包
#### 使用FutureTask类创建线程

**FutureTask类及其父类关系图如下：**
![FutureTask相关类图](https://github.com/HurricanGod/Home/blob/master/img/FutureTaskClass.png)
<br>

----

**FutureTask状态图**<br>
![FutureTask状态转换图](https://github.com/HurricanGod/Home/blob/master/img/FatureTaskStatus.png)

----
***FutureTask类实现了RunnableFuture接口，重写了run()方法，内部有1个Callable接口实例，该接口只有1个call()方法，是要执行的任务，可以根据需要实现该任务得到返回值****


