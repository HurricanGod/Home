### Spring
Spring核心容器包括**Core**、**Beans**、**Context**、**Expression Language**模块
+ Core模块：封装框架依赖最底层部分，包括资源访问、类型转换及一些常用工具
+ Beans模块：提供框架基础部分，包括**控制反转IOC**和**依赖注入DI**.BeanFactory是容器核心，本质是“工厂设计模式”的实现，而且无需编程实现**单例设计模式**，单例完全由容器控制，而且提倡面向接口编程，而非面向实现编程；所有应用程序对象与对象间的关系由框架管理，从而真正从程序逻辑中把维护对象之间的依赖关系提取出来，依赖关系都由**BeanFactory**维护。
+ Context模块：以Core和Beans为基础，集成Beans模块功能并添加资源绑定、数据验证、国际化、JavaEE支持、容器生命周期、事件传播等；**核心接口的ApplicationContext**.
  +EL模块：提供强大的表达式语言支持，支持访问和修改属性值，方法调用，支持访问及修改数组、容器和索引器，命名变量，支持算术逻辑运算，支持从Spring容器获取Bean，也支持列表投影、选择和一般列表聚合


------

#### AOP模块
提供符合AOP Alliance规范的面向切面的编程实现，可以动态地将想要的功能添加到代码中

#### Aspects模块
提供对Aspectj的集成

#### 数据访问/集成模块
该模块包括JDBC、ORM、OXM、JMS和事务管理

+ 事务管理：该模块用于Spring管理事务，只要是Spring管理对象都能得到Spring管理事务的好处，无需在代码中进行事务控制，而且支持编程和声明性的事务管理

+ JDBC模块：提供JDBC样例模版，使用这些模版能消除传统冗长的JDBC编码还有必须的事务控制

+ ORM模块：提供“对象-关系”映射框架的无缝集成

+ OXM模块：提供一个对**Object/XML**映射实现，将Java对象映射成xml数据，或者将xml数据映射成Java对象

+ JMS模块：提供一套“消息生产者、消息消费者”模版用于更加简明的使用JMS，JMS用于两个应用程序之间，或分布式系统中发送信息、进行异步通信


#### Web/Remoting模块
主要包含Web、Web-Servlet、Web-Struts、Web-Porlet模块

+ Web模块：提供基础web功能，例如,多文件上传、集成IOC容器、远程过程访问

+ Web-Servlet模块：提供Spring MVC Web框架实现。Spring MVC 框架提供了基于注解的请求资源注入、更简单的数据绑定、数据验证及一套易用的jsp 标签

#### Test模块
Spring支持**Junit**和**TestNG**测试框架，而且还额外提供了一些基于Spring的测试功能，比如测试WEb框架时，模拟Http请求功能

#### Maven
Maven是一个项目管理和综合工具,Maven提供了开发人员构建1个完整的生命周期框架，Maven使用标准的目录结构构建生命周期。





-------------

### 基于XML的DI

**注入分类**：

+ 设值注入
+ 构造注入



#### Spring Bean Scopes ——Bean的作用域

1. singleton（单例模式），由Ioc容器返回唯一的Bean实例
2. prototype（原型模型），被请求时，每次返回1个**新的bean实例**
3. request ，每个**http request**请求返回**唯一的**Bean实例
4. session，每个**http session**返回1个唯一的Bean实例
5. globalSession，http session全局Bean实例

> 默认情况下，作用域是单例



### Bean后处理器

Bean后处理器是一种特殊的Bean，Bean中所有Bean在初始化时都会自动执行该类的两个方法，需要自定义**Bean后处理器**需要实现`BeanPostProcessor`接口





`<bean/>标签` 的**id**属性与**name**属性：一般情况下，命名bean使用**id**属性，而不是用**name**属性，在没有id属性的情况下。***name属性与id属性作用相同***；当`</bean>`中含有一些**特殊字符**时，就需要使用name属性。