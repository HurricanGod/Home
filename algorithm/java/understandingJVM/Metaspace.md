# <a name="top">Metaspace</a>





`Metaspace`位于堆外，最大内存大小取决于系统内存，可以通过 `MaxMetaspaceSize`参数来限定最大元空间。`Metaspace`主要用来存放记录java类在jvm中的信息的`class metadata`，`class metadata`运行时数据包括：

+ `Klass`结构，Java类在虚拟机内部的表示
+ `method metadata`，包括字节码、局部变量表、异常表、参数信息等
+ 常量池
+ 注解
+ 方法计数器
+ ...



### 什么时候分配Metaspace







### 什么时候回收Metaspace