## 基于注解的DI

+ `@Component` 注解一般在**JavaBean**中使用
+ `@Repository` 注解一般在**DAO**实现类上使用
+ `@Controller` 注解在**SpringMVC**处理器中使用
+ `@Service` 注解一般放在**Service**的实现类上

**注**这4个注解功能一样！

-----

`@Scope`注解为**Bean的作用域**，有5种，默认为`singleton` ，5种类型分别为：

+ **singleton**
+ **prototype**
+ **request**
+ **session**
+ **globalSession**



要使用注解的方式需要在`<beans/>`里添加`context`约束，并添加`<context:component-scan />`标签指定扫描的包；添加的约束内容如下：

```xml
xmlns:context="http://www.springframework.org/schema/context"
xsi:http://www.springframework.org/schema/context/spring-context.xsd
```

**整个xml配置如下：**

```xml
<?xml version="1.0" encoding="utf-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd
        http://www.springframework.org/schema/tx
        http://www.springframework.org/schema/tx/spring-tx.xsd">
    <bean id="c3p0Datasource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="driverClass" value="com.mysql.jdbc.Driver"/>
        <property name="jdbcUrl" value="jdbc:mysql://localhost:3306/conference"/>
        <property name="user" value="root"/>
        <property name="password" value="qwer123456"/>
    </bean>

    <context:component-scan base-package="hurrican.*"/>

</beans>
```



-----

+ **byType**类型的域属性注入（使用`@Autowired` 自动注入）

​	![](https://github.com/HurricanGod/Home/blob/master/web/spring_img/annotation-DI/byType-1.png)
       ![](https://github.com/HurricanGod/Home/blob/master/web/spring_img/annotation-DI/byType-2.png)
       ![](https://github.com/HurricanGod/Home/blob/master/web/spring_img/annotation-DI/byType-3.png)

+ **byName** 类型的域属性注入（使用`@Autowired`  + `@Qualifier`自动注入）

​	![](https://github.com/HurricanGod/Home/blob/master/web/spring_img/annotation-DI/byName-1.png)

+ **@Resource** 注解式注入



​	

-----

**创建Bean时XML配置要比注解的优先级高**

