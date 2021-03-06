// A queue of bodies. A collection designed for holding bodies prior to processing.
// The bodies of the queue can be accessed in a FIFO (first-in-first-out) manner,
// i.e., the body that was first inserted by 'add' is retrieved first by 'poll'.
// The number of elements of the queue is not limited.
//
public class BodyQueue {

    private Body[] bodies;
    private int currPos;

    // Initializes this queue with an initial capacity.
    // Precondition: initialCapacity > 0.
    public BodyQueue(int initialCapacity) {
        this.bodies = new Body[initialCapacity];
        this.currPos = 0;
    }

    // Initializes this queue as an independent copy of the specified queue.
    // Calling methods of this queue will not affect the specified queue
    // and vice versa.
    // Precondition: q != null.
    public BodyQueue(BodyQueue q) {
        this.bodies = new Body[q.bodies.length];
        this.currPos = q.size();
        for (int i = 0; i < q.size(); ++i) {
            this.bodies[i] = q.bodies[i];
        }

    }

    // Adds the specified body 'b' to this queue.
    public void add(Body b) {
        if (this.bodies.length - 1 >= 0)
            System.arraycopy(this.bodies, 0, this.bodies, 1, this.bodies.length - 1);
        this.bodies[0] = b;
        this.currPos++;
        if (this.bodies.length == this.currPos) {
            Body[] help = this.bodies;
            this.bodies = new Body[this.bodies.length * 2];
            System.arraycopy(help, 0, this.bodies, 0, help.length);
        }
    }

    // Retrieves and removes the head of this queue, or returns 'null'
    // if this queue is empty.
    public Body poll() {
        if (this.currPos == 0) {
            return null;
        }
        Body b = this.bodies[--this.currPos];
        this.bodies[this.currPos] = null;
        return b;
    }

    private Body get(int index) {
        return this.bodies[index];
    }

    // Returns the number of bodies in this queue.
    public int size() {
        return this.currPos;
    }

    // Precondition: bq != null
    public BodyQueue getJoined(BodyQueue bq) {
        BodyQueue res = new BodyQueue(bq.size() + this.size() + 1);
        BodyQueue copy1 = new BodyQueue(this);
        BodyQueue copy2 = new BodyQueue(bq);
        for (int i = 0; i < Math.min(bq.size(), this.size()); ++i) {
            res.add(copy2.poll());
            res.add(copy1.poll());
        }
        for (int i = Math.min(bq.size(), this.size()); i < Math.max(this.size(), bq.size()); ++i) {
            res.add((bq.size() > this.size() ? copy2 : copy1).poll());
        }
        return res;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < this.size(); ++i) {
            s.append(this.bodies[i]);
        }
        return s.toString();
    }
}
