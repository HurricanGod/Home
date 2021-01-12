# <a name="top">ReentrantReadWriteLock</a>





`ReentrantReadWriteLock` 主要特性：

+ **公平性**
+ **可重入**
+ **锁降级**



`ReentrantReadWriteLock` 支持读锁和写锁，除读锁与读锁不互斥外，其余锁之间都互斥。读写锁是基于 `AQS` 实现的，这里不禁会引出以下问题：

+ 读锁与读锁是如何做到不互斥的？
+ 读锁是如何做个每个线程可重入的？



-----

