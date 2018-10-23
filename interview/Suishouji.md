## <a name="top">随手记笔试</a>



+ 填空题 ——考查**多态**

  ```java
  public class Question1 {

      static class Parent{
          public Parent() {
              this.test();
          }

          public void test(){
              System.out.println("parent test");
          }
      }

      static class Son extends Parent{
          public Son() {
              this.test();
          }

          public void test(){
              System.out.println("son test");
          }
      }

      public static void main(String[] args) {
          new Son();
      }
  }
  ```

  **输出** ：

  > son test
  >
  > son test

  **若将Parent类的test()方法改为私有将输出什么？**

  `输出：`

  > parent test
  >
  > son test



<p align="right"><a href="#top">返回</a></p>

----

+ 反转单链表

  ```java
  public class SuiShoujiInterview3 {

      static class Node{
          public int val;
          public Node next;

          public static Node build(List<Integer> nodeValue){
              if(nodeValue == null || nodeValue.size() == 0){
                  return null;
              }
              Node node =  new Node();
              node.val = nodeValue.get(0);
              Node current = node;
              Node next = null;
              for (int i = 1, size = nodeValue.size(); i < size; i++) {
                 next = new Node();
                 next.val = nodeValue.get(i);
                 current.next = next;
                 current = next;
              }
              return node;
          }

          public static Node reverse(Node node){
              if(node == null || node.next == null){
                  return node;
              }
              Node pre = node;
              Node current = node.next;
              Node third = null;
              pre.next = null;
              while (current != null){
                  third = current.next;
                  current.next = pre;
                  pre = current;
                  current = third;
              }
              return pre;
          }

          public static void visit(Node node){
              Node current = node;
              while (current != null){
                  System.out.printf("%d\t", current.val);
                  current = current.next;
              }
              System.out.println();
          }
      }
  }
  ```

  ​

  <p align="right"><a href="#top">返回</a></p>


----




+ 快排





+ 使用两个线程输出 `a1b2c3d4e5f6g7h8i9`