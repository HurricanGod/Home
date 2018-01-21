## json序列化



**json序列化的优点** ：

+ 数据格式简单，易于读写
+ 客户端易于解析
+ 网络上传输数据时，相比xml，`json`传输数据**占用带宽小**



### json-lib序列化

`maven`依赖：

```xml
        <dependency>
            <groupId>net.sf.json-lib</groupId>
            <artifactId>json-lib</artifactId>
            <version>2.3</version>
            <classifier>jdk15</classifier>
        </dependency>

```



**常用Api**：

+ <a href="#fromObject0">`static JSONObject fromObject(Object object)`</a>
+ <a href="#fromObject1">`static JSONObject fromObject(Object object, JsonConfig jsonConfig)`</a>
+ <a href="#setExcludes">`void setExcludes(String[] excludes)`</a>





-----

<a name="fromObject0">`JSONObject fromObject(Object object)`</a>

+ 1个参数的`fromObject()`其实调用了下面两个参数的`fromObject()`方法，该方法主要是将对象进行序列化
+ 使用`json-lib`序列化时，若对象中有`Date`类型，序列结果如下(这是1个坑)：

​	![jsonlib-1]()

> `json-lib`本身是没有提供对日期的支持，对它来说`Date`类型的数据只是一般的Objecct，从上面我们可以看到Date类型的字段被反射出来。Java → Json时主要是用反射去取属性值，再用get方法进行序列化的

Java → Json时如果日期类型需要序列化时按照一定的格式进行需要自己去实现接`JsonValueProcessor`口，这时就需要使用两个参数的`fromObject()`方法了

​	

<a name="fromObject1">`JSONObject fromObject(Object object,JsonConfig jsonConfig)`</a>

**用法**：

![jsonlib-2]()

![jsonlib-3]()





<a name="setExcludes">`void setExcludes(String[] excludes)`</a>

+ 该方法用于设置不进行序列化的字段

**示例**：

```java

    @Test
    public void testMethod4() {
        Bean1 bean1 = Bean1.getInstance();

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"bean2"});

        JSONObject jsonObject = JSONObject.fromObject(bean1, jsonConfig);
        System.out.println(jsonObject);
    }
```

![jsonlib-4]()



------

