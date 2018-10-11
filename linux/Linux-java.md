

cd /usr/lib/jvm/
tar -zxvf jdk-8u144-linux-x64.tar.gz
vi /etc/profile

export JAVA_HOME=/mnt/jdk/jdk1.8.0_181
export CLASSPATH=.:$JAVA_HOME/lib
export PATH=$JAVA_HOME/bin:$PATH

source /etc/profile

updatedb

locate java.security
> /mnt/jdk/jdk1.8.0_181/jre/lib/security/java.security

vim /mnt/jdk/jdk1.8.0_181/jre/lib/security/java.security

搜索 `securerandom.source` → 将 `securerandom.source=file:/dev/random` 改为 `securerandom.source=file:/dev/./urandom` 
