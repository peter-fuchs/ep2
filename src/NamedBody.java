import codedraw.CodeDraw;

import javax.naming.Name;

public class NamedBody implements Massive
{

    private String name;
    private Body b;

    // Initializes this with name, mass, current position and movement. The associated force
    // is initialized with a zero vector.
    public NamedBody(String name, double mass, Vector3 massCenter, Vector3 currentMovement) {
        this.name = name;
        this.b = new Body(mass, massCenter, currentMovement);
    }

    // Returns the name of the body.
    public String getName() {
        return this.name;
    }

    @Override
    public Vector3 massCenter() {
        return b.massCenter();
    }

    @Override
    public double mass() {
        return b.mass();
    }

    // Compares `this` with the specified object. Returns `true` if the specified `o` is not
    // `null` and is of type `NamedBody` and both `this` and `o` have equal names.
    // Otherwise `false` is returned.
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o .getClass() != NamedBody.class) {
            return false;
        }
        NamedBody t = (NamedBody) o;
        return t.getName().equals(this.getName());
    }

    // Returns the hashCode of `this`.
    public int hashCode() {
        return name.hashCode();

    }

    // Returns a readable representation including the name of this body.
    public String toString() {
        return this.getName() + " " + this.b;
    }

    @Override
    public void draw(CodeDraw cd) {
        this.b.draw(cd);
    }

    @Override
    public void move(Vector3 force) {
        this.b.move(force);
    }
}
