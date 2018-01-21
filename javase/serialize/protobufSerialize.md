## ProtoStuff序列化



**maven依赖** ：

```xml
        <dependency>
            <groupId>io.protostuff</groupId>
            <artifactId>protostuff-core</artifactId>
            <version>1.4.0</version>
        </dependency>

        <dependency>
            <groupId>io.protostuff</groupId>
            <artifactId>protostuff-runtime</artifactId>
            <version>1.4.0</version>
        </dependency>

```





**工具类** ：

```java
public class ProtoBufUtil {

    /**
     * @decription: 将 obj 对象序列化为字节数组
     * @param obj 待序列化的对象
     * @return: byte[]
     */
    public static <T> byte[] serializer(T obj){
        // 构建模式，该步骤比较耗时
        Schema schema = RuntimeSchema.getSchema(obj.getClass());
        return ProtobufIOUtil.toByteArray(obj, schema, LinkedBuffer.allocate(1024));
    }


    /**
     * @decription: 将字节数组反序列化指定的类
     * @param bytes 
     * @param clazz 目标类
     * @return: T
     */
    public static <T> T deserializer(byte[] bytes, Class<T> clazz){
        T obj = null;
        try {
            obj = clazz.newInstance();
            Schema<T> schema = RuntimeSchema.getSchema(clazz);
            ProtobufIOUtil.mergeFrom(bytes, obj, schema);
        } catch (Exception e) {
            System.out.println("异常信息：\n" + e.getMessage());
        }
        return obj;
    }
}

```



![]()

`ProtoStuff`序列化与`Json`序列化比较：

+ `ProtoStuff`序列化产生的结果是二进制字节流，方便在网络中传输；
+ 序列化同一个对象时，与`Json`序列化的结果相比，`ProtoStuff`序列化产生的结果更小，意味着在网络上传输时可以**节省带宽**
+ `Json`序列化结果可读性强，`ProtoStuff`序列化适用于需要进行大数据量网络传输的场景







[参考文档​]: https://www.programcreek.com/java-api-examples/index.php?api=io.protostuff.ProtobufIOUtil	"参考文档"