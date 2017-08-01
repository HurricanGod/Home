### 代理模式

**CGLIB(Code Generation Library)**:对于无接口的类，要为其创建动态代理，需要使用CGLIB来实现。

>***原理：*** 生成目标类的子类,子类对父类的方法进行增强，子类对象就成了代理对象


>***要点：*** 被增强的类必须能够被继承，不能是final修饰的类

#### 静态代理类图
![静态代理类图](https://github.com/HurricanGod/Home/blob/master/img/StaticProxy.png)



-----

## JDK动态代理

核心类：

- `java.lang.reflect.Proxy`
- `java.lang.reflect.InvocationHandler`

**JDK动态代理**要点：

- 被代理对象要实现接口
- 必须实现`java.lang.reflect.InvocationHandler`类并重写`invoke`方法
- 在`invoke`方法里可以对要代理的对象进行增强，`invoke`方法的第2个参数`method`就是被代理对象要增强的方法，调用前需要与被代理对象绑定，也就是要往`method.invoke()`的第一个参数传递被代理对象

```java
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxyPattern {
    static interface IService{
        void doSomePost();
    }

    static class Serviceimp implements IService{
        public void doSomePost(){
            System.out.println("提交表单更新数据");
        }
    }

    static class EnhanceProxy{
        private IService service = new Serviceimp();
        public void preEnhanceMethod(){
            InvocationHandler handler = new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    // 在此处可以添加被代理对象的增强行为
                    System.out.println("前置增强效果：");
                    System.out.println("检查用户是否有权限更新数据");
                    return method.invoke(service, args);
                }
            };
            IService proxy = (IService) Proxy.newProxyInstance(service.getClass().getClassLoader(),
                    service.getClass().getInterfaces(), handler);
            proxy.doSomePost();
        }

        public void postEnhanceMethod(){
            InvocationHandler handler = new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println("后置增强效果：");
                    Object res = method.invoke(service, args);
                    System.out.println("返回更新操作的结果，成功还是失败?");
                    return res;
                }
            };
            IService proxy = (IService) Proxy.newProxyInstance(service.getClass().getClassLoader(),
                    service.getClass().getInterfaces(), handler);
            proxy.doSomePost();
        }
    }

    public static void main(String[] args) {
        EnhanceProxy proxy = new EnhanceProxy();
        proxy.preEnhanceMethod();
        System.out.println();
        proxy.postEnhanceMethod();
    }

}
```

***运行结果如下：***

```
前置增强效果：
检查用户是否有权限更新数据
提交表单更新数据

后置增强效果：
提交表单更新数据
返回更新操作的结果，成功还是失败?
```



