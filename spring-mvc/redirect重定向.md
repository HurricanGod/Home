## SpringMVC——重定向到控制器的方法

----

**根据所要跳转的资源类型，重定向分为** ：

+ 重定向到页面
+ 重定向到控制器的方法

**注**：

**重定向**不能为`WEB-INF`中的页面，重定向相当于用户再次发出http请求，用户不能直接访问`WEB-INF`目录下的资源



---

**重定向时通过** `ModelAndView`中的**Model**存入数据，视图解析器`InternalResourceViewResolver`会将map中的 **key** 与 **value** 以请求参数的形式放到请求`url`后

+ **视图解析器**会将map中的value放到URL后作为请求参数传递出去，所以无论什么类型的value均会变为`String`，**放入model中的value，只能是基本数据类型，不能是自定义类型**
+ **重定向**不能从`request`读取数据；重定向时，通过`Model`传递数据可以通过获取**url**中请求参数获取数据
+ `Controller`里的方法使用**返回字符串**的方式实现**重定向**时在要重定向的页面前加上`redirect:`



----

**示例代码**：

**applicationContext.xml**配置文件

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


    <context:component-scan base-package="cn.hurrican.*"/>

    <mvc:annotation-driven />

    <mvc:default-servlet-handler />

</beans>
```



**web.xml**配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
          http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
         version="3.0">
    <display-name>Archetype Created Web Application</display-name>
    
    <!-- -->
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

    <filter-mapping>
        <filter-name>characterEncodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>


    <!-- 注册中央调度器-->
    <servlet>
        <servlet-name>userServlet</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <!-- 指定springMVC的配置文件及配置名-->
            <param-name>contextConfigLocation</param-name>
            <!--<param-value>classpath:spring-mvc.xml</param-value>-->
            <param-value>classpath:applicationContext.xml</param-value>
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





![](https://github.com/HurricanGod/Home/blob/master/spring-mvc/img/redirect.png)
