#### 进制转换
**题目描述**<br>
原理：ip地址的每段可以看成是一个0-255的整数，把每段拆分成一个二进制形式组合起来，然后把这个二进制数转变成一个长整数。<br>
举例：一个ip地址为10.0.3.193<br>
每段数字             相对应的二进制数<br>
10                   00001010<br>
0                    00000000<br>
3                    00000011<br>
193                  11000001<br>
组合起来即为：00001010,00000000,00000011,11000001,转换为10进制数就是：167773121，即该IP地址转换后的数字就是它。<br>
 
以下使用的方法效率低，单纯是为了了解常用API！  
**注意点：**
1. int型所能表示的最大值为：01111111 11111111 11111111 11111111，32位的ip无法用int来表示   
2. ``Long.toBinaryString(n)``方法可以把一个long型数据字节转换为二进制字符串   
3. 字符串类的spilt()方法如果要以"."或"|"分割字符串，需要对这两个字符进行转义，正确写法应该为str.spilt("\\.")或str.spilt("\\|")   
4. ``Integer.valueOf(string,radix)``可以把进制为radix相应的字符串转换为整数值   

```java
import java.util.Scanner;
/**
 * Created by NewObject on 2017/6/8.
 */
public class Main {

    /**
     * 把一个32位整数转为点分十进制法表示的ip地址
     * 例：0 → 0.0.0.0
     */
    public static String converIntegerToIpString(long n, boolean debugPattern) {
        StringBuffer ipString = new StringBuffer(Long.toBinaryString(n));
        if (debugPattern) {
            System.out.println("original ipString = " + ipString);
        }
        ipString = ipString.reverse();
        if (ipString.length()<32) {
            for (int i = 32-ipString.length(); i >0 ; i--) {
                ipString.append("0");
            }
        }
        if (debugPattern) {
            System.out.println("after reversing, the ipString = " + ipString);
        }
        StringBuffer[] ipbuffer = new StringBuffer[4];
        for (int i = 8,j=0; i < ipString.length(); i+=8,j++) {
            ipbuffer[j] = new StringBuffer();
            ipbuffer[j].append(ipString.substring(i - 8, i));
            ipbuffer[j] = ipbuffer[j].reverse();
            if (i == 24) {
                ipbuffer[j+1] = new StringBuffer();
                ipbuffer[j+1].append(ipString.substring(i, ipString.length()));
                ipbuffer[j+1] = ipbuffer[j+1].reverse();
                if (debugPattern) {
                    System.out.println((j+2) + ". " + ipbuffer[j+1]);
                }

            }
            if (debugPattern) {
                System.out.println((j+1) + ". " + ipbuffer[j].toString());
            }
        }
        ipString.delete(0,ipString.length());
        for (int i = ipbuffer.length-1; i >= 0; i--) {
            ipString.append(Integer.valueOf(ipbuffer[i].toString(),2)).append(".");
        }
        if (debugPattern) {
            System.out.println(ipString);
        }

        return ipString.substring(0, ipString.length()-1);
    }

    public static long convertIpStringToInteger(String ipString, boolean debugPattern) {
        long value = 0;
        String[] ip = ipString.split("\\.");
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < ip.length; i++) {
            try {
                int digital = Integer.parseInt(ip[i]);
                 //把字符串转换为整数
                StringBuffer t = new StringBuffer(Integer.toBinaryString(digital));
                //把10进制整数转换为2进制字符串
                if (t.length()<8) {
                    for (int j = 8-t.length(); j >0 ; j--) {
                        t.insert(0,"0");
                    }
                }
                buffer.append(t);
                if (debugPattern) {
                    System.out.println("digital = " + digital);
                    System.out.println("t = " + t);
                }
            } catch (Exception e) {
                System.out.println("异常信息：\n" + e.getMessage());
            }
        }
        if (debugPattern) {
            System.out.println("buffer = " + buffer);
        }
        value = Long.valueOf(buffer.toString(), 2);
        return value;
    }

    public static void main(String[] args) {
        long n = 0;
        String ip;
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext())
        {
            ip = scanner.next();
            n = scanner.nextLong();

            System.out.println(convertIpStringToInteger(ip,false));
            System.out.println(converIntegerToIpString(n, false));
        }

    }
}
```
>>**运行结果：**<br>
10.2.6.156<br>
466555<br>
167904924<br>
0.7.30.123<br>
