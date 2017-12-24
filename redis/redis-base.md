# Redis



----

**网络IO模型**

+ `redis`使用单线程的IO复用模型
+ `Redis`封装了一个简单的AeEvent事件处理框架，主要实现了`epoll`,` kqueue`和`select`，可以将速度优势发挥到最大
+ `redis`提供的简单计算功能，在计算过程中IO调度将被阻塞



`Redis`在分布式系统架构中的一些应用场景：

+ 单点登陆系统的权限验证
+ 直播平台的好友列表显示
+ 商品秒杀活动的剩余商品数目
+ 电商网站的商品排行
+ 网站数据缓存(一些需要定时更新的数据，如：积分排行榜)

-----

## Redis底层数据结构

`strings`

字符串是最基本的`Redis`**值类型**，`Redis`字符串是二进制安全的，数据在写入时是什么样，读取时就是什么样的。Redis没有直接使用c语言传统的字符串表示，使用的是简单动态字符串`SDS`， `SDS`定义如下：

```c
struct sdshdr{
  int len;		//记录buf数组已使用的字节数，即SDS所保存的字符串长度
  int free;		//记录buf数组中未使用字节数
  char buf[];	//保存字符串的数组
}
```



**C字符串与SDS之间的区别**

|        C字符串         |            SDS            |
| :-----------------: | :-----------------------: |
| 获取字符串长度为复杂度为`O(n)`  |    获取字符串长度为复杂度为`O(1)`     |
|      可能造成缓冲区溢出      |         不会造成缓冲区溢出         |
|      只能保存文本数据       |      可以保存文本数据或二进制数据       |
| 修改字符串长度n次需要n次内存重新分配 | 修改字符串长度n次**最多**需要n次内存重新分配 |
|                     |                           |



----

**整数集合**

`整数集合`是集合键的底层实现之一，当一个集合只包含整数值元素，并且这个集合元素数量不多时，`Redis`就会使用整数集合作为集合键的底层实现

![1.png](https://github.com/HurricanGod/Home/blob/master/redis/img/1.png)



整数集合的底层实现为数组，这个数组以**有序**、**无重复**的方式保存集合元素；在有需要时程序会根据新添加元素的类型(主要指存放整数的字节数太小不足以表示大的整数)，对集合中的每个元素进行升级。



整数集合采用升级来解决整数溢出问题可以尽可能地节约内存，整数集合只支持升级，**不支持降级**。



**压缩列表**

+ 列表健和哈希键的底层实现之一
+ 当一个列表键只包含少量列表项，并且这个列表项要么是小整数，要么是短字符串，`Redis`就会使用压缩列表来做列表健的底层实现




**字典**

`Redis`的字典使用哈希表作为底层实现，`Redis`字典所使用的哈希表结构定义如下：

```c
typedef struct dictht{
  dictEntry **table;		//哈希表数组
  unsigned long size;		//哈希表大小
  unsigned long sizemask;	//用于计算索引值，等于size-1
  unsigned long used;		//哈希表已使用节点数
}
```

`Redis`使用**链地址法**来解决键冲突，每个哈希表节点都有一个`next指针`，发生冲突时总是将新节点**添加到哈希表节点的表头位置** 。



**跳跃表**



-----

## Redis数据类型



`Redis`对象系统主要包括：

+ 字符串对象
+ 列表对象
+ 哈希对象
+ 集合对象
+ 有序集合对象



`Redis`对象系统实现了基于**引用计数技术**的内存回收机制

+ 当程序不再使用某个对象时，这个对象所占用的内存就会被自动释放
+ 在恰当的情况下，通过让多个数据库键共享同一个对象来节约内存






当使用`Redis`命令对数据库进行读写时，服务器不仅会对键空间执行指定的读写操作，还会执行一些额外的操作，主要包括：

+ 在读取1个键后（读操作和写操作都要对键进行读取），服务器会根据键是否存在来更新键空间**命中次数** 或 **键空间不命中次数**
+ 在读取一个键后服务器会更新键的`LRU(最近最久未使用)`时间，这个值可以用于计算键的闲置时间，可以在命令行下使用`object idletime <key>`查看键的闲置时间
+ 服务器如果在读取一个键时发现键已过期，服务器会先删除这个过期的键，然后才执行余下的操作
+ 如果有客户端使用`watch`命令监视某个键，那么服务器在对被监视的键进行修改后，会将这个键**标记为脏**，从而让事务程序注意到这个键已经被修改过
+ 服务器每次修改一个键后都会对脏键计数器的值增1，这个计数器会触发服务器的持久化以及复制操作
+ 如果服务器开启的数据通知功能，那么对键进行修改后，服务器将按配置发送相应的数据库通知




----


**设置键生存时间(TTL)或过期时间**

+ `expire <key> <ttl>` —— `expire key 5`(表示设置key键的生存时间为5秒)
+ `pexpire <key> <ttl>` 
+ `expireat <key> <timestamp>` ——将键key的过期时间设置为**timestamp**所指定的秒数时间戳
+ `setex`——在设置一个字符串键同时为键设置过期时间

![2.png](https://github.com/HurricanGod/Home/blob/master/redis/img/2.png)



`ttl`命令可以获取带有生存时间键的剩余生存时间

----

`Redis`适合全体类型的常用命令

+ <a href="#Exists">**exists**</a> —— 判断一个key是否存在，存在返回1；否则返回0
+ <a href="#Del">**del**</a> —— 删除某个key或删除一系列key，存在返回1；否则返回0(key不存在)
+ <a href="#type">**type**</a> —— 返回某个key元素的数据类型，key不存在返回空
+ <a href="#keys">**keys key-pattern**</a> —— 返回匹配的key列表
+ <a href="#rename">**rename**</a> —— 改key的名字，新键名key如果存在将会被覆盖
+ <a href="#renamenx">**renamenx**  —— 更改key的名字，如果新键名存在则修改失败
+ <a href="#dbsize">**dbsize**</a> —— 返回当前数据库的key的总数
+ **flushdb** —— 清空当前数据库中的所有键
+ **flushall** —— 清空所有数据库中的所有键







<a name="Exists">**exists**</a>

![3.png](https://github.com/HurricanGod/Home/blob/master/redis/img/3.png)



<a name="Del">**del**</a> 

![4.png](https://github.com/HurricanGod/Home/blob/master/redis/img/4.png)



<a name="type">**type**</a>

![5.png](https://github.com/HurricanGod/Home/blob/master/redis/img/5.png)

<a name="keys">**keys key-pattern**</a>

![6.png](https://github.com/HurricanGod/Home/blob/master/redis/img/6.png)



<a name="rename">**rename**</a>

![](https://github.com/HurricanGod/Home/blob/master/redis/img/7.png)

----

### 查询Redis相关信息 —— info [section]

**info**命令可以查询Redis信息参数，`info`命令如果不带参数默认返回所有信息

`info`常用命令选项如下：

|     名称      |        说明         |
| :---------: | :---------------: |
|   server    | Redis server的常规信息 |
|   clients   |    Client的连接选项    |
|   memory    |     存储占用相关信息      |
| persistence | RDB and AOF 相关信息  |
|    stats    |       常规统计        |
| replication | Master/slave请求信息  |
|     cpu     |    CPU 占用信息统计     |
|   cluster   |    Redis 集群信息     |
|  keyspace   |      数据库信息统计      |
|     all     |      返回所有信息       |
|   default   |     返回常规设置信息      |

