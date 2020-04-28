## <a name="top">反射</a>


### Class



获取 `Class` 对象的3种方法：

+ 使用 `Class.forName(String className)` 加载一个类并获取**Class对象**，该方法会抛出**ClassNotFoundException** 异常
+ 使用`类名.class`获取获取**Class对象**
+ 使用对象的`getClass()`方法获取**Class对象**


获取**Class对象**后可以使用 `newInstance()` 获取类对象的实例，这个类要求有一个无参构造函数



-----

### Method



方法的修饰符：

+ 访问权限控制符：`public`、`protect`、`private`
+ 同步锁：`synchronized`
+ 不允许被重写：`final`
+ 类方法：`static`
+ 抽象方法：`abstract`



**方法类型** ：

+ `synthetic` —— 合成方法
+ `varagrs` —— 可变参数方法
+ `bridge` —— 桥接方法