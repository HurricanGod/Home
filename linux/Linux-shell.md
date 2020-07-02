# <a name="top">Shell脚本基础</a>



+ <a href="#groupcmd">**成组命令**</a>


+ <a href="#quotes">**引号**</a>


+ <a href="#variable">**Shell变量**</a>
  + <a href="#string">**字符串处理**</a>


+ <a href="#map">**map**</a>


+ <a href="#list">**list**</a>


+ <a href="#set">**set**</a>


+ <a href="#arithmetic_op">**算术运算**</a>


+ <a href="#control">**控制结构**</a>
  + <a href="#if">**if语法**</a>
  + <a href="#for">**for语法**</a>


+ <a href="#function">**函数**</a>


+ <a href="#datetime">**时间日期**</a>


+ <a href="#crontab">**定时任务crontab**</a>


+ <a href="#set_cmd">**set命令**</a>


----

## <a name="groupcmd">**成组命令**</a>



> shell 中可以将若干条命令组合在一起，使其逻辑上为一条命令，组合的方式有两种：花括号{} 和圆括号()

+ `{}`组合命令 
  + {}格式的成组命令语法格式注意点：**左括号{ 后面有个空格，右括号 } 之前有个分号(;)**
  + 在本`shell`内执行命令表，不产生新进程



+ `()`组合命令
  + 左括号`(`**不需要留空格**，右括号前`)` *不需要加分号*
  + `()`里的成组命令是在**新的子shell**内执行，需要创建子进程





<p align="right"><a href="#groupcmd">返回</a>&nbsp |  &nbsp<a href="#top">返回目录</a></p>



----

##  <a name="quotes">**引号**</a>

+ **双引号** —— 双引号括起来的字符均作为普通字符对待(**$**  倒引号  反斜线`\`除外)

  + `$` —— 表示变量替换，用预先指定的变量值代替`$`和变量
  + ` —— 表示命令替换
  + `\` —— 仅当后面的字符是{`$`, `"`, 倒引号, `\`, 换行符} 中的一个时，`\`才是转义字符（转义字符告诉shell，只把后面的那个字符当作普通字符处理）

  ```shell
  #!/usr/bin/env bash
  echo "Hello `ls -l`"

  TODAY="`date`"
  # TODAY 为 date 命令执行后替换的结果
  echo "today is $TODAY"
  # 输出样例：today is Sat Aug 25 23:14:57     2018

  # 行尾的 \ 有表示长的输入的作用
  OUT_STR="This is not a new\
          line"
  echo ${OUT_STR}
  # 输出： This is not a new line

  ```

  ​


+ **单引号** —— 单引号括起来的字符都作为普通字符出现

  + **特殊字符也会失去意义**

  ```shell
  # TODAY 为 date 命令执行后替换的结果
  TODAY="`date`"

  # 单引号里的所有字符都没有特殊意义
  echo 'today is $TODAY'

  # 输出：today is $TODAY
  ```

  ​





+ **倒引号** —— 被倒引号括起来的字符串被`shell`解释为**命令行**，执行时`shell`会先执行命令行，并以标准输出结果取代整个倒引号部分

  + 倒引号可以是单条命令或多个命令的组合，**可以嵌套使用，嵌套使用内层必须用反斜线**`(\)`进行转义
  + 转义`\`使用`\\`

  ```sh
  # 倒引号里嵌套命令，内层命令需要使用转义符 \ 进行转义
  OUT_STR="`ls -l \`pwd\``"
  echo ${OUT_STR}
  ```

  ​



<p align="right"><a href="#quotes">返回</a>&nbsp |  &nbsp<a href="#top">返回目录</a></p>



-----

## <a name="variable">**Shell变量**</a>

### 变量赋值形式 

+ `变量名=字符串`（**赋值号两边均没有**`空格 `）
+ 变量名区分大小写，未赋值的变量默认为**空串**
+ 赋给变量的值若包含**空格**、**制表符**、**换行符**，可以使用 `单引号` 或 `双引号` 括起来
+ 引用变量时，若被引用的变量需要出现在长字符串**开头**或**中间**，为避免`shell`把被引用变量视为一个*新变量*，可以使用`{}`将变量名括起来
+ <a href="#groupcmd">**命令替换**</a>





### 引用变量值

+ `$变量名`
+ `${变量名}` —— **推荐**，把变量名和后面的字符串分隔开，避免出现混淆



```shell
name=Hurrican 123
# name 的值为 Hurrican

name="Hurrican 123"
# name 的值为 Hurrican 123
```



**备注**：

+ 一个未明确赋值过的变量是一个**空字符串**




---


### 数组

+ 显示声明一个数组 —— `declare -a 数组名`
+ 读取数组元素值 —— `${数组名[下标]}`
+ 取**越界的数组元素**将返回**空串**
+ 若不给出数组元素下标，数组名表示下标为0的数组元素 —— `${数组名} => ${数组名[0]}`
+ 可以使用`${数组名[*]}` 或 `${数组名[@]}` 访问数组的所有元素
+ 使用`unset`命令可以取消数组或数组元素的定义



**样例** ：

```sh
# 定义一个数组，名字为 list，值为 {1 2 3 4 5 6}
declare -a list=(1 2 3 4 5 6)

# 访问下标为0的数组
echo "first element is ${list[0]}"

# 访问整个数组，两种方法等效
echo "list[@] is ${list[@]}"
echo "list[*] is ${list[*]}"

# 获取数组长度
${#list[@]}
${#list[*]}

# 删除数组首元素
unset list[0]
```



<p align="right"><a href="#variable">返回</a>&nbsp |  &nbsp<a href="#top">返回目录</a></p>

-----

### 有效的变量引用表达式

**变量定义如下** ：

```shell
# 定义变量 
declare -a var
```

|         | 表达式                           | 含义                                       |
| :------ | :---------------------------- | :--------------------------------------- |
| 变量      | `$var`                        | 引用名为 `var` 的变量                           |
| 变量      | `${var}`                      | 避免出现混淆的方式引用名为 `var` 的变量，与`$var`的效果一样<br/>`${var}` 若为数组，则引用的是数组第一个元素 |
| -       |                               |                                          |
| 数组      | `${var[n]}`                   | 引用数组变量第n个元素的值                            |
| 数组      | `${var[@]}`<br/>`${var[*]}`   | 引用数组`var`中的所有非空元素                        |
| -       |                               |                                          |
| 命令行参数长度 | `${#*}`                       | 获取*运行命令行时* 实际给出的字符串参数个数，即命令行参数 `$*`数组的长度 |
|         | `${#@}`                       | 获取*运行命令行时* 实际给出的字符串参数个数，即命令行参数 `$@`数组的长度 |
| -       |                               |                                          |
| 数组长度    | `${#var[*]}`<br/>`${#var[@]}` | 获取数组中设置了值的元素个数，相当于取数组**非空元素数量**          |
| -       |                               |                                          |
| 预定义特殊变量 | `$#`                          | 命令行参数个数，**不包括** `$0`(**shell脚本名本身**)     |
| 预定义特殊变量 | `$?`                          | 表示上一条命令执行后的返回值，是一个**十进制**数               |
| 预定义特殊变量 | `$$`                          | 当前进程号                                    |
| 预定义特殊变量 | `$*`                          | 表示命令行中实际给出的所有实参                          |
| 预定义特殊变量 | `$@`                          | 表示命令行中实际给出的所有实参                          |
| 预定义特殊变量 | `$!`                          | Shell最后运行的后台进程PID                        |
| -       |                               |                                          |

**注意**：

+ `${var[*]}` 与 `${var[@]}` 当使用 `""`括起来时是有差别的
  + `${var[*]}` 被扩展成一个字符串，这个字符串是由**数组中各元素以空格**隔开组成，相当于`python`中的`" ".join(list)`
  + `${var[@]}` 被扩展成多个字符串，每个数组元素就是一个字符串

  <br/>


+ 执行`shell`脚本时可以带参数，这些参数可以称为**位置参数**，引用方式依次为：`$0`、`$1`、`$2` ... `$9`、 `${10}`
  + `$0` —— **总是表示**命令本身或`shell`脚本名，不能用 `set` 命令重新赋值
  + `shift n` 命令 —— 将**位置参数** 右移 `n` 位（`$0`不参与移位）





<p align="right"><a href="#variable">返回</a>&nbsp |  &nbsp<a href="#top">返回目录</a></p>

---

### <a name="string">**字符串处理**</a>

+ **删除字符串前缀**

  + `${var#pattern}` —— 非贪婪模式

  + `${var##pattern}` —— 贪婪模式

    ```shell
    path="/home/hurrican/hello.sh"
    echo "path = ${path}"
    echo '${path#*/} = '${path#*/}	
    # ${path#*/} = home/hurrican/hello.sh

    echo '${path##*/} = '${path##*/}
    # '${path##*/} = hello.sh
    ```

    ​


+ **删除字符串后缀**

  + `${var%pattern}` —— 非贪婪模式

  + `${var%%pattern}` —— 贪婪模式

    ```shell
    path="/home/hurrican/hello.sh"
    echo "path = ${path}"

    echo '${path%/*} = '${path%/*}
    # ${path%/*} = /home/hurrican

    echo '${path%%/*} = '${path%%/*}
    # ${path%%/*} = 
    ```

+ **截取字串**

  ```shell
  string="Hello Shell..."
  # 从索引 0 开始，取5个字符串
  echo ${string:0:5}

  # 从索引 5 开始，取5个字符串
  echo ${string:5:5}
  ```


+ **字符串替换** 

  + `${var/old/new}` —— **从左往右** 搜索满足的串，只要找到一个就停止搜索，用新串替换旧串

  ```shell
  email="Hurrican@163.com"
  echo 'email is '${email}
  echo '${email/@163.com/qq.com} = '${email/163.com/qq.com}
  # email="Hurrican@qq.com"

  email="Hurrican@163.com_@163.com"
  echo '${email/@163.com/qq.com} = '${email/163.com/qq.com}
  # ${email/@163.com/qq.com} = Hurrican@qq.com_@163.com
  ```

  + `${var//old/new}` —— **全局替换**

+ ```shell
  email="Hurrican@163.com_@163.com"
  ${email//@163.com/qq.com} = Hurrican@qq.com_@qq.com

  # ${email//@163.com/qq.com} = Hurrican@qq.com_@qq.com
  ```

  ​


+ **获取字符串长度**

  ```sh
  string='aaa_b   '
  # string.length=8
  ${#string}
  ```

  ​






<p align="right"><a href="#string">返回</a>&nbsp |  &nbsp<a href="#top">返回目录</a></p>



------

## <a name="map">**map**</a>

```shell
declare -a start_sh

start_sh[0]="/home/hurrican/tomcat8_1/tomcat8.5/bin/startup.sh"
start_sh[1]="/home/hurrican/tomcat8_2/tomcat8.5/bin/startup.sh"

# 注意：一定要用 -A 选项
declare -A start_sh_map

# 往 map 放元素
start_sh_map["1"]=${start_sh[0]}
start_sh_map["2"]=${start_sh[1]}

# 从 map 中取元素
echo ${start_sh_map["1"]}
echo ${start_sh_map["2"]}

# 获取 map 中的元素个数
echo 'start_sh_map.size = '${#start_sh_map[@]}

# 从 map 里删除元素
unset start_sh_map["1"]

# 获取所有的 key
echo ${!start_sh_map[@]}

# 遍历 map
for key in ${!start_sh_map[@]}
do
    echo "value = ${key}"
    echo "value = ${start_sh_map[$key]}"
    echo ""
done
```





<p align="right"><a href="#map">返回</a>&nbsp |  &nbsp<a href="#top">返回目录</a></p>





----

## <a name="list">**list**</a>

+ 往 `list` 里添加元素

  ```shell
  declare -a list
  list=(1 2 3 4 5 1)
  echo 'current list is: '${list[*]}

  # 在 list 尾部追加
  list=(${list[*]} 6)
  echo 'after append,the list is: '${list[*]}

  # 在 list 头部追加
  list=(0 ${list[*]})

  # current list is: 1 2 3 4 5 1
  # after append,the list is: 1 2 3 4 5 1 6
  ```

+ 删除 `list` 中的某个元素

  ```shell
  # 删除 list 中的元素
  unset list[5]
  echo 'after delete,the list is: '${list[*]}
  # after delete,the list is: 1 2 3 4 5 6
  ```

  ​

+ **生成序列**

  ```sh
  echo $(seq 10)

  # console:
  # 1 2 3 4 5 6 7 8 9 10
  ```

  ​



<p align="right"><a href="#list">返回</a>&nbsp |  &nbsp<a href="#top">返回目录</a></p>





----

## <a name="set">**set**</a>

```shell
#!/bin/sh
#打开expand_aliases选项
shopt -s expand_aliases

declare -A array
array["7080"]="server_7080"
array["7180"]="server_7180"
array["7280"]="server_7280"
array["7380"]="server_7380"
array["7580"]="server_7580"

list=(server_7080 server_7180 server_7280 server_7380 server_7480)

# 合并两个数组
merge=(${list[*]} ${array[*]})

# 求交集
echo -e "\n交集:array & list"
{ for i in ${merge[*]}; do echo ${i}; done;} | sort | uniq -d


# 求并集
echo -e "\n并集:array | list"
{ for i in ${merge[*]}; do echo ${i}; done;} | sort | uniq


# 求差集 array - list
echo -e "\n差集:array - list"
absolute_set=`{ for i in ${merge[*]}; do echo ${i}; done;} | sort | uniq -u`
intersection=`{ for i in ${merge[*]}; do echo ${i}; done;} | sort | uniq -d`
diff_set=`{ for i in ${list[*]} ${intersection[*]}; do echo ${i}; done;} | sort | uniq -u`
diff_set=`{ for i in ${diff_set[*]} ${absolute_set[*]}; do echo ${i}; done;} | sort | uniq -u`

echo -e "${diff_set[*]}"
```



`uniq`命令：`uniq [-optional] [input][output]`

| 选项       | 含义                 | 命令                                       |
| :------- | :----------------- | :--------------------------------------- |
| `-d`     | 显示**重复**的行         | `echo -e "1\n2\n3\n4\n1\n1" \|sort\|uniq -d` |
| `-u`     | 仅显示出现1次的行          | `echo -e "1\n2\n3\n4\n1\n1" \|sort\|uniq -u` |
| `-c`     | 显示重复的次数            |                                          |
| `input`  | 输入文件，没有默认从标准输入流中读取 |                                          |
| `output` | 输出文件，未指定则输出到标准输出   |                                          |



<p align="right"><a href="#set">返回</a>&nbsp |  &nbsp<a href="#top">返回目录</a></p>

------

## <a name="arithmetic_op">**算术运算**</a>

**语法格式** ——  ` let 算术表达式`

+ `let j=6*10+5` => `((j=6*10+5))`





<p align="right"><a href="#arithmetic_op">返回</a>&nbsp |  &nbsp<a href="#top">返回目录</a></p>

----

## <a name="control">**控制结构**</a>

### <a name="if">`if` 语句格式</a>

```shell
if 测试条件
then
	command
else
	command
fi

# 命令行下执行 if 语句
if test -f /hurrican/;then echo "dir";else echo "file";fi
```

​

+ **条件测试**
  + `test -f $1` —— `test`命令测试
  + `[]`条件测试 —— 左中括号 `[` 后面跟着**1个空格**， `]` 前面也有**1个空格**，


+ **测试运算符**






+ **文件测试运算符**

| 参数       | 功能                         | 命令                            |
| :------- | :------------------------- | :---------------------------- |
| `-r 文件名` | **文件存在**且用户**可读**返回True    | `test -r /hurrican/readme.md` |
| `-w 文件名` | **文件存在**且用户**可写**返回True    |                               |
| `-f 文件名` | **文件存在**且是**普通文件**返回True   | `test -f /hurrican/readme.md` |
| `-d 文件名` | **文件存在**且是**目录**返回True     |                               |
| `-s 文件名` | **文件存在**且**文件长度大于0**返回True | `test -s /hurrican/readme.md` |
|          |                            |                               |



-----

+ **字符串测试运算符**

| 参数                       | 功能                               |
| :----------------------- | :------------------------------- |
| `-z s1`                  | 字符串长度为0时返回`True`                 |
| `-n s1`                  | 字符串长度大于0时返回`True`                |
| `s1`                     | 字符串 `s1` 不是空串返回 `True`           |
| `s1 = s2`<br/>`s1 == s2` | 字符串 `s1`与字符串 `s2`**相等**返回`True`  |
| `s1 != s2`               | 字符串 `s1`与字符串 `s2`**不相等**返回`True` |
| `s1 < s2`                | 按字典序比较，`s1` 在`s2`之前返回`True`      |
| `s1 > s2`                | 按字典序比较，`s1` 在`s2`之后返回`True`      |



-----

+ **数值测试运算符**

| 参数          | 功能         | 命令   |
| :---------- | :--------- | :--- |
| `n1 -eq n2` | `n1 == n2` |      |
| `n1 -ne n2` | `n1 != n2` |      |
| `n1 -lt n2` | `n1 < n2`  |      |
| `n1 -le n2` | `n1 <= n2` |      |
| `n1 -gt n2` | `n1 > n2`  |      |
| `n1 -ge n2` | `n1 >= n2` |      |





-----

+ **逻辑运算符**

```shell
# 逻辑非 —— !
[ ! -f /hurrican ]

# 逻辑与 —— -a
[ "${var}" != "" -a ${var} -gt 0 ]

# 逻辑或 —— -o
```



<p align="right"><a href="#control">返回</a>&nbsp |  &nbsp<a href="#top">返回目录</a></p><br/>

### <a name="for">`for`循环</a>

+ **值表**方式 —— `for var [in var_set]; do command;done`
+ **算术表达式**方式 —— `for((expression1;expression2;expression3)); do command; done`



`Demo`：

```shell
#!/bin/sh
set=(1 2 3 4 5 6)

# 值表方式遍历集合
for ele in ${set[*]}
do
	echo ${ele}
done

echo -e "\n- - - - - - - - - - - - - - - - - - - -\n"

# 算术表达式方式遍历集合
for((i=0;i<${#set[*]};i++))
do
	echo ${set[i]}
done
```



<p align="right"><a href="#control">返回</a>&nbsp |  &nbsp<a href="#top">返回目录</a></p>

----

## <a name="function">**函数**</a>

**语法格式** ：

```shell
function function_name()
{
  command
  [return n]
}
```

**备注**：

+ 函数的传递使用**位置传参**和**变量**直接传递（**相当于全局变量**）
+ 函数的返回值未指定时默认为最后一条命令执行后的退出值







<p align="right"><a href="#function">返回</a>&nbsp |  &nbsp<a href="#top">返回目录</a></p>

----

## <a name="datetime">**时间日期**</a>

+ 获取当前时间戳 ——  `date "+%s"`(**以秒为单位**)
  + `%Y`：以四位数字格式打印年份
  + `%y`：以二位数字格式打印年份
  + `%m`：月份
  + `%d`：日期
  + `%H`：小时
  + `%M`：分钟
  + `%S`：秒
  + `%w`：星期，**0表示周日**






+ 获取当前日期字符串 —— `date +"%y-%m-%d %H:%M:%S"` → **18-09-09 01:14:35**


+ 获取指定时间的时间戳 —— `date -d "yyyy-mm-dd HH:MM:SS" +"%s"`(**以秒为单位**)


+ 日期加减

  ```shell
  #!/bin/sh
  # 获取当前时间字符串
  echo "current_str = "`date +"%y-%m-%d %H:%M:%S"`

  # 获取当前时间戳
  current=`date "+%s"`
  echo "current = ${current} s"

  # 获取一天前的时间戳
  ((yesterday = ${current} - 3600 * 24))
  echo "yesterday = ${yesterday} s"

  # 获取一天前时间戳对应的日期字符串
  yesterday_str=`date -d @${yesterday} +"%y-%m-%d %H:%M:%S"`
  echo "yesterday_str = ${yesterday_str}"

  # 获取30天前的日期
  date -d "-30 day" +"%Y-%m-%d"

  # 获取1个月后的日期
  date -d  `+1 month` +"%Y-%m-%d"

  # 获取1年后的日志
  date -d  `+1 year` +"%Y-%m-%d"
  ```



<p align="right"><a href="#datetime">返回</a>&nbsp |  &nbsp<a href="#top">返回目录</a></p>

----

## <a name="crontab">定时任务</a>

**crontab格式**： ```分钟 小时 日 月 星期 命令```

```sh

# 列出root用户的计时器设置
crontab -l -u root

# 编辑定时任务
crontab -e

# 添加定时任务
crontab clean-job.sh

# 编辑 crontab 默认的编辑器相关命令
select-editor
```



<p align="right"><a href="#crontab">返回</a>&nbsp |  &nbsp<a href="#top">返回目录</a></p>

-----

## <a name="set_cmd">set命令</a>

+ `set -o nounset`

  > 在默认情况下，遇到不存在的变量，会忽略并继续执行，开启该选项后，若使用了未初始化的变量则会让bash自动退出


+ `set -o errexit`

  > 在默认情况下，遇到执行出错，会跳过并继续执行，开启该选项后，执行出错则终止脚本的执行



<p align="right"><a href="#set_cmd">返回</a>&nbsp |  &nbsp<a href="#top">返回目录</a></p>

-----



