## MyBatis

### MyBatis 与 Hibernate

Hibernate提供全面的数据封装机制，是**全自动的**ORM，实现POJO和数据库表之间的映射，以及SQL自动生成和执行

MyBatis是**半自动的**ORM框架，不会自动生成SQL语句，需要自己编写，通过SQL语句映射文件将SQL所需的参数以及返回结果字段映射到指定POJO



**MyBatis特点**

1. 在xml中配置SQL语句实现SQL语句与代码分离，给维护带来便利
2. 可以结合数据库自身特点灵活控制SQL语句，具有更高的查询效率