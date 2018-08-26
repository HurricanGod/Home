# 搜索过滤命令



----

## which

```sh
which command
# 根据PATH环境变量规范的路径去查询 command 的文件名
```







## whereis（寻找特定文件）

```shell
whereis  []  文件或目录名
-b : 只寻找二进制格式文件
-s : 只找source源文件
```





## locate

```shell
locate [-i] keyword		# 忽略大小写差异
locate [-r] keyword		# 后面可接正则表达式的显式方式
```





## find

```she
find [PATH] [option] [action]
```



**-mtime n**(n为数字)：意为n天前**一天内**被修改过的文件 

+ `find ./ mtime 0`——列出24小时内被改动过的文件

>


**find ./ -name filename** ：查找文件名为`filename`的文件



**-size [+-] SIZE**：查找文件比SIZE还大或小的文件

+ `SIZE` 的规格有：c：byte、k：1024b
+ `find ./ -size +50k` ——表示在当前目录查找大于50k的文件



**-type TYPE**： 查找的文件类型为 `TYPE`的，`TYPE`的类型有：

+ `f`	——**一般正规文件**
+ `b`    ——设备文件
+ `d`    ——**目录**
+ `l`    ——连接文件
+ `s`    ——socket
+ `find ./ -type d` ——查找当前目录下类型为目录的文件



**-exec  command** —— `exec`可接其它命令来处理查找到的结果

```shell
find /var -type d -exec ls -l {} \;
# {} 表示由find命令找到的内容
# \; 因为;在bash环境下有特殊意义，所以需要用\进行转义
```





**Demo** ：

`find /home -name "my*.cnf"`

> 查找home目录下所有以my开头，cnf结尾的文件



`find /home -iname "my*.cnf"`

> 查找home目录下所有以my开头，cnf结尾的文件，忽略大小写



`find ./ -regex ".*\.cnf$"`

`find ./ -iregex ".*\.cnf$"`

> 查找当前目录下以`.cnf`结尾的文件（在正则式里 `.` 有特殊含义，需要使用`\`进行特殊转义）

+ 正则式里还需要进行转义常见字符有：
  + `(`  → `\(`
  + `)`  → `\)`
  + `|`  → `\|`




-----



## grep

```shell
grep [-acinv]  [--color=aotu]  '待查找的字符串'  filename
-a: 将二进制文件以 text 文件的方式查找数据
-c: 计算找到的 '待查找的字符串' 的次数
-i: 忽略大小写不同
-n: 顺便输出行号
-v: 反向选择，即显示出没有 '待查找的字符串' 内容的那一行
--color: 可以将找到的关键字部分加上颜色显示 
```

**例**：利用`grep`筛选配置文件中的非注释和非空行

`grep -v '^$' filename | grep -v '^#'`





![grep-n](https://github.com/HurricanGod/Home/blob/master/linux/img/grep-v.png)









