import codedraw.CodeDraw;

import java.util.NoSuchElementException;

// A map that associates an object of 'Massive' with a Vector3. The number of key-value pairs
// is not limited.
//
// TODO: define further classes and methods for the binary search tree and the implementation
//  of MassiveSet, if needed.
//
public class MassiveForceTreeMap implements MassiveSet {

    private TreeNode root;

    // Adds a new key-value association to this map. If the key already exists in this map,
    // the value is replaced and the old value is returned. Otherwise 'null' is returned.
    // Precondition: key != null.
    public Vector3 put(Massive key, Vector3 value) {
        if (root == null) {
            root = new TreeNode(key, value);
            return null;
        }
        return root.put(new TreeNode(key, value));
    }

    // Returns the value associated with the specified key, i.e. the method returns the force vector
    // associated with the specified key. Returns 'null' if the key is not contained in this map.
    // Precondition: key != null.
    public Vector3 get(Massive key) {
        if (root == null) {
            return null;
        }
        return root.get(key);
    }

    // Returns 'true' if this map contains a mapping for the specified key.
    //Precondition: key != null
    public boolean containsKey(Massive key) {
        if (root == null) {
            return false;
        }
        return root.containsKey(key);
    }

    // Returns a readable representation of this map, in which key-value pairs are ordered
    // descending according to 'key.getMass()'.
    public String toString() {
        if (root == null) {
            return "";
        }
        return root.toString();
    }

    // Returns a `MassiveSet` view of the keys contained in this tree map. Changing the
    // elements of the returned `MassiveSet` object also affects the keys in this tree map.
    public MassiveSet getKeys() {
        return this;
    }

    @Override
    public void draw(CodeDraw cd) {
        if (this.root != null)
            this.root.draw(cd);
    }

    @Override
    public MassiveIterator iterator() {
        return new MassiveForceTreeMapIterator(this);
    }

    @Override
    public boolean contains(Massive element) {
        return this.containsKey(element);
    }

    @Override
    public void remove(Massive element) {
        if (root != null) {
            this.root.remove(element, null, false);
        }
    }

    @Override
    public void clear() {
        this.root = null;
    }

    @Override
    public boolean containsNone(MassiveSet set) {
        for (Massive m : this) {
            for (Massive _m : set) {
                if (m.equals(_m)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int size() {
        if (root == null) return 0;
        return root.size();
    }

    @Override
    public MassiveLinkedList toList() {
        if (root == null) {
            return new MassiveLinkedList();
        }
        return this.root.toList();
    }
}

class TreeNode {
    private TreeNode right, left;
    private Massive key;
    private Vector3 value;

    TreeNode(Massive m, Vector3 v) {
        this.key = m;
        this.value = v;
    }

    public int size() {
        return (this.left == null ? 0 : this.left.size()) + 1 + (this.right == null ? 0 : this.right.size());
    }

    public Vector3 get(Massive m) {
        if (this.key.equals(m)) {
            return this.value;
        } else if (this.key.getMass() > m.getMass()) {
            return (this.left == null ? null : this.left.get(m));
        } else {
            return (this.right == null ? null : this.right.get(m));
        }
    }

    public Vector3 put(TreeNode n) {
        if (this.key.equals(n.key)) {
            Vector3 v = this.value;
            this.value = n.value;
            return v;
        } else if (this.key.getMass() > n.key.getMass()) {
            if (this.left == null) {
                this.left = n;
            } else {
                this.left.put(n);
            }
        } else {
            if (this.right == null) {
                this.right = n;
            } else {
                this.right.put(n);
            }
        }
        return null;
    }

    public boolean containsKey(Massive m) {
        if (this.key.equals(m)) {
            return true;
        } else if (this.key.getMass() > m.getMass()) {
            return (this.left != null && this.left.containsKey(m));
        } else {
            return (this.right != null && this.right.containsKey(m));
        }
    }

    public String toString() {
        return (this.left == null ? "" : this.left.toString()) +
                this.key.toString() + ": " + this.value.toString() + "\n" +
                (this.right == null ? "" : this.right.toString());
    }

    public void draw(CodeDraw cd) {
        if (this.left != null) this.left.draw(cd);
        this.key.draw(cd);
        if (this.right != null) this.right.draw(cd);
    }

    public MassiveLinkedList toList() {
        MassiveLinkedList l = new MassiveLinkedList();
        l.addFirst(this.key);
        MassiveLinkedList m;
        if (this.left != null) {
            m = this.left.toList();
            while (m.size() > 0) {
                l.addFirst(m.pollLast());
            }
        }
        if (this.right != null) {
            m = this.right.toList();
            while (m.size() > 0) {
                l.addLast(m.pollFirst());
            }
        }
        return l;
    }

    public void remove(Massive el, TreeNode parent, boolean leftChild) {
        if (el.equals(this.key)) {
            TreeNode n = this.getBiggestLeftChild();
            if (n == null) {
                if (leftChild) parent.left = null;
                else parent.right = null;
            } else {
                this.key = n.key;
                this.value = n.value;
            }
        } else if (this.key.mass() > el.mass()) {
            if (this.left == null) return;
            this.left.remove(el, this, true);
        } else {
            if (this.right == null) return;
            this.right.remove(el, this, false);
        }
    }

    public TreeNode getBiggestLeftChild() {
        if (this.left == null) {
            return null;
        }
        TreeNode p = this.left, n = this.left;
        while (n.right != null) {
            p = n;
            n = n.right;
        }
        if (p == this.left) {
            this.left = null;
        }
        p.right = null;
        return n;
    }

    public Vector3 getValue() {
        return value;
    }

    public void setValue(Vector3 value) {
        this.value = value;
    }
}


class MassiveForceTreeMapIterator implements MassiveIterator {
    private final MassiveLinkedList list;

    MassiveForceTreeMapIterator(MassiveForceTreeMap set) {
        this.list = set.toList();
    }

    @Override
    public Massive next() throws NoSuchElementException {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        return list.pollFirst();
    }

    @Override
    public void remove() {

    }

    @Override
    public boolean hasNext() {
        return list.getFirst() != null;
    }
}

