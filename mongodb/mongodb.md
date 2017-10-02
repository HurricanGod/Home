## MongoDb



---

**Linux** 下启动**mongodb**

+  `service mongodb start`
+  `service mongodb start -auth` —— **带验证**启动

----

**Linux** 下查看**mongodb**是否启动的方法：

+ `service mongodb status`
+ `ps -ef|grep mongodb`  —— 结果有两个线程说明**mongodb**启动了
+ `netstat -nltp`



----



带**验证启动mongodb**  如果要查看某一数据库下所有`collection`需要先进行帐号密码验证，不验证直接运行`show collections` 将会出现错误。

**验证方法** ： `db.auth("帐号", "密码")`



查看**Mongodb**下的某个数据库下所有`collection`(**关系数据库中的表**) 

命令： `show collections`



