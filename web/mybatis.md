## MyBatis

### MyBatis 与 Hibernate

Hibernate提供全面的数据封装机制，是**全自动的**ORM，实现POJO和数据库表之间的映射，以及SQL自动生成和执行

MyBatis是**半自动的**ORM框架，不会自动生成SQL语句，需要自己编写，通过SQL语句映射文件将SQL所需的参数以及返回结果字段映射到指定POJO



**MyBatis特点**

1. 在xml中配置SQL语句实现SQL语句与代码分离，给维护带来便利
2. 可以结合数据库自身特点灵活控制SQL语句，具有更高的查询效率

--------

### MyBatis中configuration 配置 

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>

    <settings>
        <!--当返回行的所有列都是空时，MyBatis默认返回null-->
        <setting name="jdbcTypeForNull" value="NULL"/>

        <!--cacheEnabled - 使全局的映射器启用或禁用缓存-->
        <setting name="cacheEnabled" value="true"/>

    </settings>

  <!--类别名配置，配置后可以通过简单类名代替全限定类名-->
    <typeAliases>
        <typeAlias type="beans.UserInfo" alias="UserInfo"></typeAlias>
    </typeAliases>

    <!-- 配置运行环境-->
    <environments default="mysql">
        <environment id="mysql">
            <!-- MyBatis 中有两种事务管理器类型，分别是：-->
            <!--1. type = "JDBC" (依赖于数据源得到的连接来管理事务范围)-->
            <!--2. type = "MANAGED" (不提交或回滚连接，让容器来管理事务的整个周期)-->
            <transactionManager type="JDBC"/>

            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/conference"/>
                <property name="username" value="root"/>
                <property name="password" value="qwer123456"/>
                <!--poolMaximumActiveConnections – 正在使用连接的数量。默认值:10 -->
                <!--poolMaximumIdleConnections – 任意时间存在的空闲连接数-->
            </dataSource>
        </environment>
    </environments>

    <mappers>
        <mapper resource="userinfoMapper.xml"/>
    </mappers>

</configuration>
```



`#{}`中可以放的内容：

+ 参数对象的属性
+ 随意内容，此时的`#{}`相当于一个**占位符**
+ 接口方法参数类型Map时，`#{}`里可以存放**Map**对象的`key`值
+ 接口方法参数类型Map且**Map对象的value为实体类对象**时，`#{}`里可以存放该实体类对象对应的***属性***
+ 参数的索引号



**延迟加载**

指在进行关联查询时，按照设置延迟规则推迟对**关联对象的select查询**，以减小数据库的压力。`MyBatis只对关联对象的查询有延迟设置`，对主加载对象是直接执行查询语句。



MyBatis根据关联对象查询的select语句的执行时机分为3种类型：

+ 直接加载（执行完主加载对象的select语句，马上执行关联对象的select查询）
+ 侵入式延迟加载（执行对主加载对象的查询时，不会执行对关联对象的查询，当要访问主加载对象详情时才会执行关联的select查询）
+ 深度延迟加载（执行对主加载对象的查询时，不会执行对关联对象的查询，访问主加载对象的详情时也不会执行关联对象的select查询，只有当真正访问关联对象详情时才会执行对关联对象的select查询）



**延迟加载应用要求：**

关联对象的查询与主加载对象的查询是分别进行的select语句，不能使用***多表连接所进行的select查询***



**延迟加载设置：** 在MyBatis主配置文件里的`settings`标签下添加**name**为`lazyLoadingEnabled`的**value**为`true` 的***setting***标签

```xml
<settings>
        <!--当返回行的所有列都是空时，MyBatis默认返回null-->
        <setting name="jdbcTypeForNull" value="NULL"/>

        <!--cacheEnabled - 使全局的映射器启用或禁用缓存-->
        <setting name="cacheEnabled" value="true"/>
  		<!--lazyLoadingEnabled - 全局的延迟加载设置-->
        <setting name="lazyLoadingEnabled" value="true"/>
  		<!--aggressiveLazyLoading - 侵入式延迟加载设置-->
        <setting name="aggressiveLazyLoading" value="flase"/>

</settings>

```



-----

### 查询缓存

MyBatis查询缓存的作用域是根据`映射配置文件mapper.xml`的**namespace**划分的，相同的namespace的mapper查询数据存放在同一个缓存区域，不同的namespace下的数据互不干扰。

***无论是一级缓存还是二级缓存都是按照namespace进行分别存放的***



MyBatis查询缓存机制根据缓存区的作用域（生命周期）可分为两种

+ 一级查询缓存
  + 一级查询缓存是基于`org.apache.ibatis.cache.impl.PerpetualCache`类的HashMap本地缓存，其生命周期为整个**SqlSession**，同一个`SqlSession`执行两次相同的sql查询，第一次执行完毕后会将结果写入缓存，第二次查询时直接从缓存里查询。***MyBatis一级查询缓存默认开启且不能关闭***
  + **MyBatis中一级查询缓存读取数据的依据是：**mapper配置文件 `查询标签select`的**id**属性和sql语句；**Hibernate**中查询缓存的依据是：查询结果对象的id
+ 二级查询缓存
  + 使用二级缓存的目的不是为了在多个查询间共享查询结果（**Hibernate**就是为了共享查询结果而设计的），而是为了防止同一个查询的返回执行
  + **二级缓存清空的实质是对所查找的key对应的value置为null**，并不是删除Map对象中存放的`Entry`
    + 使用了缓存要到数据库中查找的情况有两种：
      1. 缓存Map对象中没有要查找的 `key`
      2. 缓存中 `key` 值存在，但`value`为  ***null***

------

开启内置二级缓存的步骤：

+ 对实体进行序列化，**被缓存的Bean要求实现**  `java.io.Serializable`接口
+ 在映射文件中添加`<cache/>`标签


**二级缓存使用Demo**

-----
 <br/>
实体类定义：
![](https://github.com/HurricanGod/Home/blob/master/img/%E5%86%85%E7%BD%AE%E4%BA%8C%E7%BA%A7%E6%9F%A5%E8%AF%A2%E7%BC%93%E5%AD%98%E5%AE%9E%E4%BD%93%E7%B1%BB.png)
 
```java
// 接口定义
public interface IUsrImageDao {
    Set<UsrImage> queryImageInfoByUserId(Integer uid);
}

```

```java
// 测试类
@Test
    public void queryImageInfoByUserIdTest() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis.xml");
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        // 创建第一个SqlSession并创建dao实例
        SqlSession session = factory.openSession(true);
        IUsrImageDao dao = session.getMapper(IUsrImageDao.class);
        System.out.println("二级缓存测试\n第一次查询");
        Set<UsrImage> images = dao.queryImageInfoByUserId(4);
        for (UsrImage element : images) {
            System.out.println(element.toString());
        }
        
        // 执行完成后关闭SqlSession        
        session.close();
        
        System.out.println("第二次查询");
        // 再次创建SqlSession对象并获取dao实例
        session = factory.openSession(true);
        dao = session.getMapper(IUsrImageDao.class);
        // 再次执行一样的查询
        images = dao.queryImageInfoByUserId(4);
        for (UsrImage element : images) {
            System.out.println(element.toString());
        }

    }
```

**运行结果**
![MyBatis二级缓存验证运行结果](https://github.com/HurricanGod/Home/blob/master/img/%E5%86%85%E7%BD%AE%E4%BA%8C%E7%BA%A7%E6%9F%A5%E8%AF%A2%E7%BC%93%E5%AD%98.png)

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="dao.IUsrImageDao">

    <cache eviction="LRU" size="512"/>
    <!--<cache/>-->
    <select id="queryImageInfoByUserId" resultMap="imageMapper" useCache="true">
        select name,url,image.id,email,username
        from image,account
        where user_id = #{id} and user_id = account.id
    </select>

    <resultMap id="imageMapper" type="UsrImage">
        <id column="id" property="image_id"/>
        <result column="name" property="image_name"/>
        <result column="url" property="image_url"/>
        <result column="date" property="upload_date"/>
        <association property="user" javaType="UserInfo">
            <id column="id" property="id"/>
            <result column="username" property="username"/>
            <result column="pwd" property="pwd"/>
            <result column="email" property="email"/>
        </association>
    </resultMap>
</mapper>
```


**二级缓存的使用原则**

+ 多个**namespace**不要操作同一张表
+ 不要在关联关系表上增删改操作
+ 查询多于修改时使用二级缓存


------


**第三方缓存**  ——  `ehcache` 使用步骤

+ 添加`ehcache` 依赖的jar包或在Maven添加依赖

  ```xml
   <!-- https://mvnrepository.com/artifact/org.mybatis.caches/mybatis-ehcache -->
      <dependency>
        <groupId>org.mybatis.caches</groupId>
        <artifactId>mybatis-ehcache</artifactId>
        <version>1.1.0</version>
  </dependency>

      <!-- https://mvnrepository.com/artifact/net.sf.ehcache/ehcache-core -->
      <dependency>
        <groupId>net.sf.ehcache</groupId>
        <artifactId>ehcache-core</artifactId>
        <version>2.6.6</version>
  </dependency>
  ```

+ 在mapper配置文件里的`<cache/>` 标签里的 **type** 属性指明`ehcache`的类型

  ```xml
  <cache type = "org.mybatis.caches.ehcache.EhcacheCache" />
  ```


+ 在`classpath`下添加配置文件,名称为`ehcache.xml`

  ```xml
  <ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../config/ehcache.xsd">
    <diskStore path="java.io.tmpdir"/>
    <defaultCache
              maxElementsInMemory="10000"
              eternal="false"
              timeToIdleSeconds="120"
              timeToLiveSeconds="120"
              maxElementsOnDisk="10000000"
              diskExpiryThreadIntervalSeconds="120"
              memoryStoreEvictionPolicy="LRU">
          <persistence strategy="localTempSwap"/>
      </defaultCache>
  </ehcache>
  ```

-------

`<cache />`语句的效果：

+ 映射文件中所有的select语句将被缓存
+ 执行映射文件中任意`insert`、`update`、`delete`语句缓存将被刷新
+ 缓存采用**LRU**算法回收
+ 缓存不会被设定的时间所清空
+ MyBatis的缓存是**可读/可写**的缓存，意味着对象检索不是共享的，而是可以安全地被调用者修改，而不会干扰其它线程所做的潜在修改

**缓存的回收策略有：**

- LRU
- FIFO
- SOFT -软引用（基于垃圾回收器状态和软引用规则的对象）
- WEAK-弱引用（强制性地移除基于垃圾收集器状态和弱引用规则的对象）

```xml
<!--  创建1个FIFO缓存，每60s清空缓存，存储512个对象或列表引用，返回结果只读 -->
<cache eviction="FIFO" flushInterval="60000" size="512" readOnly="true"/>
```



**一级缓存和二级缓存的不同点**：

+ 一级缓存的生命周期为**SqlSession**，一旦关闭**SqlSession**缓存就无效
+ 二级缓存是与整个应用同步，一级缓存是同一个线程共享，二级缓存是多个线程共享

-----

查询缓存的底层实现是Map，value里保存查询结果，key为查询依据；MyBatis中**增删改**操作（无论有没有提交）都会刷新一级缓存，把缓存清空。





 



