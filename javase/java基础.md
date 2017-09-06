## Java基础

---

**Java**简单数据类型从**低级到高级** 分别为：

`(byte, short, char) < int < long < float < double` ；

+ 低级到高级可以自动完成类型转换，高级到低级需要强制类型转换。
+ `byte` 、`short` 、`char` 三种类型级别相同，不能相互自动转换，但可以强制转换



`>>`操作符进行移位操作时需要对移位参数进行模32运算

```java
int num = 32;
num = num >> 32; // num = 32
```



**传值与传引用**：

如果Java是传值，那么传递的是**值的副本**；如果传递的是**引用**， 那么传递的是**引用的副本** 

+ 对于基本类型变量（`boolean, byte, short, char, int, long, float, double`），Java是传值的副本，即把要传递的参数复制一个副本传递下去，即使副本值变了，原变量值也不会发生改变
+ 对于一切对象型变量，Java都是传引用的副本，传引用副本的实质是**复制指向地址的指针**；`C++`中当参数为**引用类型**时，传递的是**真实引用**
+ `String` 类的传递是传**引用副本** ，不过`String`是非可变类，所以传值与传引用没有什么区别 

---

### Java IO

Java的IO操作有**面向字节**和**面向字符**的两种方式

+ **面向字节**的操作以8位为单位对二进制数据进行操作，**对数据不进行转换** ，面向字节流的类都是`InputStream` 和 `OutputStream` 的子类
+ 面向字符的操作以**字符**为单位对数据进行操作，**在读的时候将二进制数据转换为字符， 在写的时候将字符转化为二进制数据** ，这些类都是`Reader` 和`Writer` 的子类




-----

容器类仅能**持有对象的引用**，把元素加入集合时并不是**将对象信息复制一份到容器里**；加入集合中的对象，不管通过什么方式获得对象的引用并把对象的属性改变了都会被反馈到容器里，因为集合中存放的对象的引用与任何方式获得的对象引用都指向对象在内存中的同一块地址

假设有一个集合`HashSet`，里面存放这`User`对象，`User`重写了`equals()`方法（**注意**：是重写，**参数类型、返回值、访问修饰符都要一致**，不是重载）：

+ 把user2添加到集合后，如果把user2的id属性改为4再从集合中删除，那么原来的user2将会从集合中移除，集合元素个数会减1
+ 持有对集合中某个对象的引用，用这个引用对对象属性做修改时不影响集合的个数
+ 如果把添加到集合中user2进行`user2 = null`操作，集合仍然保持着对这个对象的引用，**如果集合的生命比较长或者是整个应用程序周期，那么就会发生内存泄漏的**

**验证代码如下**：

```java
package hurrican.interview;

import java.util.HashSet;
import java.util.Set;

public class MemoryLeak {
    static class UserA{
        public Integer id;
        public String name;
        public String email;

        public UserA(Integer id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        public UserA() {
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof UserA) {
                UserA o = (UserA) obj;
                if (o.id.equals(this.id) && o.name.equals(this.name)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static void main(String[] args) {
        Set<UserA> set = new HashSet<>();
        UserA user1 = new UserA(1,"hurrican","123456@qq.com");
        UserA user2 = new UserA(2,"Approach","123456@qq.com");
        UserA user3 = new UserA(3,"Mr.Zhang","123456@qq.com");
        Object user4 = new UserA(1,"hurrican","123456@qq.com");
        UserA user5 = new UserA(1,"hurrican","123456@qq.com");


        System.out.println("user1.equals(user4) = " + user1.equals(user4));
        System.out.println("user1.equals(user5) = " + user1.equals(user5));


        set.add(user1);
        set.add(user2);
        set.add(user3);

        System.out.println("初始集合元素个数为：\t" + set.size());

        System.out.println("set.contains(user5) = " + set.contains(user5));

        user2.id = 4;

        set.remove(user2);

        System.out.println("把user2的id改为4后删除user2，集合元素个数为：\t" + set.size());

        user2.id = 2;

        set.add(user2);

        System.out.println("把user2的id改回2后添加user2，集合元素个数为：\t" + set.size());


        user3.name = "Mr.Wang";
        set.add(user3);
        System.out.println("把user3的name改为Mr.Wang后添加到集合，集合元素个数为：\t" + set.size());

        user3 = null;

        System.out.println("把user3置为null后集合元素个数为：\t" + set.size());

        for (UserA element : set) {
            System.out.println(element.id + "\t" + element.name);
        }
    }



}

```

![]()



