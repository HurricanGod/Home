## 事物

sql  标准定义了4种事务隔离级别，**低级别的隔离级**一般支持更高的并发处理，并拥有更低的系统开销

----

<a name="level">**数据库 4 种隔离级别**：</a>

+ **读取未提交内容** —— <a href="#read_uncommited">`read uncommited`</a>
+ **读取提交内容** ——<a href="#read_commited"> `read commited`</a>
+ **可重读** —— <a href="#repeatable_read"> `repeatable read`</a>
+ **可串行化** —— <a href="#serializable">`serializable`</a>



### **读取未提交内容** ——<a name="read_uncommited">`read uncommited`</a>

在 `read uncommited` 隔离级别，所有事务都可以 ‘看到’ 未提交的事务的执行结果，**读取未提交数据也被称为脏读**

<a href="#level">**back**</a>

-----

### **读取提交内容** ——<a name="#read_commited"> `read commited`</a>

大部分数据库默认的隔离级别为`read commited` ，这种隔离级别支持  **不可重复读** ，意味着*用户运行同一个语句两次，看到的结果是不同的*

<a href="#level">**back**</a>

---

### **可重读** —— <a name="#repeatable_read"> `repeatable read`</a>

`repeatable read` 确保同一个事务的多个实例在并发读取数据时会看到同样的数据行，会有**幻读**问题。**可重读** 是 `mysql`  默认的事务隔离级别

<a href="#level">**back**</a>

-----

### **可串行化** —— <a name="#serializable">`serializable`</a>

最高级别的隔离级别，通过强制事务排序，使之不能相互冲突从而解决幻读问题， **可串行化** 是在每个读的数据行上加锁；在这个级别可能导致大量的超时现象和锁竞争现象

<a href="#level">**back**</a>



**各种隔离级别特点**

| 隔离级  | 脏读可能性 | 不可重复读可能性 | 幻读可能性 | 加锁读  |
| :--: | :---: | :------: | :---: | :--: |
| 读未提交 |   是   |    是     |   是   |  否   |
| 读提交  |   否   |    是     |   是   |  否   |
| 重复读  |   否   |    否     |   是   |  否   |
| 可串行化 |   否   |    否     |   否   |  是   |

----



## 死锁

`InnoDB`处理死锁的方法是：**回滚拥有最少排它行级锁的事务**

---

## Mysql 中的事务



**自动提交** ：`Mysql`默认操作模式是**AutoCommit** 模式，除非显式的开始一个事务，否则它将每一个查询视为一个单独事务自动执行

`Mysql`允许使用`set transaction isolation level` 命令设置隔离级别



**InnoDB** 使用二相锁定协议，一个事务在执行过程中的任何时候都可以获取锁，但只有执行`commit` 或 `rollback` 后才会释放锁