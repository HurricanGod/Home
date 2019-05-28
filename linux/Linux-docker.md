# <a name="top">Docker</a>



**拉取基础镜像**

当我们在本地主机上使用一个不存在的镜像时，Docker就会自动下载这个镜像。如果我们想预先下载这个镜像，我们可以使用docker pull命令来下载它。

利用docker pull命令即可从相关Hub网站上拉取镜像到本地。





1.docker images   //命令用来查看docker中所包含的镜像信息

各个选项说明：

- REPOSITORY：表示镜像的仓库源
- TAG：镜像的标签
- IMAGE ID：镜像ID
- CREATED：镜像创建时间
- SIZE：镜像大小



  2.docker ps -a    //命令用来查看docker中所包含所有容器信息(运行状态/不运行状态)

  3.docker ps     //命令用来查看docker中所有正在运行的容器信息

  4.docker build -it 镜像name:镜像tag Dockerfile所在路径    //这是通过Dockerfile来构建一个镜像

  5.docker run -d -p  宿主机端口:容器端口  --name 容器名称 image   //这是通过一个image来构建一个container

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



用docker run可以一步到位创建并运行一个容器，然后进入该容器：

docker run -it --name runUbuntuContainer ubuntu:18.04 /bin/bash



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

docker build -t newfiledocker:v1 .

1. ` 新建基于 newfiledocker 的容器并在终端中打开，发现里面已经有 a 文件夹了。`
2. `> docker docker run -it newfiledocker:v1 /bin/bash`
3. `root@e3bd8ca19ffc:/# ls`

**通过Dockerfile构建**

创建Dockerfile。

首先，创建目录php-fpm，用于存放后面的相关东西：

```
runoob@runoob:~$ mkdir -p ~/php-fpm/logs ~/php-fpm/conf
```

logs目录将映射为php-fpm容器的日志目录。

conf目录里的配置文件将映射为php-fpm容器的配置文件。

进入创建的php-fpm目录，创建Dockerfile。

通过Dockerfile创建一个镜像，替换成你自己的名字：

```
runoob@runoob:~/php-fpm$ docker build -t php:5.6-fpm .
```

创建完成后，我们可以在本地的镜像列表里查找到刚刚创建的镜像：

```
runoob@runoob:~/php-fpm$ docker imagesREPOSITORY          TAG                 IMAGE ID            CREATED             SIZEphp                 5.6-fpm             025041cd3aa5        6 days ago          456.3



```

