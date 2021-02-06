# <a name="top">Docker最佳实践</a>




----

## <a name="build-image">Docker镜像制作</a>

+ <a href="#java8-image">Java8镜像</a>


<br/><br/>
----

### <a name="java8-image">Java8镜像</a>

+ <a href="#download-jre">下载JRE</a>
+ <a href="#remove-unuse-file">删除多余的文件</a>
+ <a href="#java-dockfile">Dockerfile</a>
+ <a href="#build-java-dockfile">构建Java8镜像</a>


<br/><br/>
----

#### <a name="download-jre">下载JRE</a>

```sh
 wget https://javadl.oracle.com/webapps/download/AutoDL?BundleId=242050_3d5a2bb8f8d4428bbe94aed7ec7ae784 
```

#### <a name="remove-unuse-file">删除多余的文件</a>

```sh
# 解压 && 进入解压后的目录
tar -zxvf jre-8u251-linux-x64.tar.gz && cd jre1.8.0_251

rm -rf COPYRIGHT LICENSE README release THIRDPARTYLICENSEREADME-JAVAFX.txt THIRDPARTYLICENSEREADME.txt Welcome.html

rm -rf lib/plugin.jar \
  lib/ext/jfxrt.jar \
  bin/javaws \
  lib/javaws.jar \
  lib/desktop \
  plugin \
  lib/deploy* \
  lib/*javafx* \
  lib/*jfx* \
  lib/amd64/libdecora_sse.so \
  lib/amd64/libprism_*.so \
  lib/amd64/libfxplugins.so \
  lib/amd64/libglass.so \
  lib/amd64/libgstreamer-lite.so \
  lib/amd64/libjavafx*.so \
  lib/amd64/libjfx*.so
  
# 重新生成压缩包
tar zcvf jre8.tar.gz *
```

#### <a name="java-dockfile">Dockerfile</a>

```Dockerfile
FROM ubuntu:20.04

ADD jre8.tar.gz /java
ENV LANG C.UTF-8
ENV JAVA_HOME /java
ENV PATH ${PATH}:${JAVA_HOME}/bin

WORKDIR /app
ENTRYPOINT [ "/bin/bash" ]
```

#### <a name="build-java-dockfile">构建Java8镜像</a>

```sh
docker build -t largebug/common-java8:v1 .
```


