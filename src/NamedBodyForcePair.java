import codedraw.CodeDraw;

// A body with a name and an associated force. The leaf node of
// a hierarchical cosmic system. This class implements 'CosmicSystem'.
//
public class NamedBodyForcePair implements CosmicSystem {

    // TODO: define missing parts of this class.
    private String name;
    private Body body;
    private Vector3 force;


    // Initializes this with name, mass, current position and movement. The associated force
    // is initialized with a zero vector.
    public NamedBodyForcePair(String name, double mass, Vector3 massCenter, Vector3 currentMovement) {
        this.name = name;
        this.body = new Body(mass, massCenter, currentMovement);
        this.force = new Vector3();
    }

    // Returns the name of the body.
    public String getName() {
        return this.name;
    }

    @Override
    public Vector3 getMassCenter() {
        return body.massCenter();
    }

    @Override
    public double getMass() {
        return body.mass();
    }

    @Override
    public int numberOfBodies() {
        return this.body == null ? 0 : 1;
    }

    @Override
    public double distanceTo(CosmicSystem cs) {
        return this.body.massCenter().distanceTo(cs.getMassCenter());
    }

    @Override
    public void addForceFrom(Body b) {
        if (b == this.body) {
            return;
        }
        this.force = this.force.plus(this.body.gravitationalForce(b));
    }

    @Override
    public void addForceTo(CosmicSystem cs) {
        cs.addForceFrom(this.body);
    }

    @Override
    public BodyLinkedList getBodies() {
        return new BodyLinkedList(this.body);
    }

    @Override
    public void update() {
        this.body.move(this.force);
        this.force = new Vector3();
    }

    @Override
    public void draw(CodeDraw cd) {
        this.body.draw(cd);
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
