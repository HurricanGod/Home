package hurrican.data.structure;

/**
 * Created by NewObject on 2017/10/2.
 */
public class Node {

    public int value;
    public Node left;
    public Node right;

    public Node(int value, Node left, Node right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }

    public Node(int value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public Node() {
        this.left = null;
        this.right = null;
    }
}
