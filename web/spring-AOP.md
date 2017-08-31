## AOP

**AOP原理**：底层采用动态代理实现，有`JDK动态代理` 和 `CGLIB动态代理` 两种。
![](https://github.com/HurricanGod/Home/blob/master/web/spring_img/xml-aop/AOP%E5%8E%9F%E7%90%86.png)

```java
public class A
{
  	public void add()		{	}
  	public void update()  	{	}
  	public void delete()	{	}
  	public void findAll()	{	}
}
```

- **连接点** ：类里面可以被增强的方法称为连接点
- **切入点** ：在实际操作中，如果add方法被增强了，那么实际增强的方法称为切入点
- **通知/增强** ：增强的逻辑称为增强；比如拓展了功能，则这个功能称为增强
  - 前置通知：在方法前执行
  - 后置通知：在方法后执行
  - 异常通知：方法出现异常
  - 最终通知：在后置之后执行
  - 环绕通知：在方法之前和之后执行
- **切面** ：泛指交叉业务逻辑，常用的切面有`通知`和`顾问`，把增强应用到具体方法上面，过程称为切面（把增强用到切入点过程）

-------

**Spring的AOP操作**

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

--------

### 使用表达式配置切入点

+ **常用表达式**

```
execution(<访问修饰符>?<返回类型> <方法全路径名>(<参数>)<异常>)
1.execution(* package.class.method(..))	表示某个类中的某个方法
2.execution(* package.class.*(..))	表示某个类中的所有方法
3.execution(* *.*(..))	表示所有方法
```



+ **配置文件写法：**

```xml
<aop:config >
		<!-- 配置切入点 -->
		<aop:pointcut expression="execution(* firm.manage.Employee.introduce(..))" id="pointcut1" />
		
		<!-- 配置切面，把增强用到方法上面 -->
		<aop:aspect ref="department">
			<!-- ref属性配置增强的类型，method属性配置增强类里的要增强的方法， pointcut-ref属性配置增强类引用的切入点-->
			<!-- before：增强类里的show方法作为前置 -->
			<aop:before method="show" pointcut-ref="pointcut1"/>
			<aop:after method="afterShow" pointcut-ref="pointcut1"/>
		</aop:aspect>
</aop:config>
<!--配置完成后show()方法将在firm.manage.Employee.introduce()方法之前执行，afterShow()方法将在firm.manage.Employee.introduce()方法之后执行
-->
```



### 注解方式完成AOP操作

****

1. 开启AOP注解扫描

   ```xml
   <aop:aspectj-autoproxy></aop:aspectj-autoproxy>
   ```

   ​

2. 在要增强的切入点所在的类添加注解**@Aspect**，在切入点上面添加注解`@Before( value = "execution(* 包名.类名.方法名(..)" )`

   ​

