// A map that associates a body with a force exerted on it. The number of
// key-value pairs is not limited.
//
public class BodyForceMap {

    private Body[] keys;
    private Vector3[] values;
    private int currSize;

    // Initializes this map with an initial capacity.
    // Precondition: initialCapacity > 0.
    public BodyForceMap(int initialCapacity) {
        this.keys = new Body[initialCapacity];
        this.values = new Vector3[initialCapacity];
        this.currSize = 0;
    }

    // Adds a new key-value association to this map. If the key already exists in this map,
    // the value is replaced and the old value is returned. Otherwise 'null' is returned.
    // Precondition: key != null.
    public Vector3 put(Body key, Vector3 force) {
        int index = this.getIndex(key);
        if (index >= 0 && this.keys[index] == key) {
            Vector3 returnVal = this.values[index];
            this.values[index] = force;
            return returnVal;
        } else {
            for (int j = this.keys.length-1; j > index; --j) {
                this.keys[j] = this.keys[j-1];
                this.values[j] = this.values[j-1];
            }
            this.keys[index] = key;
            this.values[index] = force;
            this.currSize++;
        }

        if (this.currSize == this.keys.length) {
            Body[] help = this.keys;
            this.keys = new Body[this.keys.length * 2];
            System.arraycopy(help, 0, this.keys, 0, help.length);

            Vector3[] help2 = this.values;
            this.values = new Vector3[this.values.length * 2];
            System.arraycopy(help2, 0, this.values, 0, help2.length);
        }

        return null;
    }

    // Returns the value associated with the specified key, i.e. the returns the force vector
    // associated with the specified body. Returns 'null' if the key is not contained in this map.
    // Precondition: key != null.
    public Vector3 get(Body key) {
        int index = this.getIndex(key);
        if (this.keys[index] == key) {
            return this.values[index];
        } else {
            return null;
        }
    }

    public int size() {
        return this.currSize;
    }

    // returns the index with binary search
    private int getIndex(Body key) {
        int left = 0;
        int right = this.currSize - 1;
        while (left <= right) {
            int middle = left + ((right - left) / 2);
            // if key is already in the set, return it
            if (keys[middle] == key) {
                return middle;
            }
            if (keys[middle].mass() < key.mass()) {
                right = middle - 1;
            } else {
                left = middle + 1;
            }
        }
        return right+1;
    }
}
