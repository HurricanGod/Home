# <a name="top">高级特性</a>

+ <a href="#insert">**insert高级语法**</a>




----
<a name="table_define">**样例中的表结构定义**</a>
```mysql
CREATE TABLE `exception_log` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `business_name` varchar(128) DEFAULT NULL,
  `exception_name` varchar(128) DEFAULT 'unknow exception' COMMENT '异常名',
  `exception_stack` varchar(2048) DEFAULT NULL COMMENT '异常堆栈',
  `detail` varchar(512) DEFAULT NULL COMMENT '异常描述',
  `last_update_time` datetime DEFAULT NULL COMMENT '最近更改时间',
  PRIMARY KEY (`id`),
  KEY `exception_name__index` (`exception_name`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='异常日志表'
```

------
## <a name="insert">insert高级语法</a>
+ 使用 `set` 语法进行插入
```mysql
# DEFAULT 表示字段定义时的默认值
insert into exception_log set last_update_time=now(),exception_name=DEFAULT;
```
<br/>
+ 一次性插入多个值

```mysql
insert into 
exception_log(detail,last_update_time) values
('test2',now()),
('test3', now()),
('test4', now());
```

<br/>

+ 将查询结果作为插入的值
**语法** ： `insert into table_name(field0, field1) select field0, field1 from table_name [ where condition]`

```mysql
insert into 
exception_log(exception_name,last_update_time) 
select exception_name,last_update_time  
from exception_log 
where id=10
;
```


<p align="right"><a href="#insert">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回顶部</a></p>

----
