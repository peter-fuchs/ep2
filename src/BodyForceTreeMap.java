// A map that associates a Body with a Vector3 (typically this is the force exerted on the body).
// The number of key-value pairs is not limited.
public class BodyForceTreeMap {

    private MyTreeNode root;

    // Adds a new key-value association to this map. If the key already exists in this map,
    // the value is replaced and the old value is returned. Otherwise 'null' is returned.
    // Precondition: key != null.
    public Vector3 put(Body key, Vector3 value) {
        if (root == null) {
            this.root = new MyTreeNode(key, value);
        } else {
            return this.putWithStart(this.root, new MyTreeNode(key, value));
        }

        return null;
    }

    // Returns the value associated with the specified key, i.e. the method returns the force vector
    // associated with the specified key. Returns 'null' if the key is not contained in this map.
    // Precondition: key != null.
    public Vector3 get(Body key) {
        MyTreeNode el = this.root;
        while (el != null) {
            if (el.key().mass() == key.mass()) {
                return el.value();
            }
            if (key.mass() > el.key().mass()) {
                // larger element -> right side
                el = el.GeT_RiGhT();
            } else {
                el = el.getLeft();
            }
        }
        return null;
    }

    // Returns 'true' if this map contains a mapping for the specified key.
    public boolean containsKey(Body key) {
        MyTreeNode el = this.root;
        while (el != null) {
            if (el.key() == key) {
                return true;
            }
            if (key.mass() > el.key().mass()) {
                el = el.GeT_RiGhT();
            } else {
                el = el.getLeft();
            }
        }
        return false;
    }

    public void reorder(Body node, double oldMass) {
        MyTreeNode el = this.root;
        while (el != null) {
            if (el.key() == node) {
                this.putWithStart(this.root, el);
                MyTreeNode right = el.GeT_RiGhT(), left = el.getLeft();
                el.setRight(null);
                el.setLeft(null);
                this.putWithStart(this.root, right);
                this.putWithStart(this.root, left);
                return;
            }
            if (oldMass > el.key().mass()) {
                el = el.GeT_RiGhT();
            } else {
                el = el.getLeft();
            }
        }
    }

    private Vector3 putWithStart(MyTreeNode start, MyTreeNode element) {
        if (start == null || element == null) {
            return null;
        }
        MyTreeNode el = start;
        MyTreeNode parent = null;
        while (el != null) {
            parent = el;
            if (element.key().mass() == el.key().mass()) {
                Vector3 oldVal = el.value();
                el.setValue(element.value());
                return oldVal;
            }
            if (element.key().mass() > el.key().mass()) {
                // mass is larger -> right side
                el = el.GeT_RiGhT();
            } else {
                // mass is smaller -> left side
                el = el.getLeft();
            }
        }
        if (element.key().mass() > parent.key().mass()) {
            parent.setRight(element);
        } else {
            parent.setLeft(element);
        }
        return null;
    }

    // Returns a readable representation of this map, in which key-value pairs are ordered
    // descending according to the mass of the bodies.
    public String toString() {
        return this.root.toString();

    }
}
