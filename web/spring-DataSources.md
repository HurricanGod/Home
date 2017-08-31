## Spring数据源及web项目原理

-----

**Ioc底层原理：**

1. xml配置文件
2. dom4j解析xml配置文件
3. 工厂设计模式
4. 反射


------

**IOC与DI的区别：**

+ **IOC**为控制反转，把对象创建交给spring进行配置
+ **DI**为依赖注入，向类里面的属性中设置值

**关系：**依赖注入不能单独存在，需要在ioc基础之上完成

-----

**Web项目原理：**把加载配置文件和创建对象过程在服务器启动时完成。

- 在服务器启动时，为每个项目创建一个`ServletContext`对象，在`ServletContext`对象创建时，使用监听器可以具体到`ServletContext对象`在什么时候创建
- 使用**监听器**监听到ServletContext对象创建时候加载Spring配置文件，把配置文件配置的对象创建出来
- 把创建出来的对象放到ServletContext域对象里面（使用`setAttribute()`方法），获取对象的时候，到ServletContext域得到（使用`getAttribute()`方法）


----

### 数据源配置

**spring**主配置文件`application.xml`下数据源配置

1. 使用**c3p0数据源** `ComboPooledDataSource`

   + 使用**properties文件配置式的c3p0**数据源需要在`spring`配置文件里需要先引入`context`约束，**location**属性填写`properties文件`的位置

   ```properties
   jdbc.driver = com.mysql.jdbc.Driver
   jdbc.url = jdbc:mysql://localhost:3306/conference
   jdbc.user = root
   jdbc.password = qwer123456
   ```

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns:context="http://www.springframework.org/schema/context"
          xsi:schemaLocation="
          http://www.springframework.org/schema/beans
          http://www.springframework.org/schema/beans/spring-beans.xsd
          http://www.springframework.org/schema/context
          http://www.springframework.org/schema/context/spring-context.xsd">
          <!-- c3p0数据源(properties文件配置)-->
   	   <!--注册properties属性文件-->
          <context:property-placeholder location="jdbc.properties"/>
          <bean id="c3p0Datasource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
                 <property name="driverClass" value="${jdbc.driver}"/>
                 <property name="jdbcUrl" value="${jdbc.url}"/>
                 <property name="user" value="${jdbc.user}"/>
                 <property name="password" value="${jdbc.password}"/>
          </bean>
          <!-- c3p0数据源(直接配置)-->
          <!--
          <bean id="c3p0Datasource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
                 <property name="driverClass" value="com.mysql.jdbc.Driver"/>
                 <property name="jdbcUrl" value="jdbc:mysql://localhost:3306/conference"/>
                 <property name="user" value="root"/>
                 <property name="password" value="qwer123456"/>
          </bean>
          -->
   </beans>
   ```

2. **DBCP**数据源 `BasicDataSource`

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns:context="http://www.springframework.org/schema/context"
          xsi:schemaLocation="
          http://www.springframework.org/schema/beans
          http://www.springframework.org/schema/beans/spring-beans.xsd
          http://www.springframework.org/schema/context
          http://www.springframework.org/schema/context/spring-context.xsd">

         <!-- DBCP数据源——BasicDataSource -->
          <bean id="basicDataSource" class="org.apache.commons.dbcp2.BasicDataSource">
                 <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
                 <property name="url" value="jdbc:mysql://localhost:3306/conference"/>
                 <property name="username" value="root"/>
                 <property name="password" value="qwer123456"/>
          </bean>
   </beans>

   ```

3. 使用**spring**内置数据源

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns:context="http://www.springframework.org/schema/context"
          xsi:schemaLocation="
          http://www.springframework.org/schema/beans
          http://www.springframework.org/schema/beans/spring-beans.xsd
          http://www.springframework.org/schema/context
          http://www.springframework.org/schema/context/spring-context.xsd">

          <!-- spring框架内置数据源-->

         <bean id="innerDataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
                 <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
                 <property name="url" value="jdbc:mysql://localhost:3306/conference"/>
                 <property name="username" value="root"/>
                 <property name="password" value="qwer123456"/>
          </bean>
   </beans>
   ```