# <a name="top">Docker</a>

+ <a href="#container">**容器**</a>
  + <a href="#create-container">**创建容器**</a>
  + <a href="#list-container">**列出容器**</a>
  + <a href="#enter-container">**进入容器**</a>
  + <a href="#stop-container">**停止容器**</a>
  + <a href="#inspect">**获取容器元数据**</a>
  + <a href="#docker-cp">**容器与宿主机间的文件复制**</a>
  + <a href="#docker-logs"> **看容器日志**</a>


+ <a href="#image">**镜像**</a>
  + <a href="#search-image">搜索镜像 </a>
  + <a htrf="#pull-image">拉取镜像</a>
  + <a href="#see-image">查看本地镜像</a>
  + <a href="#build-image">构建镜像</a>
  + <a href="#delete-image">删除镜像</a>


+ <a href="#dockfile">**Dockerfile**</a>




----

## <a name="container">容器</a>

`docker`容器在命令执行完后会自动退出，容器的生命周期取决于命令执行所需的时间，容器中执行的bash能够长期运行就能保证容器不会退出。



### <a name="create-container">创建容器</a>

**命令格式** ： `docker run [options] IMAGE [command] [args...]`

***常用options*** ：

+ `-d`：  指定容器运行于前台还是后台
+ `-i`： 打开STDIN，用于控制台交互
+ `-t`： 分配tty设备，该可以支持终端登录，默认为false
+ `-w`： 指定容器内部的工作目录
+ `-e`： 指定环境变量，容器中可以使用该环境变量
+ `-p`： 指定端口映射
+ `-h`： 指定容器的主机名
+ `-v`： 给容器挂载存储卷，挂载到容器的某个目录，**容器对数据卷的修改将立即存储到宿主机的文件系统上**
+ `--name=${name}`： 指定容器名字，`${name}`即为要指定的名字。后续可以通过名字进行容器管理，links特性需要使用名字
+ `--link=[]`： 指定容器间的关联，使用其他容器的IP、env等信息
+ `--privileged=false`： 指定容器是否为特权容器，特权容器拥有所有的capabilities
+ `--cidfile=""`： 运行容器后，在指定文件中写入容器PID值，一种典型的监控系统用法
+ `--net="bridge"`： 容器网络设置，bridge 使用docker daemon指定的网桥



```shell
#通过镜像redis-cluster-4.0.10创建名为redis-6380的容器，映射到宿主机端口为6380
docker run -d --name redis-6380 -p  6380:6379 redis-cluster-4.0.10

# 创建可以进入命令行的容器
docker run -it -d --name redis-6381 -p  6381:6379 redis-cluster-4.0.10


# 指定宿主机 /mnt/log 目录与容器里的 /var/log/redis 目录共享
docker run -it -d --name redis-6382 -p  6382:6379  -v /mnt/log/:/var/log/redis redis-cluster-4.0.10

# 创建 → 运行  → 进入容器
docker run -it -d --name redis-6383 -p  6383:6379  -v /mnt/log/:/var/log/redis redis-cluster-4.0.10 /bin/bash

# 将宿主机的 /etc/localtime 和 /etc/timezone 挂载到容器内共享
# 目的：保证容器内和宿主机的时间一致
docker run -itd --name redis-6386 -p 6386:6379 -v /etc/localtime:/etc/localtime -v /etc/timezone:/etc/timezone 55a493c67a70 
```





**命令格式** ： `docker create [OPTIONS] IMAGE [COMMAND] [ARG...] `

`docker create`只创建可写的容器并**不启动容器**



<p align="right"><a href="#container">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

-------

### <a name="list-container">**列出容器**</a>

**命令格式** ：`docker ps [OPTIONS]`

***Options*** ：

+ `-a`： 列出所有容器，包括未运行的容器
+ `-n`： 列出最近创建的n个容器




### <a name="stop-container">**停止容器**</a>

**命令格式** ：`docker stop [OPTIONS] CONTAINER-ID`

**强制停止容器**： `docker kill [OPTIONS] CONTAINER-ID`  

<p align="right"><a href="#container">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

----

### <a name="start-container">**启动容器**</a>

**命令格式** ：`docker start CONTAINER-ID`



### <a name="restart-container">**重启容器**</a>

**命令格式** ：`docker restart CONTAINER-ID`



<p align="right"><a href="#container">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

----

### <a name="enter-container">**进入容器**</a>

#### 方式1

**命令格式** ：`docker attach container-id`

***退出方式*** ： `ctrl + p` +  `ctrl + q`

![](https://github.com/HurricanGod/Home/blob/master/linux/img/docker/docker-attach.png)


####  方式2

**命令格式** ：`docker exec -it  container-id /bin/bash`

***退出方式***： `exit`



<p align="right"><a href="#container">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

-----

### <a name="remove-container">**删除容器**</a>

**命令格式** ：`docker rm [OPTIONS] CONTAINER-ID `

***常用Options*** ：

+ `-f`：通过`SIGKILL`信号强制删除正在运行的容器
+ `-v`：删除与容器关联的卷



### <a name="commit-container">**通过容器固化镜像**</a>

**命令格式** ：`docker commit <CONTAINER-ID> [repo:tag]`


![](https://github.com/HurricanGod/Home/blob/master/linux/img/docker/docker-commit.png)

通过`docker commit`虽然产生了一个新`image`，但实际上没有独立占用磁盘空间，而是在旧镜像的基础上修改，与旧镜像共享大部分内容。





<p align="right"><a href="#container">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

-----

### <a name="inspect">获取容器元数据</a>

**命令格式** ：`docker inspect [options] name|ID [NAME|ID...]`

`options`说明：

+ `-f, --format string`： 使用给定的Go模板格式化输出


+ `-s`： 显示总的文件大小
+ `--type string`： 为指定类型返回JSON

![docker-inspect](https://github.com/HurricanGod/Home/blob/master/linux/img/docker/docker-inspect.png)



###  <a name="docker-cp">容器与宿主机间的文件复制</a>

**命令格式** ：

`docker cp  CONTAINER:SRC_PATH DEST_PATH`

`docker cp SRC_PATH CONTAINER:DEST_PATH` 



### <a name="docker-logs">查看容器日志</a>

**命令格式** ：`docker logs [OPTIONS] CONTAINER`

***常用Options*** ：

+ `--details` ： 查询详细日志
+ `--since` ：后面跟一个**时间**字符串或相对时间
+ `--tail` ：显示日志最后多少行
+ `--until` ： 显示自某个timestamp之前的日志，或相对时间，与`--since`对应
+ `-t` ： 显示日志时间
+ `-f` ： 跟踪实时日志



```sh
# 查看容器7c787daa13e4最近30分钟的日志
docker logs --since 30m 7c787daa13e4

# 查看容器7c787daa13e4 6月8日12:00:00后产生的日志信息
docker logs --since "2019-06-08T12:00:00" 7c787daa13e4


# 查看容器7c787daa13e4在 2019-06-08 00:25:00 到 00:26:00 时间段内产生的日志
docker logs --since "2019-06-08T00:25:00" --until "2019-06-08T00:26:00"  7c787daa13e
```



<p align="right"><a href="#container">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

-----

## <a name="image">镜像</a>

在本地主机上使用一个不存在的镜像时，Docker就会自动下载这个镜像。下载镜像相关的命令 —— `docker pull`，该命令<a href="https://hub.docker.com/">DockerHub</a>上拉取镜像到本地。



### <a name="search-image">搜索镜像 </a>

> 用于搜索Docker Hub上的公共镜像

```sh
docker search [options] keyword
# options
# --limit  显示最大搜索结果，默认的搜索结果为25
# --no-trunc 不截断输出，显示完整输出，默认为false
# docker search ubuntu
```

+ `NAME`： 镜像仓库名称
+ `DESCRIPTION`： 镜像仓库描述
+ `STARTS`： 参考github的`stars`
+ `OFFICIAL`： 表示是否是官方仓库，`OK`的是官方维护的镜像

![docker-search](https://github.com/HurricanGod/Home/blob/master/linux/img/docker/docker-search.png)

<br/><br/>

-----

### <a name="pull-image">拉取镜像</a>

```sh
docker pull [OPTIONS] NAME[:TAG]
```

  

<br/><br/>

----

### <a name="see-image">查看本地镜像</a>

```sh
docker images

# 选项说明
# REPOSITORY —— 镜像的仓库源
# TAG —— 镜像的标签
# IMAGE ID —— 镜像ID
# CREATED —— 镜像创建时间
# SIZE —— 镜像大小

```


 ![docker-images](https://github.com/HurricanGod/Home/blob/master/linux/img/docker/docker-images.png)

<p align="right"><a href="#image">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>
<br/><br/>

----
### <a name="build-image">构建镜像</a>

Dockerfile创建完成后可以使用docker build命令根据Dockerfile构建镜像，**构建镜像的过程中，可以引用上下文中的任何文件**

**命令格式**：
```sh
docker  build  [options]  PATH | URL | -

# options
# -t —— 设置镜像标签，格式：name:tag， tag为可选项
# -f —— 显式指定 Dockerfile，不显式指定Dockerfile情况下，默认使用上下文路径下名字为Dockerfile的文件构建镜像
# --force-rm —— 删除中间容器

# 在当前文件夹构建名字为 redis-cluster-4.0.10 的镜像
docker build -t redis-cluster-4.0.10 .

# 
docker  build  -t  redis-cluster-4.0.10:v2  .
```

<p align="right"><a href="#image">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>


<br/><br/>


---
### <a name="push_image">推送镜像到远程仓库</a>

```sh
docker push repository/image_name:tag
```


<br/><br/>

-----
### <a name="delete-image">删除镜像</a>

**镜像删除失败的场景**：
+ 若有其它镜像依赖于被删除的镜像，则会删除失败
+ 若有容器使用了要被删除的镜像，删除操作也会失败


```sh
docker rmi [OPTIONS] image-id

# OPTIONS
# -f —— 强制删除
```


 ![docker-rmi](https://github.com/HurricanGod/Home/blob/master/linux/img/docker/docker-rmi.png)


<p align="right"><a href="#image">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>


-----
## <a name="dockfile">**Dockerfile**</a>

`Dockerfile` 用于编写docker镜像生成过程的文件，有13个基本指令：

+ `FROM` —— `Dockerfile`第一个指令必需是**FROM**，指定一个构建镜像的基础源镜像，如果本地没有就会从公共库中拉取，没有指定镜像的标签会使用默认的latest标签。 **指令格式** ： `FROM <image-name[:tag]>`

  

+ `MAINTAINER` —— 用于描述镜像创建者名称和邮箱。**指令格式** ：`MAINTAINER <name> <email>`

  

+ `RUN` —— `RUN`命令在镜像中创建一个容器并执行指定指令，执行结束后commit修改作为镜像的一层，**如果有多个`RUN`指令最好使用`&&`指令连接**。指令格式有两种：

  + **命令行式** ： `RUN command args...`
  + **函数调用式** ： `RUN ["command", "args"]`

  

+ `ONBUILD` —— 镜像创建后，如果其它镜像以这个镜像为基础，会先执行这个镜像的`ONBUILD`命令，后面跟的是其它指令，比如 `RUN`、 `COPY` 。**指令格式**：`ONBUILD [INSTRUCTION]`

  

+ `EXPOSE` —— **声明运行时容器提供的服务端口，但在运行时并不会因为这个声明应用就会开启这个端口的服务**，如果使用了随机端口映射将会使用`EXPOSE`暴露的端口。 **指令格式**： `EXPOSE <port> [<port1>...]`

  

+ `ENV` —— 用于为容器设置环境变量，定义环境变量后，**后面的其它指令**或**运行时的应用**都可以使用这个环境变量。指令格式：`EVN <key>=<value> [<key>=<value>]`

  

+ `ADD` —— 升级版的`COPY`指令，源路径可以是个`URL`，Docker引擎会试图下载链接里的文件放到目标路径，如果源路径是压缩包，复制到镜像时会自动解压。***适用于需要自动解压缩的场合***。**指令格式**： `ADD src-path desc-path`

  

+ `COPY` —— `COPY`指令将**构建上下文**路径的文件或目录复制到新一层镜像的目的路径，目的路径如果不存在会自动创建。**指令格式**： `COPY src-path desc-path`

  

+ `VOLUMN` —— 定义匿名卷，容器运行时应该尽量保持容器存储层不发生写操作，对于数据库类需要保存动态数据的应用，其数据库文件应该保存于卷中。为了防止运行时用户忘记将动态文件所保存目录挂载为卷，在 `Dockerfile `中可以事先指定某些目录挂载为匿名卷，这样在运行时如果用户不指定挂载，应用也可以正常运行，不会向容器存储层写入大量数据。`docker run`时指定 `-v`选项将覆盖`Dockerfile`指定的路径。**指令格式**：`VOLUMN [ "path1", "path2" ]`

  

+ `USER` —— 用于指定容器中的当前用户,后续的`RUN`、`CMD`、`ENTRYPOINT`也会使用指定的用户运行命令。**指令格式**：`USER <username>`

  

+ `WORKDIR` —— 用于指定容器的默认工作目录，为`RUN`、`CMD`、`ENTRYPOINT`指令配置**工作目录**。**指令格式**：`WORKDIR path`

  

+ `CMD` —— 用于指定启动容器的指令，**默认的容器主进程的启动命令**，指令格式有两种：

  + **shell格式**： `CMD <命令>`，`shell格式`最后会包装成`exec格式`
  + **exec格式**： `CMD [ "可执行文件", "参数1", "参数2" ...]`

  

+ `ENTRYPOINT` —— 用于指定启动容器的指令，和 `CMD` 一样，都是在指定容器启动程序及参数。指定了 `ENTRYPOINT `后， `CMD` 不再是直接的运行其命令，而是将 `CMD `的内容作为参数传给 `ENTRYPOINT` 指令。指令格式

  + **shell格式**： `ENTRYPOINT <命令>`，`shell格式`最后会包装成`exec格式`
  + **exec格式**： `ENTRYPOINT [ "可执行文件", "参数1", "参数2" ...]`



**案例** ：

```dockerfile
FROM java:8
WORKDIR  /home/java
ADD DateTestApp.class  . 
VOLUME ["/home/hurrican/docker/data"]
ENTRYPOINT [ "java", "DateTestApp" ]
```

使用`ENTRYPOINT`构建出的Java程序镜像，启动时可以很方便地添加启动参数。

<p align="right"><a href="#dockfile">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

----

**参考资料** ：

+ <a href="http://www.docker.com">***docker官网文档***</a>
