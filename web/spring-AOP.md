## <a name="top">AOP</a>

**AOP原理**：底层采用动态代理实现，有`JDK动态代理` 和 `CGLIB动态代理` 两种。
![](https://github.com/HurricanGod/Home/blob/master/web/spring_img/xml-aop/AOP%E5%8E%9F%E7%90%86.png)

------

- **连接点** ：类里面可以被增强的方法称为连接点
- **切入点** ：在实际操作中，如果add方法被增强了，那么实际增强的方法称为切入点
- **通知/增强** ：增强的逻辑称为增强；比如拓展了功能，则这个功能称为增强
  - 前置通知：在方法前执行
  - 后置通知：在方法后执行
  - 异常通知：方法出现异常
  - 最终通知：在后置之后执行
  - 环绕通知：在方法之前和之后执行
- **切面** ：泛指交叉业务逻辑，常用的切面有`通知`和`顾问`，把增强应用到具体方法上面，过程称为切面（把增强用到切入点过程）

-----

### 通知

<a href="#before">Before Advice </a>

<a href="#after">After Advice</a>

<a href="#around">Around Advice</a>



-----

<a name="before"> Before Advice </a>

**有接口的被增强对象方法**使用**Before Advice**时需要创建一个类实现`MethodBeforeAdvice` 接口里的`before(Method method, Object[] args, Object target)`方法 ；并把该实现类注入到代理对象里。该接口的方法会在被增强方法执行前执行。

**具体代码如下**

**xml配置文件**

```xml
<?xml version="1.0" encoding="utf-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd">

    <!-- 生成被增强对象-->
    <bean id="userService" class="hurrican.service.UserServiceImp"/>

    <!-- 注册切面-->
    <bean id="beforeAdvice" class="hurrican.service.UserServiceMethodBeforeAdvice"/>

    <!-- 生成代理对象-->
    <bean id="serviceProxy" class="org.springframework.aop.framework.ProxyFactoryBean">
        <!-- 指定被增强对象-->
        <property name="target" ref="userService"/>
        <!-- 指定切面-->
        <property name="interceptorNames" value="beforeAdvice"/>
    </bean>
</beans>
```
![](https://github.com/HurricanGod/Home/blob/master/web/spring_img/xml-aop/aop-before-1.png)


<a name="after"> After Advice </a>

**有接口的被增强对象方法**使用**After Advice**时需要创建一个类实现`AfterReturningAdvice`接口里的`afterReturning`()方法；该接口方法第一个参数 `Object returnValue`  表示被增强方法执行后返回的结果。

**具体代码如下：**

```java
public class UserServiceMethodAfterReturnAdvice implements AfterReturningAdvice {
    @Override
    public void afterReturning(Object returnValue,
                               Method method, Object[] args,
                               Object target) throws Throwable {
        System.out.println(method.getName() + " 执行后返回的结果");
        User user = (User) returnValue;
        System.out.println(user.toString());
        user.setId(10);
    }
}
```



```java
public class UserServiceImp implements UserService {
    @Override
    public void login() {
        System.out.println("登录成功");
    }

    @Override
    public User logout() {
        User user = new User("hurrican", "********", "177165692@qq.com");
        return user;
    }
}
```



```java
public interface UserService {
    void login();
    User logout();
}
```

```java
 @Test
    public void testMethodAfterAdvice() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-aop.xml");
        UserService service = (UserService) context.getBean("serviceProxy");
        User returnUser = service.logout();
        System.out.println("后置增强后得到的结果");
        System.out.println(returnUser.toString());
    }
```

```xml
<?xml version="1.0" encoding="utf-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd">

    <!-- 生成被增强对象-->
    <bean id="userService" class="hurrican.service.UserServiceImp"/>

    <!-- 注册切面-->
    <bean id="beforeAdvice" class="hurrican.service.UserServiceMethodBeforeAdvice"/>


    <bean id="afterAdvice" class="hurrican.service.UserServiceMethodAfterReturnAdvice"/>

    <!-- 生成代理对象-->
    <bean id="serviceProxy" class="org.springframework.aop.framework.ProxyFactoryBean">
        <!-- 指定被增强对象-->
        <property name="target" ref="userService"/>
        <!-- 指定切面-->
        <property name="interceptorNames" >
            <list>
                <value>beforeAdvice</value>
                <value>afterAdvice</value>
            </list>
        </property>
    </bean>
</beans>
```
![](https://github.com/HurricanGod/Home/blob/master/web/spring_img/xml-aop/aop-after-1.png)




<a name="around"> Around Advice </a>

要为方法添加**环绕通知** 需要实现`org.aopalliance.intercept.MethodInterceptor`接口的`invoke()`方法，并在该方法内调用参数`MethodInvocation methodInvocation`的`proceed()` 方法（**即调用被增强方法**）

```java
public class NoInterfaceService {

    public void doRemove(User user){
        System.out.println("删除操作……");
    }
}
```

```java
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Created by NewObject on 2017/8/31.
 */
public class ServiceAroundAdvice implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        System.out.println("执行检查用户是否具有删除权限操作");
        System.out.println("即将执行的方法：\t" + methodInvocation.getMethod().getName());
        Object obj = methodInvocation.proceed();
        System.out.println("执行返回提示信息操作");
        return obj;
    }
}
```

```java
 @Test
    public void testAroundAdvice() {

        ApplicationContext context = new ClassPathXmlApplicationContext("spring-aop-cglib.xml");
        NoInterfaceService service = (NoInterfaceService) context.getBean("serviceProxy");
        service.doRemove(new User());

    }
```

```xml
<?xml version="1.0" encoding="utf-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd">

    <bean id="nointerfaceService" class="hurrican.service.NoInterfaceService"/>

    <bean id="aroundAdvice" class="hurrican.service.ServiceAroundAdvice"/>

    <bean id="serviceProxy" class="org.springframework.aop.framework.ProxyFactoryBean">
        <property name="target" ref="nointerfaceService" />
        <property name="interceptorNames">
            <list>
                <value>aroundAdvice</value>
            </list>
        </property>
    </bean>

</beans>
```

![](https://github.com/HurricanGod/Home/blob/master/web/spring_img/xml-aop/aop-around-1.png)

-------

### 顾问Advisor

顾问是Spring提供的另一种切面，可以完成更为复杂的切面织入功能，`PointcutAdvisor`是顾问的一种可以指定具体的切入点。顾问将通知进行了包装，会根据不同通知类型，在不同的时间点，将切面织入到不同的切入点。

`PointcutAdvisor`接口较为常用的实现类：

+ `NameMatchMethodPointcutAdvisor` <a name="NameMatch">名称匹配方法切入点顾问</a>
+ `RegexpMethodPointcutAdvisor`<a name="RegexpMatch">正则表达式匹配方法切入点顾问</a>



### <a href="#NameMatch">名称匹配方法切入点顾问</a>

使用**名称匹配方法切入点顾问**需要创建`org.springframework.aop.support.NameMatchMethodPointcutAdvisor`的实例，并把**通知实例**注入到对象的 `advice` 属性里， 要增强的方法注入到 `mappedName` 属性里，具体操作看如下示例：

<a name="NoInterfaceService">NoInterfaceService.java</a>

```java
public class NoInterfaceService {

    public void doRemove(User user){
        System.out.println("删除操作……");
    }

    public void doQuery(){
        System.out.println("执行查询操作……");
    }
}
```



**mappedName**支持正则式注入，如下`do*`表示增强`NoInterfaceService`对象里所有以**do**开头的方法，如果改为`doRemove`将只增强**doRemove**方法

```xml
<?xml version="1.0" encoding="utf-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd">

    <bean id="aroundAdvice" class="hurrican.service.ServiceAroundAdvice"/>

    <!-- 生成被增强方法对象-->
    <bean id="nointerfaceService" class="hurrican.service.NoInterfaceService"/>

    <!-- 注册通知-->
    <bean id="checkAuthorityAdvice" class="hurrican.service.UserServiceMethodBeforeAdvice"/>

    <!-- 注册顾问-->
    <bean id="daoAdvisor" class="org.springframework.aop.support.NameMatchMethodPointcutAdvisor">
        <property name="advice" ref="checkAuthorityAdvice"/>
        <property name="mappedName" value="do*"/>
    </bean>

    <!-- 注册代理对象-->
    <bean id="serviceProxy" class="org.springframework.aop.framework.ProxyFactoryBean">
        <property name="target" ref="nointerfaceService" />
        <property name="interceptorNames">
            <list>
                <value>daoAdvisor</value>
            </list>
        </property>
    </bean>

</beans>
```

<a name="UserServiceMethodBeforeAdvice">UserServiceMethodBeforeAdvice.java<a>

```java
public class UserServiceMethodBeforeAdvice implements MethodBeforeAdvice {
    
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("需要被前置增强的方法:\t" + method.getName());
        System.out.println("执行检查用户权限操作！");
    }
}
```



### <a href="#RegexpMatch">正则表达式匹配方法切入点顾问</a>

使用**正则表达式匹配方法切入点顾问**需要创建`org.springframework.aop.support.RegexpMethodPointcutAdvisor` 对象，并把**通知实例**注入到对象的 `advice` 属性里，在`pattern`里注入满足要求的方法正则式（**方法正则式要求为全限定方法名的正则式**）

```xml
<?xml version="1.0" encoding="utf-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd">

    <!-- 生成被增强方法对象-->
    <bean id="nointerfaceService" class="hurrican.service.NoInterfaceService"/>

    <!-- 注册通知-->
    <bean id="checkAuthorityAdvice" class="hurrican.service.UserServiceMethodBeforeAdvice"/>

    <bean id="daoRegexpAdvisor" class="org.springframework.aop.support.RegexpMethodPointcutAdvisor">
        <property name="advice" ref="checkAuthorityAdvice"/>
        <property name="pattern" value=".*do.*"/>
    </bean>

    <!-- 注册代理对象 使用正则顾问-->
    <bean id="serviceProxyByRegexp" class="org.springframework.aop.framework.ProxyFactoryBean">
        <property name="target" ref="nointerfaceService" />
        <property name="interceptorNames">
            <list>
                <value>daoRegexpAdvisor</value>
            </list>
        </property>
    </bean>
</beans>
```

```java
@Test
    public void testAdvisorByRegexp() {

        ApplicationContext context = new ClassPathXmlApplicationContext("spring-aop-cglib.xml");
        NoInterfaceService service = (NoInterfaceService) context.getBean("serviceProxyByRegexp");

        service.doRemove(new User());

        System.out.println();
        service.doQuery();
    }
```

<a href="#NoInterfaceService">NoInterfaceService.java</a>

<a href="#UserServiceMethodBeforeAdvice">UserServiceMethodBeforeAdvice.java<a>



----

使用**ProxyFactoryBean**产生的增强的代理对象存在的问题：

+ 当需要创建的被代理对象有很多时，需要创建大量的代理对象，因为1个代理对象对应1个目标对象，存在大量的冗余
+ 要调用增强的方法必须生成调用代理类对象



### Bean名称自动代理生成器

只需要注册1个`org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator` 的实例，不用**单独生成**代理对象就可以实现对目标方法的增强；**可以直接获取获取目标对象实例**

```xml
<?xml version="1.0" encoding="utf-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd">

    <!-- 生成被增强方法对象-->
    <bean id="nointerfaceService" class="hurrican.service.NoInterfaceService"/>

    <!-- 注册通知-->
    <bean id="checkAuthorityAdvice" class="hurrican.service.UserServiceMethodBeforeAdvice"/>

    <!-- 注册正则顾问-->
    <bean id="daoRegexpAdvisor" class="org.springframework.aop.support.RegexpMethodPointcutAdvisor">
        <property name="advice" ref="checkAuthorityAdvice"/>
        <property name="pattern" value=".*doRemove"/>
    </bean>

    <!-- 注册自动代理生成器-->
    <bean class="org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator">
        <property name="beanNames" value="nointerfaceService"/>
        <property name="interceptorNames" value="daoRegexpAdvisor"/>
    </bean>
</beans>
```

```java
@Test
    public void testAdvisorAutoProxyCreator() {

        ApplicationContext context = new ClassPathXmlApplicationContext("spring-aop-cglib.xml");
        NoInterfaceService service = (NoInterfaceService) context.getBean("nointerfaceService");

        service.doRemove(new User());

        System.out.println();
        service.doQuery();
    }
```

<a href="#NoInterfaceService">NoInterfaceService.java</a>

<a href="#UserServiceMethodBeforeAdvice">UserServiceMethodBeforeAdvice.java<a>



-------



<right><a href="#top">**back**</a></right>