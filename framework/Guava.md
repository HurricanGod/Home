
# Guava常用类


## <a name ="ArrayListMultimap">ArrayListMultimap</a>
ArrayListMultimap等价于`Map<K, List<V>>`

```java
ArrayListMultimap<String, Integer> multimap = ArrayListMultimap.create();
multimap.put("100653", 2);
multimap.put("100653", 3);
multimap.put("100653", 4);

// list = [2, 3, 4]
List<Integer> list = multimap.get("100653");

// list = [2, 3]
multimap.remove("100653", 4);

// list = []
list = multimap.get("100654");

```


----
## <a name="HashMultimap"></a>

ArrayListMultimap等价于`Map<K, Set<V>>`



----
## <a name="ImmutableListMultimap">ImmutableListMultimap</a>



  
----
## <a name ="HashBiMap">HashBiMap</a>



## <a name="HashBasedTable">HashBasedTable</a>
HashBasedTable等价于`HashMap<R, HashMap<C, V>>`



---
## <a name="CaseFormat">CaseFormat</a>

CaseFormat是一个用于各种ASCII大小写规范间转换的枚举类，支持相互转换的枚举值如下：
|枚举值|格式|备注|
|:---|:---|:---|
|LOWER_HYPHEN|lower-hyphen||
|LOWER_UNDERSCORE|lower_underscore|主流的C++变量命名规范|
|LOWER_CAMEL|lowerCamel|主流的Java变量命名规范|
|UPPER_CAMEL|UpperCamel|Java和C++主流的类命名规范|
|UPPER_UNDERSCORE|UPPER_UNDERSCORE|Java和C++主流的常量命名规范|


`CaseFormat`提供了2个用于字符串风格转换的方法
```java
public enum CaseFormat{
  
  String convert(CaseFormat format, String s);
  
  public final String to(CaseFormat format, String str);

}

```

使用样例：
```java
// goodsId
String goodsId = CaseFormat.LOWER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL).convert("goods_id");

// goodsID
goodsId = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, "GoodsID");
```






