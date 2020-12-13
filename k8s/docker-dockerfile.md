# <a name="top">Dockerfile</a>

+ <a href="#dockfile-cmd">Dockerfile指令</a>

+ <a href= "#write-principle">Dockerfile书写原则</a>

+ <a href="#write-suggest">Dockerfile指令书写建议</a>






-----
## <a name="dockfile-cmd">Dockerfile指令</a>



### <a name="#from">FROM</a>

**指令格式** ： `FROM <image-name[:tag]>`
`Dockerfile`第一个指令必需是**FROM**，指定一个构建镜像的基础源镜像
+ 如果本地没有就会从公共库中拉取
+ 没有指定镜像的标签会使用默认的latest标签。 




###  <a name="MAINTAINER">MAINTAINER</a>


**指令格式** ：`MAINTAINER <name> <email>`
`MAINTAINER` —— 用于描述镜像创建者名称和邮箱。



### <a name="RUN">RUN</a>

**命令行式** ： `RUN command args...`
**函数调用式** ： `RUN ["command", "args"]`

`RUN`命令在镜像中创建一个容器并执行指定指令，执行结束后commit修改作为镜像的一层，**如果有多个`RUN`指令最好使用`&&`指令连接**。



### <a name="ONBUILD">ONBUILD</a>

**指令格式**：`ONBUILD [INSTRUCTION]`

`ONBUILD` —— 镜像创建后，如果其它镜像以这个镜像为基础，会先执行这个镜像的`ONBUILD`命令，后面跟的是其它指令，比如 `RUN`、 `COPY` 。



### <a name="EXPOSE">EXPOSE</a>

**指令格式**： `EXPOSE <port> [<port1>...]`

`EXPOSE` —— **声明运行时容器提供的服务端口，但在运行时并不会因为这个声明应用就会开启这个端口的服务**，如果使用了随机端口映射将会使用`EXPOSE`暴露的端口。 




### <a name="ENV">ENV</a>

**指令格式**：`EVN <key>=<value> [<key>=<value>]`

`ENV` —— 用于为容器设置环境变量，定义环境变量后，**后面的其它指令**或**运行时的应用**都可以使用这个环境变量。



### <a name="ADD">ADD</a>

**指令格式**： `ADD src-path desc-path`

`ADD` —— 升级版的`COPY`指令，源路径可以是个`URL`，Docker引擎会试图下载链接里的文件放到目标路径，如果源路径是压缩包，复制到镜像时会自动解压。***适用于需要自动解压缩的场合***。



### <a name="COPY">COPY</a>

**指令格式**： `COPY src-path desc-path`

`COPY`指令将**构建上下文**路径的文件或目录复制到新一层镜像的目的路径，目的路径如果不存在会自动创建。



### <a name="VOLUMN">VOLUMN</a>

**指令格式**：`VOLUMN [ "path1", "path2" ]`

`VOLUMN` —— 定义匿名卷
+ 容器运行时应该尽量保持容器存储层不发生写操作，对于数据库类需要保存动态数据的应用，其数据库文件应该保存于卷中。
+ 为了防止运行时用户忘记将动态文件所保存目录挂载为卷，在 `Dockerfile `中可以事先指定某些目录挂载为匿名卷，这样在运行时如果用户不指定挂载，应用也可以正常运行，不会向容器存储层写入大量数据。
+ `docker run`时指定 `-v`选项将覆盖`Dockerfile`指定的路径。



### <a name="USER">USER</a>

**指令格式**：`USER <username>`

`USER` —— 用于指定容器中的当前用户,后续的`RUN`、`CMD`、`ENTRYPOINT`也会使用指定的用户运行命令。




### <a name="WORKDIR">WORKDIR</a>

**指令格式**：`WORKDIR path`
`WORKDIR` —— 用于指定容器的默认工作目录，为`RUN`、`CMD`、`ENTRYPOINT`指令配置**工作目录**。




### <a name="CMD">CMD</a>

**shell格式**： `CMD <命令>`，`shell格式`最后会包装成`exec格式`
**exec格式**： `CMD [ "可执行文件", "参数1", "参数2" ...]`

`CMD` —— 用于指定启动容器的指令，**默认的容器主进程的启动命令**




### <a name="ENTRYPOINT">ENTRYPOINT</a>

**shell格式**： `ENTRYPOINT <命令>`，`shell格式`最后会包装成`exec格式`
**exec格式**： `ENTRYPOINT [ "可执行文件", "参数1", "参数2" ...]`

`ENTRYPOINT` —— 用于指定启动容器的指令，和 `CMD` 一样，都是在指定容器启动程序及参数。指定了 `ENTRYPOINT `后， `CMD` 不再是直接的运行其命令，而是将 `CMD `的内容作为参数传给 `ENTRYPOINT` 指令。




**案例** ：

```dockerfile
FROM java:8
WORKDIR  /home/java
ADD DateTestApp.class  . 
VOLUME ["/home/hurrican/docker/data"]
ENTRYPOINT [ "java", "DateTestApp" ]
```

使用`ENTRYPOINT`构建出的Java程序镜像，启动时可以很方便地添加启动参数。

<p align="right"><a href="#dockfile-cmd">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>



----

## <a name= "write-principle">Dockerfile书写原则</a>


+ 单一职责 —— 由于容器的本质是进程，一个容器代表一个进程，因此不同功能的应用应该尽量拆分为不同的容器，每个容器只负责单一业务进程

  

+ 提供注释信息

  

+ 保持容器最小化 —— 应该避免安装无用的软件包，不仅可以加快容器构建速度，而且可以避免镜像体积过大

  

+ 合理选择基础镜像 —— 容器的核心是应用，因此只要基础镜像能够满足应用的运行环境即可

  例如一个Java类型的应用运行时只需要JRE，并不需要JDK，因此我们的基础镜像只需要安装JRE环境即可

  

+ 使用 .dockerignore 文件 —— 使用.dockerignore文件允许我们在构建时，忽略一些不需要参与构建的文件，从而提升构建效率。.dockerignore的定义类似于.gitignore



+ 尽量使用构建缓存 

  > Docker 构建过程中，每一条 Dockerfile 指令都会提交为一个镜像层，下一条指令都是基于上一条指令构建的。如果构建时发现要构建的镜像层的父镜像层已经存在，并且下一条命令使用了相同的指令，即可命中构建缓存.



+ 正确设置时区

  从 Docker Hub 拉取的官方操作系统镜像大多数都是 UTC 时间（世界标准时间），容器中如果需要使用中国区标准时间（东八区），需要修改相应的时区信息

  + `Ubuntu` 或`Debian `

    ```sh
    RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
    RUN echo "Asia/Shanghai" >> /etc/timezone
    ```

    

  + `CentOS`

    ```sh
    RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
    ```

    

+ 使用国内软件源加快镜像构建速度



+ 最小化镜像层数

<p align="right"><a href="#write-principle">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>



----
## <a name="write-suggest">Dockerfile指令书写建议</a>

+ <a href="#docker_run">RUN</a>

+ <a href="#cmd_entrypoint">CMD 和 ENTRYPOINT</a>







----

### <a name="docker_run">RUN</a>

RUN指令在构建时将会生成一个新的镜像层并且执行RUN指令后面的内容，使用RUN指令时应该尽量遵循的原则

+ 当RUN指令后面跟的内容比较复杂时，建议使用`反斜杠(\)` 结尾并且换行
+ RUN指令后面的内容尽量按照字母顺序排序，提高可读性

**例**：

```shell

```



----

### <a name="cmd_entrypoint">CMD 和 ENTRYPOINT</a>

`CMD` 和 `ENTRYPOINT` 指令都是容器的命令入口，基本使用格式

+ `exec`模式 —— **Docker推荐使用模式**，容器的1号进程就是 `CMD` 或 `ENTRYPOINT` 中指定的命令
  + `CMD ["command", "param"]`
  + `ENTRYPOINT ["command", "param"]`
+ `shell`模式 —— Docker 会以 `/bin/sh -c command `的方式执行命令，shell 模式启动的进程在容器中实际上并不是 1 号进程
  + `CMD command param`
  + `ENTRYPOINT command param`



`CMD` 和 `ENTRYPOINT`指令的区别：




<p align="right"><a href="#write-suggest">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>