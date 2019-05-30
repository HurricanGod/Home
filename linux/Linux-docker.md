# <a name="top">Docker</a>





----

## <a name="container">容器</a>



### <a name="create-container">创建**新容器**</a>

**命令格式** ： `docker run [options] IMAGE [command] [args...]`

常用`options`：

+ `-d`：  指定容器运行于前台还是后台
+ `-i`： 打开STDIN，用于控制台交互
+ `-t`： 分配tty设备，该可以支持终端登录，默认为false
+ `-w`： 指定容器的工作目录
+ `-e`： 指定环境变量，容器中可以使用该环境变量
+ `-p`： 指定容器暴露的端口
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



### <a name="inspect">获取容器元数据</a>

**命令格式** ：`docker inspect [options] name|ID [NAME|ID...]`

`options`说明：

+ `-f, --format string`： 使用给定的Go模板格式化输出


+ `-s`： 显示总的文件大小
+ `--type string`： 为指定类型返回JSON

![docker-inspect]()











-----

## <a name="image">镜像</a>

+ **简介**

在本地主机上使用一个不存在的镜像时，Docker就会自动下载这个镜像。下载镜像相关的命令 —— `docker pull`，该命令可从相关Hub网站上拉取镜像到本地。



+ **拉取镜像** —— `docker pull`





+ **查看镜像信息** —— `docker images`

  ![docker-images]()

  **选项说明**：

  + `REPOSITORY` —— 镜像的仓库源
  + `TAG` —— 镜像的标签
  + `IMAGE ID` —— 镜像ID
  + `CREATED` —— 镜像创建时间
  + `SIZE` —— 镜像大小





+ **创建镜像** —— `docker build `

> Dockerfile创建完成后可以使用docker build命令根据Dockerfile构建镜像
>
> 命令格式： docker  build  [options]  PATH | URL | -

可选的命令选项包括：

+	`-t`：指定镜像名称

+ `-f`：显式指定 `Dockerfile`，不显式指定`Dockerfile`情况下，默认使用上下文路径下名字为`Dockerfile`的文件构建镜像



**构建镜像的过程中，可以引用上下文中的任何文件**

```sh
# 在当前文件夹构建名字为 redis-cluster-4.0.10 的镜像
docker build -t redis-cluster-4.0.10 .

# 
docker  build  -t  redis-cluster-4.0.10:v2  .
```







+ **删除镜像**



-----




  2.docker ps -a    //命令用来查看docker中所包含所有容器信息(运行状态/不运行状态)

  3.docker ps     //命令用来查看docker中所有正在运行的容器信息

  4.docker build -it 镜像name:镜像tag Dockerfile所在路径    //这是通过Dockerfile来构建一个镜像



  6.docker rmi 镜像名称:镜像tag/镜像Id    //通过一个镜像名称或者镜像Id来删除一个镜像

  7.docker rm -f 容器名称/容器Id            //通过容器名称/容器Id来删除一个容器

  8.docker pull 镜像name:镜像tag          //从仓库中拉取一个镜像

  9.docker push 镜像name:镜像tag       //往仓库中推送一个镜像

  更多docker命令，请查看docker官网文档：http://www.docker.com



docker create命令通过镜像去创建一个容器，同时吐出容器ID：

docker create --name ubuntuContainer ubuntu:18.04



用docker start即可运行该容器：

docker start ubuntuContainer

用docker exec即可进入该container：

docker exec -it 9298 bash





**commit容器，创建新镜像**

docker commit --author "rccoder" --message "curl+node" 9298 rccoder/myworkspace:v1





**Dockerfile**

用Docker进行持续集成？相比在了解Docker之前肯定听过这个事情，那就意味着需要从某个地方拷贝代码，然后执行（对，听上去有点Travis CI的那种感觉）。

是时候该Dockerfile出场了！

Dockerfile是一个由一堆命令+参数构成的脚本，使用docker build即可执行脚本构建镜像，自动的去做一些事（同类似于Travis CI中的.travis.yml）。

Dockerfile的格式统统为

INSTRUCTION arguments

必须以FROM BASE_IMAGE开头指定基础镜像。



1. `FROM rccoder/myworkspace:v1`
2. `RUN mkdir a`



