import codedraw.CodeDraw;

import java.awt.*;
import java.util.Arrays;
import java.util.Random;

/**
 * 1. Was versteht man unter Datenkapselung? Geben Sie ein Beispiel, wo dieses Konzept in dieser Aufgabenstellung angewendet wird.
 * Datenkapselung beschreibt das Kontrollieren der Sichtbarkeit von Methoden, also welche Methoden und Funktionen von außenstehenden
 * Klassen angesprochen werden können. Hierbei findet eine Datenkapselung statt, wenn Methoden auf "private" bis "packaged" gestellt werden.
 * Abhängig von dieser Sichtbarkeit haben immer unterschiedliche Klassen Zugriff auf die Methoden.
 * Ein Beispiel dafür kann in der Klasse "SpaceDraw" gefunden werden, wo die Methoden "limitAndDarken" und "kelvinToColor" nicht von außerhalb
 * aufgerufen werden können.
 *
 * 2. Was versteht man unter Data Hiding? Geben Sie ein Beispiel, wo dieses Konzept in dieser Aufgabenstellung angewendet wird.
 * Bei Data Hiding werden Attribute von Klassen/Objekten durch Ändern der Sichtbarkeit vor anderen Klassen versteckt.
 * Dies sorgt dafür, dass andere Objekte die Parameter nur durch gezielte Methoden verändern können und so die "Richtigkeit" der Daten
 * leichter überprüft werden kann (zum Beispiel in setter-Methoden).
 * Dieses Konzept ist in den Klassen "Vector3" und "Body" ersichtlich, wo jeweils alle Attribute auf "private" gesetzt wurden.
 *
 * 3. Was steht bei einem Methodenaufruf links vom `.` (z.B. bei `SpaceDraw.massToColor(1e30)` oder
 * `body.radius()`)? Woran erkennt man dabei Objektmethoden?
 * Links vom Methodenaufruf steht entweder die Instanz eines Objektes einer Klasse (in dem Beispiel das Objekt body) oder der Name der Klasse selbst
 * (zum Beispiel SpaceDraw).
 * Objektmethoden erkennt man hierbei daran, dass davor ein Objekt steht (meistens werden diese kleingeschrieben).
 */

// Simulates the formation of a massive solar system.
public class Simulation {

    // gravitational constant
    public static final double G = 6.6743e-11;

    // one astronomical unit (AU) is the average distance of earth to the sun.
    public static final double AU = 150e9; // meters

    // one light year
    public static final double LY = 9.461e15; // meters

    // some further constants needed in the simulation
    public static final double SUN_MASS = 1.989e30; // kilograms
    public static final double SUN_RADIUS = 696340e3; // meters
    public static final double EARTH_MASS = 5.972e24; // kilograms
    public static final double EARTH_RADIUS = 6371e3; // meters

    // set some system parameters
    public static final double SECTION_SIZE = 2 * AU; // the size of the square region in space
    public static final int NUMBER_OF_BODIES = 22;
    public static final double OVERALL_SYSTEM_MASS = 20 * SUN_MASS; // kilograms

    // all quantities are based on units of kilogram respectively second and meter.

    // The main simulation method using instances of other classes.
    public static void main(String[] args) {

        // simulation
        CodeDraw cd = new CodeDraw();
        Body[] bodies = new Body[NUMBER_OF_BODIES];
        Vector3[] forceOnBody = new Vector3[bodies.length];

        Random random = new Random(2022);

        for (int i = 0; i < bodies.length; i++) {
            double mass = Math.abs(random.nextGaussian()) * OVERALL_SYSTEM_MASS / bodies.length; // kg
            Vector3 massCenter = new Vector3(0.2 * random.nextGaussian() * AU, 0.2 * random.nextGaussian() * AU, 0.2 * random.nextGaussian() * AU);
            Vector3 currentMovement = new Vector3(0 + random.nextGaussian() * 5e3, 0 + random.nextGaussian() * 5e3, 0 + random.nextGaussian() * 5e3);
            bodies[i] = new Body(mass, massCenter, currentMovement);
        }
        double seconds = 0;

        // simulation loop
        while (true) {
            seconds++; // each iteration computes the movement of the celestial bodies within one second.

            // merge bodies that have collided
            for (int i = 0; i < bodies.length; i++) {
                for (int j = i + 1; j < bodies.length; j++) {
                    if (bodies[j].distanceTo(bodies[i]) <
                            bodies[j].radius() + bodies[i].radius()) {
                        bodies[i] = bodies[i].merge(bodies[j]);
                        Body[] bodiesOneRemoved = new Body[bodies.length - 1];
                        for (int k = 0; k < bodiesOneRemoved.length; k++) {
                            bodiesOneRemoved[k] = bodies[k < j ? k : k + 1];
                        }
                        bodies = bodiesOneRemoved;

                        // since the body index i changed size there might be new collisions
                        // at all positions of bodies, so start all over again
                        i = -1;
                        j = bodies.length;
                    }
                }
            }

            // for each body (with index i): compute the total force exerted on it.
            for (int i = 0; i < bodies.length; i++) {
                forceOnBody[i] = new Vector3(0,0,0); // begin with zero
                for (int j = 0; j < bodies.length; j++) {
                    if (i != j) {
                        Vector3 forceToAdd = bodies[i].gravitationalForce(bodies[j]);
                        forceOnBody[i] = forceOnBody[i].plus(forceToAdd);
                    }
                }
            }
            // now forceOnBody[i] holds the force vector exerted on body with index i.

            // for each body (with index i): move it according to the total force exerted on it.
            for (int i = 0; i < bodies.length; i++) {
                bodies[i].move(forceOnBody[i]);
            }

            // show all movements in the canvas only every hour (to speed up the simulation)
            if (seconds % (3600) == 0) {
                // clear old positions (exclude the following line if you want to draw orbits).
                cd.clear(Color.BLACK);

                // draw new positions
                for (Body body : bodies) {
                    body.draw(cd);
                }

                // show new positions
                cd.show();
            }

        }

    }


}
