// A hash map that associates a 'Massive'-object with a Vector3 (typically this is the force
// exerted on the object). The number of key-value pairs is not limited.
//
public class MassiveForceHashMap {

    // TODO: define missing parts of this class.
    private Massive[] keys;
    private Vector3[] values;
    private int size;

    // Initializes 'this' as an empty map.
    public MassiveForceHashMap() {
        this.size = 0;
        this.keys = new Massive[65];
        this.values = new Vector3[65];
    }

    // Adds a new key-value association to this map. If the key already exists in this map,
    // the value is replaced and the old value is returned. Otherwise 'null' is returned.
    // Precondition: key != null.
    public Vector3 put(Massive key, Vector3 value) {
        int i = this.indexOf(key);
        Vector3 oldVal = this.values[i];
        this.values[i] = value;
        if (this.keys[i] == null) {
            this.keys[i] = key;
            if (++size >= this.keys.length * 3/4) {
                Massive[] oldKeys = this.keys;
                Vector3[] oldVals = this.values;
                this.keys = new Massive[(this.keys.length << 1) - 1];
                this.values = new Vector3[(this.values.length << 1) - 1];
                for (int j = 0; j < oldKeys.length; ++j) {
                    this.keys[i = indexOf(oldKeys[j])] = oldKeys[j];
                    this.values[i] = oldVals[j];
                }
            }
        }
        return oldVal;
    }

    // Returns the value associated with the specified key, i.e. the method returns the force vector
    // associated with the specified key. Returns 'null' if the key is not contained in this map.
    // Precondition: key != null.
    public Vector3 get(Massive key) {
        return this.values[indexOf(key)];
    }

    private int indexOf(Massive key) {
        if (key == null) {
            return keys.length - 1;
        }
        int i = key.hashCode() & (keys.length - 2);
        while (keys[i] != null && !keys[i].equals(key)) {
            i = (i + 1) & (keys.length - 2);
        }
        return i;
    }

    // Returns 'true' if this map contains a mapping for the specified key.
    public boolean containsKey(Massive key) {
        return this.keys[this.indexOf(key)] != null;
    }

    // Returns a readable representation of this map, with all key-value pairs. Their order is not
    // defined.
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < keys.length - 1; i++) {
            if (keys[i] != null) {
                s.append((s.length() == 0) ? "(" : ", (").append(keys[i]).append(", ").append(values[i]).append(")");
            }
        }
        return "{" + s + "}";
    }

    // Compares `this` with the specified object for equality. Returns `true` if the specified
    // `o` is not `null` and is of type `MassiveForceHashMap` and both `this` and `o` have equal
    // key-value pairs, i.e. the number of key-value pairs is the same in both maps and every
    // key-value pair in `this` equals one key-value pair in `o`. Two key-value pairs are
    // equal if the two keys are equal and the two values are equal. Otherwise `false` is returned.
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o .getClass() != MassiveForceHashMap.class) {
            return false;
        }
        MassiveForceHashMap t = (MassiveForceHashMap) o;
        if (size != t.size) {
            return false;
        }
        for (int i = 0; i < keys.length - 1; i++) {
            if (keys[i] != null &&
                    (values[i] == null ? !t.containsKey(keys[i]) || t.get(keys[i]) != null
                                       : !values[i].equals(t.get(keys[i])))) {
                return false;
            }
        }
        return true;
    }

    // Returns the hashCode of `this`.
    public int hashCode() {
        int h = size;
        for (int i = 0; i < keys.length - 1; i++) {
            if (keys[i] != null) {
                h += keys[i].hashCode();
                if (values[i] != null) {
                    h += values[i].hashCode();
                }
            }
        }
        return h;
    }

    // Returns a list of all the keys in no specified order.
    public MassiveLinkedList keyList() {
        MassiveLinkedList l = new MassiveLinkedList();
        for (Massive key : keys) {
            if (key != null) {
                l.addLast(key);
            }
        }
        return l;
    }

}