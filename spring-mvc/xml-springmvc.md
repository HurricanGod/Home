## <a name="top">SpringMVC使用xml配置开发</a>

![]()

### <a name="catatory">目录</a>

<a href="#HandlerMapping">处理器映射器HandlerMapping</a>

<a href="#ViewResolver">视图解析器ViewResolver</a>



-----

### <a name="HandlerMapping">处理器映射器HandlerMapping</a>

`HandlerMapping`接口负责根据`request`请求找到对应的`Handler处理器`及`Interceptor`拦截器，并将它们封装在`HandlerExecutionChain`对象中，返回给**中央调度器**





<a name="HandlerMapping">**HandlerMapping** 接口常用实现类：</a>

+ `BeanNameUrlHandlerMapping` 

  + 会根据请求的URL与spring容器中定义的处理器bean的**name**属性值进行匹配，从而在spring容器中找到处理bean的实例
  + 不足：处理器的id就是1个Url请求路径，不符合命名规范；处理器bean的定义与请求Url绑定在一起，如出现多个Url请求同一个处理器的情况，就需要在Spring容器中配置多个该处理器的`<bean/>`，这将导致容器会创建多个该处理器的实例

  ​

+ `SimpleUrlHandlerMapping` ，根据请求的Url与Spring容器中定义的处理器映射器子标签的key属性进行匹配；匹配上后，再将该`key`的value值与处理器bean的id值进行匹配，从而在spring容器中找到处理器bean

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
        http://www.springframework.org/schema/tx/spring-tx.xsd
        http://www.springframework.org/schema/mvc
        http://www.springframework.org/schema/mvc/spring-mvc.xsd">

    <bean id="userController" class="cn.hurrican.controllers.UserController"/>

    <bean class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
        <property name="mappings">
            <props>
                <prop key="start.do">userController</prop>
                <prop key="home.do">userController</prop>
            </props>
        </property>
    </bean>

</beans>
```

<a href="#catatory">返回</a>

----

`DispatcherServlet`的`doDispatch`方法

```java
/*
*	处理处理器的实际调度，处理器将按照请求servlet
*	的HandlerMapping顺序获得；
*	通过查询注册到servlet的HandlerAdapter来找到第
*	一个支持handler的HandlerAdapter适配器
*	所有的http方法都会被这个方法执行
*/

protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception 
```



----

创建Controller实例除了可以实现`Controller`或<a href="#HttpRequestHandler">`HttpRequestHandler`</a>接口外，还可以继承`AbstractController`类，`AbstractController`类可以**限制请求的提交方式**



<a name="HttpRequestHandler">`HttpRequestHandler`</a>

```java
public class ImageController implements HttpRequestHandler {


    @Override
    public void handleRequest(HttpServletRequest httpServletRequest,
                              HttpServletResponse httpServletResponse)
            throws ServletException, IOException {
        
    }
}
```

`HttpRequestHandler`接口的`handleRequest`方法没有返回值，要实现页面之间的跳转需要借助参数`httpServletRequest`或`httpServletResponse`，此外还可以调用`setAttribute()`往`httpServletRequest`设置值

----

`MultiActionController`继承`AbstractController`，自定义**控制器**时继承`MultiActionController`可以有**多个方法**用于接收不同的请求；要为**控制器**设定**方法名称解析器**，一般使用`PropertiesMethodNameResolver`解析器对**方法名**进行解析；最后需要把**自定义的控制器**注册到`SimpleUrlHandlerMapping`中



**注**

在`Spring`配置文件里最好不要把`<mvc:default-servlet-handler />`放在配置文件开头，应该**放在配置文件最后面**；放在spring配置文件中间或者开头很容易导致`SpringMVC`配置的路径无法访问的情况。



### <a name="demo">MultiActionController实例</a>

+ <a href="#UserController">UserController.java</a>
+ <a href="#web">web.xml</a>
+ <a href="#spring-mvc">spring-mvc.xml</a>

-----

<a name="UserController">UserController.java</a>


<html>
<head>
</head>
<BODY BGCOLOR="#2b2b2b">
<TABLE CELLSPACING=0 CELLPADDING=5 COLS=1 WIDTH="100%" BGCOLOR="#C0C0C0" >
<TR><TD><CENTER>
<FONT FACE="Arial, Helvetica" COLOR="#000000">
UserController.java</FONT>
</center></TD></TR></TABLE>
<pre>

<a name="l1"><span class="ln">1    </span></a><span class="s0">package </span><span class="s1">cn.hurrican.controllers</span><span class="s0">;</span><span class="s1"> 
<a name="l2"><span class="ln">2    </span></a> 
<a name="l3"><span class="ln">3    </span></a></span><span class="s0">import </span><span class="s1">org.springframework.web.servlet.ModelAndView</span><span class="s0">;</span><span class="s1"> 
<a name="l4"><span class="ln">4    </span></a></span><span class="s0">import </span><span class="s1">org.springframework.web.servlet.mvc.multiaction.MultiActionController</span><span class="s0">;</span><span class="s1"> 
<a name="l5"><span class="ln">5    </span></a> 
<a name="l6"><span class="ln">6    </span></a></span><span class="s0">import </span><span class="s1">javax.servlet.http.HttpServletRequest</span><span class="s0">;</span><span class="s1"> 
<a name="l7"><span class="ln">7    </span></a></span><span class="s0">import </span><span class="s1">javax.servlet.http.HttpServletResponse</span><span class="s0">;</span><span class="s1"> 
<a name="l8"><span class="ln">8    </span></a> 
<a name="l9"><span class="ln">9    </span></a></span><span class="s2">/** 
<a name="l10"><span class="ln">10   </span></a> * Created by NewObject on 2017/9/12. 
<a name="l11"><span class="ln">11   </span></a> */</span><span class="s1"> 
<a name="l12"><span class="ln">12   </span></a></span><span class="s0">public class </span><span class="s1">UserController </span><span class="s0">extends </span><span class="s1">MultiActionController { 
<a name="l13"><span class="ln">13   </span></a> 
<a name="l14"><span class="ln">14   </span></a>    </span><span class="s0">public </span><span class="s1">ModelAndView login(HttpServletRequest httpServletRequest</span><span class="s0">,</span><span class="s1"> 
<a name="l15"><span class="ln">15   </span></a>                                HttpServletResponse httpServletResponse) 
<a name="l16"><span class="ln">16   </span></a>            </span><span class="s0">throws </span><span class="s1">Exception{ 
<a name="l17"><span class="ln">17   </span></a>        System.out.println(</span><span class="s3">&quot;接收登录请求&quot;</span><span class="s1">)</span><span class="s0">;</span><span class="s1"> 
<a name="l18"><span class="ln">18   </span></a>        ModelAndView mv = </span><span class="s0">new </span><span class="s1">ModelAndView(</span><span class="s3">&quot;/page/login.html&quot;</span><span class="s1">)</span><span class="s0">;</span><span class="s1"> 
<a name="l19"><span class="ln">19   </span></a>        </span><span class="s0">return </span><span class="s1">mv</span><span class="s0">;</span><span class="s1"> 
<a name="l20"><span class="ln">20   </span></a>    } 
<a name="l21"><span class="ln">21   </span></a> 
<a name="l22"><span class="ln">22   </span></a> 
<a name="l23"><span class="ln">23   </span></a>    </span><span class="s0">public </span><span class="s1">ModelAndView register(HttpServletRequest request</span><span class="s0">,</span><span class="s1">HttpServletResponse response) 
<a name="l24"><span class="ln">24   </span></a>            </span><span class="s0">throws </span><span class="s1">Exception{ 
<a name="l25"><span class="ln">25   </span></a>        System.out.println(</span><span class="s3">&quot;接收注册请求&quot;</span><span class="s1">)</span><span class="s0">;</span><span class="s1"> 
<a name="l26"><span class="ln">26   </span></a>        ModelAndView mv = </span><span class="s0">new </span><span class="s1">ModelAndView(</span><span class="s3">&quot;/page/register.html&quot;</span><span class="s1">)</span><span class="s0">;</span><span class="s1"> 
<a name="l27"><span class="ln">27   </span></a>        </span><span class="s0">return </span><span class="s1">mv</span><span class="s0">;</span><span class="s1"> 
<a name="l28"><span class="ln">28   </span></a>    } 
<a name="l29"><span class="ln">29   </span></a> 
<a name="l30"><span class="ln">30   </span></a> 
<a name="l31"><span class="ln">31   </span></a> 
<a name="l32"><span class="ln">32   </span></a>} 
<a name="l33"><span class="ln">33   </span></a></span></pre>
</body>
</html>

<a href="#demo">back</a>

<a name="web">web.xml</a>

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



<a href="#demo">back</a>



<a name="spring-mvc">spring-mvc.xml</a>

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

    <bean id="userManage" class="cn.hurrican.controllers.UserController">
        <property name="methodNameResolver" ref="propertiesMethodNameResolver"/>
    </bean>


    <bean id="propertiesMethodNameResolver"
          class="org.springframework.web.servlet.mvc.multiaction.PropertiesMethodNameResolver">
        <property name="mappings">
            <props>
                <prop key="/user/login.do">login</prop>
                <prop key="/user/register.do">register</prop>
            </props>
        </property>
    </bean>



    <bean class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
        <property name="urlMap">
            <map>
                <entry key="/user/*.do" value-ref="userManage"/>
            </map>
        </property>
    </bean>
    <mvc:default-servlet-handler />

</beans>
```



<a href="#demo">back</a>

-----

### <a name="ViewResolver">视图解析器ViewResolver</a>

视图解析器用于完成对当前web应用内部资源的封装与跳转，而对于内部资源的查找规则是：`ModelAndView`中指定的视图名称与为视图解析器配置的前缀与后缀相结合的方式，拼接成一个web应用内部资源路径



`InternalResourceViewResolver`解析器会把处理器方法返回的模型属性存放到对应的`request`中，然后将请求转发到目标URL



`BeanNameViewResourceViewResolver`视图解析器将**资源**封装为**Spring容器中注册的Bean实例**



**两种资源视图对象**：

+ `RedirectView` —— **外部资源视图**
+ `JstlView` —— **内部资源视图**



```xml
<!--定义外部资源视图-->
<bean id="redirectView" class="org.springframework.web.servlet.view.RedirectView">
  <property name="url" value="http://www.baidu.com" />
</bean>
```



```xml
<!--定义内部资源视图-->
<bean id="innerView" class="org.springframework.web.servlet.view.JstltView">
  <property name="url" value="page/hello.html" />
</bean>
```

