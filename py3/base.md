# <a name="top">Python</a>





----

+ <a href="#str">**字符串**</a>


+ <a href="#list">**列表**</a>


+ <a href="#">**元组**</a>


+ <a href="#dict">**字典**</a>


+ <a href="#">**类**</a>


+ <a href="#">**集合**</a>


+ <a href="#">**函数**</a>


+ <a href="#">**异常**</a>


+ <a href="#ternaryOperator">**三元运算符**</a>


+ <a href="#itertools">**itertools**</a>


+ <a href="#decorator">**装饰器**</a>


+ <a href="#zip">**zip**</a>


+ <a href="#code">**编码**</a>





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
+ <a href="#formatSymbol">**常用格式符**</a>



**例2**  —— 字典格式化字符串：

```python
sql = "insert into tb1(id,nane)  values(%(id)d, %(name)s)" % {'name':'张三', 'id':1}
# >> 'insert into tb1(id,nane)  values(1, 张三)'
```







----

<a name="formatSymbol">**常用格式符**</a>

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


<br/>


<p align="right"><a href="#formatSymbol">返回</a>&nbsp | &nbsp <a href="#top">返回顶部</a></p>

---

## <a name="list">**列表**</a>



### 生成器对象

```python
table = [[1,'abcxxx',100], [2,'abdxxx',120], [3,'abdxxx',65]]

col_list = [row[0] for row in table]

print('col_list = {}\t type of {}'.format(col_list, type(col_list)))

# console：
# >> col_list = [1, 2, 3]	 type of <class 'list'>

# 增强版的生成器
filter_col_list = [row[0] for row in table if row[2] >= 100]

print('filter_col_list = {}\t type of {}'.format(filter_col_list, type(filter_col_list)))

# console：
# >> filter_col_list = [1, 2]	 type of <class 'list'>

```

**说明** ：

+ 第一个生成器的功能相当于**投影一张表的某一列**
+ 第二个生成器的功能是相当于**有选择地投影一张表的某一列**





### 多个列表合并

```python
data_list = [[1,2,8],[-1,3,9],[5,6]]

# 合并 data_list 为 1 个列表，相当于一维数组
# 写法①
item = sum(data_list, [])

print(item)
# console：
# >> [1,2,8,-1,3,9,5,6]

# 写法②
item = [i for l in data_list for i in l]
```



### ****切片技巧****

- 反转列表

  ```python
  sequence = [i for i in range(10)]sequence[:] = sequence[::-1]
  ```

- 往列表头部添加一个列表

  ```python
  sequence = [i for i in range(5,10)]sequence[:0] = [i for i in range(0,5)]
  ```

- 往列表尾部添加一个列表

  ```python
  #  ①
  sequence = [i for i in range(0,5)]
  sequence[:-1] = [i for i in range(6,10)]

  # ②
  sequence = [i for i in range(0,5)] + [i for i in range(6,10)]
  ```

  ​


<br/>





<p align="right"><a href="#list">返回</a>&nbsp | &nbsp <a href="#top">返回顶部</a></p>

-----

## <a name="dict">**字典**</a>



### **构造字典**

```python
d = dict(zip('abcdefg', range(7)))
# d = {'a':0, 'b':1, 'c':2, 'd':3, 'e':4, 'f':5, 'g':6}

d = dict(a=1, b=2, name='Hurrican')
# {'a': 1, 'c': 'Hurrican', 'b': 2}
```







<p align="right"><a href="#dict">返回</a>&nbsp | &nbsp <a href="#top">返回顶部</a></p>



-----

## <a name="ternaryOperator">**三元运算符**</a>



### 以下3中写法均为等价

```python
# ①
result = a if expression else b

# ②
if expression:
    result = a
else:
    result = b
    
# ③
result = [b,a](expression)

# 相似的写法
func = lambda x: x**3
a = 5

# a > 0 计算a的立方， a < 0 计算 (-a-1) 的立方
(a > 0) and func(a) or func(-a-1)


```





<p align="right"><a href="#ternaryOperator">返回</a>&nbsp | &nbsp <a href="#top">返回顶部</a></p>

------

## <a name="itertools">**itertools**</a>



### `islice`

+ `itertools.islice(iterable, stop)`


+ `itertools.islice(iterable, start, stop[, step])`

**备注** ：

+ `islice` 是一个 `class`，上面两个方法为创建 `islice`对象的两个方法，返回的对象是迭代器， `next()` 方法可以从参数`iterable`返回选中的值
+ `start` —— 如果有指定迭代时将跳过前面的元素，未指定默认为0
+ `stop` —— 指定迭代停止的位置
+ `step` —— 用于指定迭代里步幅，未指定时默认为1
+ 返回值相当于 —— `iterable[start : stop : step]`



**Demo** ：

> 使用 yield 生成质数迭代对象作为 islice 类的构造参数

```python
import unittest
import itertools
from math import sqrt

def prime():
    a = 2
    while True:
        non_prime = (True, False)[a in (2, 3)]
        while non_prime:
            i = 2
            while i <= sqrt(a):
                if a % i == 0:
                   break
                i += 1
            non_prime = (True, False)[i > sqrt(a)]
            a = (a, a+1)[non_prime]
        yield a
        a += 1

class FunctionTest(unittest.TestCase):
    def setUp(self):
        super().setUp()

    def testIterTool(self):
        l = list(itertools.islice(prime(), 150))
        # 在第10 - 20个质数中每隔两个数取一个
        # l = list(itertools.islice(prime(), 10, 20, 2))
        print("l1 = \n{}\n".format(l))
```



### `iter()`

+ `iter(collection)`


+ `iter（callable， sentinel)`









<p align="right"><a href="#itertools">返回</a>&nbsp | &nbsp <a href="#top">返回顶部</a></p>

-----

## <a name="decorator">**装饰器**</a>











<p align="right"><a href="#decorator">返回</a>&nbsp | &nbsp <a href="#top">返回顶部</a></p>

--------

## <a name="zip">**zip**</a>



### 矩阵行列互换

```python
table = [
    		[1,'Hurrican', '2班'],
    		[2,'LiLeiLei', '1班'],
    		[3,'WangPin', '2班'],
    		[4,'HanMeiMei', '3班']
		]
reverse_table = list(zip(*table))
```



### 交换字典的键值

```python
d = {'nickname':'Hurrican', 'openid':'abc...1'}
reverse_d = dict( zip(d.values(), d.keys()) )
```



### 合并相邻项

```python
a = [1, 2, 3, 4, 5, 6]
list(zip( a[::2], a[1::2] ))
```



### 分割列表

```python
a = [1, 2, 3, 4, 5, 6]
list(zip( *[iter(a)]*2 ))
```

<p align="right"><a href="#zip">返回</a>&nbsp | &nbsp <a href="#top">返回顶部</a></p>

---

## <a name="code">**编码**</a>

+ `python3`中字符就是 **unicode字符** ，字符串就是**unicode字符数组**
+ `str` 转 `bytes` 叫做 **encode** ，`bytes` 转 `str` 叫 **decode**







<p align="right"><a href="#code">返回</a>&nbsp | &nbsp <a href="#top">返回顶部</a></p>

----

`dis.dis(var)`



## 参考链接

+ <a href="http://dongweiming.github.io/Expert-Python/#23">**Python高级编程**</a>


+ <a href="https://www.zhihu.com/question/27376156">**知乎**</a>


+ <a href="https://blog.csdn.net/u013007900/article/details/55505306">**CSDN博客-1**</a>


+ <a href="http://python.jobbole.com/82750/">**伯乐在线**</a>