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
  fgets(buffer,BUFSIZ,stdin);
	write_fp = popen("wc -c", "w");
	if(write_fp!=NULL)
	{
		fwrite(buffer, sizeof(char), strlen(buffer), write_fp);
    pclose(write_fp);
    exit(EXIT_SUCCESS);
	}
	exit(EXIT_FAILURE);
}
```
#### 删除功能d
运行`cat -n |sed -e 'd' `
结果 ：>**无任何内容**
 
