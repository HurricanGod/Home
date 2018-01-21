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

  ​![jsonlib-1](https://github.com/HurricanGod/Home/blob/master/javase/img/jsonlib-1.png)

> `json-lib`本身是没有提供对日期的支持，对它来说`Date`类型的数据只是一般的Objecct，从上面我们可以看到Date类型的字段被反射出来。Java → Json时主要是用反射去取属性值，再用get方法进行序列化的

Java → Json时如果日期类型需要序列化时按照一定的格式进行需要自己去实现接`JsonValueProcessor`口，这时就需要使用两个参数的`fromObject()`方法了

​	

<a name="fromObject1">`JSONObject fromObject(Object object,JsonConfig jsonConfig)`</a>

**用法**：

![jsonlib-2](https://github.com/HurricanGod/Home/blob/master/javase/img/jsonlib-2.png)

![jsonlib-3](https://github.com/HurricanGod/Home/blob/master/javase/img/jsonlib-3.png)





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

![jsonlib-4](https://github.com/HurricanGod/Home/blob/master/javase/img/jsonlib-4.png)



------

### 反序列化

当需要反序列化时可以使用`static Object toBean()`方法进行反序列化，当这里也**有个坑**——如果序列串有字段对应的Java实体中的**Date**类型，在不做任何配置下会得到**系统当前时间**，达不到转换期待的结果

![jsonlib-5](https://github.com/HurricanGod/Home/blob/master/javase/img/jsonlib-5.png)



**解决方法** ：

+ 实现接口`ObjectMorpher`，该接口有3个方法`Object morph(Object o)` 、`Class morphsTo()`、 `boolean supports(Class aClass)` ，作用分别为：**把序列化串中字符串转换为期待得到的对象** 、**用于指定要得到对象的类** 、 **用于支持哪种类型的解析，一般都是String**
+ 将自己实现的`ObjectMorpher`注册到`json-lib`中，即在调用`JSONObject.toBean()`方法前先调用`JSONUtils.getMorpherRegistry().registerMorpher(Morpher morpher)`方法



**代码示例**：

#### 实现`ObjectMorpher`接口代码

```java
 public class DateUtilMorpher implements ObjectMorpher {
    /**
     * 添加的自定义的日期格式，
     * 如json串中的日期格式为 2018-1-11 13:10:10，
     * 这里就应该把日期格式定义为 "yyyy-MM-dd HH:mm:ss"
     */
    private String pattern = "yyyy-MM-dd HH:mm:ss";

    @Override
    /**
     * @decription:
     * @param o 为json串中需要特殊处理字段的值，这里是要处理日期格式，o为某种格式的日期字符串
     * @return: java.lang.Object 期望得到的对象
     */
    public Object morph(Object o) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        if (o == null) {
            return null;
        }
        try {
            return dateFormat.parse((String) o);
        } catch (Exception e) {
            System.out.println("异常信息：\n" + e.getMessage());
            return null;
        }
    }

    @Override
    /**
     * @return: java.lang.Class 期望得到对象的类型
     */
    public Class morphsTo() {
        return Date.class;
    }

    @Override
    /**
     * 一般参数 aClass 为 String 时返回 true
     */
    public boolean supports(Class aClass) {
        if (aClass == String.class) {
            return true;
        }
        return false;
    }
}
```

![jsonlib-6](https://github.com/HurricanGod/Home/blob/master/javase/img/jsonlib-6.png)



[​]: https://www.cnblogs.com/natsu72/p/7809893.html	"json → java 日期字符串转日期"