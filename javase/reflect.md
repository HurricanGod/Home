## 反射



获取 `Class` 对象的3种方法：

+ 使用 `Class.forName(String className)` 加载一个类并获取**Class对象**，该方法会抛出**ClassNotFoundException** 异常
+ 使用`类名.class`获取获取**Class对象**
+ 使用对象的`getClass()`方法获取**Class对象**



获取**Class对象**后可以使用 `newInstance()` 获取类对象的实例，这个类要求有一个无参构造函数