# <a name="top">Spring的AOP操作</a>

------

`AspectJ`是一个面向切面的框架，Spring中使用**AOP**开发时一般使用`Aspectj`的实现方式，`AspectJ`有5种通知类型：

- <a name="before1">前置通知</a>
- <a name="after1">后置通知</a>
- <a name="around1">环绕通知</a>
- <a name="exection1">异常通知</a>
- <a name="final1">最终通知</a>（无论程序是否正常执行，该通知都会执行）



- **连接点** ：类里面可以被增强的方法称为连接点
- **切入点** ：在实际操作中，如果目标方法被增强了，那么实际增强的方法称为切入点
- **通知/增强** ：增强的逻辑称为增强；比如拓展了功能，则这个功能称为增强
- **切面** ：泛指交叉业务逻辑，常用的切面有`通知`和`顾问`，把增强应用到具体方法上面，过程称为切面（把增强用到切入点过程）


-----

1. 在Spring里面进行aop操作，使用aspectj实现

   1. aspectj不是spring的一部分，和spring一起使用进行aop操作
   2. Spring2.0以后新增了对AspectJ的支持

   ​

2. 使用aspectj实现aop的两种方式

   - <a name="xml">基于aspectj的xml配置</a>


- <a name="annotation">基于aspectj的注解方式</a>

   ​

1. **AOP操作需要的jar包**

   1. aopalliance-1.0.jar
   2. aspectjweaver-1.8.*.jar
   3. spring-aop-4.2.0.RELEASE.jar
   4. spring-aspects-4.2.0.RELEASE.jar

----

- 前置通知：在方法前执行
  - <a href="#xml_before">配置文件实现</a>
  - <a href="#before">注解实现</a>
- 后置通知：在方法后执行
  - <a href="xml_after">配置文件实现</a>
  - <a href="#after">注解实现</a>
- 异常通知：方法出现异常
  - <a href="#">配置文件实现</a>
  - <a href="#exection">注解实现</a>
- 最终通知：在后置之后执行
  - <a href="#xml_fina">配置文件实现</a>
  - <a href="#final">注解实现</a>
- 环绕通知：在方法之前和之后执行
  - <a href="#xml_around">配置文件实现</a>
  - <a href="#around">注解实现</a>



------

### 使用表达式配置切入点

- **常用表达式**

```
execution(<访问修饰符>?<返回类型> <方法全路径名>(<参数>)<异常>)
1.execution(* package.class.method(..))	表示某个类中的某个方法
2.execution(* package.class.*(..))	表示某个类中的所有方法
3.execution(* *.*(..))	表示所有方法
```

-----

## <a href="#xml">xml配置文件完成AOP操作</a>

----

<a name="xml_before">**前置通知**</a>

使用`xml`方式前置增强需要使用`<aop:config>`标签，`<aop:aspect ref="?????">`用于配置切面，**ref** 属性配置`增强方法对象的id` ，`<aop:before>`用于配置**切入点**和**通知**，`method`属性配置**通知**，`pointcut`属性配置**要被增强的方法**

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

    <!-- 注册切面-->
    <bean id="xmlwayEnhance" class="hurrican.service.XmlConfigEnhance"/>
    <bean id="userSevice" class="hurrican.service.UserManageService"/>

    <aop:config>
        <aop:aspect ref="xmlwayEnhance">
            <aop:before method="chenkUserAuthority"
                        pointcut="execution(* *..UserManageService.doRemove(..))"/>
        </aop:aspect>
    </aop:config>

    <context:component-scan base-package="hurrican.*"/>
    <aop:aspectj-autoproxy/>
</beans>
```

<a name="UserManageService">**UserManageService.java**</a>

```java
package hurrican.service;

import hurrican.model.User;

public class UserManageService {

    public void doInsert(User user){
        System.out.println("执行添加用户操作……");
    }

    public User doQueryById(Integer id){
        User user = new User();
        System.out.println("根据id查询指定用户信息……");
        return user;
    }

    public boolean doUpdatePassword(User user){
        boolean flag = false;
        System.out.println("修改密码……");
        return flag;
    }

    public void doRemove(){
        System.out.println("删除僵尸用户……");
    }
}
```

<a name="XmlConfigEnhance">**XmlConfigEnhance.java**</a>

```java
package hurrican.service;

import hurrican.model.User;
import org.aspectj.lang.ProceedingJoinPoint;

public class XmlConfigEnhance {

    public void afterReturningAlterData(Object result){
        System.out.println("执行后置通知，正在修改用户信息……");
        if (result instanceof User) {
            System.out.println("原用户信息为：");
            System.out.println(result.toString());
            ((User) result).setId(10);
            ((User) result).setUsername("hurrican");
            ((User) result).setPassword("");
            ((User) result).setEmail("19646366@163.com");
        }
    }

    public boolean chenkUserAuthority(){
        boolean hasAuthority = false;
        System.out.println("执行权限检查操作……");
        return hasAuthority;
    }

    public boolean aroundAdvice(ProceedingJoinPoint point) {
        System.out.println("环绕增强：\t获取用户帐号及密码\n");
        Boolean obj = null;
        try {
            obj = (Boolean) point.proceed(point.getArgs());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println("\n环绕增强：\t返回修改密码操作结果");
        return obj;
    }

    public void afterAdvice(){
        System.out.println("最终通知：\t释放数据库连接");
    }
}

```



```java
  @Test
    public void testBeforeAdvice() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-aspectj.xml");
        UserManageService userManageService = (UserManageService) context.getBean("userSevice");
        userManageService.doRemove();
    }
```



<a name="xml_after">**后置通知**</a>

使用`xml`方式后置通知需要使用`<aop:config>`标签，`<aop:aspect ref="?????">`用于配置切面，**ref** 属性配置`增强方法对象的id` ，`<aop:after-returning>`用于配置**切入点**和**通知**，`method`属性配置**通知**，`pointcut`属性配置**要被增强的方法**

**处理带返回值的被增强方法**  的后置通知

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

    <!-- 注册切面-->
    <bean id="xmlwayEnhance" class="hurrican.service.XmlConfigEnhance"/>
    <bean id="userSevice" class="hurrican.service.UserManageService"/>

    <aop:config>
        <aop:aspect ref="xmlwayEnhance">
            <aop:after-returning method="afterReturningAlterData"
                                 pointcut="execution(* *..UserManageService.doQueryById(..))"
                                 returning="result"/>
        </aop:aspect>
    </aop:config>

    <context:component-scan base-package="hurrican.*"/>
    <aop:aspectj-autoproxy/>
</beans>
```

<a href="#XmlConfigEnhance">**XmlConfigEnhance.java**</a>

<a href="#UserManageService">**UserManageService.java**</a>

```java
@Test
    public void testAfterReturingAdvice() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-aspectj.xml");
        UserManageService userManageService = (UserManageService) context.getBean("userSevice");
        User user = userManageService.doQueryById(1);
        System.out.println("\n用户最新信息为");
        System.out.println(user.toString());
    }
```





<a name="xml_around">**环绕通知**</a>

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

    <!-- 注册切面-->
    <bean id="xmlwayEnhance" class="hurrican.service.XmlConfigEnhance"/>
    <bean id="userSevice" class="hurrican.service.UserManageService"/>

    <aop:config>
        <aop:aspect ref="xmlwayEnhance">
            <aop:around method="aroundAdvice"
                        pointcut="execution(* *..UserManageService.doUpdatePassword(..))"/>
        </aop:aspect>
    </aop:config>
    
    
    <context:component-scan base-package="hurrican.*"/>
    <aop:aspectj-autoproxy/>
</beans>
```



<a href="#XmlConfigEnhance">**XmlConfigEnhance.java**</a>

<a href="#UserManageService">**UserManageService.java**</a>

```java
 public boolean aroundAdvice(ProceedingJoinPoint point) {
        System.out.println("环绕增强：\t获取用户帐号及密码\n");
        Boolean obj = null;
        try {
            obj = (Boolean) point.proceed(point.getArgs());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println("\n环绕增强：\t返回修改密码操作结果");
        return obj;
    }
```





```java
 @Test
    public void testAroundAdvice() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-aspectj.xml");
        UserManageService userManageService = (UserManageService) context.getBean("userSevice");
        userManageService.doUpdatePassword(new User());
    }
```



<a name="xml_final">**最终通知**</a>

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

    <!-- 注册切面-->
    <bean id="xmlwayEnhance" class="hurrican.service.XmlConfigEnhance"/>
    <bean id="userSevice" class="hurrican.service.UserManageService"/>
    
    <aop:config>
        <aop:aspect ref="xmlwayEnhance">
            <aop:after method="afterAdvice"
                       pointcut="execution(* *..UserManageService.*(..))"/>
        </aop:aspect>
    </aop:config>

    <context:component-scan base-package="hurrican.*"/>
    <aop:aspectj-autoproxy/>
</beans>
```



```java
 @Test
    public void testAfterAdvice() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-aspectj.xml");
        UserManageService userManageService = (UserManageService) context.getBean("userSevice");
        userManageService.doUpdatePassword(new User());
        System.out.println("\n- - - - - - - -我是华丽的分割线 - - - - - - - - -\n");
        userManageService.doRemove();
    }
```



```java
 public void afterAdvice(){
        System.out.println("最终通知：\t释放数据库连接");
    }
```

<a href="#XmlConfigEnhance">**XmlConfigEnhance.java**</a>

<a href="#UserManageService">**UserManageService.java**</a>



----

## <a href="#annotation">注解方式完成AOP操作</a>

------

**开启AOP注解扫描**

```xml
<aop:aspectj-autoproxy></aop:aspectj-autoproxy>
```

### <a name="before">前置通知</a>

在要增强的切入点所在的类添加注解**@Aspect**，在切入点上面添加注解`@Before( value = "execution(* 包名.类名.方法名(..)" )`

<a name="beforexml">**spring-aspectj.xml**</a>

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
    <bean id="check" class="hurrican.service.CheckAuthorityEnhance" />
    <bean id="service" class="hurrican.service.NoInterfaceService" />


    <context:component-scan base-package="hurrican.*"/>
    <aop:aspectj-autoproxy />
</beans>
```



<a name="NoInterfaceService">**NoInterfaceService.java**</a>

```java
public class NoInterfaceService {

    public void doRemove(User user){
        System.out.println("删除操作……");
    }

    public void doQuery(){
        System.out.println("执行查询操作……");
    }

    public boolean doUpdateEmail(String email){
        System.out.println("执行绑定邮箱操作……");
        System.out.println("改绑邮箱号为：\t" + email);
        return true;
    }

    public User queryById(){
        System.out.println("根据id查找用户……");
        User user = new User("大王", "helloworld", "123456@qq.com");
        return user;
    }
}
```



```java
@Aspect
public class CheckAuthorityEnhance {

//    @Before(value = "execution(* hurrican.service.NoInterfaceService.do*(..))")
    @Before(value = "execution(* *..NoInterfaceService.do*(..))")
    public boolean chenkUserAuthority(){
        boolean hasAuthority = false;
        System.out.println("执行权限检查操作……");
        return hasAuthority;
    }
}
```



```java
@Test
    public void testBeforeEnhance() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-aspectj.xml");
        NoInterfaceService service = (NoInterfaceService) context.getBean("service");
        service.doRemove(new User());

        System.out.println();

        service.doQuery();
    }
```





`@Before`的**通知（增强逻辑或用于增强的方法）**可以带一个类型为`JoinPoint`的参数

```java
@Before(value = "execution(* *..NoInterfaceService.doUpdateEmail(..))")
    public boolean emailIsValidity(JoinPoint joinPoint){
        boolean validity = false;
        System.out.println("JoinPoint = " + joinPoint.toString());
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            String email = (String) args[0];
            System.out.println("执行邮箱格式匹配操作……");

            Pattern pattern = Pattern.compile("[+/?\\w]+@[\\w]+\\.[\\w]+");
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
                validity = true;
                System.out.println("邮箱格式匹配！");
            } else {
                System.out.println("邮箱格式不正确！");
            }
        }
        return validity;
    }
```

<a href="#NoInterfaceService">**NoInterfaceService.java**</a>

```java
 @Test
    public void testBeforeEnhanceJoinPoint() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-aspectj.xml");
        NoInterfaceService service = (NoInterfaceService) context.getBean("service");
        service.doUpdateEmail("12545?++dd@163.com");
    }
```

<a href="#beforexml">**spring-aspectj.xml**</a>



------

### <a name="after">后置通知</a>

注解方式使用**后置通知**需要使用注解`@AfterReturing`

```java
package hurrican.service;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Aspect
public class CheckAuthorityEnhance {

    @AfterReturning(value = "execution(* *..NoInterfaceService.doQuery())")
    public void returnQueryResult(){
        System.out.println("写日志并返回查询结果");
    }
}

```

```java
 @Test
    public void testAfterReturingEnhance() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-aspectj.xml");
        NoInterfaceService service = (NoInterfaceService) context.getBean("service");
        service.doQuery();
    }
```



<a href="#beforexml">**spring-aspectj.xml**</a>

<a href="#NoInterfaceService">**NoInterfaceService.java**</a>

**运行结果**：

>执行查询操作……
>写日志并返回查询结果

**获取被增强方法的返回结果**

```java
@AfterReturning(value = "execution(* *..NoInterfaceService.queryById(..))", returning = "obj")
    public void setPasswordNull(Object obj){
        System.out.println("后置增强操作：密码清空……");
        if (obj instanceof User) {
            ((User) obj).setPassword("");
            System.out.println("密码已清空！");
        }
    }
```



```java
public User queryById(){
        System.out.println("根据id查找用户……");
        User user = new User("大王", "helloworld", "123456@qq.com");
        return user;
    }
```



```java
 @Test
    public void testGetTesultAfterReturingEnhance() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-aspectj.xml");
        NoInterfaceService service = (NoInterfaceService) context.getBean("service");
        service.queryById();
    }
```

<a href="#beforexml">**spring-aspectj.xml**</a>

<a href="#NoInterfaceService">**NoInterfaceService.java**</a>







------

### <a name="around">环绕通知</a>

注解方式使用**环绕通知**需要使用注解`@Around`，通知方法需要带一个`ProceedingJoinPoint`类型参数，并在方法中调用该参数的`proceed`方法

```java

    @Around(value = "execution(* *..NoInterfaceService.queryById(..))")
    public void aroundAdvice(ProceedingJoinPoint point) {
        System.out.println("环绕增强：\t获取用户id\n");
        Object obj = null;
        try {
            obj = point.proceed(point.getArgs());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println("\n环绕增强：\t释放数据库连接");
    }
```



```java
 @Test
    public void testAroundEnhance() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-aspectj.xml");
        NoInterfaceService service = (NoInterfaceService) context.getBean("service");
        service.queryById();
    }
```

<a href="#beforexml">**spring-aspectj.xml**</a>

<a href="#NoInterfaceService">**NoInterfaceService.java**</a>



-----

### <a name="exection">异常通知</a>

注解方式使用**异常通知**需要使用注解`@AfterThrowing`

```java
@AfterThrowing(value = "execution(* *..NoInterfaceService.*(..))")
    public void execeptionAdvice(){
        System.out.println("被增强的方法出现异常,异常信息已写入日志");
    }
```



-----

### <a name="final">最终通知</a>

注解方式使用**最终通知**需要使用注解`@After`

```java
  @After(value = "execution(* *..NoInterfaceService.doUpdateEmail(..))")
    public void afterAdvice(){
        System.out.println("最终通知：\t返回改绑邮箱结果");
    }
```



```java
 @Test
    public void testAfterEnhance() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-aspectj.xml");
        NoInterfaceService service = (NoInterfaceService) context.getBean("service");
        service.doUpdateEmail("12545?++dd@163.com");
    }
```





-----

<right><a href="#top">**back**</a></right>