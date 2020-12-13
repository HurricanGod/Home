# <a name="top">容器监控</a>



Docker 容器的监控方案有很多，除了 Docker 自带的 `docker stats` 命令，还有很多开源的解决方案，例如 sysdig、cAdvisor、Prometheus 等，都是非常优秀的监控工具。





## docker stats 命令

Docker自带的 `docker stats` 命令可以很方便地看到主机上的所有容器的CPU、内存、网络IO、磁盘IO、PID等资源的使用情况。



启动1个资源限制为1核2G的容器

```sh
docker run --cpu=1 -m=2g --name=nginx -d nginx
```

使用 `docker stats` 命令查看容器运行状态





-----

## <a name="cAdvisor">cAdvisor</a>

cAdvisor 是谷歌开源的一款通用的容器监控解决方案。cAdvisor 不仅可以采集机器上所有运行的容器信息，还提供了基础的查询界面和 HTTP 接口，更方便与外部系统结合。



**cAdvisor 的安装与使用**

cAdvisor 官方提供了 Docker 镜像，我们只需要拉取镜像并且启动镜像即可

+ 把打好的镜像放在了 Docker Hub

  ```dockerfile
  docker pull lagoudocker/cadvisor:v0.37.0
  ```

  

+ 启动 cAdvisor

  ```sh
  docker run \
    --volume=/:/rootfs:ro \
    --volume=/var/run:/var/run:ro \
    --volume=/sys:/sys:ro \
    --volume=/var/lib/docker/:/var/lib/docker:ro \
    --volume=/dev/disk/:/dev/disk:ro \
    --publish=8080:8080 \
    --detach=true \
    --name=cadvisor \
    --privileged \
    --device=/dev/kmsg \
    lagoudocker/cadvisor:v0.37.0
  ```

  

----

## <a name="">监控原理</a>

Docker 是基于 `Namespace`、`Cgroups` 和 `联合文件系统` 实现的。 Cgroups 不仅可以用于容器资源的限制，还可以提供容器的资源使用率。无论何种监控方案的实现，**底层数据都来源于 Cgroups**。



Cgroups 的工作目录为 `/sys/fs/cgroup`，`/sys/fs/cgroup` 目录下包含了 Cgroups 的所有内容。Cgroups包含很多子系统，可以用来对不同的资源进行限制。例如对CPU、内存、PID、磁盘 IO等资源进行限制和监控。