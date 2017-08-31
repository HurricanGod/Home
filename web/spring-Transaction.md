## spring事务管理

****

Spring事务管理的方式：

+ 编程式事务管理


+ 声明式事务管理
  + 基于xml的配置文件实现
  + 基于注解实现

-----

**事务管理的api：**

Spring事务管理高层抽象主要包括3个接口

- PlatformTransactionManager（事务管理器）
- TransactionDefinition（事务定义信息：隔离、传播、超时、只读）
- TransactionStatus（事务具体运行状态）





**1.**配置事务管理器（xml方式）

```xml
<bean id="transactionManager"
  class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
  <property name="dataSource" ref="dataSource"></property>
</bean>
```

**2.**配置事务增强

```xml

```

**3.**配置AOP切面产生代理

```xml
 
```



------

**注解方式配置事务管理**

1. 配置事务管理器

   ```xml
   <bean id="transactionManager"  class="org.springframework.jdbc.datasource.DataSourceTransactionManager">  
   <property name="dataSource" ref="dataSource"></property>
   </bean>
   ```

   ​

2. 配置事务注解

   ```xml
   <tx:annotation-driven 
     transaction-manager="transactionManager" />
   ```

   ​

3. 在要使用事务的方法所在类上面添加注解

