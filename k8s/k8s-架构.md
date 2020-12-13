# <a name="top">Kubernetes</a>

+ <a href="#k8s-architecture">Kubernetes架构</a>
  +  <a href="#master">Master节点</a>
  +  <a href="#node">Node节点</a>

+ <a name="#k8s-concept">Kubernetes核心概念</a>




----

## <a name="k8s-architecture">Kubernetes架构</a>


`Kubernetes`采用声明式API来工作，所有组件的运行过程都是异步的，整个工作过程大致为用户声明想要的状态，然后`Kubernetes`各个组件相互配合并努力达到用户想要的状态。`Kubernetes`采用典型的主从架构，分为 `Master` 和 `Node` 两个角色

+ `Master`： `Kubernetes`集群的控制节点，负责整个集群的管理和控制功能
+ `Node` ：工作节点，负责业务容器的生命周期管理







### <a name="master">Master节点</a>

Master 节点负责对集群中所有容器的调度，各种资源对象的控制，以及响应集群的所有请求。Master 节点包含三个重要的组件

+ <a href="#kube-apiserver">`kube-apiserver`</a>
+ <a href="#kube-scheduler">`kube-scheduler`</a>
+ <a href="#kube-controller-manager">`kube-controller-manager`</a>





#### <a name="kube-apiserver">kube-apiserver</a>

+ `kube-apiserver` 主要负责提供 ` Kubernetes` 的 API 服务，所有的组件都需要与 `kube-apiserver`  交互获取或者更新资源信息，它是 Kubernetes Master 中最前端组件
+ `kube-apiserver` 的**所有数据都存储在 etcd **中，etcd 的稳定性直接关系着 Kubernetes 集群的稳定性，生产环境中 etcd 一定要部署多个实例以确保集群的高可用



<p align="right"><a href="#top">返回顶部</a> &nbsp | &nbsp <a href="#master">返回</a> </p>

#### <a name="kube-scheduler">kube-scheduler</a>

> kube-scheduler 用于监听未被调度的 Pod，然后根据一定调度策略将 Pod 调度到合适的 Node 节点上运行。





<p align="right"><a href="#top">返回顶部</a> &nbsp | &nbsp <a href="#master">返回</a> </p>


-----

#### <a name="kube-controller-manager">kube-controller-manager</a>

`kube-controller-manager` 负责维护整个集群的状态和资源的管理，实际上是一系列资源控制器的总称。

> 为了保证 Kubernetes 集群的高可用，Master 组件需要部署在多个节点上，由于 Kubernetes 所有数据都存在于 etcd 中，Etcd 是基于 Raft 协议实现，因此生产环境中 Master 通常建议至少三个节点（如果你想要更高的可用性，可以使用 5 个或者 7 个节点）


<p align="right"><a href="#top">返回顶部</a> &nbsp | &nbsp <a href="#master">返回</a> </p>

----

###  <a name="node">Node节点</a>

Node 节点是 Kubernetes 的工作节点，负责运行业务容器。Node 节点主要包含两个组件

+ `kubelet` —— 在每个工作节点运行的代理，它**负责管理容器的生命周期**，`Kubelet` 通过监听分配到自己运行的主机上的 Pod 对象，确保这些 Pod 处于运行状态，并且负责定期检查 Pod 的运行状态，将 Pod 的运行状态更新到 Pod 对象中
+ `kube-proxy` —— `Kube-proxy` 是在每个工作节点的**网络插件**，它实现了 Kubernetes 的 Service 的概念，`Kube-proxy` 通过维护集群上的网络规则，实现集群内部可以通过负载均衡的方式访问到后端的容器。





-----

## <a name="k8s-concept">Kubernetes核心概念</a>



----

+  <a name="cluster"> 集群  </a>
   + 集群是一组被 Kubernetes 统一管理和调度的节点，被 Kubernetes 纳管的节点可以是物理机或者虚拟机
   + 集群其中一部分节点作为 Master 节点，负责集群状态的管理和协调，另一部分作为 Node 节点，负责执行具体的任务，实现用户服务的启停等功能



+  <a name="label"> 标签(Label)  </a> —— 一组键值对，每一个资源对象都会拥有此字段，使用 Label 对资源进行标记，然后根据 Label 对资源进行分类和筛选



+ <a name="namespace"> 命名空间(Namespace)</a> —— 通过命名空间来实现资源的虚拟化隔离，将一组相关联的资源放到同一个命名空间内，避免不同租户的资源发生命名冲突，从逻辑上实现了多租户的资源隔离




+ <a name="pod"> 容器组(Pod)</a> —— 真正的业务进程的载体，在 Pod 运行前，Kubernetes 会先启动一个 Pause 容器开辟一个网络命名空间，完成网络和存储相关资源的初始化，然后再运行业务容器

  + `Pod ` 是 `Kubernetes` 中的**最小调度单位**，它**由一个或多个容器组成**
  + `Pod` 内的容器**共享相同的网络命名空间和存储卷**





+ <a name="deployment"> 部署(Deployment)</a> —— 一组 Pod 的抽象，通过 `Deployment ` 控制器保障用户指定数量的容器副本正常运行，并且实现了滚动更新等高级功能，当我们需要更新业务版本时，Deployment 会按照我们指定策略自动的杀死旧版本的 Pod 并且启动新版本的 Pod





+ <a name="stateful_set"> 状态副本集(StatefulSet)</a> —— 一组 Pod 的抽象，跟 `Deployment ` 类似，StatefulSet 主要用于有状态应用的管理，StatefulSet 生成的 Pod 名称是固定且有序的，确保每个 Pod 独一无二的身份标识





+ <a name="daemon_set"> 守护进程集(DaemonSet)</a>
  +  `DaemonSet ` 确保每个 Node 节点上运行一个 Pod，当我们集群有新加入的 Node 节点时，Kubernetes 会自动帮助我们在新的节点上运行一个 Pod
  + 一般用于日志采集，节点监控等场景



+ <a name="job"> 任务(Job)</a> —— Job 可以帮助我们创建一个 Pod 并且保证 Pod 的正常退出，如果 Pod 运行过程中出现了错误，Job 控制器可以帮助我们创建新的 Pod，直到 Pod 执行成功或者达到指定重试次数



+ <a name = "service">服务(Service)</a> 
  + 一组 Pod 访问配置的抽象，由于 Pod 的地址是动态变化的，我们不能直接通过 Pod 的 IP 去访问某个服务，Service 通过在主机上配置一定的网络规则，帮助我们实现通过一个固定的地址访问一组 Pod



+ <a name = "config_map"> 配置集(ConfigMap)</a> 
  + 用于存放我们业务的配置信息，使用 `Key-Value ` 的方式存放于 `Kubernetes `中
  + 可以帮助我们将配置数据和应用程序代码分开



+ <a name = "secret"> 加密字典(Secret)</a> —— `Secret `用于存放我们业务的敏感配置信息，类似于 `ConfigMap`，使用 Key-Value 的方式存在于 Kubernetes 中，主要用于**存放密码和证书等敏感信息**



----

