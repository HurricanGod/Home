



-----

### join()方法 与Thread.sleep()方法的区别：

+ `join(long)`的功能在内部是使用**wait()**方法来实现的，`join()`方法具有释放锁的特点
+ `Thread.sleep(long)` 方法不释放锁