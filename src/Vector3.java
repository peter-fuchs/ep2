import codedraw.CodeDraw;

import java.awt.*;
import java.util.Objects;

// This class represents vectors in a 3D vector space.
public class Vector3 {

    private double x;
    private double y;
    private double z;

    // Returns a copy of a new vector-object with the according x-, y- and z-values
    public static Vector3 copy(Vector3 v) {
        return new Vector3(v.x, v.y, v.z);
    }

    // constructor
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3() {
        new Vector3(0, 0,0);
    }

    public Vector3(Vector3 v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    // Returns the sum of this vector and vector 'v'.
    public Vector3 plus(Vector3 v) {
        return new Vector3(this.x + v.x, this.y + v.y, this.z + v.z);
    }

    // Returns the product of this vector and 'd'.
    public Vector3 times(double d) {
        return new Vector3(this.x * d, this.y * d, this.z * d);
    }

    // Returns the sum of this vector and -1*v.
    public Vector3 minus(Vector3 v) {
        Vector3 mul = Vector3.copy(v);
        return this.plus(mul.times(-1));
    }

    // Returns the Euclidean distance of this vector
    // to the specified vector 'v'.
    public double distanceTo(Vector3 v) {

        double dX = this.x - v.x;
        double dY = this.y - v.y;
        double dZ = this.z - v.z;

        return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
    }

    // Returns the length (norm) of this vector.
    public double length() {
        return this.distanceTo(new Vector3()); // distance to origin.
    }

    // Normalizes this vector: changes the length of this vector such that it becomes 1.
    // The direction and orientation of the vector is not affected.
    public void normalize() {
        double length = this.length();
        this.x /= length;
        this.y /= length;
        this.z /= length;
    }

    // Draws a filled circle with a specified radius centered at the (x,y) coordinates of this vector
    // in the canvas associated with 'cd'. The z-coordinate is not used.
    public void drawAsFilledCircle(CodeDraw cd, double radius) {
        double x = cd.getWidth() * (this.x + Simulation8.SECTION_SIZE / 2) / Simulation8.SECTION_SIZE;
        double y = cd.getWidth() * (this.y + Simulation8.SECTION_SIZE / 2) / Simulation8.SECTION_SIZE;
        radius = cd.getWidth() * radius / Simulation8.SECTION_SIZE;
        cd.fillCircle(x, y, Math.max(radius, 1.5));
    }

    // Returns the coordinates of this vector in brackets as a string
    // in the form "[x,y,z]", e.g., "[1.48E11,0.0,0.0]".
    public String toString() {
        return "["+x+","+y+","+z+"]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector3 vector3 = (Vector3) o;
        return Double.compare(vector3.x, x) == 0 && Double.compare(vector3.y, y) == 0 && Double.compare(vector3.z, z) == 0;
    }
}

