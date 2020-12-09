# <a name="top">SpEL表达式</a>





+ 引用字面量
  + `#{9.9}` —— 浮点常量
  + `#{'Java'}` —— 字符串常量
  + `#{true}` —— bool常量



+ 引用`bean`属性或方法
  + `#{obj.id}`
  + `#{obj.toString()}`
  + `#{obj.getPrice()?.toInt()}` —— `getPrice()`不为null的情况下才执行 `toInt()`方法



+ 引用集合
  + `#{array[0].id}` —— 获取数组中第1个元素的id
  + `#{map['key']}` —— 访问map中的 `key` 对应的值



+ 引用静态方法或常量 —— `T()`
  + `T(java.lang.System).currentTimeMillis()`
  + `T(java.lang.System).out`





+ **表达式**
  + **instanceof表达式** ：`#{vale nstanceof T(String)}`
  + **安全导航表达式**： `#{var?.var.toString()}`
  + **正则表达式**：`matches` —— `#{username matches 'admin.*'}`
  + **三元表达式**：`? a:b` —— `#{obj.price > 0 ? obj.price : 0}`





+ 运算符
  + **算术运算**：`+`、`-`、`*`、`/`、`%`、`^`
  + **比较运算**：`>`、`<`、`==`、`>=`、`<=`
  + **逻辑运算**：`and`、`or`、`not`





+ `SpEL`上下文内置参数
  + `#{root}`








