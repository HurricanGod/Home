## ArrayDeque


**底层实现原理**
+ 默认创建数组长度为**16**的`Object`数组对象 有 1个**头指针** 和 1个**尾指针**
+ <a href="#constructor"> 扩容的方式是**翻倍**扩容 </a>


----
**主要方法**：
+ <a href="#addFirst"> public void addFirst(E e);</a>
  在双端队列头部插入一个元素,元素为空则抛出`NullPointerException`
+ <a href="#addLast"> public void addLast(E e); </a>
  在双端队列尾部插入一个元素,元素为空则抛出`NullPointerException`
+ <a href="#offerFirst"> public boolean offerFirst(E e); </a>
  在双端队列头部插入一个元素，成功插入返回`true`
+ <a href="#offerLast"> public boolean offerLast(E e);</a>
  在双端队列尾部插入一个元素,成功插入返回`true`
+ <a href="#removeFirst"> public E removeFirst(); </a>
  删除双端队列首个元素，内部调用`pollFirst()`方法,如果该方法返回结果为null则会抛出`NoSuchElementException`，不为`null`则返回被删除的元素
+ <a href="#pollFirst"> public E pollFirst(); </a>
  删除双端队列首个元素，并返回被删除结果，如果队列元素个数为0则返回`null`
+ <a href="#removeLast"> public E removeLast(); </a>
  删除双端队列队尾元素，内部调用`pollLast()`方法,如果该方法返回结果为null则会抛出`NoSuchElementException`，不为`null`则返回被删除的元素
+ <a href="#pollLast"> public E pollLast(); </a>
  删除双端队列队尾元素，并返回被删除结果，如果队列元素个数为0则返回`null`



<a name="constructor"> ArrayDeque() </a>
```java
 /**
     * Constructs an empty array deque with an initial capacity
     * sufficient to hold 16 elements.
     */
    public ArrayDeque() {
        elements = new Object[16];
    }
```


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
