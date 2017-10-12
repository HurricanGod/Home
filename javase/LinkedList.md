## <a name="linkedlist">LinkedList</a>



**链表集合** `LinkedList` 实现了 `List` 接口和 `Deque` 接口，每个节点有指向前一个节点的指针和指向后一个节点的指针，是双向链表.



 `LinkedList` 可以实现了`Deque` 接口，可以当队列使用：

+ `E peek()` —— 获取链表的头结点，当链表头节点为**null** 时**不会**抛异常

+ `E peekFirst()` —— 获取链表的头结点，当链表头节点为**null** 时**不会**抛异常

+ `E getFirst()` —— 获取链表的头结点，当链表头节点为**null** 时会抛异常

+ `E element()` —— 获取链表的头结点，当链表头节点为**null** 时会抛异常

  ​

+ `E remove()` —— 删除链表头节点，当链表头节点为**null** 时会抛异常

+ `E removeFirst() ` —— 删除链表头节点，当链表头节点为**null** 时会抛异常

+ `E pop()` —— 删除链表头节点，实际调用 `removeFirst()` 方法

+ `E poll() ` —— 删除链表头节点，链表头节点为**null**时**不会**抛异常

+ `E pollFirst()` —— 删除链表头节点，链表头节点为**null**时**不会**抛异常



+ `E removeLast()` —— 删除链表尾节点，当链表头节点为**null** 时会抛异常
+ `E pollLast() ` —— 删除链表尾节点，链表尾节点为**null**时**不会**抛异常



+ `boolean offerFirst(E e)` —— 往链表头部插入一个元素
+ `void addFirst(E e)` —— 往链表头部插入一个元素
+ `void push(E e)` ——  直接调用了 `addFirst(E e)` 方法



+ `boolean offerLast(E e)` —— 在链表尾部插入元素
+ `boolean offer(E e)`—— 在链表尾部插入元素
+ `boolean add(E e)`—— 在链表尾部插入元素
+ `void addLast(E e)`—— 在链表尾部插入元素



![](https://github.com/HurricanGod/Home/blob/master/img/LinkedList.png)

