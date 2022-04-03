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
            return this.put(new MyTreeNode(key, value));
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

    // reorders the element if the mass changes -> it needs resorting
    public void reorder(Body node, double oldMass) {
        MyTreeNode el = this.root;
        // loop through elements
        while (el != null) {
            // if same element
            if (el.key() == node) {
                MyTreeNode right = el.GeT_RiGhT(), left = el.getLeft();
                el.setRight(null);
                el.setLeft(null);
                // reorder element and children
                this.put(el);
                this.put(right);
                this.put(left);
                return;
            }
            // else jump according to oldMass
            if (oldMass > el.key().mass()) {
                el = el.GeT_RiGhT();
            } else {
                el = el.getLeft();
            }
        }
    }

    private Vector3 put(MyTreeNode element) {
        if (element == null) {
            return null;
        }
        MyTreeNode el = this.root;
        MyTreeNode parent = null;
        // loop through elements
        while (el != null) {
            parent = el;
            // if same element update value
            if (element.key().mass() == el.key().mass()) {
                Vector3 oldVal = el.value();
                el.setValue(element.value());
                return oldVal;
            }

            // jump into child
            if (element.key().mass() > el.key().mass()) {
                // mass is larger -> right side
                el = el.GeT_RiGhT();
            } else {
                // mass is smaller -> left side
                el = el.getLeft();
            }
        }
        // right or left child
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
