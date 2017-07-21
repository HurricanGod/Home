### 代理模式

**CGLIB(Code Generation Library)**:对于无接口的类，要为其创建动态代理，需要使用CGLIB来实现。

>***原理：*** 生成目标类的子类,子类对父类的方法进行增强，子类对象就成了代理对象

>***要点：*** 被增强的类必须能够被继承，不能是final修饰的类
 
#### 静态代理类图
![静态代理类图](https://github.com/HurricanGod/Home/blob/master/img/StaticProxy.png)
