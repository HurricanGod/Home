## Maven

**主要功能：**

1. 管理依赖
2. 项目构建

构建过程：

+ 清理 —— `clean`
+ 编译 —— `compile`
+ 测试 —— `test`
+ 报告 ——
+ 打包 —— `package`
+ 部署 —— `deploy`

----

Windows测试maven**环境配置成功**的方法：命令行下运行`mvn -v`



**maven**有两个配置文件：

+ 用户配置
+ 全局配置（一般放在maven的安装目录下的conf文件夹里，名字为`setting.xml`）

### 用户配置

用户配置存放在`~/.m2/repository/setting.xml`，**setting.xml**一开始是没有的，需要自己新建或把全局配置里的`setting.xml`复制过去
![](https://github.com/HurricanGod/Home/blob/master/img/maven1.png)

**注**

如果用户配置文件不存在，则使用全局配置



----

## maven工程结构

```
Project
	|--src（源码包）
		|--main（正常的源码包）
			|--java（.java文件的目录）
			|--resources（资源文件的目录）
		|--test（测试的源码包）
			|--java
			|--resources
	|--target（class文件、报告等信息存储的地方）
	|--pom.xml（maven工程的描述文件）

```



| 依赖范围(Scope) | 对于主代码classpath有效 | 对于测试代码classpath有效 | 被打包，对于运行时classpath有效 |             例子             |
| :---------: | :--------------: | :---------------: | :------------------: | :------------------------: |
|   compile   |        Y         |         Y         |          Y           |           log4j            |
|    test     |        --        |         Y         |          --          |           junit            |
|  provided   |        Y         |         Y         |          --          |        servlet-api         |
|   runtime   |        --        |        --         |          Y           | JDBC Driver Implementation |



**依赖范围**`scope`用来控制依赖和编译，测试，运行的classpath的关系，主要的3种依赖关系为：

1. `compile`： 默认依赖范围，对于**编译、测试和运行** 3种classpath都有效
2. `test`： 测试依赖范围，只对于测试classpath有效
3. `provided`： 已提供依赖范围。对于编译，测试的classpath都有效，但对运行无效，**打包时不把依赖加入到包里**；例如servlet-api，服务器容器里默认有这个jar包，因此项目发布时不需要把本地的`servlet-api`一起打包



###  依赖冲突

在Maven中存在两种冲突方式：

1. 跨pom文件冲突
   + MavenB项目依赖MavenA项目，MavenC项目依赖MavenB项目，MavenA项目使用的mysql驱动为5.1.10，MavenB项目使用的mysql驱动为5.1.20，那么MavenC项目的依赖的mysql驱动默认为5.1.20版本
2. 同一个pom文件中的冲突
   + 后面的依赖会覆盖前面同名的依赖

```xml
<dependency>
	<groupId>com.hurrican</groupId>
	<artifactId>MavenProjectName</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<option>true</option>
</dependency>
```

`option`b标签用于表示依赖是否会传递下去，默认为**false**，表示会传递下去



### 排除依赖

`exclusions`标签可以进行排除依赖

```xml
<exclusions>
  <exclusion>
    <groupId>servlet</groupId>
    <artifactId>servlet-api</artifactId>
  </exclusion>
</exclusions>
```



### 插件

**指定编译的jdk版本**

```xml
<plugins>
  <!-- 编译插件，指定编译的jdk版本-->
  <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
      <source>1.7</source>
      <target>1.7</target>
      <encoding>UTF-8</encoding>
    </configuration>
  </plugin>
</plugins>
```



Maven的web工程使用`Tomcat`插件

```xml
<plugins>
  <plugin>
    <groupId>org.apache.tomcat.maven</groupId>
    <artifactId>tomcat7-maven-plugin</artifactId>
    <configuration>
     <port>8080</port>
      <path>/</path>
    </configuration>
  </plugin>
</plugins>
```

**启动**： `tomcat[n]:run`(n表示Tomcat版本)



### 父工程统一管理版本号

父工程`dependencyManagement`标签用于管理的依赖，实质并没有真正依赖，它只是管理依赖的版本；**子工程**的`denpendency`标签不用`version`标签确定依赖的**版本号**，版本号由父工程确定



------

## Maven仓库管理

**仓库分类**：

+ 本地仓库
  + 默认在`~/.m2/repository`，如果有用户配置则在用户指定的路径里
+ 远程仓库
  + 中央仓库（不包含有版本的jar包，http://repo1.maven.org/maven2 ）
  + 私服
  + http://mvnrepository.com/ (常用)
  
  
  
  ----
  ## Maven命令
  ```sh
  clean package -Dmaven.test.skip=true -P product
  ```
  + `clean` —— 清除上次构建结果，保证本次构建不受影响
  + `-U参数` —— 强制让Maven检查所有SNAPSHOT依赖更新，确保集成是最新的状态
  + `-e参数` —— 构建出现异常时打印完整的**stack trace**
  + `-P参数` —— 用于激活pom.xml配置中`<profiles>`标签下的**profile**
  
  
