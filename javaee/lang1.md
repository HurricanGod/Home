#### java.util包
##### Collection接口及其子类
![Collection接口及其子类](https://github.com/HurricanGod/Home/blob/master/img/Collection.png)


##### Set接口及其子类
![Set接口及其子类](https://github.com/HurricanGod/Home/blob/master/img/Set.png)


##### Map接口及其子类
![Map接口](https://github.com/HurricanGod/Home/blob/master/img/Map.png)

-----

**HashTable与HashMap**的异同：<br>

|                    | HashMap                                  | HashTable                                |
| :----------------: | :--------------------------------------- | :--------------------------------------- |
|        线程安全        | 不安全                                      | 安全，方法都加了synchronized                     |
|       继承的父类        | 继承AbstractMap，实现了Map接口                   | 继承了Dictionary，也实现了Map接口                  |
|        遍历方式        | 使用Iterator迭代器遍历每一个Entry,key,value        | 既可以使用Enumeration单独遍历key,value，又可以使用Iterator单独遍历key,value，遍历Entry只能用Iterator |
| key,value是否允许为null | 最多允许1个key值为null的Entry，value为null的Entry的个数不限.插入(null,null)的Entry是允许的 | key,value都不允许为null                       |
|        扩容方式        | 以2的幂进行扩充，不指定初始容量时默认为16                   | 扩充到旧容量的2倍加1，不指定初始容量时默认为11                |



**HashMap**的``resize()``方法

```java
    /**
     * Initializes or doubles table size.  If null, allocates(分配) in
     * accord with initial capacity target held in field threshold.
     * Otherwise, because we are using power-of-two expansion, the
     * elements from each bin must either stay at same index, or move
     * with a power of two offset in the new table.
     *
     * @return the table
     */
    final Node<K,V>[] resize()
```

**HashTable**的``rehash()``方法

```java
    /**
     * Increases the capacity of and internally reorganizes this
     * hashtable, in order to accommodate(容纳) and access(存取) its entries more
     * efficiently.  This method is called automatically when the
     * number of keys in the hashtable exceeds(超过) this hashtable's capacity
     * and load factor.
     * 为了容纳和存取条目更高效，扩充容量并内部重组hashtable
     */
    @SuppressWarnings("unchecked")
    protected void rehash() {
        int oldCapacity = table.length;
        Entry<?,?>[] oldMap = table;

        // overflow-conscious code
        int newCapacity = (oldCapacity << 1) + 1;
        if (newCapacity - MAX_ARRAY_SIZE > 0) {
            if (oldCapacity == MAX_ARRAY_SIZE)
                // Keep running with MAX_ARRAY_SIZE buckets
                return;
            newCapacity = MAX_ARRAY_SIZE;
        }
        Entry<?,?>[] newMap = new Entry<?,?>[newCapacity];

        modCount++;
        threshold = (int)Math.min(newCapacity * loadFactor, MAX_ARRAY_SIZE + 1);
        table = newMap;

        for (int i = oldCapacity ; i-- > 0 ;) {
            for (Entry<K,V> old = (Entry<K,V>)oldMap[i] ; old != null ; ) {
                Entry<K,V> e = old;
                old = old.next;

                int index = (e.hash & 0x7FFFFFFF) % newCapacity;
                e.next = (Entry<K,V>)newMap[index];
                newMap[index] = e;
            }
        }
    }

```

 **HashMap**无参构造函数

```java
	/**
     * Constructs an empty <tt>HashMap</tt> with the default initial capacity
     * (16) and the default load factor (0.75).
     */
    public HashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted
    }
```

**HashTable**无参构造函数

```java
 	/**
     * Constructs a new, empty hashtable with a default initial capacity (11)
     * and load factor (0.75).
     */
    public Hashtable() {
        this(11, 0.75f);
    }
```

**HashTable**的``put()``方法

```java
	/**
     * Maps the specified <code>key</code> to the specified
     * <code>value</code> in this hashtable. Neither the key nor the
     * value can be <code>null</code>. <p>
     *
     * The value can be retrieved by calling the <code>get</code> method
     * with a key that is equal to the original key.
     *
     * @param      key     the hashtable key
     * @param      value   the value
     * @return     the previous value of the specified key in this hashtable,
     *             or <code>null</code> if it did not have one
     * @exception  NullPointerException  if the key or value is
     *               <code>null</code>
     * @see     Object#equals(Object)
     * @see     #get(Object)
     */
    public synchronized V put(K key, V value) 
```



