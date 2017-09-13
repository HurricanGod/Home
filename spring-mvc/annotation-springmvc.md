## <a name="springmvc">注解式开发SpringMVC</a>

----

在**方法名**添加`@RequestMapping`注解

+ `@RequestMapping`注解的`value`属性用于指定**访问的资源**，`method`用于指定**请求的类型**

+ 注解式开发时`自定义Controller`不用继承任何类或实现接口，直接在`自定义Controller`里添加`@Controller`注解，然后在`Spring`的配置文件里添加**注解扫描** 和**mvc注解支持**；此外`自定义Controller`里的**方法名、返回类型、参数**可以任意

  ```xml
  	<!--扫描cn.hurrican包下的所有类-->
      <context:component-scan base-package="cn.hurrican.*"/>
  	<!--设置mvc注解支持-->
      <mvc:annotation-driven />
  ```



```java
 @RequestMapping(value = "/login.do", method = RequestMethod.POST)
    public ModelAndView handleRequest(String username, String password){

        System.out.println("username = " + username);
        System.out.println("password = " + password);

        ModelAndView mv = new ModelAndView();
        mv.setViewName("/index.html");

        return mv;
    }
```



### 处理器的请求映射规则的定义



**1. 对请求URI的命名空间的定义**

在控制器类上添加`@RequestMapping`注解并设置`value`属性的值，**value**属性的值为**命名空间**. 

如下代码所示：`UserController`下的命名空间为`/user`，所有要访问**UserController**下的方法需在浏览器请求时加上`/user` ；这里假设web项目名称为**mvc**，如果要访问`handleRequest`方法，在浏览器应该输入`http://localhost:8080/mvc/user/login.do`

```java
@Controller
@RequestMapping("/user")
public class UserController {


    @RequestMapping(value = "/login.do", method = RequestMethod.POST)
    public ModelAndView handleRequest(String username, String password){

        System.out.println("username = " + username);
        System.out.println("password = " + password);

        ModelAndView mv = new ModelAndView();
        mv.setViewName("/index.html");

        return mv;
    }
}
```

![]()



**2. 请求URI中通配符的应用**

![]()

![]()


