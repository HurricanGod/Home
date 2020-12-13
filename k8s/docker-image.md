# <a name="top">Docker镜像</a>

+ <a href="#image">镜像相关的命令</a>
  + <a href="#search-image">搜索镜像 </a>
  + <a href="#pull-image">拉取镜像</a>
  + <a href="#see-image">查看本地镜像</a>
  + <a href="#build-image">构建镜像</a>
  + <a href="#rename-image">镜像重命名</a>
  + <a href="#delete-image">删除镜像</a>

+ <a href="#image-realization">镜像实现原理</a>







---



## <a name="image">镜像相关的命令</a>

镜像是一个特殊的文件系统，它提供了容器运行时所需的程序、软件库、资源、配置等静态数据。即**镜像不包含任何动态数据，镜像内容在构建后不会被改变**。



### <a name="search-image">搜索镜像 </a>

```sh
# 在Docker Hub上搜索公共镜像
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

从<a href="https://hub.docker.com/">DockerHub</a>上拉取镜像到本地。

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

**构建镜像主要有两种方式**：

+ 使用 `docker commit` 命令从运行中的容器提交为镜像

+ 编写 `Dockerfile` 并使用 `docker build` 命令构建镜像

  ```sh
  # 命令格式
  docker  build  [options]  PATH | URL | -
  
  # options
  # -t —— 设置镜像标签，格式：name:tag， tag为可选项
  # -f —— 显式指定 Dockerfile，不显式指定Dockerfile情况下，默认使用上下文路径下名字为Dockerfile的文件构建镜像
  # --force-rm —— 删除中间容器
  
  # 在当前文件夹构建名字为 redis-cluster-4.0.10 的镜像
  docker build -t redis-cluster-4.0.10 .
  
  # 
  docker build -t redis-cluster-4.0.10:v2  .
  ```

  使用 `Dockerfile `构建镜像具有以下特性：

  + Dockerfile 的每一行命令都会生成一个独立的镜像层，并且拥有唯一的 ID
  + Dockerfile 的命令是完全透明的，通过查看 Dockerfile 的内容，就可以知道镜像是如何一步步构建的




<p align="right"><a href="#image">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>
<br/><br/>



----

### <a name="rename-image">镜像重命名</a>

```sh
 
 docker tag [source_image][:tag] [target_image][:tag]
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



------

## <a name="image-realization">镜像实现原理</a>

docker 镜像是由一系列镜像层`(layer)`组成的，每一层代表了镜像构建过程中的一次提交，当我们需要修改镜像内的某个文件时，只需要在当前镜像层的基础上新建一个镜像层，并且只存放修改过的文件内容。分层结构使得镜像间共享镜像层变得非常简单和方便。**镜像底层的实现依赖于联合文件系统**（UnionFS）。

`Dockerfile` 的每一行命令，都生成了一个镜像层，每一层的 **diff文件夹**下只存放了增量数据。分层的结构使得 Docker 镜像非常轻量，每一层根据镜像的内容都有一个唯一的 ID 值，当不同的镜像之间有相同的镜像层时，便可以实现不同的镜像之间共享镜像层的效果。



