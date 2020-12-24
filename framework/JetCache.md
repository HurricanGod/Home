# <a name ="top">JetCache</a>







## maven依赖

```sh
<dependency>
    <groupId>com.alicp.jetcache</groupId>
    <artifactId>jetcache-starter-redis</artifactId>
    <version>2.5.11</version>
</dependency>
```



----
## SpringBoot使用JetCache

+ 添加配置

```yml
jetcache:
  areaInCacheName: false
  statIntervalMinutes: 15
  remote:
    default:
      valueEncoder: java
      valueDecoder: java
      host: 127.0.0.1
      port: 6379
      database: 0
      uri: redis://127.0.0.1:6379
      poolConfig:
        minIdle: 5
        maxIdle: 10
        maxWait: 2000
      type: redis.lettuce
      keyConvertor: fastjson
  local:
    default:
      limit: 100000
      type: caffeine
      keyConvertor: fastjson
    otherArea:
      limit: 100000
      type: linkedhashmap
      keyConvertor: none

```

+ 在SpringBoot启动类上添加以下注解：
  + `@EnableCreateCacheAnnotation` —— 使用`@CreateCache`管理缓存
  + `@EnableMethodCache` —— 方法级别的缓存，可以使用 `@Cached` 来管理缓存


`@Cached`注解
+ `String area()`
+ `String name()`
+ `boolean enabled()`
+ `TimeUnit timeUnit()`
+ `int expire()`
+ `int localExpire()`
+ `CacheType cacheType()`
+ `int localLimit()`
+ `String serialPolicy()`
+ `String keyConvertor()`
+ `String key()` —— 支持SpEL表达式
+ `boolean cacheNullValue()`
+ `String condition()`
+ `String postCondition()`

注解的属性值可获取的上下文如下
|名称|位置|变量|类型|描述|
|:--|:--|:--|:--|:--|
|methodName|root对象|#root.methodname|String|被调用的方法名|
|method|root对象|#root.method|Method|被调用的方法|
|target|root对象|#root.target|Obj|当前被调用方法的实例，比如当前调用的是Service#run方法，target为Service的实例|
|targetClass|root对象|#root.targetClass|Class|当前被调用方法所属的类|
|args|root对象|#root.args|数组|被调用方法的参数列表，可以使用索引下标进行访问|
|caches|root对象|#root.caches|?|当前方法调用使用的缓存列表|
|Argument Name|执行上下文|#name|Object|被调用方法的参数，当前被调用的方法假设为`Service#run(String name)`，可以通过#name获取到方法参数|
|result|执行上下文|#result|Object|方法执行后的返回值|


