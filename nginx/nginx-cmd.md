# <a name="top">Nginx常用命令</a>





----

+ **检查nginx配置文件**

  ```nginx
  # 每次修改 nginx 配置文件后都要进行配置文件检查
  nginx -t
  /etc/init.d/nginx configtest

  # 检查指定目录下的 nginx.conf 配置文件是否正确
  nginx -t -c /特定目录/nginx.conf
  ```

  ​


+ **启动nginx**

  ```nginx
  service nginx start

  # 重启
  service nginx restart

  # 指定配置文件启动 nginx
  nginx -c /特定目录/nginx.conf
  ```

  ​


+ **重新加载nginx配置文件**

  ```nginx
  # 在 nginx 已经启动的情况下重新加载配置文件
  service nginx reload
  
  nginx -s reload
  /etc/init.d/nginx reload
  ```

  ​


+ **停止nginx**

  ```nginx
  # 立即关闭 nginx 
  service nginx stop
  nginx -s stop

  # 优雅地关闭 nginx，等待woker线程处理完
  nginx -s quit 

  killall nginx
  ```

  ​



---

**备注** ：

+ 默认情况下Nginx启动后会监听`80`端口，`80`**端口被占用**会导致启动失败
