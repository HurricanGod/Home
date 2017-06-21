#### 设计模式-单例:simple_smile:
保证1个类仅有1个实例，并提供一个访问该实例的方法。因此单例模式的构造函数为私有化的，并且有1个静态方法用于获取唯一的实例    
java的单例模式有两种，分别为：
1. 饿汉式单例，在加载类信息的就调用私有构造函数实例化自己，不管后面的程序需不需要使用该实例。特点是实现起来简单，但会造成内存浪费。
2. 懒汉式单例，只有在第1次要使用这个类实例时才创建该实例。特点：会面临多线程的安全性问题，稍不注意就会使得对象的实例不唯一。

**我们来看一下Integer一些有趣的例子**         

```java
import org.junit.Test;
public Class Test{
   @Test
    public void testSingleton() {
        Integer i1 = -128;
        Integer i2 = -128;
        Integer a1 = 128;
        Integer a2 = 128;
        Integer b1 = -127;
        Integer b2 = -127;
        System.out.println("i1 == i2\t\t" + (i1 == i2));
        System.out.println("i1.equals(i2)\t" + (i1.equals(i2)));
        System.out.println("a1 == a2\t\t" + (a1 == a2));
        System.out.println("a1.equals(a2)\t" + (a1.equals(a2)));
        System.out.println("b1 == b2\t\t" + (b1 == b2));
        System.out.println("b1.equals(b2)\t" + (b1.equals(b2)));
    }
}
```


**运行结果：**
>
```
i1 == i2		true
i1.equals(i2)	        true
a1 == a2		false
a1.equals(a2)	        true
b1 == b2		true
b1.equals(b2)	        true
```
***从上面结果我们可以知道，对于Integer，它缓存了-128~127之间的整数，如果以这范围的整数初始化Integer将得到同一个对象*** 
#### 单例模式实现常量池
**需求：**      
1. 模仿Integer类实现用-128~127之间的整数实例化对象时得到的唯一的      
2. 用其它整数实例化时得到的对象为不同的     

```java
public class IntegerExt {
    private int i;
    private IntegerExt(int i){
        this.i = i;
    }
    public int toIntValue(){
        return i;
    }
    private static IntegerExt[] cacheInt = null;  //定义用于保存单例对象的数组
    public static IntegerExt getInstance(int i){
    	if(i>=-128 && i<=127){
      // 判断缓存数据是否为null，若为null则创建1个新实例
    		if(cacheInt == null){
    			synchronized(IntegerExt.class){
            //再次判断是否创建实例，原理：可能存在在某个线程进入同步块前有多个线程已经进入了第1个判断条件
            //但同步块里只允许1个线程进入，如果不加null的判断，
            //当进入同步块的线程执行完毕后其它形成就会进入同步块继续创建实例，使得实例不止1个
      				if(cacheInt == null){
    					cacheInt = new IntegerExt[256];
    					for(int j=-128; j<128;j++){
    						cacheInt[j+128] = new IntegerExt(j);
    					}
    				}
    			}
    			return cacheInt[128+i];
    		}else{
    			return cacheInt[128+i];
    		}	
    	}else{
    		return new IntegerExt(i);
    	}
    }
    
    public boolean equals(IntegerExt other){
    	boolean isequal = other == null ?false:true;
    	isequal = isequal && this.toIntValue()==other.toIntValue();
    	return isequal;
    }
}

```
