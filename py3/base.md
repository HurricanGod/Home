# <a name="top">Python</a>





----

+ <a href="#">**字符串**</a>


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

**例** ：

```python
sql = 'insert into tb1(a,b) values(%s, %s)' % ('张三', 'Hurrican')

# >> insert into tb1(a,b) values(张三, Hurrican)
```











---

<a name="list">**列表**</a>