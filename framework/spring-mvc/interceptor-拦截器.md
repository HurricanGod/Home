## <a name="interceptor">拦截器</a>

`SpringMVC`中的**拦截器** 的作用是拦截指定用户请求，并进行相应的预处理和后处理；**拦截器** 的时间点： `HandleMapping`根据用户提交请求映射到要执行的`Controller`，然后根据`Contrroller`找到`HandlerAdaptor`并在**HandlerAdaptor**执行处理器前。



**要点**：

**拦截器**会在`Controller`里的方法执行前先执行，如果有多个拦截器会按照拦截器的配置顺序执行

---

**Interceptor**的使用：

+ 实现`HandlerInterceptor`接口并重写的`preHandle()`方法、`postHandle()`方法及`afterCompletion()`方法
+ `preHandle()`方法会在**Controller**里的方法执行前**先执行**
+ `preHandle()`若返回**false** 则后面的**拦截器** 或 **控制器方法**都不会被执行
+ `afterCompletion()` 执行的条件是：`preHandle()` 执行了并且**返回结果为true**



![]()

![]()

---

<a name="back">**示例Demo**：</a>

+ <a href="#interceptor1">**LoginInterceptor**</a>
+ <a href="#interceptor2">**AuthorityInterceptor**</a>
+ <a href="#spring">**SpringMVC配置文件**</a>



<a name="interceptor1">**LoginInterceptor**</a>

```java
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("id");
        if (id == null || id.equals("")) {
            String s = request.getContextPath() + "/page/login.html";
            System.out.println("s = " + s);
            response.sendRedirect(s);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("执行登录检查拦截器里的postHandle方法");
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        System.out.println("执行登录检查拦截器里的afterCompletion方法");
    }
}
```

<a name="back">**back**</a>

<a name="interceptor2">**AuthorityInterceptor**</a>

```java
public class AuthorityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        System.out.println("执行权限检查！");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("执行权限检查拦截器里的postHandle方法");
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        System.out.println("执行权限检查拦截器里的afterCompletion方法");
    }
}
```

<a name="back">**back**</a>

<a name="spring">**SpringMVC配置文件**</a>

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
    <!-- 配置LoginInterceptor拦截器 -->
	<!-- 拦截路径设置为web目录下所有控制器下的方法对应的路径 -->
	<!-- **表示匹配0个或多个目录 -->
	<!-- *表示匹配0个或多个字符 -->
	<!-- 用于指定不被拦截的路径 -->
    <mvc:interceptors>
        <mvc:interceptor>
            <mvc:mapping path="/**"/>
            <mvc:exclude-mapping path="/user/login.do"/>
            <mvc:exclude-mapping path="/user/isExist.do"/>
            <mvc:exclude-mapping path="/user/register.do"/>
            <bean class="cn.hurrican.interceptor.LoginInterceptor"/>
        </mvc:interceptor>
    </mvc:interceptors>

    <mvc:interceptors>
        <mvc:interceptor>
            <mvc:mapping path="/**"/>
            <bean class="cn.hurrican.interceptor.AuthorityInterceptor"/>
        </mvc:interceptor>
    </mvc:interceptors>

    <mvc:annotation-driven />

    <!-- 把对静态资源访问请求映射到DefaultServletHttpRequestHandler上-->
    <mvc:default-servlet-handler />

</beans>
```

<a name="back">**back**</a>