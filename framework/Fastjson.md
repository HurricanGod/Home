# <a name="top">Fastjson</a>





##  <a name="datetime">日期格式</a>



`Fastjson`默认使用 `yyyy-MM-dd HH:mm:ss` 格式序列化`Date`类型数据，可以识别的日期格式主要有：

+ `yyyy-MM-dd HH:mm:ss`
+ `yyyy-MM-dd HH:mm:ss.SSS`
+ `yyyy/MM/dd HH:mm:ss`
+ `yyyy年M月d日 HH:mm:ss`
+ `dd-MM-yyyy HH:mm:ss`
+ `dd/MM/yyyy HH:mm:ss`
+ `yyyyMMdd`
+ `yyyy/MM/dd`
+ `yyyy年M月d日`
+ `dd/MM/yyyy`
+ `MM/dd/yyyy`
+ `dd-MM-yyyy`
+ `yyyy-MM-dd'T'HH:mm:ss`
+ `yyyy-MM-dd'T'HH:mm:ss.SSS`
+ `yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS`





----

### 常见的序列化特性







### Annotation注解

+ `JSONField `

  + `name()` —— 指定序列化反序列化的字段名称

  + `format()`

    + `unixtime` —— 将**数值**类型当做Unix时间戳进行反序列化、反序列化 `Date`

      ```java
      @Data
      public class Foo {
          @JSONField(format = "unixtime")
          private Date createTime;
      }
      
      // 序列化： now() → {"createTime":1607423248}
      // 反序列化："{\"createTime\":1607423648}" → 2020-12-08 18:34:08
      ```

      

    + `millis` 

      ```java
      @Data
      public class Foo {
          @JSONField(format = "millis")
          private Date createTime;
      }
      
      // 序列化： now() → {"createTime":1607423567234}
      // 反序列化："{\"createTime\":1607423648000}" → 2020-12-08 18:34:08
      ```

      

    + `yyyyMMddHHmmss` —— 序列化或反序列化后字段的类型为`String`

      ```java
      @Data
      public class Foo {
          @JSONField(format = "yyyyMMddHHmmss")
          private Date createTime;
      }
      
      // 序列化： now() → {"createTime":"20201208183428"}
      // 反序列化："{\"createTime\":\"20201208183000\"}" → 2020-12-08 18:34:08
      ```

      

    

  + `serialize()` —— 是否序列化

  + `deserialize()` —— 是否反序列化
```java
@Data
    public class Foo {
    
        private Date createTime;
    
        @JSONField(serialize = false)
        private Boolean isShow;
    }
    	
    
    @Test
    public void testJsonFieldFormat(){
        Foo foo = new Foo();
        foo.setCreateTime(new Date());
        foo.setIsShow(false);
        System.out.println(JSON.toJSONString(foo));
        // {"createTime":1607438771308}
    }
```

​    

​    

  + `Class<?> serializeUsing()` 
  
    +  使用指定的类进行序列化，该类需要实现 `com.alibaba.fastjson.serializer.ObjectSerializer`接口
    
      ```java
      public static class YnSerializer implements ObjectSerializer{
      
          /**
           *
           * @param serializer serializer里有SerializeWriter成员，可以直接使用SerializeWriter拼接字符串
           * @param object 待序列化的对象
           * @param fieldName 
           * @param fieldType
           * @param features
           */
          @Override
          public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
              if(object != null){
                  serializer.write(Boolean.TRUE.equals(object) ? "Y" : "N");
              }
          }
      }
      ```
    
    + 在注解上指定序列化使用的类`@JSONField(serializeUsing = YnSerializer.class)`

  

  + `Class<?> deserializeUsing()` 
  
    + 使用指定的类进行反序列化，该类需要实现 `com.alibaba.fastjson.parser.deserializer.ObjectDeserializer` 接口
    
      ```java
      public static class YnDeSerializer implements ObjectDeserializer{
      
          @Override
          public Object deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
              Object originVal = parser.parse(fieldName);
              if("Y".equals(originVal)){
                  return true;
              }else if( "N".equals(originVal) ){
                  return false;
              }
              return null;
          }
      
          @Override
          public int getFastMatchToken() {
              return JSONToken.LITERAL_STRING;
          }
      }
      
      ```
    
      
    
    + 在注解上指定反序列化使用的类 `@JSONField(deserializeUsing =YnDeSerializer.class)`
    
    
    
    
    
    
    
    ----
  
    ## 自定义序列化&&反序列化
  
    
  
    ### 自定义序列化
  
    + 实现 `com.alibaba.fastjson.serializer.ObjectSerializer`接口
    + 注册到`SerializeConfig`中
    
    
    
    
    
    
    
    
  **参考**
    
  + <a href="https://github.com/alibaba/fastjson/wiki/ObjectSerializer_cn">fastjson自定义序列化官方文档</a>
      
  -----
    
  
    
  ### 自定义反序列化
    
    + 实现 `com.alibaba.fastjson.parser.deserializer.ObjectDeserializer` 接口
  + 将自定义反序列化类注册到 `ParserConfig`中
      
  
      
  
    **参考**
  
    + <a href="https://github.com/alibaba/fastjson/wiki/ObjectDeserializer_cn">fastjson自定义反序列化官方文档</a>
    
    
    
    
    
    -----
  
    
  
    
    
    

## Reference

+ <a href="http://kimmking.github.io/2017/06/06/json-best-practice/">JSON最佳实践</a>



