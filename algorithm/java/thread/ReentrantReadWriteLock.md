## ReentrantReadWriteLock



`ReentrantLock` 具有**完全互斥排他** 的作用，同一时间只允许一个线程执行`lock()`方法后的代码，效率比较低下。

`ReentrantReadWriteLock` 有两个锁，一个共享锁，一个排他锁，读锁间不互斥，写锁间互斥，允许多个线程同时执行读，但同一时刻只允许一个线程写入操作



