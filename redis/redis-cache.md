# <a name="top">Redis缓存问题</a>

+ <a href="#cache_">**缓存雪崩**</a>
+ <a href="#cache_pierce">**缓存穿透**</a>
+ <a href="#cache_preheat">**缓存预热**</a>
+ <a href="#cache_">**缓存更新**</a>
+ <a href="#cache_">**缓存降级**</a>




-----
## <a name="cache_">缓存雪崩</a>





<p align="right"><a href="#top">返回目录</a></p>

-----

## <a name="#cache_pierce">**缓存穿透**</a>

**缓存穿透** 指由于不恰当的业务或者恶意攻击持续高并发请求不存在的数据，最终导致**所有请求都落在数据库上**，造成数据库压力增大或者崩溃。



**解决方法**： 

> 把不存在的数据也放入缓存，设置为特殊值



<p align="right"><a href="#cache_pierce">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a></p>

----

## <a href="#cache_preheat">**缓存预热**</a>

在系统启动时把热点数据预先加载好的手段称为**缓存预热** 。



<p align="right"><a href="#cache_preheat">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a></p>

