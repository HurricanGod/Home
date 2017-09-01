##  <a name="top">spring事务管理</a>

****

Spring事务管理的方式：

+ 编程式事务管理


+ 声明式事务管理
  + <a href="#xml_tr">基于xml的配置文件`Aspectj` 的AOP实现</a>
  + <a href="#annotation_tr">基于注解实现</a>

-----

**事务管理的api：**

Spring事务管理高层抽象主要包括3个接口

- PlatformTransactionManager（事务管理器）
- TransactionDefinition（事务定义信息：隔离、传播、超时、只读）
- TransactionStatus（事务具体运行状态）




`PlatformTransactionManager`接口的两个常用实现类：

+ `DataSourceTransactionManager` ：使用**JDBC** 或 **iBatis**进行持久化数据时使用
+ `HibernateTransactionManager` ：使用**Hibernate**进行持久化数据时使用



**Spring的回滚方式**

Spring事务默认的回滚方式为：发生运行时异常时回滚，发生受查异常时提交。



-----

### <a name="xml_tr">xml方式`Aspectj`的AOP配置事务管理</a>

- <a href="#IPayDao.java">**IPayDao.java**</a>
- <a href="#IPayDao.xml">**IPayDao.xml**</a>
- <a href="#PayService.java">**PayService.java**</a>
- <a href="#exception.java">**UserNoExistException.java**</a>
- <a href="#runexception.java">**BalanceInsufficiencyException.java**</a>
- <a href="#spring-aop.xml">**spring-tx-aop.xml**</a>



<a name="spring-aop.xml">**spring-tx-aop.xml**</a>

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

    <!-- c3p0数据源(properties文件配置)-->
    <context:property-placeholder location="classpath:jdbc.properties"/>

    <bean id="c3p0Datasource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
              <property name="driverClass" value="${jdbc.driver}"/>
              <property name="jdbcUrl" value="${jdbc.url}"/>
              <property name="user" value="${jdbc.user}"/>
              <property name="password" value="${jdbc.password}"/>
       </bean>

    <!-- 注册MyBatis的SqlSessionFactory-->
    <bean id="mysqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="c3p0Datasource"/>
        <property name="configLocation" value="classpath:mybatis.xml"/>
    </bean>

    <!-- 注册IPayDao接口-->
    <bean id="payDao" class="org.mybatis.spring.mapper.MapperFactoryBean">
        <property name="sqlSessionFactory" ref="mysqlSessionFactory"/>
        <property name="mapperInterface" value="modeldao.IPayDao"/>
    </bean>

    <!-- 创建PayService实例-->
    <bean id="payService" class="service.PayService">
        <property name="dao" ref="payDao"/>
    </bean>
    <bean id="transactionManager"
          class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="c3p0Datasource"/>
    </bean>

    <!-- 注册事务通知-->
    <tx:advice id="interceptor" transaction-manager="transactionManager">
        <tx:attributes>
            <tx:method name="transferring" isolation="DEFAULT" propagation="REQUIRED"
                    rollback-for="UserNoExistException"/>
        </tx:attributes>
    </tx:advice>

    <aop:config>
        <aop:pointcut id="transferMoney" expression="execution(* *..PayService.transferring(..))"/>
        <aop:advisor advice-ref="interceptor" pointcut-ref="transferMoney"/>
    </aop:config>
</beans>
```
![](https://github.com/HurricanGod/Home/blob/master/web/spring_img/tx/aop-tx.png)
<a href="#spring-aop.xml">back to  `Aspectj` 的AOP配置事务管理</a>



------

### <a name="annotation_tr">注解方式配置事务管理</a>

1. 配置事务管理器

   ```xml
   <!-- 注册事务管理器-->
   <bean id="transactionManager"  class="org.springframework.jdbc.datasource.DataSourceTransactionManager">  
    <property name="dataSource" ref="dataSource"></property>
   </bean>
   ```

   ​

2. 配置事务注解

   ```xml
   <!--注册事务注解驱动-->
   <tx:annotation-driven transaction-manager="transactionManager" />
   ```

   ​

3. 在要使用事务的方法所在上面添加`@Transactional`注解，设置数据库隔离级别为`isolation`的值为`Isolation.DEFAULT`——**Isolation.DEFAULT(使用数据库底层的默认隔离级别)**；设置事务传播类型`propagation`为`Propagation.REQUIRED` ；设置`rollbackFor`的值，当有`rollbackFor`设定的**可查异常**发生时进行回滚操作（**运行时异常默认是回滚的**）


```java
@Transactional(isolation = Isolation.DEFAULT,
            propagation = Propagation.REQUIRED,
            rollbackFor = {UserNoExistException.class})
```





### <a name="sm-tx">基于**Spring+MyBatis注解的事务管理**示例如下：</a>

+ <a href="#spring-tx.xml">**spring-tx.xml**</a>
+ <a href="#IPayDao.java">**IPayDao.java**</a>
+ <a href="#IPayDao.xml">**IPayDao.xml**</a>
+ <a href="#PayService.java">**PayService.java**</a>
+ <a href="#exception.java">**UserNoExistException.java**</a>
+ <a href="#runexception.java">**BalanceInsufficiencyException.java**</a>
+ <a href="#PayServiceTest.java">**PayServiceTest.java**</a>



----

<a name="spring-tx.xml">**spring-tx.xml**</a>

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

    <!-- c3p0数据源(properties文件配置)-->
    <context:property-placeholder location="classpath:jdbc.properties"/>

    <bean id="c3p0Datasource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
              <property name="driverClass" value="${jdbc.driver}"/>
              <property name="jdbcUrl" value="${jdbc.url}"/>
              <property name="user" value="${jdbc.user}"/>
              <property name="password" value="${jdbc.password}"/>
       </bean>

    <!-- 注册MyBatis的SqlSessionFactory-->
    <bean id="mysqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="c3p0Datasource"/>
        <property name="configLocation" value="classpath:mybatis.xml"/>
    </bean>

    <!-- 注册IPayDao接口-->
    <bean id="payDao" class="org.mybatis.spring.mapper.MapperFactoryBean">
        <property name="sqlSessionFactory" ref="mysqlSessionFactory"/>
        <property name="mapperInterface" value="modeldao.IPayDao"/>
    </bean>

    <!-- 创建PayService实例-->
    <bean id="payService" class="service.PayService">
        <property name="dao" ref="payDao"/>
    </bean>


    <bean id="transactionManager"
          class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="c3p0Datasource"/>
    </bean>

    <tx:annotation-driven transaction-manager="transactionManager"/>

</beans>
```

<a href="#sm-tx">**back to Spring+MyBatis注解的事务管理**</a>



-----

<a name="IPayDao.java">**IPayDao.java**</a>

```java
package modeldao;

import beans.BankAccount;

public interface IPayDao {
    void update(Double moneys, Integer id);

    BankAccount selectById(Integer id);
}
```



<a href="#sm-tx">**back to Spring+MyBatis注解的事务管理**</a>

<a href="#xml_tr">back to  `Aspectj` 的AOP配置事务管理</a>

-----

<a name="IPayDao.xml">**IPayDao.xml**</a>

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="modeldao.IPayDao">

    <update id="update" >
        update bank_account
        set balance = balance + #{0}
        where user_id = #{1}
    </update>

    <select id="selectById" resultMap="bankAccountMapper">
        select user_id,balance
        from bank_account
        where user_id = #{0}
    </select>

    <resultMap id="bankAccountMapper" type="BankAccount">
        <id column="user_id" property="user_id"/>
        <result column="balance" property="balance"/>
    </resultMap>
</mapper>
```

<a href="#sm-tx">**back to Spring+MyBatis注解的事务管理**</a>

<a href="#xml_tr">back to  `Aspectj` 的AOP配置事务管理</a>

------

<a name="PayService.java">**PayService.java**</a>

```java
package service;

import beans.BankAccount;
import hurrican.customexception.BalanceInsufficiencyException;
import hurrican.customexception.UserNoExistException;
import modeldao.IPayDao;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class PayService {
    private IPayDao dao;

    /**
     * 注解字段-isolation：隔离级别，Isolation.DEFAULT(使用数据库底层的默认隔离级别)
     * 注解字段-propagation：事务传播类型
     * 注解字段-rollbackFor：指定回滚的异常类
     * 
     * @throws UserNoExistException ---- 继承 Exception 的异常
     * @throws BalanceInsufficiencyException --继承 RunException 的异常
     */

    @Transactional(isolation = Isolation.DEFAULT,
            propagation = Propagation.REQUIRED,
            rollbackFor = {UserNoExistException.class})
    public boolean transferring(Integer srcId, Double money, Integer dscId)
            throws UserNoExistException,BalanceInsufficiencyException {
        boolean flag = true;
        BankAccount account1 = dao.selectById(srcId);
        BankAccount account2 = dao.selectById(dscId);

        if (account1 != null) {
            if (account1.getBalance() - money < 0) {
                throw new BalanceInsufficiencyException("账户余额不足！");
            }
            dao.update(-money, srcId);

            if (account2 != null) {
                dao.update(money, dscId);
            } else {
                throw new UserNoExistException("用户不存在!");
            }

        } else {
            throw new UserNoExistException("用户不存在!");
        }

        return flag;
    }

    public PayService() {
    }

    public void setDao(IPayDao dao) {
        this.dao = dao;
    }
}
```



<a href="#sm-tx">**back to Spring+MyBatis注解的事务管理**</a>

<a href="#xml_tr">back to  `Aspectj` 的AOP配置事务管理</a>

----

<a name="PayServiceTest.java">**PayServiceTest.java**</a>

```java
public class PayServiceTest {

    @Test
    public void transferringTest() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-tx.xml");
        PayService payService = (PayService) context.getBean("payService");
        try {
            payService.transferring(3, (double) 800, 4);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
```



<a href="#sm-tx">**back to Spring+MyBatis注解的事务管理**</a>

-----

<a name="exception.java">**UserNoExistException.java**</a>

```java
package hurrican.customexception;

public class UserNoExistException extends Exception {

    @Override
    public String getMessage() {
        return "user is not exist\n" + super.getMessage();
    }

    public UserNoExistException(String message){
        super(message);
    }

    public UserNoExistException() {
    }
}
```



<a href="#sm-tx">**back to Spring+MyBatis注解的事务管理**</a>

<a href="#xml_tr">back to  `Aspectj` 的AOP配置事务管理</a>

----

<a name="runexception.java">**BalanceInsufficiencyException.java**</a>

```java
package hurrican.customexception;

public class BalanceInsufficiencyException extends RuntimeException {

    public BalanceInsufficiencyException(String message){
        super(message);
    }

    public BalanceInsufficiencyException() {
    }
    
    @Override
    public String getMessage() {
        return "Balance Insufficiency" + super.getMessage();
    }
}
```
![](https://github.com/HurricanGod/Home/blob/master/web/spring_img/tx/anno-1.png)

![](https://github.com/HurricanGod/Home/blob/master/web/spring_img/tx/anno-2.png)
<a href="#sm-tx">**back to Spring+MyBatis注解的事务管理**</a>

<a href="#xml_tr">back to  `Aspectj` 的AOP配置事务管理</a>
