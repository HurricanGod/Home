#  <a name="top">Docker底层原理&&关键技术</a>



+ <a href="#namespace">资源隔离</a>

+ <a href="#cgroup">资源限制</a>

+ <a href="#component">Docker组件</a>

+ <a href="#network">网络模型</a>

+ <a href="#storage">数据存储</a>





----

##  <a name="namespace">资源隔离</a>

`Docker` 是使用 Linux 的 `Namespace` 技术实现各种资源隔离的，Docker 利用 Linux 内核的 `Namespace` 特性，**实现了每个容器的资源相互隔离**，从而保证容器内部只能访问到自己 `Namespace`的资源。

### 什么是 Namespace

> Namespace 是 Linux 内核的一个特性，该特性可以实现在同一主机系统中，对进程 ID、主机名、用户 ID、文件名、网络和进程间通信等资源的隔离。



### 各种Namespace的作用







### 为什么 Docker 需要 Namespace









<p align="right"><a href="#namespace">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

-----

## <a name="cgroup">资源限制</a>









<p align="right"><a href="#cgroup">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

----

## <a name="component">Docker组件</a>











<p align="right"><a href="#component">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

----

## <a name="network">网络模型</a>











<p align="right"><a href="#network">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

-----

## <a name="storage">数据存储</a>











<p align="right"><a href="#storage">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>