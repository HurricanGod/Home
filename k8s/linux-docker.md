# <a name="top">Ubuntu安装Docker</a>


## 更换镜像源安装工具包

**备份sources.list**

```sh
cd /etc/apt

cp sources.list sources.list.bk

echo "" > sources.list
```


**使用以下内容替换sources.list**

```sh
deb http://archive.ubuntu.com/ubuntu/ bionic main restricted
deb http://archive.ubuntu.com/ubuntu/ bionic universe
deb http://archive.ubuntu.com/ubuntu/ bionic-updates universe
deb http://archive.ubuntu.com/ubuntu/ bionic multiverse
deb http://archive.ubuntu.com/ubuntu/ bionic-updates multiverse
deb http://archive.ubuntu.com/ubuntu/ bionic-backports main restricted universe multiverse
deb http://security.ubuntu.com/ubuntu/ bionic-security main restricted
deb http://security.ubuntu.com/ubuntu/ bionic-security universe
deb http://security.ubuntu.com/ubuntu/ bionic-security multiverse
```



**更新镜像源&&安装工具包**

```sh
apt-get update

# 
apt-get -y install apt-transport-https ca-certificates curl software-properties-common

```


<br/><br/>
-----

## <a name="docker-ce">安装docker</a>

### 更换阿里云镜像源

```sh
echo -e "deb http://mirrors.aliyun.com/ubuntu/ xenial main restricted universe multiverse \n\
deb http://mirrors.aliyun.com/ubuntu/ xenial-security main restricted universe multiverse \n\
deb http://mirrors.aliyun.com/ubuntu/ xenial-updates main restricted universe multiverse \n\
deb http://mirrors.aliyun.com/ubuntu/ xenial-proposed main restricted universe multiverse \n\
deb http://mirrors.aliyun.com/ubuntu/ xenial-backports main restricted universe multiverse \n\
deb-src http://mirrors.aliyun.com/ubuntu/ xenial main restricted universe multiverse \n\
deb-src http://mirrors.aliyun.com/ubuntu/ xenial-security main restricted universe multiverse \n\
deb-src http://mirrors.aliyun.com/ubuntu/ xenial-updates main restricted universe multiverse \n\
deb-src http://mirrors.aliyun.com/ubuntu/ xenial-proposed main restricted universe multiverse \n\
deb-src http://mirrors.aliyun.com/ubuntu/ xenial-backports main restricted universe multiverse \n\
deb [arch=amd64] http://mirrors.aliyun.com/docker-ce/linux/ubuntu bionic stable" > sources.list

apt-get update

```

### 添加证书

```sh
curl -fsSL http://mirrors.aliyun.com/docker-ce/linux/ubuntu/gpg | apt-key add -
```


-----

### 安装docker-ce

**查看可选的docker-ce版本**

```sh
apt-cache madison docker-ce

# docker-ce | 5:20.10.3~3-0~ubuntu-bionic | http://mirrors.aliyun.com/docker-ce/linux/ubuntu bionic/stable amd64 Packages
# docker-ce | 5:20.10.2~3-0~ubuntu-bionic | http://mirrors.aliyun.com/docker-ce/linux/ubuntu bionic/stable amd64 Packages
# docker-ce | 5:20.10.1~3-0~ubuntu-bionic | http://mirrors.aliyun.com/docker-ce/linux/ubuntu bionic/stable amd64 Packages
# docker-ce | 5:20.10.0~3-0~ubuntu-bionic | http://mirrors.aliyun.com/docker-ce/linux/ubuntu bionic/stable amd64 Packages
# ...
# docker-ce | 18.06.3~ce~3-0~ubuntu | http://mirrors.aliyun.com/docker-ce/linux/ubuntu bionic/stable amd64 Packages
# docker-ce | 18.06.2~ce~3-0~ubuntu | http://mirrors.aliyun.com/docker-ce/linux/ubuntu bionic/stable amd64 Packages
# docker-ce | 18.06.1~ce~3-0~ubuntu | http://mirrors.aliyun.com/docker-ce/linux/ubuntu bionic/stable amd64 Packages
# docker-ce | 18.06.0~ce~3-0~ubuntu | http://mirrors.aliyun.com/docker-ce/linux/ubuntu bionic/stable amd64 Packages
# docker-ce | 18.03.1~ce~3-0~ubuntu | http://mirrors.aliyun.com/docker-ce/linux/ubuntu bionic/stable amd64 Packages
```

**备注**：
+ 第二列为 `docker-ce` 的版本，例如：18.03.1~ce~3-0~ubuntu


**安装docker**

```sh
# 安装 18.03.1~ce~3-0~ubuntu版本的docker
apt-get install docker-ce=18.03.1~ce~3-0~ubuntu

# 安装最新版本的docker
apt-get install docker-ce
```



<br/><br/>
----

## <a name="docker-ce-reinstall">重装docker</a>

**卸载docker**

```sh
apt-get remove docker-ce
```


```
No process in pidfile '/var/run/docker-ssd.pid' found running; none killed.
invoke-rc.d: initscript docker, action "stop" failed.
dpkg: error processing package docker-ce (--remove):
 installed docker-ce package pre-removal script subprocess returned error exit status 1
dpkg: error while cleaning up:
 installed docker-ce package post-installation script subprocess returned error exit status 1
Errors were encountered while processing:
 docker-ce
```

**如果安装或卸载docker时报如上错误则先把** `/var/lib/dpkg/info/docker-ce*` **下的文件删除再重试**

```sh
rm -f /var/lib/dpkg/info/docker-ce*
```

