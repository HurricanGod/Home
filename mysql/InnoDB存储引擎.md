## InnoDB存储引擎



+ <a href="#redoFile">**重做日志文件**</a>
+ <a href="#tableFile">**表空间文件**</a>





<a name="redoFile">**重做日志文件**</a>

+ 默认情况下，在InnoDB存储引擎的数据目录下会有两个名为`ib_logfile0` 和`ib_logfile1`的文件，该文件为`InnoDB` **存储引擎的日志文件**，即**重做日志文件**
+ **重做日志文件**记录了对于存储引擎的事务日志，可以使用**重做日志文件**对数据库进行恢复，**保证数据库的完整性**
+ 每个`InnoDB`存储引擎至少有1个重做日志文件组，每个文件组下至少有2个重做日志文件
+ 日志组中的每个重做日志文件的大小一致，以循环写的方式运行。（`InnoDB`存储引擎先写重做日志文件1，写到文件的最后会切换到日志文件2，重做日志文件2也被写满后又会切换到重做日志文件1）



<a name="tableFile">**表空间文件**</a>

+ `InnoDB存储引擎`采用将存储的数据按表空间进行存放的设计，默认配置下会有一个初始大小为10mb，名为ibdata1的文件，该文件是默认的表空间文件






----

<a name="InnodbLock">InnoDB存储引擎中的锁</a>


`InnoDB`存储引擎实现了两个标准的行级锁：

+ 共享锁(S Lock)，允许事务读一行数据
+ 排他锁(X Lock)，允许事务删除或者更新一行数据



为了支持不同粒度上进行加锁操作，`InnoDB`存储引擎支持一种额外的锁方式，称为**意向锁**——**将锁定的对象分为多个层次，意味着事务希望在更细粒度上进行加锁**



如果把上锁的对象看成一棵树，那么对最下层的对象的对象加锁，也就是对最细粒度的对象进行加锁，那么首先需要粗粒度的对象上锁。

![层次结构](https://github.com/HurricanGod/Home/blob/master/redis/img/%E5%B1%82%E6%AC%A1%E7%BB%93%E6%9E%84.png)

如上图所示，若需要对页上的记录r进行加排他锁X，那么分别需要对数据库A、表、页加上意向锁IX，**最后才对记录r加上排它锁**



`InnoDB`中的意向锁即为**表级**的锁，支持两种意向锁：

+ **意向共享锁**——事务想要获取一张表中某几行的共享锁
+ **意向排他锁**——事务想要获得一张表中某几行的排他锁



**一致性非锁定读**

指`InnoDB`存储引擎通过**行多版本控制**的方式来当前执行时间数据库中行的数据

+ 如果读取的行正在执行`Delete`或`Update`操作，此时读取操作不会等待行上的锁释放，相反的，`InnoDB`存储引擎会去**读取行的一个快照数据**
+ 快照数据是指该行之前版本的数据，该实现是通过`undo`段来完成，`undo`用来在事务中回滚数据，快照数据本身是没有额外的开销，读取快照数据不用上锁，没有事务对历史数据进行修改操作
+ 并不是每个事务隔离级别下都采用非锁定一致性读，`InnoDB`存储引擎中的**读已提交**和**可重读**采用了**一致性非锁定读**，但对快照的定义不同。
  + `read committed`隔离级别，对于快照数据，非一致性读**总是读取被锁定行的最新一份快照数据**
  + `repeatable read`隔离级别下，对于快照数据，非一致性读总是读取**事务开始时的行数据版本**
+ `InnoDB`存储引擎默认使用一致性非锁定读，但也支持一致性的锁定读操作
  + `select ... for update` —— 对读取的行记录加1个排它锁X，其它事务不能对已锁定的行加上任何锁
  + `select ... lock in share mode`——对读取的行记录加上1个共享锁S
  + 两种一致性锁定读**必须在一个事务中**，事务提交时，锁也就释放了，务必加上`begin,start transaction或者set autocommit=0`

![](https://github.com/HurricanGod/Home/blob/master/redis/img/%E9%9D%9E%E9%94%81%E5%AE%9A%E4%B8%80%E8%87%B4%E6%80%A7%E8%AF%BB.png)



![](https://github.com/HurricanGod/Home/blob/master/redis/img/%E9%9D%9E%E9%94%81%E5%AE%9A%E4%B8%80%E8%87%B4%E6%80%A7%E8%AF%BB.gif)

-----


**自增长与锁**

`InnoDB`存储引擎的内存结构中，对每个含有自增长值的表都有1个**自增长计数器**，对含有自增长计数器的表插入时，计数器会被初始化，使用如下语句获取计数器的值：

```sql
select max(auto_inc_col) from t for update
```

插入操作会根据自增长计数器的值加1后获取下一个主键，这种实现方式叫做`auto-inc locking` ，采用了特殊的表锁机制，为了提高插入性能，锁不是在一个事务完成后释放，而是在自增长值插入的sql语句后立即释放

`auto-inc locking`实现自增长存在的问题：

+ 并发插入性能较差，事务必须等待前一个插入完成





`Mysql 5.1.22`版本后，`InnoDB`存储引擎提供一种**轻量级互斥量**的自增长实现机制，提供了一个参数`innodb_autoinc_lock_mode`来控制自增长的模式



### 行锁算法

+ `Record Lock`——单个行记录上的锁
  + `Record Lock`总是锁住索引记录，如果建表时没有设置索引会使用隐式的主键进行锁定
+ `Gap Lock`——间隙锁，锁定一个范围，但不包含记录本身
+ `Next-Key Lock`——锁定一个范围并锁定记录本身
  + 对行查询都是都是采用这种算法，例如1个索引有10,11,13,20，该索引可能被`Next-Key Locking`的区间为：`(-∞,10],(10,11],(11,13],(13,20],(20,+∞]`
  + 当查询的索引含有唯一索引时，`InnoDB`存储引擎会对`Next-Key Lock`进行优化，将其降级为`Record Lock`，即**查询的列是唯一索引**的情况下，锁住索引本身，而不是范围
  + 若唯一索引有多个列组成，而查询仅是查找多个唯一索引列中的其中一个。查询其实是`range`类型查询





![唯一索引的锁定示例](https://github.com/HurricanGod/Home/blob/master/redis/img/next-key-locking.gif)



|  时间  |                   会话A                    |                  会话B                   |
| :--: | :--------------------------------------: | :------------------------------------: |
|  1   |                 `begin;`                 |                                        |
|  2   | `select * from next_key_table where a=5 for update;` |                                        |
|  3   |                                          |                `begin;`                |
|  4   |                                          | `insert into next_key_table select 4;` |
|  5   |                                          |          `commit` #成功提交，不会被阻塞          |
|  6   |                 `commit`                 |                                        |



`InnoDB`存储引擎采用`next-key locking`机制避免**幻读**问题，**幻读**指：**同一个事务下，连续执行两次同样的SQL语句可能导致不同的结果，第二次的SQL可能返回之前不存在的行**





**幻读演示**：

| 时间   | 会话A                                      | 会话B                                      |
| ---- | ---------------------------------------- | ---------------------------------------- |
| 1    | `set session tx_isolation='READ-COMMITTED';` |                                          |
| 2    | `begin`                                  |                                          |
| 3    | `select * from  next_key_table where a>2 for update;`（事务A此时读取出的数据只有两条，先不提交事务） |                                          |
| 4    |                                          | `begin`                                  |
| 5    |                                          | `insert into next_key_table select 4;`（往表里插入1条记录） |
| 6    |                                          | `commit;`                                |
| 7    | `select * from  next_key_table where a>2 for update;`（事务A再次查询发现多了1行，这种现象称为幻读） |                                          |

![幻读演示](https://github.com/HurricanGod/Home/blob/master/redis/img/%E5%B9%BB%E8%AF%BB%E6%BC%94%E7%A4%BA.gif)

`InnoDB`采用**Next-Key Locking**算法避免幻读，它锁住的并不是一行或者几行记录，而是锁住一个范围，如演示中`InnoDB`存储引擎锁的不是**a=3和a=5**那两行记录，而是锁住**大于2**的所有记录，对于**插入id大于2**的记录都不被允许。



可以通过`InnoDB存储引擎`的**next-key locking**机制在应用层面实现唯一性检查：

+ 用户通过索引查找一个值并对该行加上共享锁，即使查询的值不存在，其锁定也是一个范围
  + 若没有返回任何行，新插入的值一定是唯一的
  + 若执行加共享锁的`lock in share mode`操作时出现并发问题，最终也会只有1个事务成功执行，其它事务抛出死锁错误

```sql
select * from table where col=xxx lock in share mode
```





**默认情况下InnoDB存储引擎不会回滚超时引发的错误异常**，不过若**发现死锁**，`InnoDB`存储引擎会马上**回滚事务**

<a href="#InnodbLock">InnoDB存储引擎中的锁</a>


