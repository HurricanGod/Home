# <a name="top">Docker</a>



+ <a href="#container-lifecycle">容器生命周期</a>

+ <a href="#container-cmd">**容器相关的命令**</a>
  + <a href="#create-container">**创建容器**</a>
  + <a href="#list-container">**列出容器**</a>
  + <a href="#enter-container">**进入容器**</a>
  + <a href="#stop-container">**停止容器**</a>
  + <a href="#inspect">**获取容器元数据**</a>
  + <a href="#docker-cp">**容器与宿主机间的文件复制**</a>
  + <a href="#docker-logs"> **看容器日志**</a>






----

## <a name="container-lifecycle">容器生命周期</a>



容器是基于镜像创建的可运行实例，并且单独存在，一个镜像可以创建出多个容器。运行容器化环境时，实际上是在容器内部创建该文件系统的读写副本。 这将添加一个容器层，该层允许修改镜像的整个副本。








----

## <a name="container-cmd">容器相关的命令</a>



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



----

**参考资料** ：

+ <a href="http://www.docker.com">***docker官网文档***</a>
