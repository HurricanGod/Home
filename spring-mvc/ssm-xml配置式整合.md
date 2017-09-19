# <a name="ssm">ssm整合</a>



+ <a href="#">**Maven**依赖</a>
+ <a href="#web">**web.xml 配置文件**</a>
+ <a href="#spring">**spring 配置文件**</a>
+ <a href="#mybatis">**mybatis 配置文件**</a>
+ <a href="#dao">**dao接口**</a>
+ <a href="#service">**service层**</a>
+ <a href="#controller">**controller层**</a>
+ <a href="#interceptor">**拦截器**</a>
+ <a href="#bean">**Bean**</a>

----

<a name="web">**web.xml**配置的内容</a>

- 指定**Spring配置文件**的位置(设置`contextConfigLocation`的值) 
- 配置**ServletContext**监听器`ContextLoaderListener`
- 配置**SpringMVC**字符编码**过滤器**——`CharacterEncodingFilter`
- 配置**SpringMVC**的**中央调度器**——`DispatcherServlet`


----

<a name="web.xml">**web.xml配置内容**</a>

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
          http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
         version="3.0">
    <display-name>Archetype Created Web Application</display-name>

    <!-- 指定Spring配置文件的位置及名称-->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:spring-*.xml</param-value>
    </context-param>

    <!-- 注册ServletContext监听器-->
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

    <!-- 配置字符编码过滤器 -->
    <filter>
        <filter-name>characterEncodingFilter</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>

        <init-param>
            <param-name>encoding</param-name>
            <param-value>utf-8</param-value>
        </init-param>

        <init-param>
            <param-name>forceEncoding</param-name>
            <param-value>true</param-value>
        </init-param>

    </filter>
    <!-- 配置字符编码过滤器拦截路径 -->
    <filter-mapping>
        <filter-name>characterEncodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>


    <!-- 注册SpringMVC中央调度器-->
    <!-- 注意：即便在Spring配置文件里指定了加载Spring配置文件-->
    <!--       配置DispatcherServlet时同样需要重新指定spring配置文件的位置-->
    <servlet>
        <servlet-name>springMvcDispatcherServlet</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:spring-mvc.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>

    <!-- 配置DispatcherServlet拦截的请求-->
    <servlet-mapping>
        <servlet-name>springMvcDispatcherServlet</servlet-name>
        <url-pattern>*.do</url-pattern>
    </servlet-mapping>


    <welcome-file-list>
        <welcome-file>index.html</welcome-file>
    </welcome-file-list>
</web-app>
```

<a href="#ssm">**返回顶部**</a>

-----

<a name="spring">**spring 配置文件**</a>

为了方便管理配置，把**spring 配置文件**分为5部分：

+ <a href="#spring-db">`spring-db.xml` —— **用于配置数据源Datasource**</a>
+ <a href="#spring-mvc">`spring-mvc.xml`—— **用于配置 SpringMVC 的注解扫描，注解驱动开启，静态资源访问配置，拦截器等**</a>
+ <a href="#spring-mybatis">`spring-mybatis.xml` —— **主要配置会话工厂** `SqlSessionFactoryBean` 和 **扫描式动态代理** `MapperScannerConfigurer`</a>
+ <a href="#spring-service">`spring-service.xml` —— **主要配置Service层的bean** </a>
+ <a href="#spring-tx">`spring-tx.xml` —— **用于配置AOP配置式事务，主要的bean有** ：**数据源事务管理器** `DataSourceTransactionManager`</a>





---

<a name="spring-db">`spring-db.xml`</a>

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

    <!-- spring框架内置数据源-->

    <!--<bean id="innerDataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">-->
    <!--<property name="driverClassName" value="com.mysql.jdbc.Driver"/>-->
    <!--<property name="url" value="jdbc:mysql://localhost:3306/conference"/>-->
    <!--<property name="username" value="root"/>-->
    <!--<property name="password" value="qwer123456"/>-->
    <!--</bean>-->

    <!-- c3p0数据源(properties文件配置)-->
    <context:property-placeholder location="classpath:jdbc.properties"/>

    <bean id="c3p0Datasource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="driverClass" value="${jdbc.driver}"/>
        <property name="jdbcUrl" value="${jdbc.url}"/>
        <property name="user" value="${jdbc.user}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>

</beans>
```



<a href="#spring">**返回→spring 配置文件**</a>

---

<a name="spring-mvc">`spring-mvc.xml`</a>

```xml
<?xml version="1.0" encoding="utf-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/mvc
        http://www.springframework.org/schema/mvc/spring-mvc.xsd">


    <!-- SpringMVC默认异常处理器-->
    <!--<bean class="org.springframework.web.servlet.handler.SimpleMappingExceptionResolver">-->
    <!--<property name="defaultErrorView" value="/page/error.html"/>-->
    <!--</bean>-->

    <!-- 文件上传配置 -->
    <bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
        <property name="defaultEncoding" value="utf-8"/>
        <property name="maxUploadSize" value="2097152"/>
    </bean>


    <!-- 拦截器配置-->
    <!--
    <mvc:interceptors>
        <mvc:interceptor>
            <mvc:mapping path="/**"/>
            <bean class="拦截器全限定类名"/>
        </mvc:interceptor>
    </mvc:interceptors>
    -->
    <mvc:interceptors>
        <mvc:interceptor>
            <mvc:mapping path="/**"/>
            <mvc:exclude-mapping path="/user/login.do"/>
            <mvc:exclude-mapping path="/user/register.do"/>
            <bean class="cn.hurrican.interceptors.LoginInterceptor"/>
        </mvc:interceptor>
    </mvc:interceptors>

    <!-- 开启注解扫描-->
    <context:component-scan base-package="cn.hurrican.*"/>

    <!-- 开启注解驱动-->
    <mvc:annotation-driven />

    <!-- 把对静态资源访问请求映射到DefaultServletHttpRequestHandler上-->
    <mvc:default-servlet-handler />

</beans>
```



<a href="#spring">**返回→spring 配置文件**</a>

----

<a name="spring-mybatis">`spring-mybatis.xml` </a>

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

    <!-- 创建-SqlSessionFactory对象 -->
    <bean id="mysqlSqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="configLocation" value="classpath:mybatis.xml" />
        <property name="dataSource" ref="c3p0Datasource" />
    </bean>


    <!-- 扫描式动态代理-->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="sqlSessionFactoryBeanName" value="mysqlSqlSessionFactory"/>
        <property name="basePackage" value="cn.hurrican.dao"/>
    </bean>


</beans>
```



<a href="#spring">**返回→spring 配置文件**</a>

----

<a name="spring-service">`spring-service.xml` </a>

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

    <bean id="accountMsgService" class="cn.hurrican.services.AccountMsgService">
        <property name="dao" ref="accountMsgDao"/>
    </bean>

    <bean class="cn.hurrican.services.UserService" id="userService">
        <property name="dao" ref="userDao"/>
    </bean>
</beans>
```



<a href="#spring">**返回→spring 配置文件**</a>

----

<a name="spring-tx">`spring-tx.xml`</a>

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

    <!-- 注册事务管理器 -->
    <bean id="transactionManager"
          class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="c3p0Datasource"/>
    </bean>

    <!--  注册事务通知 -->
    <tx:advice id="txAdvice" transaction-manager="transactionManager">
        <tx:attributes>
            <tx:method name="add*" isolation="DEFAULT" propagation="REQUIRED"/>
            <tx:method name="update*" isolation="DEFAULT" propagation="REQUIRED"/>
        </tx:attributes>
    </tx:advice>


    <!--  AOP配置 -->
    <aop:config>
        <aop:pointcut id="accountMagPointcut"
                      expression="execution(* cn.hurrican.services.*.*(..) )"/>
        <aop:advisor advice-ref="txAdvice" pointcut-ref="accountMagPointcut" />
    </aop:config>
</beans>
```



<a href="#spring">**返回→spring 配置文件**</a>

---

<a name="mybatis">**mybatis 配置文件**</a>

+ **mybatis 配置文件** ——主配置文件，**主要配置生成扫描式代理dao以及bean简单类名，主配置文件放在** `classpath`下
+ **mybatis 配置文件** ——`mapper`配置文件，**主要配置增删改查语句，放在dao接口所在的包并且每个mapper配置文件名与对应的dao接口名一样**



**MyBatis主配置文件如下**：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>

    <settings>
        <!--当返回行的所有列都是空时，MyBatis默认返回null-->
        <setting name="jdbcTypeForNull" value="NULL"/>

        <!--cacheEnabled - 使全局的映射器启用或禁用缓存-->
        <setting name="cacheEnabled" value="true"/>
        <setting name="lazyLoadingEnabled" value="true"/>
        <setting name="aggressiveLazyLoading" value="flase"/>

    </settings>

    <!-- 配置后mapper.xml可以简单类名-->
    <typeAliases>
        <package name="cn.hurrican.beans"/>
    </typeAliases>


    <mappers>
        <package name="cn.hurrican.dao"/>
    </mappers>

</configuration>
```



<center>

![mapper.xml配置文件]()

</center>

<a href="#ssm">**返回顶部**</a>

----

### <a name="dao">dao接口</a>

**dao接口** 主要是数据库访问接口及**MyBatis**的映射配置文件，主要完成数据库的**增删改查** 操作。

+  <a href="#AccountMsgDao">`AccountMsgDao.java`</a>
+  <a href="#UserDao">`UserDao.java`</a>

---

 <a name="AccountMsgDao">`AccountMsgDao.java`</a>

![AccountMsgDao定义]()

<a href="#dao">**back → dao接口**</a>

---

<a name="UserDao">`UserDao.java`</a>

![UserDao定义]()

<a href="#dao">**back → dao接口**</a>

<a href="#ssm">**返回顶部**</a>

---

### <a name="service">service层</a>

**service层**主要是实现对 **Controller层** 调用支持，主要是完成业务操作

+ <a href="#AccountMsgService">`AccountMsgService.java`</a>
+ <a href="#UserService">`UserService.java`</a>



<a name="AccountMsgService">`AccountMsgService.java`</a>

![AccountMsgService]()

<a href="#service">**back → service层**</a>



<a name="UserService">`UserService.java`</a>

![UserService]()

<a href="#service">**back → service层**</a>

<a href="#ssm">**返回顶部**</a>

----

### <a name="controller">controller层</a>

**controller层**主要接收客户端请求，把客户端提交的数据封装后调用**Service层**的业务逻辑，并控制页面的跳转，负责完成与客户端交互的任务

![]()

![]()



<a href="#ssm">**返回顶部**</a>

----

### <a name="interceptor">拦截器</a>

**拦截器** 拦截主要用于拦截请求**Controller** 里方法，拦截器会在**被拦截方法**前执行，要使用拦截器可以实现`HandlerInterceptor` 接口并在**spring-mvc.xml** 里配置拦截路径；项目中用一个`LoginInterceptor` **登录拦截器** 拦截未登录的用户

![interceptor]()



<a href="#ssm">**返回顶部**</a>

---

### <a name="bean">Bean</a>

**UserInfo.java**

```java
package cn.hurrican.beans;

import java.io.Serializable;

/**
 * Created by NewObject on 2017/8/10.
 */
public class UserInfo implements Serializable{
    private Long id;
    private String username;
    private String email;
    private String pwd;

    public UserInfo(String username, String email, String pwd) {
        this.username = username;
        this.email = email;
        this.pwd = pwd;
    }

    public UserInfo(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public UserInfo()
    {
        this.id = -1L;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPwd() {
        return pwd;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }



    @Override
    public String toString() {
        return new StringBuffer().append("id:\t").append(this.id)
                .append("\nusername:\t").append(this.username).append("\n").toString();
    }
}
```

<a href="#bean">**back→bean**</a>

**AccountMsg.java**

```java
package cn.hurrican.beans;

import java.util.Date;

/**
 * Created by NewObject on 2017/9/15.
 */
public class AccountMsg {

    private Integer id;
    private Integer userid;
    private String account;
    private String pwd;
    private String describe;
    private String bindEmail;
    private String bindPhone;
    private Date registerTime;

    public AccountMsg(String account, String pwd, String decsribe,
                      String bindEmail, String bindPhone, Date registerTime) {
        this.account = account;
        this.pwd = pwd;
        this.describe = decsribe;
        this.bindEmail = bindEmail;
        this.bindPhone = bindPhone;
        this.registerTime = registerTime;
    }

    public AccountMsg() {
    }

    public Integer getId() {
        return id;
    }

    public String getAccount() {
        return account;
    }

    public String getPwd() {
        return pwd;
    }

    public String getDescribe() {
        return describe;
    }

    public String getBindEmail() {
        return bindEmail;
    }

    public String getBindPhone() {
        return bindPhone;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setBindEmail(String bindEmail) {
        this.bindEmail = bindEmail;
    }

    public void setBindPhone(String bindPhone) {
        this.bindPhone = bindPhone;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}

```



<a href="#bean">**back→bean**</a>

<a href="#ssm">**返回顶部**</a>

---

