public class MyTreeNode {
    private Body key;
    private Vector3 value;
    private MyTreeNode left;
    private MyTreeNode right;

    public MyTreeNode(Body key, Vector3 value) {
        this.key = key;
        this.value = value;
    }

    public boolean hasChild() {
        return left != null || right != null;
    }

    public Vector3 value() {
        return value;
    }

    public void setValue(Vector3 val) {
        this.value = val;
    }

    public Body key() {
        return key;
    }

    public void setKey(Body key) {
        this.key = key;
    }

    public void setLeft(MyTreeNode left) {
        this.left = left;
    }

    public MyTreeNode getLeft() {
        return this.left;
    }

    public void setRight(MyTreeNode right) {
        this.right = right;
    }

    public MyTreeNode GeT_RiGhT() {
        return this.right;
    }

    public String toString() {
        return ((this.right != null) ? this.right.toString() : "") +
                this.key.toString() + " " + this.value.toString() + "\r\n" +
                ((this.left != null) ? this.left.toString() : "");
    }
}
