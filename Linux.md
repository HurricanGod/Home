**进程**
 
在Linux运行的每个进程都有一个唯一的进程标识符PID，PID的数值是逐渐增大的，一般子进程的PID会比父进程大。
如果 父进程死亡或退出，则子进程会被指定一个父进程**init**（PID为1）。

**system函数**：运行以字符串参数的形式传递给它的命令并等待该命令完成 

```c
#include <stdlib.h>
#include <stdio.h>
int main()
{
    printf("Running ps with system\n");
    system("ps -ax");
    printf("Done.\n");
    exit(0);
}
```
**运行结果：**system创建一个子进程，子进程执行完ps命令后，父进程才执行，输出“Done”后才结束。
 
 **exec函数系列**：该系列函数作用是更换进程映像，根据指定文件名找到可执行文件，并用该可执行文件代替调用进程的内容。通俗地将就是：将当前进程替换为一个新进程，且新进程与原进程有相同的PID，原来进程的内容将不会执行。
 ```c
#include <unistd.h>
#include <stdio.h>

int main()
{
    printf("Running ps with execlp\n");
    execlp("ps", "ps", "-ax", 0);
    printf("Done.\n");
    exit(0);
}
 ```
 
 <b>pid_t wait(int *status)</b>
 wait系统调用将暂停父进程直到它的子进程结束为止。该调用返回子进程的PID。子进程的退出状态写入status指向的位置。

| 宏定义  | 作用          |
| ------------- |:-------------:|
|WIFEXITED(status)| 如果子进程正常结束则为非0值|
|WEXITSTATUS(status)|取得子进程exit()返回的结束代码|
|WIFSIGNALED(status)|如果子进程是因为信号而结束则此宏值为真|
|WTERMSIG(status))|取得子进程因信号而中止的信号代码|
|WIFSTOPPED(status)|如果子进程处于暂停执行情况则此宏值为真,一般只有使用WUNTRACED 时才会有此情况|
|WSTOPSIG(status)|取得引发子进程暂停的信号代码|
 
**例子 ：**
```c
# include <stdio.h>
# include <wait.h>
# include <sys/types.h>
# include <signal.h>
# include <unistd.h>
# include <stdlib.h>
int INTR=1;
void stop()
{
	printf("Good Bye!\n");
	INTR=0;
}
void waitting()
{
	while(INTR);
}
void main()
{
	pid_t pid;
	int *status;
	int message;
	while((pid=fork())==-1);
	if(pid==0)
	{
		//son process 
		signal(16,stop);
		waitting();
		printf("son process exit!\n");
		exit(200);
	}
	else
	{
		//father process
		printf("input 16 to stop son process\n");
		scanf("%d",&message);
		if(message!=16)
		{
			while(message!=16)
			{
				setbuf(stdin,NULL);
				printf("input 16 to stop son process\n");
				scanf("%d",&message);
			}
		}
		kill(pid,message);
		wait(status);
		printf("status address is %x\n",status);
		printf("*status = %x\n",*status);
		printf("%d\n",WIFEXITED(status));
		if(WIFEXITED(status))
		{
			printf("son process exited normally,exit code is: %d\n", WEXITSTATUS(status));
		}
		else
		{
			printf("son process exited abnormality!\n");
			printf("exit code is: %d\n", WEXITSTATUS(status));
		}
		if (WIFSIGNALED(status))
        		printf("child exited abnormal by signal exit code is: %d\n", WTERMSIG(status));
		printf("main process is exiting!\n");
	}
}

```
运行结果：
<p>
<b>input 16 to stop son process</b>
<p>16</p>
<p>Good Bye!</p>
<p>son process exit!</p>
<p>status address is bf9eb65c</p>
<p>*status = c800</p>
<p>0</p>
<p>son process exited abnormality!</p>
<p>exit code is: 182</p>
<p>child exited abnormal by signal exit code is: 92</p>
<b>main process is exiting!</b>
</p>
<br/>
<hr/>


**fork**函数
 作用：创建子进程
 区别：fork()函数在父进程返回一个大于0的整数（PID），在子进程返回0
 
 
**僵尸进程**
在UNIX 系统中，一个进程结束了，但是它的父进程没有等待(调用**wait / waitpid**)他， 那么它将变成一个僵尸进程；
如果该进程的父进程已经先结束，那么该进程就不会变成僵尸进程。

**演示僵尸进程的产生**
```c
#include<stdio.h>
#include<sys/types.h>
#include<sys/wait.h>
#include<stdlib.h>
#include<signal.h>
#include<unistd.h>
int INTR=1;
void stop()
{
	INTR=0;
	printf("process is exit\n");
}
void waiting()
{
	while(INTR)
	{
		sleep(1);
		printf("waiting\n");
	}
}
void main()
{
	pid_t child;
	int i,*status;
	while((child=fork())==-1); 	
	if(child==0)
	{
		//child process
		signal(16,stop);
		waiting();
	}
	else
	{
		//父进程
		sleep(3);
		kill(child,16);
		for(i=0;i<5;i++)
		{
			printf("father process output %d\n",i);
			sleep(1); 
		}
		system("ps -A -o stat,ppid,pid,cmd|grep -e '^[Zz]';ps -ax");
		//调用system函数，执行shell命令查找僵尸进程
		scanf("%d",&i);
		printf("father process exit!\n");
	}
}
```
 

 **gcc命令行选项**
 可将gcc选项分为4组：预处理选项、编译选项、优化选项和连接选项。
 <table>
  <tr>
   <td>选项</td>
   <td>功能</td>
   <td>例子</td>
  </tr>
  <tr>
   <td>-I dir</td>
   <td>指定搜索头文件的路径dir，指定该参数后会先在指定路径搜索要包含的头文件，
       若找不到，则到标准路径（/user/lib）搜索
    </td>
   <td>gcc -I /tmp hello.c</td>
  </tr>
  <tr>
   <td>-c</td>
   <td>只生成目标文件，不进行连接。用于对源文件的分别编译</td>
   <td>gcc -c meng1.c</td>
  </tr>
  <tr>
   <td>-o file</td>
   <td>将编译完成的可执行文件输出到文件file中。未使用该选项默认输入到a.out文件中</td>
   <td>gcc meng1.c -o m.exe</td>
  </tr>
  <tr>
   <td>-g</td>
   <td>指示编译程序在目标代码中加入可供调试程序GDB使用的附加信息</td>
   <td>gcc -g meng1.c -o m.exe</td>
  </tr>
   <tr>
   <td>-O</td>
   <td>试图减少代码大小和执行时间，不执行需要花费大量编译时间的任何优化</td>
   <td>gcc -O meng1.c -o m.exe</td>
  </tr>
  <tr>
   <td>-L dir</td>
   <td>把指定目录dir加到连接程序搜索文件的路径表中</td>
   <td>gcc -c f1.c f2.c -o f.exe -L$Home/lib</td>
  </tr>
 </table>
 
