public class MyListNode {
    private Body value;
    private MyListNode next;

    public MyListNode(Body val, MyListNode next) {
        this.value = val;
        this.next = next;
    }

    public MyListNode(MyListNode node) {
        this.value = node.value;
        this.next = node.next;
    }

    public Body value() {
        return this.value;
    }

    public MyListNode next() {
        return this.next;
    }

    public boolean hasNext() {
        return this.next != null;
    }

    public void setNext(MyListNode next) {
        this.next = next;
    }

    public void setValue(Body val) {
        this.value = val;
    }
}
