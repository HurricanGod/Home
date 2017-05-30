**进程**
 
在Linux运行的每个进程都有一个唯一的进程标识符PID，PID的数值是逐渐增大的，一般子进程的PID会比父进程大。
如果 父进程死亡或退出，则子进程会被指定一个父进程**init**（PID为1）。
 
 
 
 **fork**函数
 作用：创建子进程
 区别：fork()函数在父进程返回一个大于0的整数（PID），在子进程返回0
 
 
**僵尸进程**
在UNIX 系统中，一个进程结束了，但是它的父进程没有等待(调用**wait / waitpid**)他， 那么它将变成一个僵尸进程；
如果该进程的父进程已经先结束，那么该进程就不会变成僵尸进程。



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
   <td>gcc -O meng1.c -o m.exe</td>
  </tr>
 </table>
 
