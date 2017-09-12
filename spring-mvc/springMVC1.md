## SpringMVC

-----

### 中央调度器DispatcherServlet



+  `DispatcherServlet`在`web.xml`配置，实现了`Servlet`接口，继承`FrameworkServlet`，本质上是Servlet
+  `DispatcherServlet`的名字`<servlet-name>`可以任意，但需要在`<init-param>`里指定springMVC的配置文件路径，即指定参数`contextConfigLocation`的值
+  **中央调度器** ` <servlet-mapping>`中的`<url-pattern>`不能写为`/*`；因为`DispatcherServlet`会将**所有的动态页面请求**（包括`jsp,servlet`）当作一个`Controller`请求，中央调度器将调用**处理器映射器** 查找相应的处理器，由于查找不到**处理器**所以会返回`404`
+  不建议写成`/`，写成`/` **DispatcherServlet**会将静态资源（比如`html,js,css,png`）当作向`Controller`的请求
+  **DispatcherServlet** 也不要写成`/xxx/???/*.do`，写成这种方式会导致无法访问任何资源


解决``使用`/`导致的静态资源无法访问的方法：

**方法1**

- 在spring配置文件引入`mvc`的约束，约束内容如下：

  ```xml
  xmlns:mvc="http://www.springframework.org/schema/mvc"xsi:schemaLocation="http://www.springframework.org/schema/mvchttp://www.springframework.org/schema/mvc/spring-mvc.xsd"
  ```

  ​

- 在spring配置文件里加上`  <mvc:default-servlet-handler />`，`  <mvc:default-servlet-handler />`会将对静态资源的访问请求通过`HandlerMapping`映射到**默认Servlet**请求处理器`DefaultServletHttpRequestHandler`上，该处理器将调用`Tomcat`的`DefaultServlet`来处理静态资源的访问请求

  ```xml
  <mvc:default-servlet-handler />
  ```




**方法2**

使用专门用于处理静态资源访问请求的处理器`ResourceHttpRequestHandler`并添加`<mvc:resources />`标签，专门用于解决静态资源无法访问的问题

```xml
<mvc:resources location="" mapping="" />
```

+ `location`表示静态资源所在的目录，**可以是** `/WEB-INF/`目录及其子目录
+ `mapping`表示对该资源的请求



----

`web.xml`中，标签`servlet`下的子标签`<load-on-startup>`用于指定是否在启动时加载该Servlet，配置的值大于1表示启动时加载此Servlet，配置的数字越小优先级越高

```xml

    <!-- 注册中央调度器-->
    <servlet>
        <servlet-name>userController</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <!-- 指定springMVC的配置文件及配置名-->
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:spring-mvc.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>

    <!-- 为userController配置路径映射-->
    <servlet-mapping>
        <servlet-name>userController</servlet-name>
      	<!-- 只要是.do结尾的请求都会被转发到userController中进行处理-->
        <url-pattern>*.do</url-pattern>
    </servlet-mapping>

```





----

<a name="project">**完整项目**：</a>

+ <a href="#UserController">**UserController.java**</a>
+ <a href="#web">**web.xml**</a>
+ <a href="#spring-mvc">**spring-mvc.xml**</a>



<a name="UserController">**UserController.java**</a>

```java
public class UserController implements Controller {
    @Override
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest,
                                      HttpServletResponse httpServletResponse)
            throws Exception {
        ModelAndView mv = new ModelAndView();
      	//接收.do结尾的请求，并把请求转发到/page/login.html页面
        mv.setViewName("/page/login.html");

        return mv;
    }
}
```



<a href="#project">back</a>



<a name="web">**web.xml**</a>

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
          http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
         version="3.0">
    <display-name>Archetype Created Web Application</display-name>

    <!-- 注册中央调度器-->
    <servlet>
        <servlet-name>userServlet</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <!-- 指定springMVC的配置文件及配置名-->
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:spring-mvc.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>

    <!-- 为userController配置路径映射-->
    <servlet-mapping>
        <servlet-name>userServlet</servlet-name>
        <url-pattern>*.do</url-pattern>
    </servlet-mapping>

    <welcome-file-list>
        <welcome-file>index.html</welcome-file>
    </welcome-file-list>
</web-app>
```



<a href="#project">back</a>



<a name="spring-mvc">**spring-mvc.xml**</a>

```xml
<?xml version="1.0" encoding="utf-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd
        http://www.springframework.org/schema/tx
        http://www.springframework.org/schema/tx/spring-tx.xsd
        http://www.springframework.org/schema/mvc
        http://www.springframework.org/schema/mvc/spring-mvc.xsd">


    <!-- c3p0数据源(properties文件配置)-->
    <context:property-placeholder location="classpath:jdbc.properties"/>
    <bean id="c3p0Datasource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="driverClass" value="${jdbc.driver}"/>
        <property name="jdbcUrl" value="${jdbc.url}"/>
        <property name="user" value="${jdbc.user}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>


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

    <bean id="userController" class="cn.hurrican.controllers.UserController"/>

    <bean class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
        <property name="mappings">
            <props>
                <prop key="/user/login.do">userController</prop>
            </props>
        </property>
    </bean>

    <mvc:default-servlet-handler />
</beans>
```



<a href="#project">back</a>