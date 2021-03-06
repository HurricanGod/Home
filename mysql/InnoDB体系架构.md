## InnoDB体系架构

+ **后台线程**
  + `Master Thread`
  + `IO Thread`
  + `Purge Thread`
  + `Page Cleaner Thread`
+ **内存**
  + **缓冲池**
  + `LRU List`、`Free List`、`Flush List`
  + **重做日志缓冲**
  + **额外内存池**


----

`Innodb`存储引擎是基于磁盘存储的，并将其中的记录**按照页的方式**进行管理，因此可以将其视为基于磁盘得到数据库系统，基于磁盘的数据库系统通常使用**缓冲池技术**来提高数据库中整体性能



**缓冲池** 就是一块内存区域，通过内存的速度来弥补磁盘速度较慢对数据库性能的影响。在数据库进行读取页操作，首先将从磁盘读取的页存放在缓冲池中，下一次读取相同页时，先判断该页是否在缓冲池中，若该页在缓冲池命中则直接读取该页，否则从磁盘读取。

对数据库页中的修改操作，首先修改在缓冲池中的页，然后再以一定的频率刷新到磁盘；**页从缓冲池刷新回磁盘的操作并不是发生在每次页发生更新时触发** ，而是使用`Checkpoint`的机制刷新回磁盘。

**缓冲池**缓存的数据类型有：

+ 索引页
+ 数据页
+ 插入缓冲
+ 自适应哈希索引
+ `InnoDB`存储的锁信息
+ 数据字典信息
+ `undo`页（重做日志缓冲 `redo log buffer`）



数据库中的缓冲池通常使用`LRU`算法进行管理，在InnoDB存储引擎中使用**改进的LRU**算法，在`LRU`列表中加入**midpoint**位置，新读取的页不是直接放在`LRU列表`的头部，而是放在**midpoint**位置。

`InnoDB存储引擎`将新读取的页放在**midpoint**位置不放在`LRU`首部的原因：

+ 直接将新读取的页放在`LRU`首部，有些sql操作可能使缓冲池中的页被刷出，从而影响缓冲池的效率，这类操作主要是一些需要访问表中许多页的操作，往往只是一次查询中需要，并不是活跃的热点数据



**在LRU列表中的页被修改后，该页称为脏页**，通过`Checkpoint`机制将脏页刷新回磁盘，脏页既存在于`LRU列表`又存在`Flush`列表中，`LRU列表`用于管理**缓冲池页的可用性**，`Flush列表`用于**管理将页刷新回磁盘**



----

**重做日志缓冲**

+ `InnoDB`存储引擎首先将重做日志信息先放入到重做日志缓冲区，然后按照一定频率将其刷新到重做日志文件



**重做日志**在下列三种情况会将重做日志缓冲中的内容刷新到磁盘中的重做日志文件中

+ `Master Thread`每一秒将重做日志缓冲刷新到重做日志文件
+ 每个**事务提交**时会将重做日志缓冲刷新回重做日志文件
+ 当重做日志缓冲池中的剩余空间小于`1/2`时，重做日志缓冲会被刷新到重做日志文件
+ 为了避免发生数据丢失问题，数据库系统普遍采用`Write Ahead Log`策略，当事务提交时，先写重做日志，再修改页，若发生宕机可以通过**重做日志**恢复数据


---

## 检查点技术

`Checkpoint`技术主要解决的问题：

+ 缩短数据库的恢复时间
+ 缓冲池不够用时，将脏页刷新回磁盘
+ 重做日志不可用时，刷新脏页



`InnoDB`内部有两种**Checkpoint** ，分别为：

+ `Sharp Checkpoint` ——发生在数据库关闭时将所有脏页刷新回磁盘
+ `Fuzzy Checkpoint` ——只刷新一部分脏页回磁盘



可能发生`Fuzzy Checkpoint`的情况主要有一下几种：

+ `Master Thread Checkpoint` —— 差不多以每秒或每十秒的速度从缓冲池的脏页列表中刷新一定比例的脏页到磁盘，刷新过程是异步的，在此期间`InnoDB`存储引擎可以进行其它操作（**比如用户查询操作**）
+ `Flush_LRU_List Checkpoint` —— `InnoDB`存储引擎要保证LRU列表中有差不多100个空闲页可用
+ `Async/Sync Flush Checkpoint` ——指重做日志文件不可用的情况下，需要强制将一些页刷新回磁盘，脏页从脏页列表中选取
+ `Dirty Page too much Checkpoint`


-----

## Master Thread工作方式

`Master Thread`具有最高的线程优先级别，内部有多个循环组成：

+ 主循环 ——有两大部分操作，每秒操作和每10秒操作

  + 每秒操作包括：

    + 日志缓冲刷新到磁盘，即时这个事务还没有提交
    + 合并插入缓冲（**可能发生**）
    + 至多刷新100个`InnoDB`的缓冲池中的脏页到磁盘（**可能发生**）
    + 当前没有用户活动，切换到后台循环（**可能发生**）

    ​

  + 每10秒操作包括：

    + 刷新100个脏页回磁盘（**可能发生**）
    + 合并至多5个插入缓冲
    + 将日志缓冲刷新到磁盘
    + <a name="delUndo">删除无用的`Undo`页</a>
    + 刷新100个或者10个脏页到磁盘


+ 后台循环 —— 若当前没有用户活动或者数据库关闭，则会切换到这个循环，后台循环主要执行的操作
  + 删除无用的`Undo`页
  + 合并20个插入缓冲
  + 跳回到主循环
  + 不断刷新100个页直到符合条件
+ 刷新循环
+ 暂停循环



<a hreef="#delUndo">删除无用的`Undo`页</a>：对表进行`update`、`delete`这类操作时，原先的行被标记为删除，但因为一致性读的关系，需要保留这些行的版本信息，在删除无用的`Undo`页过程中InnoDB存储引擎会判断当前事务系统中已经被删除的行可以被删除，如果可以删除则立刻对其进行删除.



-------

`InnoDB`关键特性

+ <a href="#insertBuffer">插入缓冲</a>
+ 两次写
+ <a href="#hash">自适应哈希索引</a>
+ <a href="#AIO">异步IO</a>
+ 刷新邻接页



<a name="insertBuffer">**插入缓冲**—— `Insert Buffer`</a>

 `Insert Buffer`对于**非聚集索引**的插入或者更新操作，不是每一次直接插入到索引页中，而是先**判断插入的非聚集索引页是否在缓冲池**中，若在，直接插入；若不在，则先放入到一个`Insert Buffer`对象中。然后再以一定的频率和情况进行`Insert Buffer`和辅助索引页子节点的合并操作，这时通常能将多个插入合并到一个操作中，提高了对于非聚集索引插入性能。



`Insert Buffer`需要满足的条件：

+ **索引是辅助索引**
+ **索引不是唯一索引**



如果**辅助索引**是唯一的，在**插入缓冲时**必然要去查找索引页判断新插入的记录的唯一性，查找索引页是一个离散读取的过程，从而使`Insert Buffer`失去意义



`Change Buffer`是`Insert Buffer`的升级，`Change Buffer`适用的对象依然是非唯一辅助索引，可以对`DML操作` —— `insert`、`update`、`delete` 都进行缓存

对一条记录进行`update`操作可能分为两个过程：

+ 将记录标记为删除
+ 真正将记录删除




----

<a name="duble-write">**两次写**</a>

`doublewrite`给InnoDB存储引擎的数据页的可靠性带来保障。




<a name="hash">**自适应哈希索引**</a>

`InnoDB存储引擎`会监控对表上各索引页的查询，如果观察到建立哈希索引可以带来速度提升，则建立哈希索引，称为自适应哈希索引。**存储引擎**会自动根据访问的频率和模式来**自动地为某些热点页**建立哈希索引



<a name="AIO">**异步IO**</a>

`Innodb`采用异步io的方式来处理磁盘操作。

**优点** ：

+ 用户可以发生1个IO请求后立刻发送另一个IO请求
+ 异步IO可以进行`IO Merge`操作，将多个IO合并成1个IO
  + 例：用户需要访问页的`(space,page_no)`为(8,6)、(8,7)、(8,8)，如果进行同步IO需要进行3次IO操作，采用异步IO则只需要1次
