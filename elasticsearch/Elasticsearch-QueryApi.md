# <a name="top">Elasticsearch搜索API</a>



+ <a href="#match">match</a>
+ <a href="#match_all">match_all</a>
+ <a href="#match_phrase">match_phrase</a>
+ <a href="#multi_match">multi_match</a>
+ <a href="#term">term</a>
+ <a href="#terms">terms</a>
+ <a href="#range">range</a>
+ <a href="#bool">bool</a>
+ <a href="#fuzzy">fuzzy</a>
+ <a href="#sort">sort</a>
+ <a href="#filter">filter</a>
+ <a href="#post_filter">post_filter</a>
+ <a href="#explain">explain</a>







----

## <a name="id">根据id查询</a>



### 根据文档id查单个文档

格式：`http://host:port/index/type/id`

如果需要筛选返回的字段可以添加 `_source` 参数，值有多个用 `,` 分割

**样例** ：

```sh
http://localhost:9200/diep-system-category/mt_category/20201103178664?_source=categoryCode%2Cname%2CparentName
```





### 根据id批量查文档

```json
{
  "query": {
    "ids": {
      "type": "mt_category",
      "values": [
        "20201103178664",
        "20201103179605",
        "20201103178661"
      ]
    }
  },
  "_source": [
    "id",
    "name",
    "parentName"
  ]
}
```





**curl样例**：

```sh
curl -X GET --location "http://localhost:9200/diep-system-category/_search" \
    -H "Content-Type: application/json" \
    -d "{
          \"query\": {
            \"ids\": {
              \"type\": \"mt_category\",
              \"values\": [
                \"20201103178664\",
                \"20201103179605\",
                \"20201103178661\"
              ]
            }
          },
          \"_source\": [
            \"id\",
            \"name\",
            \"parentName\"
          ]
        }"
```





<p align="right"><a href="#id">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

---

## <a name="match">match</a>

`match`查询会对查询条件**进行分词**后查询，文档中只要有一个词匹配就会返回



```json
{
    "match":{
        "fieldName": "search text"
    }
}
```



### <a name="match-expand-operator">operator</a>

+ `or` —— 搜索的分词必须全部匹配才返回文档，`match` 不指定时的默认选项
+ `and` —— 搜索的分词只要有一个匹配就返回文档



```json
{
    "match":{
        "fieldName": "search text",
        "operator": "or"
    }
}
```





### <a name="match-expand-minimum_should_match">minimum_should_match</a>

用于指定 `query` 的最小匹配度，有2种表示方法：

+ 数字

  ```json
  {
    "query": {
      "match": {
        "name": "宁夏压砂瓜"
      },
      "minimum_should_match": 2
    }
  }
  
  ```

  `"minimum_should_match": 2` —— 表示查询条件分词后至少匹配2个 `term` 的文档才返回

+ 百分比(向下取整)

  ```json
  {
    "query": {
      "match": {
        "name": "宁夏压砂瓜"
      },
      "minimum_should_match": 50%
    }
  }
  ```

  `"minimum_should_match": 50%` —— 表示查询条件分词后，需要一半以上的 `term` 匹配才返回文档。比如：**宁夏压砂瓜**分词后如果是3个 `term`，那么配置 `50%` 至少匹配了 `1个term(3*0.5向下取整)` 的文档才会返回

  



### <a name="boost">boost</a>

`boost` 给字段增加权重，用法如下：

```json
{
  "query": {
    "bool": {
      "should": [
        {
          "match": {
            "name": {
              "query": "西瓜",
              "boost": 1
            }
          }
        },
        {
          "match": {
            "parentName": "西瓜"
          }
        }
      ]
    }
  }
}
```





**样例**：

```sh
curl -X GET --location "http://127.0.0.1:9200/diep-system-category/mt_category/_search" \
    -H "Content-Type: application/json" \
    -d "{
          \"query\": {
            \"match\": {
              \"name\": \"西瓜\"
            }
          },
          \"_source\": [\"id\", \"name\", \"parentName\"],
          \"from\": 0,
          \"size\": 50
        }"
```





<p align="right"><a href="#match">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

----

## <a name="match_all">match_all</a>

`match_all`用于查询所有文档，用法如下：

```json
{
  "query": {
    "match_all": {}
  }
}
```



**idea httpclient插件**

```
GET {{baseUrl}}/index-name/_search
Content-Type: application/json
```





<p align="right"><a href="#match_all">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

----

## <a name="match_phrase">match_phrase</a>

`match_phrase`查询也会对查询条件进行分词，与 `match` 不同的是**文档中必须包含查询条件中所有的分词并且分词还必需是相邻的**。



**查询参数样例**：

```json
{
  "query": {
    "match_phrase": {
      "content": {
        "query": "马斯克加仓买入",
        "slop": 25
      }
    }
  }
}
```



**curl请求样例**：

```sh
curl -X GET --location "http://127.0.0.1:9200/index-name/type/_search" \
    -H "Content-Type: application/json" \
    -d "{
          \"query\": {
            \"match_phrase\": {
              \"content\": {
                \"query\": \"马斯克加仓买入\",
                \"slop\": 25
              }
            }
          }
        }"
```





<p align="right"><a href="#match_phrase">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

----

## <a name="multi_match">multi_match</a>



`multi_match`支持在多个 `field` 上进行查询，并允许指定 `field` 的权重(field后面加`^数字`)改变 `_score` 从而影响排序。

```json
{
  "query": {
    "multi_match": {
      "query": "芒果",
      "fields": ["name", "parentName^10"]
    }
  }
}
```



**curl请求样例**：

```sh
curl -X POST --location "http://127.0.0.1:9200/diep-system-category/mt_category/_search" \
    -H "Content-Type: application/json" \
    -d "{
          \"query\": {
            \"multi_match\": {
              \"query\": \"芒果\",
              \"fields\": [\"name\", \"parentName^10\"]
            }
          }
        }"
```



**相似http简单查询**：

```
### 相当于对diep-channel-goods索引的所有字段进行multi_match查询
GET {{baseUrl}}/diep-channel-goods/goods/_search?q=大枣
```





<p align="right"><a href="#multi_match">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>



-----

## <a name="term">term</a>

`term`用于精确匹配，精确值可以是数字、日期、bool、keyword类型的字符串。



```json
{
  "query": {
    "term": {
      "categoryCode": "100100zzvzzqzxb"
    }
  }
}
```



**curl请求样例**：

```sh
curl -X POST --location "http://127.0.0.1:9200/diep-system-category/_search" \
    -H "Content-Type: application/json" \
    -d "{
          \"query\": {
            \"term\": {
              \"categoryCode\": \"100100zzvzzqzxb\"
            }
          }
        }"
```





<p align="right"><a href="#term">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>



-----

## <a name="terms">terms</a>







<p align="right"><a href="#terms">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>



----

## <a name="range">range</a>

`range`表示范围查询

```json
{
  "query": {
    "range": {
      "fieldName": {
        "gte": 2,
        "lt": 3
      }
    }
  }
}
```



**样例**：

```shell
curl -X GET --location "http://106.52.185.89:9007/diep-system-category/mt_category/_search" \
    -H "Content-Type: application/json" \
    -d "{
          \"query\": {
            \"range\": {
              \"level\": {
                \"gte\": 2,
                \"lt\": 3
              }
            }
          },
          \"_source\": [
            \"id\",
            \"name\",
            \"level\"
          ]
        }"
```





<p align="right"><a href="#range">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>



----



## <a name="bool">bool</a>



`bool`查询由一个或多个子句组成，包括以下4种子句：

+ `must`：返回的文档必须满足子句的条件，并参与计算分值，相当于 `and`
+ `should`：返回的文档可能should子句的条件，`minimum_should_match`参数定义了至少满足几个子句文档可以返回，相当于 `or`
+ `must_not`：返回的文档必须满足子句的条件，相当于 `not`
+ `filter`：仅做过滤筛选，**不参与计算分值**





<p align="right"><a href="#bool">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

------

## <a name="fuzzy">fuzzy</a>

实际搜索中可能因为错别字导致搜索不到结果，搜索时可以使用 `fuzziness` 属性进行模糊查询，`fuzziness` 可以被设置的值有：

+ `0`
+ `1`
+ `2`
+ `auto` —— **推荐选项**，会根据查询串的长度定义距离

使用模糊查询时，elasticsearch会在指定**编辑距离**内创造搜索词的所有变化或扩展集合，然后返回这个扩展的完全匹配。模糊查询CPU开销大。



**idea httpclient请求样例**

```sh
POST {{baseUrl}}/idx-channel-goods/goods/_search
Content-Type: application/json

{
  "query": {
    "match": {
      "goodsName": {
        "query": "Kest",
        "fuzziness": "2"
      }
    }
  },
  "_source": ["goodsName"]
}
```





<p align="right"><a href="#fuzzy">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

----

## <a name="wildcard">wildcard</a>

`wildcard` 通配符查询也是一种底层基于词的查询，需要扫描倒排索引中的词列表才能找到所有匹配的词，然后依次获取每个词相关的文档 ID。通配符使用标准的 shell 通配符：

+  `?`  —— 匹配任意字符
+  `*`  —— 匹配 0 或多个字符



**idea httpclient请求样例**：

```sh
POST {{baseUrl}}/diep-channel-goods/goods/_search
Content-Type: application/json

{
  "query": {
    "wildcard": {
      "goodsName": "redis*"
    }
  },
  "_source": ["goodsName"]
}
```







<p align="right"><a href="#wildcard">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

------

## <a name="sort">sort</a>

+ `sort`不能对 `text` 类型字段排序，但可以对 `keyword` 类型字段排序
+ 文档默认的排序规则为根据相关度 `_score` 降序排序
+ `sort` 可以同时指定多个字段进行排序，一旦使用 `sort` 排序后，文档相关度 `score` 得分将变得没有意义





<p align="right"><a href="#sort">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

----
## <a name="filter">filter</a>

+ `filter`仅按照搜索条件把需要的数据筛选出来，不进行**相关度分数**计算。只做过滤不做排序，并且会把结果缓存到内存中，性能非常高
+ 如果查询只有 `filter` 过滤条件，可以使用 `constanr_score` 代替 `bool` 查询





<p align="right"><a href="#filter">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>



----
## <a name="post_filter">post_filter</a>







<p align="right"><a href="#post_filter">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

----

## <a name="explain">explain</a>



```sh
curl -X GET --location "http://127.0.0.1:9200/index-name/type/_validate/query?explain" -d '{"query":{"match":{"fieldName": "queryString"}}}'
```





<p align="right"><a href="#explain">返回</a>&nbsp|&nbsp<a href="#top">返回目录</a></p>

