## m阶B树



**特点：**

1. 根节点至少有2个子节点
2. 除根节点、失败节点外，每个节点至少有⌈m/2⌉个子女（即⌈m/2⌉个指针），***最多有m个子女，m-1个关键码***
3. 所有失败节点都位于同一层

B树的搜索时间与B树的阶数m和B树的高度h直接有关。

`失败节点：`是虚拟出来的节点，指向失败节点的指针为空

B树至少是半满的，层数较小而且完全平衡。还有1个重要特点是**B树的节点大小可以和磁盘块大小相同**

```
设q = ⌈m/2⌉,B树各层节点数为：
第1层有1个节点
第2层至少有2个节点
第3层至少有2*q（每个节点至少有⌈m/2⌉个子女，而第2层至少有2个）
第4层至少有2*q*q个节点   
```

<br/> 
`缺点：`因为B树最少要保持半满，所以在最坏情况下会浪费50%的空间   

-----

### B树的插入

![B树的插入](https://github.com/HurricanGod/Home/blob/master/img/B%E6%A0%91%E6%8F%92%E5%85%A5.png)



---

### B树删除

+ 若在`B树`上删除的**关键码不是叶节点**，设被删除关键码为k，删除关键码k后应该以该节点p所指向的**子树中的最小关键码x代替被删除的关键码k，然后在x所在的叶节点删除x**（`关键码x肯定是在叶节点`）

![B树的删除](https://github.com/HurricanGod/Home/blob/master/img/B%E6%A0%91%E5%88%A0%E9%99%A4.png)

-----

## B+树

结构定义：

1. 每个节点最多有m棵子树（子节点）
2. 根节点最少有1棵子节点，除根节点外至少有⌈m/2⌉个子节点
3. 有n(**n<=m**)个子节点必有n个关键码
4. 所有叶节点在同一层，关键码按从小到大顺序排列，每个叶节点相互链接在一起
5. 所有非叶节点是叶节点的索引，**关键码与指向子节点的指针构成索引项**



**注意**

B+树的构造一般自下而上，**只有叶节点才存放着对实际数据记录的索引，即记录的实际存储地址**

B+树一般有两个头指针，一个指向B+树的根节点，另一个指向关键码最小的叶节点，B+树可以进行两种搜索运算：

1. 从叶节点开始的顺序搜索
2. 从根节点开始到叶节点自上而下的随机搜索

### B+树的插入过程
![B+树的插入](https://github.com/HurricanGod/Home/blob/master/img/B%2B%E6%A0%91%E6%8F%92%E5%85%A5.png)



------

### B-Tree索引

`B-Tree`索引的数据存储是有序的，每个叶节点到跟的距离一样（InnoDB使用B+ Tree索引结构），由于根节点保存了指向子节点的指针，所以可以从根节点出发查找数据

索引保存数据的顺序等于``create table``命令中的列顺序

```sql
create table testindex(
	id int auto_increment,
  	name varchar(16) not null,
	age int default 0,
	gender varchar(3) not null,
  	address varchar(64) not null,
	primary key(id)
) engine=InnoDB;
create index testindex_name on testindex(name);
create index testindex_address_name on testindex(address,name);
insert into testindex(name,age,gender,address) 
values('李封',58,'男','四川省成都市');
```



```sql
create table ordertable(
  	tradetime date,
  	orderid int,
  	trademoney float not null,
  	decription varchar(128),
  	primary key(orderid, tradetime)
);
insert into ordertable
values('2016-12-25',1,100.5,'电费');
insert into ordertable
values('2017-6-1',2,10.5,'外卖');
```



**B-Tree**的局限：

+  如果查找没有从索引列最左边开始，B树索引没什么用处
   + 例1：执行``select * from testindex where age=18``查询并不会使用到索引
   + 例2：执行``select * from ordertable where tradetime ='2016-12-25'``查询并不会使用索引加快查询，因为主键为`orderid, tradetime`两个字段，mysql为主键建立B+树索引，orderid排在前面，自然是根据orderid为前缀进行查询，where查询时`tradetime`与`orderid`类型不一样，查询引擎会放弃使用索引查询直接全表查询；如果执行的是``select * from ordertable where orderid =1;``则会使用到索引加快查询
   + 例3：执行``select * from testindex where name like '%封';``查询并不会使用到索引进行优化查询
+  不能跳过索引中的列
+  存储引擎不能优化访问任何在第一个范围条件右边的列
   + 例1：执行`select * from testindex where address like '广东省%' and name ='李封'`查询时，由于有多列索引**(address, name)**,但查询语句where中第1个条件为表示范围的模糊查询，后面的条件即便有索引查询也不能优化



### 哈希索引

建立在哈希表的基础上，只对使用了索引中的每一列精确查找有用，对于每一行，存储引擎计算出被索引列的哈希码，把哈希码保存到索引中，并且保存1个指向哈希表中的指针。

```sql
create table testhash(
id int,
name varchar(16) not null,
age int,
gender varchar(3),
key using hash(name)
) ENGINE=MEMORY;

mysql> select * from testhash;
+------+--------+------+--------+
| id   | name   | age  | gender |
+------+--------+------+--------+
|    1 | 张三   |   19 | 男     |
|    2 | 晓琳   |   21 | 女     |
|    3 | 李四   |   25 | 男     |
|    5 | 西施   |   19 | 女     |
|    4 | 刘吉   |   40 | 男     |
+------+--------+------+--------+
```

若哈希函数为`hash()`并且有如下例子：

```hash(&#39;张三&#39;) = 2333
hash('张三') = 2333
hash('晓琳') = 8569
hash('李四') = 6954
hash('西施') = 2394
hash('刘吉') = 1024
```

**索引的数据结构为：**

| 数据片段 |         值          |
| :--: | :----------------: |
| 1024 | 指向testhash表row5的指针 |
| 2333 | 指向testhash表row1的指针 |
| 2394 | 指向testhash表row4的指针 |
| 6954 | 指向testhash表row3的指针 |
| 8569 | 指向testhash表row2的指针 |

```sql
select age from testhash where name='张三'
```

若执行上面查询语句，mysql会根据`'张三'`计算出哈希值2333，然后在索引中找到2333这个项对应的地址，从而找到正确的数据行。



**哈希索引的局限：**

+ 不能使用哈希索引进行排序
+ 哈希索引**不支持部分键匹配**，哈希码是根据被索引字段的全部值计算出来的，例：在(name,age)上建立索引，但查询时语句为`where name='张三'`是不会加快查询，索引不会起作用
+ 哈希索引不能加快范围查询，只支持`in()、=、<=>`的相等比较，不支持`>、<……`的比较
+ 哈希索引采用链地址法解决冲突问题，访问哈希索引的速度快，但如果碰撞率高就需要依次查询链表访问对应的行
+ 碰撞率高删除某一行时对索引的维护代价大



### 聚集索引

当表有聚集索引时，数据行实际保存在索引的叶子页上，即实际数据行保存和键值保存在一起；**每个表只能有1个聚集索引**，`InnoDB`按照主键进行聚集，如果没有定义主键则会尝试用唯一非空索引代替，如果没有唯一非空索引则会定义隐藏的主键进行聚集。



**聚集索引的缺点：**

+ 插入速度一种依赖于插入顺序
+ 更新聚集索引列代价昂贵，更新时需要把更新的行移动到新的位置
+ 建立在聚集索引上的表在插入新行、或行的主键被更新，该行必须被移动的时候会进行分页（即B+树的节点分裂），分页导致表占用更多的磁盘空间
+ 第二索引访问需要两次索引查找，因为第二索引保存了行的主键，而不是行指针

如果按照主键顺序插入到包含聚集索引的表里，每次都会插入到前一条记录的后面，当页面达到最大填充因子，便插入到新页，前面的页面近似全部填满，页面紧凑；但使用**UUID**作为聚集索引进行插入时，由于插入的主键是随机的，因此插入是无序的，每当插入1个主键就需要与插入页已插入的主键进行比较，比如用插入排序算法插入到合适的位置来维持有序，因此增加了排序插入的开销，索引移动的开销，导致**大量的随机IO**

**MySql不能在索引中执行like操作，只允许在索引中进行简单比较**

----

**覆盖索引** ：查询的目标列为索引包含的字段叫做覆盖索引

优点：

1. 索引记录远小于全行大小，如果只读取索引就能极大地减少数据访问量

**通常维护1个多列索引要比维护多个单列索引容易**

