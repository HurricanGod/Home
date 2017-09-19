## 类型转换器

**SpringMVC**中有默认的类型转换器，默认的类型转换器将String类型自动转换为相应的数据类型，但默认类型转换器并不是可以将用户提交的String转换为用户所需要的类型时就需要**自定义类型转换器**



**SpringMVC**没有日期类型的转换器，若表单中提交的日期字符串，被处理方法的对应的形参类型为**java.util.Date**，则需要自定义类型转换器

---

使用**自定义类型转换器**步骤：

+ 创建自定义类型转换类

+ 在**Spring**配置文件里配置**自定义类型转换类**的实例`instance`

+ 将`instance`实例注册到`ConversionServiceFactoryBean`的**converters**属性中

+ 注册mvc注解驱动，并把`ConversionServiceFactoryBean`的**实例**赋给`conversion-service`属性

  ```xml
  <mvc: annotation-driven  conversion-service="xxxx">
  ```



-----

## 数据回显



`SimpleMappingExceptionResolver`捕获的是处理器方法在执行过程中发生的异常，而**类型转换异常发生在处理器方法执行之前**，使用`SimpleMappingExceptionResolver`无法捕获到类型转换异常；**注解式异常处理**可以获取**类型转换异常**