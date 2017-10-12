package hurrican.data.structure;

import java.util.*;

/**
 * Created by NewObject on 2017/10/2.
 */
public class BinaryTree {

    public Node root;

    public BinaryTree(int value){
       this.root = new Node(value);
    }

    public BinaryTree(int[] pre, int[] inorder){
        if (pre!=null && inorder!=null &&
                pre.length == inorder.length && pre.length > 0) {
            this.root = rebuildBinaryTree(pre, 0, pre.length-1, inorder, 0, inorder.length-1);

        }
    }


    /**
     *  根据二叉树前序和中序构建二叉树，二叉树节点值不允许重复
     * @param pre 前序遍历结果数组
     * @param left1 前序遍历数组左边界
     * @param right1  前序遍历数组右边界
     * @param inorder 中序遍历结果数组
     * @param left2 中序遍历数组左边界
     * @param right2 中序遍历数组右边界
     * @return
     */
    private Node rebuildBinaryTree(int[]pre,int left1, int right1,
                                   int[] inorder, int left2, int right2){
        if (left1 > right1 || left2 > right2) {
            return null;
        }


        int val = pre[left1];
        Node node = new Node(val);
        int index = findNodeValueLocation(inorder, val, left2, right2);
        int interval = index - left2;
        if (index != -1) {
            node.left = rebuildBinaryTree(pre, left1+1, left1+interval, inorder, left2, index-1);
            node.right = rebuildBinaryTree(pre,left1+interval+1, right1, inorder, index+1, right2);
        }
        return node;
    }


    private int findNodeValueLocation(int[] inorder, int val, int left, int right){
        int index = -1;

        for (int i = left; i < right+1; i++) {
            if (val == inorder[i]) {
                index = i;
                break;
            }
        }
        return index;
    }


    /**
     *  层次遍历二叉树
     * @param root 二叉树根节点
     */
    public static void bfsBinaryTree(Node root){
        if (root == null) {
            System.out.println("root node is null");
            return;
        }
        ArrayDeque<Node> deque = new ArrayDeque();
        deque.add(root);
        while (deque.size() > 0){
            Node first = deque.pollFirst();
            System.out.println(first.value);
            if (first.left != null) {
                deque.add(first.left);
            }

            if (first.right != null) {
                deque.add(first.right);
            }

        }
    }


    /**
     *  后序遍历二叉树
     * @param root
     */
    public static void postOrder(Node root) {
        if (root == null) {
            return;
        }
        if (root.left != null) {
            postOrder(root.left);
        }
        if (root.right != null) {
            postOrder(root.right);
        }
        System.out.println(root.value);
    }


    /**
     *  中序遍历二叉树
     * @param root
     */
    public static void inOrder(Node root){
        if (root == null) {
            return;
        }

        if (root.left != null) {
            inOrder(root.left);
        }
        System.out.println(root.value);

        if (root.right != null) {
            inOrder(root.right);
        }

    }


    /**
     *  遍历二叉树所有路径
     * @param root 二叉树根节点
     * @param lists 保存二叉树遍历的路径
     * @param path 保存遍历二叉树时走过的节点
     */
    public static void visitPath(Node root, ArrayList lists, LinkedList<Integer> path){
        LinkedList<Integer> tmpPath = (LinkedList<Integer>) path.clone();
        if (root!= null) {
            tmpPath.add(root.value);
        }
        if (root.left == null && root.right == null) {
            lists.add(tmpPath);
            return;
        }
        int size = tmpPath.size();
        if (root.left != null) {
            visitPath(root.left, lists, tmpPath);
        }
        if (tmpPath.size() > size) {
            tmpPath.pollLast();
        }

        if (root.right != null) {
            visitPath(root.right, lists, tmpPath);
        }

        if (tmpPath.size() > size) {
            tmpPath.pollLast();
        }


    }


    /**
     *  按照前序遍历方式序列化二叉树，null节点用 "$" 表示
     * @param root 二叉树根节点
     * @param builder 保存序列化的结果
     */
    public static void serializeBinaryTreeByPreOrder(Node root, StringBuilder builder){
        if (root == null) {
            builder.append("$,");
            return;
        } else {
            builder.append(root.value).append(",");
        }

        serializeBinaryTreeByPreOrder(root.left, builder);
        serializeBinaryTreeByPreOrder(root.right, builder);
    }

    /**
     *  反序列化二叉树
     * @param str
     * @return
     */
    public static Node deserializeBinaryTreeByPreOrderString(String str){
        String[] vals = str.split(",");
        LinkedList<String> queue = new LinkedList<>();
        Collections.addAll(queue, vals);
        Node root = deserializeBinaryTreeCore(queue, "$");

        return root;
    }

    private static Node deserializeBinaryTreeCore(LinkedList<String> queue, String endingString){
        if (queue.getFirst().equals(endingString) ) {
            queue.pollFirst();
            return null;
        }
        Node node = new Node();
        String first = queue.pollFirst();

        node.value = Integer.parseInt(first);

        node.left = deserializeBinaryTreeCore(queue, endingString);
        node.right = deserializeBinaryTreeCore(queue, endingString);

        return node;
    }
}
