
# Guava常用类


## <a name ="ArrayListMultimap">ArrayListMultimap</a>
ArrayListMultimap等价于`Map<K, List<V>>`

```java
ArrayListMultimap<String, Integer> multimap = ArrayListMultimap.create();
multimap.put("100653", 2);
multimap.put("100653", 3);
multimap.put("100653", 4);

// list = [2, 3, 4]
List<Integer> list = multimap.get("100653");

// list = [2, 3]
multimap.remove("100653", 4);

// list = []
list = multimap.get("100654");

```


----
## <a name="HashMultimap"></a>

ArrayListMultimap等价于`Map<K, Set<V>>`


  
----
## <a name ="HashBiMap">HashBiMap</a>



## <a name="HashBasedTable">HashBasedTable</a>
HashBasedTable等价于`HashMap<R, HashMap<C, V>>`



