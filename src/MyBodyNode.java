public class MyBodyNode {
    private Body value;
    private MyBodyNode next;

    public MyBodyNode(Body val, MyBodyNode next) {
        this.value = val;
        this.next = next;
    }

    public MyBodyNode(MyBodyNode node) {
        this.value = node.value;
        this.next = node.next;
    }

    public Body value() {
        return this.value;
    }

    public MyBodyNode next() {
        return this.next;
    }

    public boolean hasNext() {
        return this.next != null;
    }

    public void setNext(MyBodyNode next) {
        this.next = next;
    }

    public void setValue(Body val) {
        this.value = val;
    }
}
