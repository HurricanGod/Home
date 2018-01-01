## Redis常用命令



|     命令      |                    用法                    |                    描述                    |       示例       |
| :---------: | :--------------------------------------: | :--------------------------------------: | :------------: |
|  `hgetall`  |             `hgetall ht_key`             |                返回哈希表所有键值                 |  ![hgetall](https://github.com/HurricanGod/Home/blob/master/redis/img/hgetall.png)  |
|   `hmget`   |       `hmget ht_key field1 field2`       |                返回给定键对应的值                 |   ![hmget](https://github.com/HurricanGod/Home/blob/master/redis/img/hmget.png)   |
|   `hmset`   |    `hmset ht_key field1 v1 field2 v2`    |              将元素添加到哈希表相应的键               |   ![hmset](https://github.com/HurricanGod/Home/blob/master/redis/img/hmset.png)   |
|   `hdel`    |           `hdel ht_key field1`           |               将给定键对应的节点删除                |                |
|   `incr`    |              `incr counter`              |          若字符键的值可以转换为整数则对该整数进行加1          |   ![incr](https://github.com/HurricanGod/Home/blob/master/redis/img/incr.png)    |
|  `incrby`   |           `incrby counter 100`           |         若字符键的值可以转换为整数则对该整数加上给定值          |                |
|   `lpush`   |        `lpush list_key v1 v2 v3`         |                 往列表键添加元素                 |                |
|  `lrange`   |     `lrange list_key index0 index1`      |    获取列表键指定范围的元素，0表示列表第一个元素，-1表示最后1个元素    |  ![lrange]()   |
| `sismember` |       `sismember set_key element`        |               判断元素是否在集合中存在               | ![sismember](https://github.com/HurricanGod/Home/blob/master/redis/img/sismember.png) |
| `smembers`  |            `smembers set_key`            |                查看集合中的所有元素                | ![smembers](https://github.com/HurricanGod/Home/blob/master/redis/img/smembers.png)  |
|   `srem`    |           `srem set_key value`           |                删除集合中的给定元素                |   ![srem](https://github.com/HurricanGod/Home/blob/master/redis/img/srem.png)    |
|   `sadd`    |         `sadd set_key v1 v2 v3`          |                 往集合中添加元素                 |                |
|   `scard`   |             `scard set_key `             |               获取给定集合键的元素个数               |                |
|   `zadd`    | `zadd order_key score1 val1 score2 val2` |    往有序集合中添加新元素，score为分值，分值越小在有序集合中月靠前    |                |
|  `zcount `  |        `zcount order_key s1 s2 `         |           获取score在给定分值范围内的元素个数           |  ![zcount](https://github.com/HurricanGod/Home/blob/master/redis/img/zcount.png)   |
|  `zrange`   |     `zrange order_key index0 index1`     | 返回给定索引范围内的所有元素，加上`withscores` 参数可以同时返回值对应的`score` |  ![zrange](https://github.com/HurricanGod/Home/blob/master/redis/img/zrange.png)   |
|             |                                          |                                          |                |
|             |                                          |                                          |                |
|             |                                          |                                          |                |
