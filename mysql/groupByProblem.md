## Mysql中Group By查询问题



![表](https://github.com/HurricanGod/Home/blob/master/mysql/img/groupby1.png)

![出错](https://github.com/HurricanGod/Home/blob/master/mysql/img/groupby2.png)

![解决](https://github.com/HurricanGod/Home/blob/master/mysql/img/groupby3.png)

---

相信在进行`group by` 分组查询时一定遇到过如下查询错误：

**this is incompatible with sql_mode=only_full_group_by（与sql_mode = only_full_group_by不兼容）**

----

要解决这个问题需要先了解`sql_mode`，使用`select @@sql_mode` 查看**mysql**的`sql_mode`，查询结果如下：

```
only_full_group_by,strict_trans_tables,no_zero_in_date,no_zero_date,error_for_division_by_zero,no_auto_create_user,no_engine_substitution
```



查看**sql_mode**后发现一个叫做`only_full_group_by` 模式；在`only_full_group_by` 模式下，使用**select**查询的目标列的所有列的值都是明确语义，简单的说**目标列**中的值要么是来自**聚集函数**的结果，要么来自**group by**表达式的值

因此，在`only_full_group_by` 模式下如果一个查询 **group by** 的列**不是所有目标查询列** 将会出现 **sql_mode 不兼容**的错误



-----

**对已经创建好的表，可以使用下面命令去除** `only_full_group_by`  **模式**

```mysql
set sql_mode ='strict_trans_tables,
				no_zero_in_date,no_zero_date,
				error_for_division_by_zero,no_auto_create_user,
				no_engine_substitution';
```

**注意** ：`set sql_mode`设置**sql_mode**模式时字符串**不能换行或中间包含空格**



----

去除 `only_full_group_by`  **模式全局设置**

```mysql
set @@sql_mode ='	strict_trans_tables,
				no_zero_in_date,no_zero_date,
				error_for_division_by_zero,no_auto_create_user,
				no_engine_substitution';
```

