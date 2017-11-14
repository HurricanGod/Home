## IO



---

### 字节流

+ `InputStream` (**抽象类**)
+ `OutputStream`  (**抽象类**)



----

<a name="inputStream">`InputStream` 常见实现类：</a>

+ `FileInputStream`
+ `PipedInputStream`
+ `ObjectInputStream`
+ `ByteArrayInputStream`



----



### 字符流

+ `Reader`  (**抽象类**)
+ `Writer`  (**抽象类**)



`Reader` 常见实现类：

+ `InputStreamReader`
+ `BufferedReader`
+ `PipedReader` （**应用于线程间通信**）



`InputStreamReader` 常见构造函数如下：

+ `InputStreamReader(` <a href="#inputStream">`InputStream in`</a>`)`
+ `InputStreamReader(`  <a href="#inputStream">`InputStream in`</a> `, String charsetName)`
+ `InputStreamReader(`  <a href="#inputStream">`InputStream in`</a> `, Charset cs)`