## Tomcat



### 目录与文件

+ **术语**
  + `$CATALINA_HOME` → `catalina_home` → 指**Tomcat安装的根目录**
    + `$CATALINA_HOME/ReadMe.txt` 表示Tomcat安装跟目录下的`ReadMe.txt`文件
  + `$CATALINA_BASE` → 可以为每个Tomcat实例定义1个`$catalina_base`，这样就可以**启动多个Tomcat实例**
    + 未配置多个Tomcat实例时， `catalina_home`相当于`$catalina_base`


+ **关键目录**

  + `/bin`
    + 存放用于启动及关闭的文件
  + `/conf`
    + 配置文件及相关`DTD`文件，其中最重要的文件是`server.xml`，是Tomcat的主配置文件
  + `/log`
    + 日志文件默认目录

  ​	 

  + `webapps`
    + 存放web应用相关文件

​	

