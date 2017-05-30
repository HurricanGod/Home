**sed语法**
sed [address1[,address2]] [options] '{command}' [filename] 
例：sed [-nefr] [动作]

**选项**

| 选项 |  含义 |
|:----:|:-----:|
|-n| 使用安静(silent)模式,加上 -n 参数后，则只有经过sed 特殊处理的那一行(或者动作)才会被列出来|
|-e| 直接在命令列模式上进行 sed 的动作编辑|
|-f| 直接将 sed 的动作写在一个文件内， -f filename 则可以运行 filename 内的 sed 动作|
|-r| sed 的动作支持的是延伸型正规表示法的语法|
<hr/><br/>

**工作原理**

顺序逐行将文件读入到内存中。然后，它执行为该行指定的所有操作，并在完成请求的修改之后将该行放回到内存中，以将其转储至终端
<hr/><br/>

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
#### 删除功能d
运行`cat -n |sed -e 'd' `<br/>
结果 ：>**无任何内容**<br/>

1. 删除指定行<br/>
运行 ``cat -n pipe.c | sed -e '1d'``<br/>
结果 ：除了第1行被删除，其余都显式在命令行<br/>

2. 删除指定地址范围的行<br/>
运行 ``at -n pipe.c | sed -e '1,15d'``<br/>
结果 ：删除第1到15行<br/>

3. 从一行开始每隔一行删一行<br/>
运行 ``cat -n pipe.c | sed  '0~1d'``<br/>
结果 ：全部内容被删<br/>

4. 从第1行开始每隔7行删一行<br/>
运行 ``cat -n pipe.c | sed '1~7d'``<br/>
结果 ：第1、8、15行被删除<br/>

5. 删除pipc文件中包含字符串“xxx”和空行之间的所有行<br>
运行 `` sed -e '/xxx/,/^$/d' pipe.c``<br>
结果 ：在包含“xxx”行和包含空行之间的行都被删除 <br>

6. 删除空行<br/>
运行 ``sed -e '/^$/d' pipe.c``<br/>

7. 删除最后一行<br/>
运行 ：``sed -e '$d' pipe.c``<br/>

8. 删除第一行到空行<br/>
运行 ：``sed -e '1,/^$/d' pipe.c``<br/>

#### 打印功能

1. 安静模式下打印1行<br/>
运行 ：``sed -n '1p' pipe.c``<br/>
结果 ：对应pipe.c的第1行<br/>

2. 非安静模式下打印1行<br/>
运行 ``sed '1p' express``<br/>
结果 ：**打印出所有内容，但第1行打印了2次**<br/>
解析 ：<strong>如果未加-n选项，会打印所有内容；此外"p"前面的数字如果为k，那么打印出来的内容中前k行都是2次。</strong><br/>
 
