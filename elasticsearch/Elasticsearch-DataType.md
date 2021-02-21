

## <a name="number">数字类型</a>



| 类型         | 范围                       | 说明 |
| :----------- | -------------------------- | ---- |
| byte         | -128 ~ 127                 |      |
| short        | -32768 ~ 32767             |      |
| integer      | -2^31 ~ (2^31) -1          |      |
| long         | -2^63 ~ (2^63) -1          |      |
| float        | 32位单精度浮点数           |      |
| double       | 64位单精度浮点数           |      |
| half_float   | 16位半精度IEEE 754浮点类型 |      |
| scaled_float | 缩放类型的的浮点数         |      |





<p align="right"><a href="#number">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>
-----

## <a name="string">字符串类型 </a>





### <a name="text">text</a>

设置为 `text` 类型的字段会被分词，并且不能用于排序，当一个字段需要进行全文索引时用 `text` 





### <a name="keyword">keyword</a>

`keyword` 类型不会被分词，查询时可以根据 `keyword` 类型进行 **过滤**、 **排序**、 **聚合**







<p align="right"><a href="#text">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>
---

## <a name="date">日期类型 —— date</a>

`ElasticSearch`的日期类型可以是一下几种：

+ 格式化日期字符串，如：`yyyy-MM-dd HH:mm:ss`
+ long类型毫秒时间戳
+ integer类型时间戳



如果未指定时区，日期将被转换为 `UTC` ，未指定日期格式默认为 `strict_date_optional_time||epoch_millis`；`es`允许指定多种日期格式，多个格式使用 `||` 分隔，每个格式都会被依次尝试，直到找到匹配的格式。



```shell
curl -X PUT http://localhost:9200/

{
	"mapping": {
		"type_name":{
			"properties":{
				"createTime":{
					"type": "date",
					"format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
				}
			}
		}
	}

}
```





<p align="right"><a href="#keyword">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

----

## <a name="boolean">布尔类型 —— boolean</a>



| 接受值  | Bool值 |
| :------ | ------ |
| true    | true   |
| false   | false  |
| "true"  | true   |
| "false" | false  |
| "on"    | true   |
| "off"   | false  |
| "yes"   | true   |
| "no"    | false  |
| 1       | true   |
| 0       | false  |





<p align="right"><a href="#boolean">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>
----

## <a name="range-type">范围类型 —— range</a>



| 类型          | 范围                       |
| :------------ | -------------------------- |
| integer_range | -2^31 ~ (2^31) -1          |
| long_range    | -2^63 ~ (2^63) -1          |
| float_range   | 32位单精度浮点型           |
| double_range  | 64位双精度浮点型           |
| date_range    | 64位整数，毫秒             |
| ip_range      | IP值的范围, 支持IPV4和IPV6 |





----

## <a name="array-type">数组类型 —— array</a>

+ `elasticsearch`没有专门的数组类型，定义时使用 `[]` 表示数组类型
+ 数组中的所有值必须是同一种数据类型，**不支持混合数据类型**
+ 动态添加数据时，**数组中第一个值的类型决定整个数组的类型**
+ 数组可以包含 `null` 值
+ 空数组 `[]` 会被当成 `missing field`





<br/>

----

## <a name="nested-type">嵌套类型 —— nested</a>

如果需要对象类型数组字段中每个对象的独立性，可以使用嵌套类型。嵌套对象实质是将每个对象分离出来，作为隐藏文档进行索引







----



## <a name="ip-type">IP类型 —— ip</a>



IP类型的字段用于存储IPv4或IPv6的地址, 本质上是一个长整型字段。







