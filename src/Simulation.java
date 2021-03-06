import codedraw.CodeDraw;

import java.awt.*;
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
 * Links vom Methodenaufruf steht entweder die Instanz einer Klasse (in dem Beispiel das Objekt body) oder der Name der Klasse selbst
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
        // Body[] bodies = new Body[NUMBER_OF_BODIES];
        BodyQueue bq = new BodyQueue(NUMBER_OF_BODIES);
        BodyForceMap bfm = new BodyForceMap(NUMBER_OF_BODIES);
        Body mainBody = new Body(OVERALL_SYSTEM_MASS / 8, new Vector3(), new Vector3());

        Random random = new Random(2022);
        // addSolarSystem(bq, bfm);
        addRandom(bq, bfm, random, mainBody);

        double seconds = 0;

        // simulation loop
        while (true) {
            seconds++; // each iteration computes the movement of the celestial bodies within one second.

            BodyQueue outerloop = new BodyQueue(bq);
            while (outerloop.size() > 0) {
                Body el = outerloop.poll();
                BodyQueue innerloop = new BodyQueue(bq);
                while (innerloop.size() > 0) {
                    Body el2 = innerloop.poll();
                    if (el != el2) {
                        Vector3 forceToAdd = el.gravitationalForce(el2);
                        bfm.put(el, bfm.get(el).plus(forceToAdd));
                    }
                }
            }

            BodyQueue reset = new BodyQueue(bq);
            while (reset.size() > 0) {
                Body b = reset.poll();
                // move body, then reset to 0
                b.move(bfm.get(b));
                bfm.put(b, new Vector3());
            }

            // show all movements in the canvas only every hour (to speed up the simulation)
            if (seconds % (3600) == 0) {
                // clear old positions (exclude the following line if you want to draw orbits).
                cd.clear(Color.BLACK);

                System.out.println(new BodyQueue(bq).poll());

                // draw new positions
                BodyQueue draw = new BodyQueue(bq);
                while (draw.size() > 0) {
                    Body b = draw.poll();
                    b.draw(cd);
                }

                // show new positions
                cd.show();
            }

        }

    }

    private static void addSolarSystem(BodyQueue bq, BodyForceMap bfm) {
        Body sun = new Body(1.989e30,new Vector3(0,0,0),new Vector3(0,0,0));
        Body earth = new Body(5.972e24,new Vector3(-1.394555e11,5.103346e10,0),new Vector3(-10308.53,-28169.38,0));
        Body mercury = new Body(3.301e23,new Vector3(-5.439054e10,9.394878e9,0),new Vector3(-17117.83,-46297.48,-1925.57));
        Body venus = new Body(4.86747e24,new Vector3(-1.707667e10,1.066132e11,2.450232e9),new Vector3(-34446.02,-5567.47,2181.10));
        Body mars = new Body(6.41712e23,new Vector3(-1.010178e11,-2.043939e11,-1.591727E9),new Vector3(20651.98,-10186.67,-2302.79));
        bq.add(sun);
        bfm.put(sun, new Vector3());
        bq.add(earth);
        bfm.put(earth, new Vector3());
        bq.add(mercury);
        bfm.put(mercury, new Vector3());
        bq.add(venus);
        bfm.put(venus, new Vector3());
        bq.add(mars);
        bfm.put(mars, new Vector3());
    }

    private static void addRandom(BodyQueue bq, BodyForceMap bfm, Random random, Body mainBody) {
        for (int i = 0; i < NUMBER_OF_BODIES; i++) {
            double mass = Math.abs(random.nextGaussian()) * OVERALL_SYSTEM_MASS / NUMBER_OF_BODIES; // kg
            Vector3 massCenter = new Vector3(0.2 * random.nextGaussian() * AU, 0.2 * random.nextGaussian() * AU, 0.2 * random.nextGaussian() * AU);
            Vector3 currentMovement = new Vector3(0 + random.nextGaussian() * 5e3, 0 + random.nextGaussian() * 5e3, 0 + random.nextGaussian() * 5e3);
            Body b = new Body(mass, massCenter, currentMovement, mainBody);
            bq.add(b);
            bfm.put(b, new Vector3());
        }
    }
}
