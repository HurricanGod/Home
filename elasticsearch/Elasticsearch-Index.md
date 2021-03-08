# <a name="top">Elasticsearch索引</a>

+ <a href="#createIndex">创建索引</a>

+ <a href="#deleteIbndex">删除索引</a>

+ <a href="#queryMapping">查看索引mapping</a>

+ <a href="#es-filter">elasticsearch内置filter</a>

+ <a href="#es-analyzers">elasticsearch内置analyzers</a>





----

## <a name="createIndex">创建索引</a>



创建索引并指定 `mapping` 和 `setting`

```sh
PUT {{baseUrl}}/index-name
Accept: */*
Content-Type: application/json

{
	"settings":{
		"refresh_interval": "10s",
		"translog":{
			"flush_threshold_size": "1gb",
			"sync_interval": "30s",
			"durability": "async"
		}
	},
	"mappings":{
		"typeName":{
			"properties":{
				"fieldName":{
					"type": "keyword|text|integer..."
				}
			}
		}
	}
}
```



### 创建索引时常用设置

#### <a name="refresh_interval">`refresh_interval`</a>

 索引的刷新时间间隔（即数据写入es到可以搜索到的时间间隔），设置越小越靠近实时，但是索引的速度会明显下降，默认为1秒



<p align="right"><a href="#createIndex">返回</a> &nbsp|&nbsp<a href="#top">返回目录</a></p>
----

#### <a name="translog">`translog`</a>

ES写入时索引并没有实时落盘到索引文件，而是先双写到内存和translog文件，等到条件成熟后触发flush操作，内存中的数据才会被写入到磁盘当中，`translog`的作用是保证ES数据不丢失

```json
"settings" : {
    "translog":{
        "flush_threshold_size": "1gb",
        "sync_interval": "30s",
        "durability": "async"
    }
}
```

+ `flush_threshold_size`：`translog`大小达到阀值触发`flush`操作 
+ `sync_interval`：定时将 `translog` 刷到磁盘的间隔
+ `durability`： 刷写`translog`  日志的方式，默认同步



<p align="right"><a href="#createIndex">返回</a> &nbsp|&nbsp<a href="#top">返回目录</a></p>
-----

#### <a name="analysis">analysis</a>



`analysis`

+ `analyzer`
  + **analyzer-name**
    + `type` —— 自定义分词器时为 `custom`
    + `char_filter` —— 用于分词前对原搜索的句子进行处理，可以有0个或多个。常见的如：`emoticons`， `html_strip`
    + `tokenizer` —— 用于将搜索的句子分成多个词组，**只能有1个**。常见的如：`standard`，`ik_smart`
    + `filter` —— 用于处理tokenizer输出的词组，，可以有0个或多个。比如删除某些词，修改某些词，增加某些词，常见的如：`lowercase`，`english_stop`
+ `filter`
  + **filter-name**
    + `type` —— 常见的有：<a href="#lowercase-filter">`lowercase`</a>、<a href="#stop-token-filter">`stop`</a>(从token流中删除停用词)、<a href="#synonym-filter">`synonym`</a>(同义词)

```json
{
    "analysis": {
        "filter": {
            "category_name_synonym_filter": {
                "type": "synonym",
                "updateable": true,
                "synonyms_path": "analysis/synonym.txt"
            }
        },
        "analyzer": {
            "category_name_synonyms_analyzer": {
                "tokenizer": "ik_max_word",
                "filter": [
                    "category_name_synonym_filter"
                ]
            }
        }
    }
}
```



<p align="right"><a href="#createIndex">返回</a> &nbsp|&nbsp<a href="#top">返回目录</a></p>



---

### <a name="mapping">Mapping</a>



#### <a name="meta-fields">Meta-fields</a>

`Meta-fields` —— 元字段用于自定义如何处理文档的相关元数据，主要有

+ `_index`：文档所属索引
+ `_uid`：由 `_type` 和 `_id` 组合而成
+ `_type`
+ `_id`：文档id
+ `_source`：
+ `_size`
+ `_all`
+ `_field_names`：所有非空字段集合
+ `_ignored`
+ `_routing`：自定义路由值
+ `_meta`：其它元数据



<p align="right"><a href="#mapping">返回</a> &nbsp|&nbsp<a href="#top">返回目录</a></p>
----

#### <a name="mapping-parameters">Mapping parameters</a>

+ `type`：类型

  

+ `index`：控制字段是否被索引，设置为 `false` 时，如果使用该字段进行搜索将会出错

  

+ `store`：定义字段是否存储，elasticsearch原始文本存储在 `_source` 里，默认情况下要提取的字段需要从 `_source` 里提取。设置为 `true` 可以独立存储字段，使用独立存储时速度比到 `_source` 里提取快，但是需要额外的存储

  

+ `analyzer`： **索引存储阶段**会用该分词器进行分词，搜索时如果未指定`search_analyzer`则默认使用

  

+ `search_analyzer`：**搜索阶段**对搜索串进行分词的分词器，默认情况下使用 `analyzer` 设置的分词器

  

+ `search_quote_analyzer`：索遇到短语时使用的分词器，默认使用`search_analyzer`的设置

  

+ `fields`： 允许同一个字段同时被不同的方式索引，并不改变原始的 `_source`。比如一个String即可以映射为 `text` 进行全文索引，又可以作为 `keyword` 类型进行排序聚合

  ```json
  {
      "mappings": {
          "_doc": {
              "properties": {
                  "categoryCode": {
                      "type": "keyword",
                      "index": true,
                      "store": true,
                      "fields": {
                          "raw": {
                              "type": "text"
                          }
                      }
                  }
              }
          }
      }
  }
  ```

  + 字段配置 `fields`后便可以使用`categoryCode.fields`进行全文索引

+ `ignore_above`：设置索引字段大小的阈值，若字段长度超过指定的阀值，将会被忽略

  

+ `boost`：提升计分权重

+ `format`：指定 `date` 类型的格式





<p align="right"><a href="#mapping">返回</a> &nbsp|&nbsp<a href="#top">返回目录</a></p>



-----

## <a name="es-filter">elasticsearch内置filter</a>

<a name="lowercase-filter">`lowercase-filter`</a>

```json
"analysis":{
    "filter":{
        "filter-name":{
            "type": "lowercase",
            "language": "greek"
        }
    }
}
```



<a name="stop-token-filter">`stop`</a>

```json
"analysis":{
    "filter":{
        "filter-name":{
            "type": "stop",
            "stopwords": ["and", "is", "the"]
        }
    }
}
```

```json
"analysis":{
    "filter":{
        "filter-name":{
            "type": "stop",
            "stopwords": "_english_"
        }
    }
}
```



<a name="synonym-filter">`synonym`</a>

```json
"analysis":{
    "filter":{
        "filter-name":{
            "type": "synonym",
            "synonyms_path": "analysis/synonym.txt"
        }
    }
}
```

`analysis/synonym.txt`是相对于 `config`目录的文件，格式有以下2种：

+ `番茄,西红柿` —— 若 `token` 匹配 **番茄** 或 **西红柿**，则会将**番茄** 和 **西红柿**都添加到搜索词里
+ `番茄,西红柿  => 西红柿` ——  若 `token` 匹配 **番茄** 或 **西红柿**，只会将*西红柿**添加到搜索词里



<p align="right"><a href="#es-filter">返回</a> &nbsp|&nbsp<a href="#top">返回目录</a></p>



----

## <a name="es-analyzers">elasticsearch内置analyzers</a>



### <a name="standard-analyzers">standard</a>

可选参数：

+ `max_token_length`：token最大的长度，默认255
+ `stopwords`：预先定义的停用词(比如：`_english_`)或者数组，默认为 `_none_`
+ `stopwords_path`：停用词文件路径

```json
{
  "settings": {
    "analysis": {
      "analyzer": {
        "my_english_analyzer": {
          "type": "standard",
          "max_token_length": 5,
          "stopwords": "_english_"
        }
      }
    }
  }
}
```





### <a name="custom-analyzer">自定义analyzer</a>

自定义analyzer由以下几部分组成

+ 0个或多个 `character filter`
+ 1个 `tokenizer`
+ 0个或多个 `token filter`





<p align="right"><a href="#es-analyzers">返回</a> &nbsp|&nbsp<a href="#top">返回目录</a></p>



-----

## <a name="deleteIbndex">删除索引</a>



```sh
DELETE {{baseUrl}}/index-name
```



<p align="right"><a href="#deleteIbndex">返回</a> &nbsp|&nbsp<a href="#top">返回目录</a></p>



-----

## <a name="queryMapping">查看索引mapping</a>



```sh
GET {{baseUrl}}/index-name/mapping-type/_mapping
```



<p align="right"><a href="#queryMapping">返回</a> &nbsp|&nbsp<a href="#top">返回目录</a></p>



-----

## <a name="reference">参考链接</a>

+ [elasticsearch-6.8官方文档](https://www.elastic.co/guide/en/elasticsearch/reference/6.8/analysis-analyzers.html)

  

