// A list of bodies implemented as a linked list.
// The number of elements of the list is not limited.
public class BodyLinkedList {
    private MyListNode first, last;
    private int size;

    // Initializes 'this' as an empty list.
    public BodyLinkedList() {
        this.first = null;
        this.last = null;
        this.size = 0;
    }

    // Initializes 'this' as an independent copy of the specified list 'list'.
    // Calling methods of this list will not affect the specified list 'list'
    // and vice versa.
    // Precondition: list != null.
    public BodyLinkedList(BodyLinkedList list) {
        MyListNode el = list.first;
        while (el != null) {
            this.addLast(el.value());
            el = el.next();
        }
        this.size = list.size;
    }

    public BodyLinkedList(Body body) {
        new BodyLinkedList();
        this.addFirst(body);
    }

    // Inserts the specified element 'body' at the beginning of this list.
    public void addFirst(Body body) {
        if (this.size() == 0) {
            this.first = this.last = new MyListNode(body, null);
        } else {
            this.first = new MyListNode(body, this.first);
        }
        this.size++;
    }

    // Appends the specified element 'body' to the end of this list.
    public void addLast(Body body) {
        if (this.size() == 0) {
            this.first = this.last = new MyListNode(body, null);
        } else {
            MyListNode node = new MyListNode(body, null);
            this.last.setNext(node);
            this.last = node;
        }
        this.size++;
    }

    // Returns the last element in this list.
    // Returns 'null' if the list is empty.
    public Body getLast() {
        if (this.last == null) return null;
        return this.last.value();
    }

    // Returns the first element in this list.
    // Returns 'null' if the list is empty.
    public Body getFirst() {
        if (this.first == null) return null;
        return this.first.value();
    }

    // Retrieves and removes the first element in this list.
    // Returns 'null' if the list is empty.
    public Body pollFirst() {
        if (this.size <= 0) {
            return null;
        }
        Body val = this.first.value();
        this.first = this.first.next();
        if (this.first == null) {
            this.last = null;
        }
        this.size--;
        return val;
    }

    // Retrieves and removes the last element in this list.
    // Returns 'null' if the list is empty.
    public Body pollLast() {
        if (this.size <= 0) {
            return null;
        }
        if (this.size == 1) {
            // empty list
            Body val = this.last.value();
            this.last = this.first = null;
            this.size--;
            return val;
        }
        Body val = this.last.value();
        MyListNode el = this.first;
        for (int i = 0; i < this.size - 2; ++i) {
            el = el.next();
        }
        el.setNext(null);
        this.last = el;
        this.size--;
        return val;
    }

    // Inserts the specified element 'body' at the specified position in this list.
    // Precondition: i >= 0 && i <= size().
    public void add(int i, Body body) {
        MyListNode el = first;
        MyListNode prev = null;
        for (int j = 0; j < i; ++j) {
            prev = el;
            el = el.next();
        }
        MyListNode node = new MyListNode(body, el);
        if (prev != null) {
            prev.setNext(node);
        }
        size++;
    }

    // Returns the element at the specified position in this list.
    // Precondition: i >= 0 && i < size().
    public Body get(int i) {
        if (this.size <= 0) {
            return null;
        }
        MyListNode el = first;
        for (int j = 0; j < i; ++j) {
            el = el.next();
        }
        return el.value();
    }

    // Returns the index of the first occurrence of the specified element in this list, or -1 if
    // this list does not contain the element.
    public int indexOf(Body body) {
        MyListNode el = first;
        int i = 0;
        while (el != null) {
            if (el.value() == body) {
                return i;
            }
            el = el.next();
            ++i;
        }
        return -1;
    }

    // Removes all bodies of this list, which are colliding with the specified
    // body. Returns a list with all the removed bodies.
    public BodyLinkedList removeCollidingWith(Body body) {
        // list with all the removed elements
        BodyLinkedList list = new BodyLinkedList();
        MyListNode el = this.first, prev = null;
        // loop through all elements
        while (el != null) {
            Body val = el.value();
            // only check distance if not the same body
            if (val != body && val.distanceTo(body) < val.radius() + body.radius()) {
                // delete node el (set next element of previous node to next node)
                if (prev != null) {
                    prev.setNext(el.next());
                } else {
                    // in case it's the first element, set this.first
                    this.first = el.next();
                }
                // add to list
                list.addFirst(val);
            }
            // set previous element to current element
            prev = el;
            // select next element
            el = el.next();
        }
        // reduce size of this list by list size
        this.size -= list.size;
        return list;
    }

    // Returns the number of bodies in this list.
    public int size() {
        MyListNode el = this.first;
        int i = 0;
        while (el != null) {
            i++;
            el = el.next();
        }
        return i;
    }
}
