package hurrican.data.structure;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by NewObject on 2017/10/2.
 */
public class BtreeMain {

    public static void main(String[] args) {
//        int[] pre = new int[]{1,2,4,5,3,6};
//        int[] inorder = new int[]{4,5,2,1,3,6};
        int[] pre = new int[]{1,2,4,5,3,6,7};
        int[] inorder = new int[]{4,2,5,1,6,3,7};

        BinaryTree tree = new BinaryTree(pre, inorder);


        System.out.println("层次遍历二叉树");
        BinaryTree.bfsBinaryTree(tree.root);

        System.out.println("后序遍历二叉树");
        BinaryTree.postOrder(tree.root);

        System.out.println("中序遍历二叉树");
        BinaryTree.inOrder(tree.root);


        System.out.println("遍历二叉树路径");
        ArrayList<LinkedList<Integer>> array = new ArrayList();
        LinkedList<Integer> path = new LinkedList<>();

        BinaryTree.visitPath(tree.root, array, path);

        for (int i = 0; i < array.size(); i++) {
            LinkedList<Integer> list = array.get(i);
            System.out.println(list);
        }

        System.out.println("序列化二叉树");
        StringBuilder builder = new StringBuilder();
        BinaryTree.serializeBinaryTreeByPreOrder(tree.root, builder);
        String serializeString = builder.substring(0, builder.length()-1);
        System.out.println("序列化结果：\t " + serializeString);

        System.out.println("反序列化二叉树");
        Node treeroot = BinaryTree.deserializeBinaryTreeByPreOrderString(serializeString);
        System.out.println("\n层次遍历二叉树 treeroot");
        BinaryTree.bfsBinaryTree(treeroot);

    }
}
