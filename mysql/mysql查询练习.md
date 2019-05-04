最近看了关于SQL相关的面试题，对于我这种2年没写复杂SQL语句的渣渣来说实在有点脑阔疼，题目内容大概为：

1. 查询每个班级成绩最高的学生及其成绩
2. 查询每个班级成绩第二的学生及其成绩
3. 查询每个班级总分前三的学生



> 第一个查询一看就比较简单，难就难在第2和3个查询，因为分组后没有取第n或前n条数据的函数



由于是面试题，没有表结构，所以就随意脑补了一下建表代码：

```sql
CREATE TABLE `t_user_score` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(16) DEFAULT NULL,
  `course_name` varchar(32) DEFAULT NULL,
  `score` double DEFAULT NULL,
  `class_name` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```



为了练习查询往表中插入如下数据：

```mysql
INSERT INTO `t_user_score` VALUES ('1', '张三', '数学', '34', 'a');
INSERT INTO `t_user_score` VALUES ('2', '张三', '语文', '58', 'a');
INSERT INTO `t_user_score` VALUES ('3', '张三', '英语', '58', 'a');
INSERT INTO `t_user_score` VALUES ('4', '李四', '数学', '45', 'a');
INSERT INTO `t_user_score` VALUES ('5', '李四', '语文', '87', 'a');
INSERT INTO `t_user_score` VALUES ('6', '李四', '英语', '45', 'a');
INSERT INTO `t_user_score` VALUES ('7', '王五', '数学', '76', 'a');
INSERT INTO `t_user_score` VALUES ('8', '王五', '语文', '34', 'a');
INSERT INTO `t_user_score` VALUES ('9', '王五', '英语', '89', 'a');
INSERT INTO `t_user_score` VALUES ('10', 'Tony', '语文', '86', 'b');
INSERT INTO `t_user_score` VALUES ('11', 'Tony', '数学', '95', 'b');
INSERT INTO `t_user_score` VALUES ('12', 'Tony', '英语', '92', 'b');
INSERT INTO `t_user_score` VALUES ('13', 'Jack', '数学', '56', 'b');
INSERT INTO `t_user_score` VALUES ('14', 'Jack', '语文', '95', 'b');
INSERT INTO `t_user_score` VALUES ('15', 'Jack', '英语', '96', 'b');
INSERT INTO `t_user_score` VALUES ('16', 'Dong', '数学', '93', 'b');
INSERT INTO `t_user_score` VALUES ('17', 'Dong', '语文', '99', 'b');
INSERT INTO `t_user_score` VALUES ('18', 'Dong', '英语', '94', 'b');
INSERT INTO `t_user_score` VALUES ('19', 'God', '数学', '100', 'c');
INSERT INTO `t_user_score` VALUES ('20', 'God', '语文', '89', 'c');
INSERT INTO `t_user_score` VALUES ('21', 'God', '英语', '99', 'c');
INSERT INTO `t_user_score` VALUES ('22', 'HurricanGod', '数学', '96', 'c');
INSERT INTO `t_user_score` VALUES ('23', 'HurricanGod', '语文', '79', 'c');
INSERT INTO `t_user_score` VALUES ('24', 'HurricanGod', '英语', '80', 'c');
INSERT INTO `t_user_score` VALUES ('25', 'Mary', '数学', '66', 'c');
INSERT INTO `t_user_score` VALUES ('26', 'Tom', '数学', '89', 'c');
```



表结构数据脑补完毕后开始实现查询功能：

+ **查询每个班级成绩最高的学生及其成绩**

  ```mysql
  select class_name, max(score), user_name
  from t_user_score
  where course_name = '数学'
  group by class_name
  ```

  ​

+ **查询每个班级成绩第二的学生及其成绩**

  我们以数学成绩为例，实现时参考了网上主流的做法（**方法1**）以及自己偏好使用的`join`连接实现。

  **方法1**的SQL如下：

  ```mysql
  select A.id, A.user_name, A.class_name, min(A.score)
  from t_user_score as A
  where A.course_name = '数学' and (
  	select count(*)
  	from t_user_score as B
  	where B.course_name = '数学' 
  	and A.class_name = B.class_name
  	and A.score < B.score
  ) <=1
  group by A.class_name 
  order by A.class_name, A.score 
  ;
  ```

  + 该方法采用相关子查询的方式来实现，相当于**两个for循环**，课程名为**数学**的记录若为n，则该复杂度为`O(n^2)`，这种SQL估计只会在面试中出现
  + 对于每一条课程名为**数学**的记录，查找同个班级同课程情况下比该记录分数高的记录条数k，`k=0`表示该记录为该班级该科目中的最高分，`k=1`表示次高分......

  **方法2**：

  ```mysql
  select A.id, A.user_name, A.class_name,A.score
  from t_user_score as A
  inner join t_user_score as B 
  on A.class_name = B.class_name
  and A.course_name = B.course_name 
  and A.score < B.score
  where A.course_name = '数学'
  group by  A.class_name, A.user_name
  having count(*) = 1
  ;
  ```

  + **方法2**采用内连接自己的方式来实现，关键条件为：`A.score < B.score`，同个班级的数学成绩，排名第K**内连接后**就会有`K-1`条记录
  + `having count(*) = 1`表示取排名第二的记录
  + `having count(*) <= K` 表示取每个班的第两名到`K+1`名的记录


+ **查询每个班级总分前三的学生**

  ```mysql
  select *
  from (select A.user_name, A.class_name, sum(A.score) as total
  from t_user_score as A
  group by  A.class_name, A.user_name) as t
  where (
  	select count(*)
  	from (select A.user_name, A.class_name, sum(A.score) as total
  				from t_user_score as A
  				group by  A.class_name, A.user_name) as tt
  	where tt.class_name = t.class_name 
  	and tt.total > t.total
  ) < 3
  order by class_name, total desc 
  ;
  ```

  `ps`：写过·最烂的SQL，虽然以后还有更烂的。。。

  + 实现时参考了第二个查询的方法1









