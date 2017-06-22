#### java.lang包常用类     

![java.lang常用类](https://github.com/HurricanGod/Home/blob/master/img/java.lang.jpeg)

##### java.lang.Math需要注意的几个方法：        
![Math类需要注意的方法](https://github.com/HurricanGod/Home/blob/master/img/Math.jpg)     

```java
@Test
    public void testMathClass() {
        double c = 10.8;
        System.out.println("c = " + c);
        System.out.println("Math.floor(c) = " + Math.floor(c)); //向下取整,返回double类型 10.0
        System.out.println("Math.ceil(c) = " + Math.ceil(c));   //向上取整,返回double类型 11.0
        System.out.println("Math.round(c) = " + Math.round(c)); //四舍五入 11
        System.out.println("Math.rint(c) = " + Math.rint(c));   //求最接近c的数 11.0
        c  = 10.4;
        System.out.println("c = " + c);
        System.out.println("Math.round(c) = " + Math.round(c)); //四舍五入 10
        System.out.println("Math.rint(c) = " + Math.rint(c));   //求最接近c的数 10.0
        System.out.println(Math.random());
    }

```

>>
**运行结果：**
```
c = 10.8
Math.floor(c) = 10.0
Math.ceil(c) = 11.0
Math.round(c) = 11
Math.rint(c) = 11.0
c = 10.4
Math.round(c) = 10
Math.rint(c) = 10.0
0.4904361136150911
```  

##### Byte需要注意点

**java中整型数据是以补码的形式保存在内存中的，所有的整型数据都是有符号，不像C++那样有无符号整型**<br><br>
对于1个字节，如果是无符号来说表示范围为**0x00~0xff**，与0~255一一对应。<br>
但对于有符号整型，1字节能表示的范围是`-128~127`。<br>
用补码表示整型时：<br>
0000 0000 ：0 <br>
0111 1111 ：127<br>
**1111 1111**表示什么？<br>
如果是原码当然是-127，但这里是补码，表示为-127显然是错的。 <br>

----

补码的求法为把**原码按位取反然后再加1**，要把补码变换为原码的方法有2种：<br>
1. 先把补码取反（***符号位除外***）然后再加1. <br>
   例： 1111 1111（*补码*） → 1000 0000 → 1000 0001（**原码**）  <br> 
2. 从右往左，找到第1个不为**0**的位，把该位左边的所有位取反（***符号位除外***），得到的结果即为原码.<br>
   例： 1111 1111（*补码*） → 1000 0001（**原码**）<br>

-----
那么-128该怎么表示？<br>
    在补码里并没有`-0`的存在，如果存在`-0`那么1000 0000和0000 0000岂不是同时表示0?这样做显然会浪费1个数字<br>
因此在补码里`1000 0000`并不表示`-0`，而是表示的是***-128***。需要注意的是原码是无法表示`-128`的<br>

**在补码表示法中**<br>
0000 0000 ：0    <br>
0111 1111 ：127  <br>
1000 0000 ：-128 <br>
1111 1111 ：-1   <br>
 
##### Runtime类、Process类及ProcessBuilder类 
java中的Runtime类表示运行时操作类，是1个封装了的JVM进程的类，每个JVM都对应1个Rumtime类实例，Runtime类本身的构造方法是私有化的，使用了单例模式<br>
需要注意的是Runtime实例的**exec()**方法，如果用exec()方法来执行windows或linux命令，这时需要特别注意与管道有关的命令<br/>
 
下面使用Runtime实例执行**ipconfig -all**查看本机ip地址信息<br>
```java
@Test
    public void testExecMethodOfRuntime() {
        String command = "ipconfig -all";
        Runtime runtime = Runtime.getRuntime();
        Process process;
        try {
            process = runtime.exec(command);
            InputStreamReader input = new InputStreamReader(process.getInputStream(), "GBK");
            BufferedReader reader = new BufferedReader(input);
            String line;
            while ((line=reader.readLine())!=null){
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
```
这时执行命令是没有问题的，也有结果输出。<br>

-----

但如果执行``ipconfig -all | findstr 默认网关``命令，即从``ipconfig -all``输出结果中获取所有含有**默认网关**的行
![运行结果比较](https://github.com/HurricanGod/Home/blob/master/img/runtimeExec.png)
:scream:
<br>``ipconfig -all | findstr 默认网关``命令被Runtime实例解释为1个命令，从而执行命令失败，如果需要执行带管道的shell命令就需要使用字符串数组作为参数
<br>使用``cmd /c "ipconfig -all | findstr 默认网关"``来替换，其中这条cmd命令以数组作为参数传递给**exec()**方法时，命令分为3部分
<br>``String[] commands = {"cmd", "/c","ipconfig -all | findstr 默认网关"}``，“**cmd**”作为一个命令参数，“/c”又作为一个参数。
<br>使用`cmd /?`查看帮助文档如下：
<br>``/C      执行字符串指定的命令然后终止,如果字符串加有引号，可以接受用命令分隔符 "&&"分隔多个命令``
<br>也就是说会执行**/c**后面的字符串命令，如果需要执行多条命令可以使用``&&``进行分割.
<br>也就是可以执行形如**cmd /c ipconfig -all| findstr 默认网关&& route print**或
<br>**cmd /c "ipconfig -all| findstr 默认网关&& route print"**的命令<br>

**修改后代码如下**
```java
 @Test
    public void testExecMethodOfRuntime() {
        String[] command = {"cmd", "/c", "ipconfig -all |findstr 默认网关"};
        Runtime runtime = Runtime.getRuntime();
        Process process;
        try {
            process = runtime.exec(command);
            InputStreamReader input = new InputStreamReader(process.getInputStream(), "GBK");
            BufferedReader reader = new BufferedReader(input);
            String line;
            while ((line=reader.readLine())!=null){
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
```
**运行结果：**<br>


 
