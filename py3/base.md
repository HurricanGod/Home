# <a name="top">Python</a>





----

+ <a href="#str">**字符串**</a>


+ <a href="#">**列表**</a>


+ <a href="#">**元组**</a>


+ <a href="#">**字典**</a>


+ <a href="#">**类**</a>


+ <a href="#">**集合**</a>


+ <a href="#">**函数**</a>


+ <a href="#">**异常**</a>










-----

## <a name="str">**字符串**</a>

Python中内置了对字符串进行格式化的操作` %`，格式化字符串时，Python使用一个字符串作为模板。模板中有格式符，这些格式符为真实值预留位置，并说明真实数值应该呈现的格式。Python用一个`tuple`将多个值传递给模板，每个值对应一个格式符

**例1** —— 元组格式化字符串：

```python
sql = 'insert into tb1(a,b) values(%s, %s)' % ('张三', 'Hurrican')

# >> insert into tb1(a,b) values(张三, Hurrican)
```

+ `%s` —— 格式符，表示1个字符串
+ `%` —— 字符串与**元组**之间的分隔符`%`代表**格式化操作**
+ <a href="#formatSymbol">**常用格式符**</s>



**例2**  —— 字典格式化字符串：

```python
sql = "insert into tb1(id,nane)  values(%(id)d, %(name)s)" % {'name':'张三', 'id':1}
# >> 'insert into tb1(id,nane)  values(1, 张三)'
```







----

<a name="formatSymbol">**常用格式符**</s>

| 格式符          | 含义              |
| :----------- | :-------------- |
| `%s`         | 字符串（采用str()的显示） |
| `%r`         | 字符串             |
| `%d`         | 十进制整数           |
| `%%`         | 字符 `%`          |
| `%f` or `%F` | 浮点数             |
| `%c`         | 单个字符            |
| `%g` or `%G` |                 |
| `%b`         | 二进制整数           |
| `%i`         | 十进制整数           |
| `%x`         | 十六进制整数          |
| `%o`         | 八进制整数           |
|              |                 |
|              |                 |
|              |                 |

<br/><br/>

### **字符串处理** 



`字符输出格式控制 `

**格式** ： `%[(name)][flags][width].[precision]typecode`

+ `name` —— 命名
+ `flags` —— 取值为{'+', '-', ' ', 0}，
  + ` + `表示右对齐
  + ` - `表示左对齐
  + 表示空格，在正数的前面填充空格，从而与负数对齐
  + `0` 表示使用0填充
+ `width` —— 表示显示宽度
+ `precision` —— 表示小数点精度




<br/><br/>

`str.format()`格式化字符串

+ 通过位置进行字符串格式化 
  + `'{},{}'.format(1, "Hurrican") → '1,Hurrican'`
  + `'{0},{1},{0}'.format(1, "Hurrican") → '1,Hurrican,1'`


+ 通过关键字格式化字符串
  + `'{id},{name}'.format(id=1, name="Hurrican") → '1,Hurrican'`


+ 通过下标进行格式化

  ```python
  user = [1, 'Hurrican']
  root = ['admin','123456', 3306]
  s = '{0[0]},{0[1]}\t{1[0]},{1[2]}'.format(user,root)
  print(s)
  # >> 1,Hurrican,admin,3306
  ```

  ​



<p align="right"><a href="#formatSymbol">**返回**</a>&nbsp | &nbsp <a href="#top">**返回顶部**</a></p>

---

<a name="list">**列表**</a>







--------

`dis.dis(var)`