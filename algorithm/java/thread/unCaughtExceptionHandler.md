## 未捕获异常处理器



`Runnable` 的**run()** 不能抛出异常，不被检测的异常发生将会导致线程终止，线程发生异常死亡之前，线程会被传递到一个**用于捕获异常的处理器** ，处理器是一个实现了`Thread.UncaughtExceptionHandler` 接口的子类，接口定义如下：

```java
@FunctionalInterface
    public interface UncaughtExceptionHandler {
        /**
         * Method invoked when the given thread terminates due to the
         * given uncaught exception.
         * <p>Any exception thrown by this method will be ignored by the
         * Java Virtual Machine.
         * @param t the thread
         * @param e the exception
         */
        void uncaughtException(Thread t, Throwable e);
    }
```

该接口只有一个`uncaughtException()`一个方法，设置用于处理未捕获异常处理器的方法有两种：

+ 调用线程对象的`setUncaughtExceptionHandler(UncaughtExceptionHandler eh)`方法，为线程设置一个**未捕获异常处理器**
+ 调用`Thread`类的静态方法——`setDefaultUncaughtExceptionHandler(UncaughtExceptionHandler eh)`为线程设置一个**未捕获异常处理器**



如果不为**独立线程**设置默认的**未捕获异常处理器** ，默认的处理器为该线程的`ThreadGroup`对象



**线程组(ThreadGroup)**是一个可以统一管理的线程的集合，默认情况下创建的所有线程都属于同一个线程组。`ThreadGroup` 实现了`Thread.UncaughtExceptionHandler`接口，重写的`unCaugthException()`方法如下：

```java
private final ThreadGroup parent;
public void uncaughtException(Thread t, Throwable e) {
        if (parent != null) {
            parent.uncaughtException(t, e);
        } else {
            Thread.UncaughtExceptionHandler ueh =
                Thread.getDefaultUncaughtExceptionHandler();
            if (ueh != null) {
                ueh.uncaughtException(t, e);
            } else if (!(e instanceof ThreadDeath)) {
                System.err.print("Exception in thread \""
                                 + t.getName() + "\" ");
                e.printStackTrace(System.err);
            }
        }
    }

```

+ 如果该线程组的父线程组不为null，则调用父线程组的`uncaughtException()`方法
+ 否则获取该线程的默认**未捕获异常处理器**
  + 如果默认的异常处理器不为`null`则执行默认异常处理器的`uncaughtException()`方法
  + 如果异常不属于`ThreadDeath`类的实例，则把异常信息栈打印出来
  + 如果异常是`ThreadDeath`类的实例，则什么也不做



