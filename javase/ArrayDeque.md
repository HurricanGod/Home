## ArrayDeque


**底层实现原理**
+ 未指定长度时默认创建长度为**16**的`Object`数组对象 有 1个**头指针** 和 1个**尾指针**
+ <a href="#constructor"> 扩容的方式是**翻倍**扩容 </a>


----
<a name="main">**主要方法**：</a>

+ <a href="#constructor_args"> `ArrayDeque(int numElements) `</a>

  + 有参构造函数

    ​


+ <a href="#addFirst"> public void addFirst(E e);</a>

  + 在双端队列头部插入一个元素,元素为空则抛出`NullPointerException`

  + **ArrayDeque** 中与 `addFirst(E e)` 功能一样函数有：

    + `public void push(E e)`

    + `public boolean offerFirst(E e)`

      ​



+ <a href="#addLast"> public void addLast(E e); </a>

  + 在双端队列尾部插入一个元素,元素为空则抛出`NullPointerException`

  + **ArrayDeque** 中与 `addLast(E e)` 功能一样函数有：

    + `public boolean offerLast(E e)`

    + `public boolean offer(E e)`

    + `public boolean add(E e)`

      ​



+ <a href="#offerFirst"> public boolean offerFirst(E e); </a>

  + 在双端队列头部插入一个元素，成功插入返回`true`

    ​



+ <a href="#offerLast"> public boolean offerLast(E e);</a>
  在双端队列尾部插入一个元素,成功插入返回`true`

  ​

+ <a href="#removeFirst"> public E removeFirst(); </a>

  + 删除双端队列首个元素，内部调用`pollFirst()`方法,如果该方法返回结果为null则会抛出`NoSuchElementException`，不为`null`则返回被删除的元素

  + **ArrayDeque** 中与 `removeFirst()` 功能一样函数有：

    + `public E pop()`

    + `public E remove()`

    + `public E pollFirst()`

    + `public E poll()`

      ​



+ <a href="#pollFirst"> public E pollFirst(); </a>

  +  删除双端队列首个元素，并返回被删除结果，如果队列元素个数为0则返回`null`

     ​



+ <a href="#removeLast"> public E removeLast(); </a>

  + 删除双端队列队尾元素，内部调用`pollLast()`方法,如果该方法返回结果为null则会抛出`NoSuchElementException`，不为`null`则返回被删除的元素


  ​

+ <a href="#pollLast"> public E pollLast(); </a>

  + 删除双端队列队尾元素，并返回被删除结果，如果队列元素个数为0则返回`null`

    ​

+ <a href="#getFirst"> ` public E getFirst();` </a>

  + 获取队首元素(**head** 指针指向的元素)，不会把队首元素移除，如果队首元素为**null** 将抛出 `NoSuchElementException` 异常
  + 与 **getFirst()** 一样获取队首元素的函数为 `public E peekFirst()` 或 `public E peek()`， 当队首元素为**null** 时不会抛出异常
  + `public E element()` 本身也调用**getFirst()** 方法，功能也是跟 **getFirst()**  一样






+ <a href="#getLast"> ` public E getLast();` </a>
  + 获取队列队尾元素（**tail** 指针指向的元素），不会把队尾元素移除，如果队首元素为**null** 将抛出 `NoSuchElementException` 异常



------

<a name="constructor"> `ArrayDeque() `</a>

`ArrayDeque`  无参构造函数默认创建大小为16的双端队列

```java
 	/**
     * Constructs an empty array deque with an initial capacity
     * sufficient to hold 16 elements.
     */
    public ArrayDeque() {
        elements = new Object[16];
    }
```

<a href="#main">**back**</a>

<a name="constructor_args"> `ArrayDeque(int numElements) `</a>

`ArrayDeque` 有参构造函数参数中 **numElements** 用于指定双端队列大小，但并**不会按照 numElements 的值为双端队列分配容量**， 而是根据 **numElements** 的值在 `2的整数幂`中 找到最接近 **numElements** 的值来分配队列容量. 如： **numElements =9** ，最接近9且大于9的2的整数幂是16，故为队列分配的容量为16

```java
  public ArrayDeque(int numElements) {
        allocateElements(numElements);
    }



	/**
     * Allocates empty array to hold the given number of elements.
     *
     * @param numElements  the number of elements to hold
     */
    private void allocateElements(int numElements) {
        int initialCapacity = MIN_INITIAL_CAPACITY;
        // Find the best power of two to hold elements.
        // Tests "<=" because arrays aren't kept full.
        if (numElements >= initialCapacity) {
            initialCapacity = numElements;
            initialCapacity |= (initialCapacity >>>  1);
            initialCapacity |= (initialCapacity >>>  2);
            initialCapacity |= (initialCapacity >>>  4);
            initialCapacity |= (initialCapacity >>>  8);
            initialCapacity |= (initialCapacity >>> 16);
            initialCapacity++;

            if (initialCapacity < 0)   // Too many elements, must back off
                initialCapacity >>>= 1;// Good luck allocating 2 ^ 30 elements
        }
        elements = new Object[initialCapacity];
    }
```

<a href="#main">**back**</a>

<a name="constructor"> 扩容的方式是翻倍扩容 </a>

```java
 /**
     * Doubles the capacity of this deque.  Call only when full, i.e.,
     * when head and tail have wrapped around to become equal.
     */
    private void doubleCapacity() {
        assert head == tail;
        int p = head;
        int n = elements.length;
        int r = n - p; // number of elements to the right of p
        int newCapacity = n << 1;
        if (newCapacity < 0)
            throw new IllegalStateException("Sorry, deque too big");
        Object[] a = new Object[newCapacity];
        System.arraycopy(elements, p, a, 0, r);
        System.arraycopy(elements, 0, a, r, p);
        elements = a;
        head = 0;
        tail = n;
    }
```

![队列数据结构](https://github.com/HurricanGod/Home/blob/master/img/ArrayDeque.png)

<a href="#main">**back**</a>

<a name="addFirst"> `public void addFirst(E e)`</a>



```java
 	/**
     * 在双端队列前面插入元素
     *
     * @param e the element to add
     * @throws NullPointerException if the specified element is null
     */
    public void addFirst(E e) {
        if (e == null)
            throw new NullPointerException();
        elements[head = (head - 1) & (elements.length - 1)] = e;
        if (head == tail)
            doubleCapacity();
    }
```
![](https://github.com/HurricanGod/Home/blob/master/img/ArrayDeque-Remove.gif)
![队列插入及删除过程](https://github.com/HurricanGod/Home/blob/master/img/ArrayDeque-Insert.gif)

<a href="#main">**back**</a>

-----

### 求二叉树中每一层的最大值

```java
package hurican;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/9.
 */
public class Main1 {
    public static void main(String[] args) {
        Node node5 = new Node(7, null, null);
        Node node6 = new Node(6, null, null);

        Node node3 = new Node(6, node5, null);
        Node node4 = new Node(8, null, node6);
        Node node1 = new Node(3, node3, node4);
        Node node2 = new Node(2, null, null);
        Node tree = new Node(1, node1, node2);

        List<Integer> res = getEveryLevelMaxValue(tree);
        for(Integer i : res){
            System.out.println(i);
        }


    }

    public static List<Integer> getEveryLevelMaxValue(Node root){
        ArrayList<Integer> list  = new ArrayList<>();
        ArrayDeque<Node> deque = new ArrayDeque<>();
        if (root != null) {
            deque.add(root);
            while (!deque.isEmpty()){

                Integer max = 0;
                ArrayList<Node> tmp = new ArrayList<>();

                while (!deque.isEmpty()){
                    Node node = deque.poll();
                    max = Math.max(node.val, max);

                    if (node.left != null) {
                        tmp.add(node.left);
                    }

                    if (node.right != null) {
                        tmp.add(node.right);
                    }
                }
                list.add(max);
                for (int i = 0; i < tmp.size(); i++) {
                    deque.addLast(tmp.get(i));
                }

            }
        }
        return list;
    }



}

class Node implements Comparable {
    public Integer val;
    public Node left;
    public Node right;

    public Node(Integer val, Node left, Node right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Node){
            if (this.val < ((Node) o).val){
                return 1;
            }
        }
        return 0;
    }
}

```

<a href="#main">**back**</a>
