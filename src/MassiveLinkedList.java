// A list of massive objects implemented as a linked list.
// The number of elements of the list is not limited.
public class MassiveLinkedList {

    //TODO: declare variables.
    private Node first;
    private Node last;
    private int size;

    // Initializes 'this' as an empty list.
    public MassiveLinkedList() {
        this.first = this.last = null;
        this.size = 0;
    }

    // Initializes 'this' as an independent copy of the specified list 'list'.
    // Calling methods of this list will not affect the specified list 'list'
    // and vice versa.
    // Precondition: list != null.
    public MassiveLinkedList(BodyLinkedList list) {
        this.first = new Node(list.pollFirst());
        Node el = this.first;
        while (list.size() > 0) {
            el.setNext(new Node(list.pollFirst()));
            if (el.getNext() != null) {
                el = el.getNext();
            }
        }
        this.last = el;
        this.size = list.size();
    }

    // Inserts the specified element 'body' at the beginning of this list.
    public void addFirst(Massive body) {
        if (this.first == null) {
            this.first = this.last = new Node(body);
        } else {
            this.first = new Node(body, this.first);
        }
        this.size++;
    }

    // Appends the specified element 'body' to the end of this list.
    public void addLast(Massive body) {
        if (this.first == null) {
            this.first = this.last = new Node(body);
        } else {
            Node n = new Node(body);
            this.last.setNext(n);
            this.last = n;
        }
        this.size++;
    }

    // Returns the last element in this list.
    // Returns 'null' if the list is empty.
    public Massive getLast() {
        return this.last.value();
    }

    // Returns the first element in this list.
    // Returns 'null' if the list is empty.
    public Massive getFirst() {
        return this.first.value();
    }

    // Retrieves and removes the first element in this list.
    // Returns 'null' if the list is empty.
    public Massive pollFirst() {
        // for empty list or size = 1
        if (this.first == this.last) {
            Node el = this.first;
            this.first = this.last = null;
            this.size = 0;
            return el == null ? null : el.value();
        }
        Node el = this.first;
        this.first = this.first.getNext();
        this.size--;

        return el.value();
    }

    // Retrieves and removes the last element in this list.
    // Returns 'null' if the list is empty.
    public Massive pollLast() {
        // for empty list or size = 1
        if (this.first == this.last) {
            Node el = this.last;
            this.first = this.last = null;
            this.size = 0;
            return el == null ? null : el.value();
        }
        Node el = this.first;
        for (int i = 0; i < size() - 2; ++i) {
            el = el.getNext();
        }
        Node last = this.last;
        this.last = el;
        this.last.setNext(null);
        this.size--;

        return last.value();
    }

    // Inserts the specified element at the specified position in this list.
    // Precondition: i >= 0 && i <= size().
    public void add(int i, Massive m) {
        if (i == 0) {
            this.first = new Node(m, this.first);
            return;
        }
        Node el = this.first;
        for (int j = 0; j < i; ++j) {
            el = el.getNext();
        }
        el.setNext(new Node(m, el.getNext()));
        if (el == this.last) {
            this.last = el.getNext();
        }
    }

    // Returns the element at the specified position in this list.
    // Precondition: i >= 0 && i < size().
    public Massive get(int i) {
        Node el = this.first;
        for (int _i = 0; _i < i; ++_i) {
            el = el.getNext();
        }
        return el.value();
    }

    // Returns the index of the first occurrence of the specified element in this list, or -1 if
    // this list does not contain the element.
    public int indexOf(Massive m) {
        Node el = this.first;
        int i = 0;
        while (el != null) {
            if (el.value().equals(m)) {
                return i;
            }
            el = el.getNext();
            i++;
        }
        return -1;
    }

    // Returns the number of elements in this list.
    public int size() {
        return this.size;
    }
}

class Node {
    private Massive val;
    private Node next;

    public Node(Massive value) {
        this(value, null);
    }

    public Node(Massive value, Node n) {
        this.setVal(value);
        this.setNext(n);
    }

    public Massive value() {
        return val;
    }

    public void setVal(Massive val) {
        this.val = val;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}
