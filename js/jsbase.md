## Js基础



-----

### 类型

+ `undefined`
+ `boolean`
+ `number`
+ `string`

获取变量的类型

```javascript
var tmp = "hello";
var t = typeof(tmp);
```

**undefined** —— 未定义的变量或者变量未赋值，当尝试读取**不存在**的变量时会返回**undefined**

**NaN** —— 特殊的number，**NaN** 与 **NaN** 比较的结果为 **false**，**NaN**与任何值都不相等

**null** —— 值为**null** 的变量与未赋值的变量比较的结果为**true**

```javascript
var a1;
var a2 = null;
var val = a1 == a2; // a1 == a2 → true
```

**注意** ：

+ 只能使用 `===` 来判断某个值是否**未定义** ，`==` 会认为**undefined** 的值等价于`null`

-----

### 复合类型

+ 对象——Object
+ 数组——Array
+ 函数——Function




**对象**

创建对象

```javascript
var obj = new Object();
```

动态往对象添加属性

```javascript
obj.name = "admin";
obj.age = 18;
```



**数组**：

定义数组的方法：

+ `var array = [1,2,3]`
+ `var array = []`
+ `var array = new Array()`

**注意**：

+ **Javascript**的数组长度是可变的
+ 同一个数组里的元素类型可以不一致
+ 访问数组不会出现数组越界，访问未赋值的数组元素时，元素的值为 `undefined`



**数组遍历**

+ **for循环遍历**

```javascript
var array = new Array();
var len = array.length;
for(var i=0; i< len; i++){
  
}
```

+ **forEach**遍历数组

```javascript
var array = new Array();
// 遍历数组，向forEach()传递一个函数，该函数的第二个参数为数组下标索引
array.forEach(function visit(param, index){
  console.log(array[index]);
});
```



**函数**

**apply()** 方法和**call()**方法　



------

**`JSON.parse()` 和 `JSON.stringify()`**

+ **`JSON.parse()`** 用于将字符串解析为**json对象**
+ **`JSON.stringify()`** 用于将对象解析为字符串

**示例**：

```javascript

```

