# <a name="top">Web应用程序的部署</a>

+ <a href="#jdk">**JDK安装**</a>


+ <a href="#mysql_install">**MySQL安装**</a>







-----

## <a name="jdk">JDK安装</a>

+ 解压 `jdk` 到指定目录

  ```shell
  mkdir -p /home/hurrican/jdk
  cd /home/hurrican/jdk

  # 将 jdk 压缩包解压到 /home/hurrican/jdk 目录
  tar -zxvf jdk-8u144-linux-x64.tar.gz -C ./
  ```

+ 配置 `java` 环境变量

  ```shell
  vim /etc/profile

  # 在最后面追加以下内容，其中jdk解压路径(/home/hurrican/jdk)以各自具体的路径为准
  export JAVA_HOME=/home/hurrican/jdk/jdk1.8.0_181
  export CLASSPATH=.:$JAVA_HOME/lib
  export PATH=JAVA_HOME/bin:PATH

  ```

+ **使配置的环境变量生效**

  ```shell
  source /etc/profile

  # 查看是否成功配置 java 环境变量
  java -version
  ```

+ 配置加快 `Tomcat` 启动的**Java**系统属性

  ```shell
  # 查找 java.security 所在的文件路径
  locate java.security

  # 若未找到 java.security 文件，先检查是否安装了jdk
  # 安装 jdk 还是没有找到可以使用 updatedb 命令刷新一下 Linux 文件数据库
  updatedb

  # 在运行 locate java.security 命令后输出内容示例如下：
  # /home/hurrican/jdk/jdk1.8.0_181/jre/lib/security/java.security

  vim /home/hurrican/jdk/jdk1.8.0_181/jre/lib/security/java.security

  # 搜索 securerandom.source
  #  将 securerandom.source=file:/dev/random 改为 securerandom.source=file:/dev/./urandom
  ```

  ​

    

<p align="right"><a href="#jdk">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a></p>

-----

## <a name="mysql_install">**MySQL安装**</a>





<p align="right"><a href="#mysql_install">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a></p>