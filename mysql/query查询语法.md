

## 数据查询

**select** 语句一般格式为：

```sql
select [distinct] <目标列1> [,<目标列1>...]
from [表名|视图名]
where <条件表达式>
group by <列名> [having <条件表达式>]
order by <列名> [asc|desc] [,<列名> [asc|desc]...]
```



+ **查询语句**根据`where`子句中的条件表达式从`from`指定的表或视图中找出满足条件的元组，再按照`select`子句中目标列表达式选出元组的属性形成结果表
+ 如果有`group by` 子句，则按`group by` 指定列名的属性值进行分组，**属性值**相等的元组为1个组，`group by`子句必须放在`where` 子句后面
+ 如果`group by` 子句带有`having`短语，则满足跟在`having`短语后面**条件表达式**的元组才会输出
+ `order by` 子句根据列名进行排序后输出元组

---

+ 如果需要**消除结果中重复的元组**，可以使用`distinct` ， `distinct`必须放在**被查询列的最前面** ，注意：`distinct`是对**所有目标列相同**的元组去重，而不是对某一目标列去重
+ **字符匹配**——`[not] like '<匹配串>'`
  + **匹配串** 可以是字符串，也可以包含通配符`%`或`_`
  + 当需要表示`%`或`_`原本符号意思时需要对通配符进行转义，具体语法为`[not ] like `  `字符<待换码的通配符>字符` `escape <换码字符>` ；`like 'name?_%' escape '?'`代表以`name_`开头的字符串
+ **聚集函数**遇到空值时，除`count(*)`外都跳过空值处理非空值；`where` 子句中不能使用聚集函数作为**条件表达式**， **聚集函数**只能用于`select`、`group by`中的`having`子句
  + 如果使用了`group by`子句，聚集函数将作用与每一个分组，每一个分组都会有1个函数值，未对查询结果进行分组则作用于**整个查询结果**
+ `where`子句与`having`短语的区别：
  - `where` 子句作用于基本表或视图，从中选择满足条件的元组
  - `having`短语作用于组，从中选择满足条件的组
+ **连接查询**
  - **自然连接** ：在等值连接的基础上把目标列**重复的属性去掉**则为**自然连接**
  - **自身连接** ：一个表与其自己进行连接称为**自连接**
  - **外连接** ： 
    - 语法：`select <目标列> from 表1 left outer join 表2 on (关系表达式)`
    - 左外连接语法如上所示，**表1**中不满足条件的元组将会`null`显示出来
+ **嵌套查询**  ，子查询的`select`语句不能使用`order by`子句，`order by`子句只能对**最终查询结果**进行排序
 
如果想要对子查询结果进行排序可以考虑把子查询转换为临时表，再从临时表中查数据
**示例**：
```sql
select ConferenceInfo.id,startdate,cnName,enName
from ConferenceInfo
where id in ( 
	select * 
	from
	(
		select conferenceid
		from FontConference
		group by(conferenceid)
		order by count(conferenceid) desc
		limit 4
	)as t
) 
AND	startdate > '2017-10-29'
order by startdate 
```
 



----

### 实战

有表**student**和**course**，表定义如下：

```sql
 create table `course` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `sid` int(10) unsigned DEFAULT NULL,
  `subject` varchar(64) DEFAULT NULL,
  `score` decimal(10,0) DEFAULT NULL,
  primary key (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
```



```sql
 CREATE TABLE `student` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `sno` int(10) unsigned DEFAULT NULL,
  `sname` varchar(32) DEFAULT NULL,
  primary key (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
```

![](https://github.com/HurricanGod/Home/blob/master/img/mysql-select1.png)

-----

1. **查询每个学生各科总分，分数按照从高到低显示**

   ```sql
   select sno,sum(score)
   from student,course
   where sno = sid
   group by (sno)
   order by sum(score) desc;
   ```

   **注意点** ：`order by`可以根据表中**任何字段**排序，**对非目标列进行** `order by`排序是允许的

   ![](https://github.com/HurricanGod/Home/blob/master/img/mysql-select2.png)

2. **查询每个学科最高分及对应的学生名字，按学科名升序排列**

   ```mysql
   select sname,max(score)
   from student,course
   where sno = sid
   group by `subject`
   order by `subject` asc
   ```

   **注意点** ：`group by`先对查询结果进行分组，分组后**聚集函数**作用于每一个分组得到1个函数值

   ![](https://github.com/HurricanGod/Home/blob/master/img/mysql-select3.png)

3. **查询前三名和倒数前三名的学生的学科总分及学生名字**

   ```mysql
   (
   select sname,sum(score)
   from student,course
   where sno = sid
   group by sno
   order by sum(score) DESC
   LIMIT 0,3
   )
   union 
   (
   select sname,sum(score)
   from student,course
   where sno = sid
   group by sno
   order by sum(score) ASC
   LIMIT 3
   )
   ```

   **注意点** ：

   + 这里使用`union`对查询出来前三名和倒数前三名进行**并操作** ，使用`limit`限制结果个数
     + `limit`后面如果**只跟着1个整数n**则表示取查询结果的前n条数据
     + `limit`后面若跟着**两个整数m,n**则表示取查询出来的结果中从第m条开始的n条数据
     + `limit`默认是从0开始的
   + 使用`union`进行**集合交**运算时，如果查询语句里有`order by`子句的需要把**整个查询语句**都括起来，否则会出错

4. **把所有“李”姓中非“明”结尾的成绩减3，所有姓名以“明”结尾的非“李”姓成绩加2**

   ```mysql
   update course,student
   set score = score -3
   where sid = sno 
   and sname like '李%' 
   and sname not like '李%明';

   update course,student
   set score = score +2
   where sid = sno 
   and sname like '%明'
   and sname not like '李%明';
   ```

   **注意点**：`update`语句如果更新的属性需要与别的表关联，语法上与`select`语句差不多

