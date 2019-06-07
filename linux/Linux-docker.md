# <a name="top">Docker</a>

+ <a href="#container">**容器**</a>
  + <a href="#create-container">***创建新容器***</a>
  + <a href="#enter-container">***进入容器***</a>
  + <a href="#stop-container">***停止容器***</a>


+ <a href="#image">**镜像**</a>


+ <a href="#dockfile">**Dockerfile**</a>





----

## <a name="container">容器</a>



### <a name="create-container">创建**新容器**</a>

**命令格式** ： `docker run [options] IMAGE [command] [args...]`

常用`options`：

+ `-d`：  指定容器运行于前台还是后台
+ `-i`： 打开STDIN，用于控制台交互
+ `-t`： 分配tty设备，该可以支持终端登录，默认为false
+ `-w`： 指定容器内部的工作目录
+ `-e`： 指定环境变量，容器中可以使用该环境变量
+ `-p`： 指定端口映射
+ `-h`： 指定容器的主机名
+ `-v`： 给容器挂载存储卷，挂载到容器的某个目录
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



####  方式2

**命令格式** ：`docker exec -it  container-id /bin/bash`

***退出方式***： `exit`



-----

### <a name="remove-container">**删除容器**</a>

**命令格式** ：`docker rm [OPTIONS] CONTAINER-ID `

***Options*** ：

+ `-f`：通过`SIGKILL`信号强制删除正在运行的容器
+ `-v`：删除与容器关联的卷



### <a name="commit-container">**通过容器固化镜像**</a>

**命令格式** ：`docker commit <CONTAINER-ID> [repo:tag]`



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



<p align="right"><a href="#container">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

-----

## <a name="image">镜像</a>

**简介** ：在本地主机上使用一个不存在的镜像时，Docker就会自动下载这个镜像。下载镜像相关的命令 —— `docker pull`，该命令可从相关Hub网站上拉取镜像到本地。



+ **搜索镜像** —— `docker search [options] keyword`

​	用于搜索Docker Hub上的公共镜像

​	`--limit` ：显示最大搜索结果，默认的搜索结果为25

​	`--no-trunc` ： 不截断输出，显示完整输出，默认为`false`

![docker-search]()

+ `NAME`： 镜像仓库名称
+ `DESCRIPTION`： 镜像仓库描述
+ `STARTS`： 参考github的`stars`
+ `OFFICIAL`： 表示是否是官方仓库，`OK`的是官方维护的镜像

​	

+ **拉取镜像** —— `docker pull [OPTIONS] NAME[:TAG]`





+ **查看本地镜像** —— `docker images`

  ![docker-images](https://github.com/HurricanGod/Home/blob/master/linux/img/docker/docker-images.png)

  **选项说明**：

  + `REPOSITORY` —— 镜像的仓库源
  + `TAG` —— 镜像的标签
  + `IMAGE ID` —— 镜像ID
  + `CREATED` —— 镜像创建时间
  + `SIZE` —— 镜像大小


<p align="right"><a href="#image">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

----


+ **构建镜像** —— `docker build `

> Dockerfile创建完成后可以使用docker build命令根据Dockerfile构建镜像
>
> 命令格式： docker  build  [options]  PATH | URL | -

可选的命令选项包括：

+	`-t`：设置镜像标签，格式：**name:tag**， `tag`为可选项
+	`-f`：显式指定 `Dockerfile`，不显式指定`Dockerfile`情况下，默认使用上下文路径下名字为`Dockerfile`的文件构建镜像
+	`--force-rm`： 删除中间容器



**构建镜像的过程中，可以引用上下文中的任何文件**

```sh
# 在当前文件夹构建名字为 redis-cluster-4.0.10 的镜像
docker build -t redis-cluster-4.0.10 .

# 
docker  build  -t  redis-cluster-4.0.10:v2  .
```







+ **删除镜像** —— `docker rmi [OPTIONS] image-id`

  `OPTIONS`：

  + `-f` ： 强制删除

  **镜像删除失败的场景**：

  + 若有其它镜像依赖于被删除的镜像，则会删除失败
  + 若有容器使用了要被删除的镜像，删除操作也会失败

  ​

  ![docker-rmi]()




<p align="right"><a href="#image">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

-----

## <a name="dockfile">**Dockerfile**</a>





<p align="right"><a href="#dockfile">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

----

**参考资料** ：

+ <a href="http://www.docker.com">***docker官网文档***</a>