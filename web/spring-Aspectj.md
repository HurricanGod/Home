****Spring的AOP操作****

1. 在Spring里面进行aop操作，使用aspectj实现

   1. aspectj不是spring的一部分，和spring一起使用进行aop操作
   2. Spring2.0以后新增了对AspectJ的支持

   ​

2. 使用aspectj实现aop的两种方式

   1. 基于aspectj的xml配置
   2. 基于aspectj的注解方式

   ​

3. **AOP操作需要的jar包**

   1. aopalliance-1.0.jar
   2. aspectjweaver-1.8.*.jar
   3. spring-aop-4.2.0.RELEASE.jar
   4. spring-aspects-4.2.0.RELEASE.jar

------

### 使用表达式配置切入点

- **常用表达式**

```
execution(<访问修饰符>?<返回类型> <方法全路径名>(<参数>)<异常>)
1.execution(* package.class.method(..))	表示某个类中的某个方法
2.execution(* package.class.*(..))	表示某个类中的所有方法
3.execution(* *.*(..))	表示所有方法
```

- **配置文件写法：**

```
<aop:config >        <!-- 配置切入点 -->        <aop:pointcut expression="execution(* firm.manage.Employee.introduce(..))" id="pointcut1" />                <!-- 配置切面，把增强用到方法上面 -->        <aop:aspect ref="department">            <!-- ref属性配置增强的类型，method属性配置增强类里的要增强的方法， pointcut-ref属性配置增强类引用的切入点-->            <!-- before：增强类里的show方法作为前置 -->            <aop:before method="show" pointcut-ref="pointcut1"/>            <aop:after method="afterShow" pointcut-ref="pointcut1"/>        </aop:aspect></aop:config><!--配置完成后show()方法将在firm.manage.Employee.introduce()方法之前执行，afterShow()方法将在firm.manage.Employee.introduce()方法之后执行-->

```

### 注解方式完成AOP操作

------

1. 开启AOP注解扫描

   ```
   <aop:aspectj-autoproxy></aop:aspectj-autoproxy>

   ```

   ​

2. 在要增强的切入点所在的类添加注解**@Aspect**，在切入点上面添加注解`@Before( value = "execution(* 包名.类名.方法名(..)" )`



<right><a href="#top">**back**</a></right>