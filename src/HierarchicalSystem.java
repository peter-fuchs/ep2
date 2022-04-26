import codedraw.CodeDraw;

// A cosmic system that is composed of a central named body (of type 'NamedBodyForcePair')
// and an arbitrary number of subsystems (of type 'HierarchicalSystem') in its orbit.
// This class implements 'CosmicSystem'.
//
public class HierarchicalSystem implements CosmicSystem {

    // TODO: define missing parts of this class.
    private NamedBodyForcePair central;
    private CosmicSystem[] inOrbit;

    // Initializes this system with a name and a central body.
    public HierarchicalSystem(NamedBodyForcePair central, CosmicSystem... inOrbit) {
        this.central = central;
        this.inOrbit = inOrbit;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(this.central.getName());
        s.append(" {");
        for (CosmicSystem cs : this.inOrbit) {
            s.append(cs.toString()).append(", ");
        }
        s.delete(s.length() - 2, s.length());
        return s.append("}").toString();
    }

    @Override
    public Vector3 getMassCenter() {
        Vector3 massCenter = this.central.getMassCenter().times(this.central.getMass());
        for (CosmicSystem cs : this.inOrbit) {
            massCenter.plus(cs.getMassCenter().times(cs.getMass()));
        }
        return massCenter.times(1 / getMass());
    }

    @Override
    public double getMass() {
        double mass = central.getMass();
        for (CosmicSystem cs : this.inOrbit) {
            mass += cs.getMass();
        }
        return mass;
    }

    @Override
    public int numberOfBodies() {
        int numberOfBodies = central.numberOfBodies();
        for (CosmicSystem cs : this.inOrbit) {
            numberOfBodies += cs.numberOfBodies();
        }
        return numberOfBodies;
    }

    @Override
    public double distanceTo(CosmicSystem cs) {
        return cs.getMassCenter().distanceTo(this.getMassCenter());
    }

    @Override
    public void addForceFrom(Body b) {
        this.central.addForceFrom(b);
        for (CosmicSystem cs : this.inOrbit) {
            cs.addForceFrom(b);
        }
    }

    @Override
    public void addForceTo(CosmicSystem cs) {
        this.central.addForceTo(cs);
        for (CosmicSystem _cs : this.inOrbit) {
            _cs.addForceTo(cs);
        }
    }

    @Override
    public BodyLinkedList getBodies() {
        BodyLinkedList l = this.central.getBodies();
        for (CosmicSystem cs : this.inOrbit) {
            BodyLinkedList ll = cs.getBodies();
            while (ll.size() > 0) {
                l.addFirst(ll.pollFirst());
            }
        }

        return l;
    }

    @Override
    public void update() {
        this.central.update();
        for (CosmicSystem cs : this.inOrbit) {
            cs.update();
        }
    }

    @Override
    public void draw(CodeDraw cd) {
        this.central.draw(cd);
        for (CosmicSystem cs : this.inOrbit) {
            cs.draw(cd);
        }
    }
}
