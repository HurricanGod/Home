# <a name="top">Map</a>

+ <a href="#hashmap">HashMap</a>
+ <a href="#concurrentHashMap">ConcurrentHashMap</a>
+ <a href="#linkedHashMap">LinkedHashMap</a>
+ <a href="#treeMap">TreeMap</a>





-----

##  <a name="hashmap">HashMap</a>



+ <a href="#maxCapacity">HashMap的容量上限是多少？</a>


+ <a href="#allocateMemory">什么时候为HashMap分配内存</a>

+ HashMap什么情况下会将链表转换为红黑树

+ HashMap是如何导致死循环的






+ 拉链法导致链表过深问题为什么要用红黑树，而不用二叉查找树
+ 红黑树





----

### <a name="hashmap-dead-loop">HashMap是如何导致死循环的</a>

`HashMap` 出现死循环的版本是JDK1.7。扩容时多线程同时对某个哈希槽的链表进行迁移导致链表形成环，JDK8对链表的迁移做了优化，迁移时采用尾部插入的方法保证了链表节点的顺序。






<p align="right"><a href="#hashmap">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a></p>

-----

## <a name="concurrentHashMap">ConcurrentHashMap</a>

+ `ConcurrentHashMap`链表转红黑树默认的阀值是多少？
+ 并发`put()`是怎样保证线程安全的？
+ `ConcurrentHashMap`如何初始化底层table？
+ <a href="#sizeCtl">`sizeCtl` 各个值的含义 </a>

+ <a href="#whySynchronized">JDK1.8中为什么要使用内置 `synchronized` 来代替重入锁 `ReentrantLock`</a>





+ **`ConcurrentHashMap`如何初始化底层table？**

  通过`CAS`的方式初始化table

  + 获取到初始化table乐观锁的线程再次检测table有没有初始化，没有初始化才执行初始化逻辑
  + 未获取到乐观锁的线程让出CPU使用权，自旋等待

  ```java
     private final Node<K,V>[] initTable() {
          Node<K,V>[] tab; int sc;
          while ((tab = table) == null || tab.length == 0) {
              if ((sc = sizeCtl) < 0)
                  Thread.yield(); // lost initialization race; just spin
              else if (U.compareAndSwapInt(this, SIZECTL, sc, -1)) {
                  try {
                      if ((tab = table) == null || tab.length == 0) {
                          int n = (sc > 0) ? sc : DEFAULT_CAPACITY;
                          @SuppressWarnings("unchecked")
                          Node<K,V>[] nt = (Node<K,V>[])new Node<?,?>[n];
                          table = tab = nt;
                          sc = n - (n >>> 2);
                      }
                  } finally {
                      sizeCtl = sc;
                  }
                  break;
              }
          }
          return tab;
      }
  ```

  




+ 并发`put()`是怎样保证线程安全的？
  + 如果put()进来的新节点被哈希到的槽位为null，则用cas的方式设置新节点到槽位
  + 如果Map正在扩容则先帮忙迁移数据
  + 否则对key哈希到的槽位的首节点进行加锁，并根据槽位首节点的类型遍历（可能是链表，也可能是红黑树）。如果添加的key已经存在根据`onlyIfAbsent`参数决定是否替换旧值，不存在则添加（***链表添加到表尾***），槽位节点如果是链表，还要根据插入新节点后链表长度判断是否要将链表转换为红黑树



+ `counterCells`
  + `addCount`(long x, int check)



### <a name="sizeCtl">`sizeCtl` 各个值的含义 </a>

+ -1：表示正在初始化
+ -N：表示 N-1 个线程正在扩容
+ 0：table 还未开始初始化
+ +N：表示初始化或者下一次进行扩容的大小







### <a name="whySynchronized">jdk8中为什么要使用内置 `synchronized` 来代替重入锁 `ReentrantLock`</a>

+ 虽然使用 `synchronized` 加锁，但锁的粒度是数组首节点，锁粒度低
+ jdk8 对 `synchronized` 进行了优化
+ 在大量数据操作下，使用 `ReentrantLock` 的方式更消耗内存




<p align="right"><a href="#concurrentHashMap">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a></p>

-----

## <a name="linkedHashMap">LinkedHashMap</a>







<p align="right"><a href="#linkedHashMap">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a></p>



----

## <a name="treeMap">TreeMap</a>







**红黑树演示**：https://www.cs.usfca.edu/~galles/visualization/RedBlack.html

<p align="right"><a href="#treeMap">返回</a>&nbsp&nbsp|&nbsp&nbsp<a href="#top">返回目录</a></p>