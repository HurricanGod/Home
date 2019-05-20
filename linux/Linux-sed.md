## <a name="top">sed命令</a>
+ <a href="#del">删除功能</a>
+ <a href="#print">打印功能</a>
+ <a href="#replace">替换功能</a>
+ <a href="#nm">n/N命令</a>

----
**工作原理**

顺序逐行将文件读入到内存中。然后，它执行为该行指定的所有操作，并在完成请求的修改之后将该行放回到内存中，以将其转储至终端
<br/>

`sed [address1[,address2]][options] '{command}' [filename] `


例：`sed [-nefr][动作]`

**选项**

|  选项  | 含义                                       |
| :--: | :--------------------------------------- |
|  -n  | 使用安静(silent)模式,加上 -n 参数后，则只有经过sed 特殊处理的那一行(或者动作)才会被列出来 |
|  -e  | 直接在命令列模式上进行 sed 的动作编辑                    |
|  -f  | 直接将 sed 的动作写在一个文件内， -f filename 则可以运行 filename 内的 sed 动作 |
|  -r  | sed 的动作支持的是延伸型正规表示法的语法                   |


----

**样例文件pipe.c**
```c
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
void main()
{
	FILE *write_fp;
  	char buffer[BUFSIZ + 1];
  	int chars_read;
  	memset(buffer, '\0', sizeof(buffer));
	//初始化缓冲区
  	fgets(buffer,BUFSIZ,stdin);
	write_fp = popen("wc -c", "w");
	if(write_fp!=NULL)
	//写指针不为空
	{
		fwrite(buffer, sizeof(char), strlen(buffer), write_fp);
    		pclose(write_fp);
    		exit(EXIT_SUCCESS);
	}
	exit(EXIT_FAILURE);
	//退出失败
}
```
#### <a name="del">删除功能</a>

运行`cat -n |sed -e 'd' `<br/>
结果 ：>**无任何内容**<br/><br/>

1. 删除指定行<br/>
   运行 ``cat -n pipe.c | sed -e '1d'``<br/>
   结果 ：除了第1行被删除，其余都显式在命令行<br/><br/>
2. 删除指定地址范围的行<br/>
   运行 ``at -n pipe.c | sed -e '1,15d'``<br/>
   结果 ：删除第1到15行<br/><br/>
3. 从一行开始每隔一行删一行<br/>
   运行 ``cat -n pipe.c | sed  '0~1d'``<br/>
   结果 ：全部内容被删<br/><br/>
4. 从第1行开始每隔7行删一行<br/>
   运行 ``cat -n pipe.c | sed '1~7d'``<br/>
   结果 ：第1、8、15行被删除<br/><br/>
5. 删除pipe.c文件中包含字符串“xxx”和空行之间的所有行<br/>
   运行 `` sed -e '/xxx/,/^$/d' pipe.c``<br/>
   结果 ：在包含“xxx”行和包含空行之间的行都被删除 <br/><br/>
6. 删除空行<br/>
   运行 ``sed -e '/^$/d' pipe.c``<br/><br/>
7. 删除最后一行<br/>
   运行 ：``sed -e '$d' pipe.c``<br/><br/>
8. 删除第一行到空行<br/>
   运行 ：``sed -e '1,/^$/d' pipe.c``<br/><br/>



----

#### <a name="print">打印功能</a>

1. 安静模式下打印1行<br/>
   运行 ：``sed -n '1p' pipe.c``<br/>
   结果 ：打印pipe.c的第1行<br/><br/>
2. 非安静模式下打印1行<br/>
   运行 ``sed '1p' express``<br/>
   结果 ：**打印出所有内容，但第1行打印了2次**<br/>
   解析 ：<strong>如果未加-n选项，会打印所有内容；此外"p"前面的数字如果为k，那么打印出来的内容中前k行都是2次。</strong><br/><br/>



----

### <a name="replace">替换功能</a>

`sed "s/old value/new value/"` —— 替换每一行中出现的第一个`old value`,即一行中如果有多个`old value`的串，只会替换第1个
`sed "s/old value/new value/"` —— 全局替换，会替换一行中的所有`old value`


**替换样例** ：


1. 运行 ``cat -n pipe.c |sed 's/include/hello/'``<br/>
   结果 ：pipe.c文件中所有的“include”文件被替换为<strong>“hello”</strong><br/><br/>

2. **多次替换**（使用-e选项）<br/>
   运行 ``cat -n pipe.c |sed -e 's/include/hello/' -e 's/hello/include/'``<br/>
   结果 ：先将pipe.c文件中的串include改为串hello，接着再把串hello改回串include，即对源文件未做任何修改<br/><br/>

3. 用分号来分隔命令<br/>
   运行 ``echo my name is hello | sed 's/is/are/; s/hello/world/' ``<br/>
   结果 ：**my name are world**<br/>
   注意 ：<strong>分号必须是紧跟在斜线之后的第一个字符</strong><br/><br/>
   例 ：

```shell
cat -n pipe.c|sed 's/include/hello/;0~2d'
# 将pipe.c文件中的“include”替换为“hello”，接着从第0行开始，每隔2行把该行删除，
# 因为没有第0行，所以删除的是第2，4，6，8……行，即偶数行


# 将nginx配置中的某一行注释掉
sed -i "s/127.0.0.1:8080/#127.0.0.1:8080/g" /etc/nginx/nginx.conf
```


**全局替换样例** ：



```shell
echo my name is hello hello | sed 's/hello/world/g' 
# 结果：my name is world world
 
# myfile.html文件内容如下：
# <b>This</b> is what <b>I</b> meant.
cat -n myfile.html | sed -e 's/<b>//g' -e 's/<\/b>//g'
# 结果：1	This is what I meant.
#      2	
# 由于“</b>”中的‘/’会引起歧义，所以需要使用“\/”对“/”进行转义
```

1. 替换每行第N个匹配

```shell
echo my name is hello hello | sed 's/hello/world/2' 
# my name is hello world
echo my name is hello hello | sed 's/hello/world/3' 
# my name is hello hello
```


----


### <a name="nm">n/N命令</a>
**n命令**：读取下一行到模式空间（pattern space）。由于模式空间中有按照正常流程读取的内容，使用n命令后，模式空间中又有了一行，此时模式空间中有2行内容，但是先读取的那一行不会被取代、覆盖或删除；当n命令后，还有其他命令p的时候，此时打印出的结果是n命令读取的那一行的内容。
```shell
cat -n pipe.c|sed -n 'p;n'
# 打印pipe.c文件里的奇数行
# 解析：sed命令先读第1行到模式空间，然后执行p打印命令，由于后面还有一个n命令，将再读取1行（第2行）到模式空间；
# n命令后没有命令了接着清空模式空间（第2行被清除了），读取第3行并打印，再读取第4行到模式空间（清除）……
# 最后只有奇数行被打印出来
 
cat -n pipe.c|sed -n 'n;p'
# 打印pipe.c文件里的偶数行
# 解析：先读取第1行到模式空间，遇到n命令后再读取1行到模式空间，接着遇到p命令将打印n命令读取的那行内容，
# 打印完后清空模式空间，如此循环下去打印的都是偶数行
```

**N命令**：将下一行添加到pattern space中。将当前读入行和用**N命令**添加的下一行看成“一行”<br/>
1. 运行：``cat -n pipe.c|sed -n 'p;N'``<br/>
      结果：输出奇数行<br/>
      解析：p命令打印完读入的第1行后，执行N命令再读入1行到模式空间并把两行看做1行，接着模式空间被清空，从第3行读取内容……<br/><br/>

2. 运行`` cat -n pipe.c|sed -n 'N;p'``<br/>
      结果：如果pipe.c内容的行数为偶数则打印全部内容，否则pipe.c的最后一行不打印，其它都打印<br/><br/>

3. 输出匹配行的下一行<br/>
      运行：``sed -n '/exit/{n;p}' pipe.c``<br/>
      结果：输出包含"exit"行的下一行<br/><br/>

4. 区别/exit/**n;p**和/exit/**{n;p}**<br/>
      运行：`sed -n '/exit/n;p' pipe.c`<br/>
      结果：除了包含"exit"的行其它行都被打印出来<br/><br/>

5. 为文件加行号<br/>
      命令：`sed = pipe.c | sed 'N;s/\n/:/'`<br/>
      解析：**sed = pipe.c**显示的是行号和每一行内容，把这结果通过管道作为输入，读取数字和行内容把它们看做一行并把"\n"替换为":"<br/><br/>

6. 实现tac命令功能<br/>
      命令：`sed -e '1!G;h;$!d' pipe.c`<br/><br/>


