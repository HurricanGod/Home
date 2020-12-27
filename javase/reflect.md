## <a name="top">反射</a>

+ <a href="#class">Class</a>
  + <a href="#class-api">常用API</a>



+ <a href="#Method">Method</a>
  + <a href="#method-type">方法类型</a>




+ <a href="#Field">Field</a>

  + <a href="#Field-Api">常用Api</a>





+ <a href="#ParameterizedType">ParameterizedType</a>





----
### <a name="class">Class</a>



获取 `Class` 对象的3种方法：

+ 使用 `Class.forName(String className)` 加载一个类并获取**Class对象**，该方法会抛出**ClassNotFoundException** 异常
+ 使用`类名.class`获取获取**Class对象**
+ 使用对象的`getClass()`方法获取**Class对象**



#### <a name="class-api">常用API</a>

+ `public Field[] getDeclaredFields() throws SecurityException`

  该方法返回本类定义的public、protected、private、package修饰的**成员变量**和**类变量**，**不会返回父类的字段**。

+ `public Field[] getFields() throws SecurityException`

  该方法返回本类所有public修饰的**成员变量**和**类变量**，**包括父类的**

```java
@Data
public class A {

    public static final int A_DEFAULT_ID = 100;

    public int id;

    private String name;

    private List<Integer> refIdList;
}

public class A1 extends A{

    private static final int  CONST_ONE = 1;

    public String a1Address;

    private String email;

    public String getEmail() {
        return email;
    }
}


public class SimpleTest {
     @Test
    public void testA1(){
        Class<A1> clazz = A1.class;
        
        Field[] declaredFields = clazz.getDeclaredFields();
        // declaredFields = {CONST_ONE, a1Address, email}
        
        Field[] fields = clazz.getFields();
        // fields = {a1Address, A_DEFAULT_ID, id}
    }
}
```





+ `public Method[] getDeclaredMethods() throws SecurityException`

  该方法返回本类定义的所有public、protected、private、package修饰的实例方法和静态方法



+ `public Method[] getMethods() throws SecurityException`

  该方法返回**本类及其父类**所有`public`修饰的实例方法和静态方法





+ `public native boolean isAssignableFrom(Class<?> cls)`

  判断参数给定的类是否属于当前类的子类或者跟当前类是相同的类型

  ```java
  // true
  List.class.isAssignableFrom(ArrayList.class);
  
  // false
  ArrayList.class.isAssignableFrom(List.class);
  ```

  



<p align="right"><a href="#class">返回</a> &nbsp&nbsp| <a href="#top">返回目录</a> </p>

-----
### <a name="Method">Method</a>



方法的修饰符：

+ 访问权限控制符：`public`、`protect`、`private`
+ 同步锁：`synchronized`
+ 不允许被重写：`final`
+ 类方法：`static`
+ 抽象方法：`abstract`



#### <a name="method-type">方法类型</a>

+ `synthetic` —— 合成方法
+ `varagrs` —— 可变参数方法
+ `bridge` —— 桥接方法







<p align="right"><a href="#Method">返回</a> &nbsp&nbsp| &nbsp&nbsp<a href="#top">返回目录</a> </p>

---
### <a name="Field">Field</a>



#### <a name="Field-Api">常用Api</a>

+ `public Type getGenericType()`



+ `public Class<?> getType()`








<p align="right"><a href="#Field">返回</a> &nbsp&nbsp| &nbsp&nbsp<a href="#top">返回目录</a> </p>

------
### <a name="ParameterizedType">ParameterizedType</a>



```java
public interface ParameterizedType extends Typ{
    
    Type[] getActualTypeArguments();
    
    
    Type getRawType();
    
    
    Type getOwnerType();
}
```







<p align="right"><a href="#ParameterizedType">返回</a> &nbsp&nbsp| &nbsp&nbsp<a href="#top">返回目录</a> </p>