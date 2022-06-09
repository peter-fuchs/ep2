import codedraw.CodeDraw;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// Simulates the solar system.
//
public class Simulation9 {

    // gravitational constant
    public static final double G = 6.6743e-11;

    // one astronomical unit (AU) is the average distance of earth to the sun.
    public static final double AU = 150e9; // meters

    // set some system parameters
    public static final double SECTION_SIZE = 10 * AU; // the size of the square region in space

    // all quantities are based on units of kilogram respectively second and meter.

    // The main simulation method using instances of other classes.
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java Simulation9 <path> <yyyy-ddd-mm>");
            return;
        }
        String path = args[0];
        String day = args[1];

        // simulation
        CodeDraw cd = new CodeDraw();

        Map<NamedBody, Vector3> map = new HashMap<>();
        // create solar system with 13 bodies

        map.put(new NamedBody("Oumuamua", 8e6, new Vector3(0, 0, 0), new Vector3(0, 0, 0)),
                new Vector3(0, 0, 0));
        map.put(new NamedBody("Earth", 5.972E24, new Vector3(0, 0, 0), new Vector3(0, 0, 0)),
                new Vector3(0, 0, 0));
        map.put(new NamedBody("Moon", 7.349E22, new Vector3(0, 0, 0), new Vector3(0, 0, 0)),
                new Vector3(0, 0, 0));
        map.put(new NamedBody("Mars", 6.41712E23, new Vector3(0, 0, 0), new Vector3(0, 0, 0)),
                new Vector3(0, 0, 0));
        map.put(new NamedBody("Deimos", 1.8E20, new Vector3(0, 0, 0), new Vector3(0, 0, 0)),
                new Vector3(0, 0, 0));
        map.put(new NamedBody("Phobos", 1.08E20, new Vector3(0, 0, 0), new Vector3(0, 0, 0)),
                new Vector3(0, 0, 0));
        map.put(new NamedBody("Mercury", 3.301E23, new Vector3(0, 0, 0), new Vector3(0, 0, 0)),
                new Vector3(0, 0, 0));
        map.put(new NamedBody("Venus", 4.86747E24, new Vector3(0, 0, 0), new Vector3(0, 0, 0)),
                new Vector3(0, 0, 0));
        map.put(new NamedBody("Vesta", 2.5908E20, new Vector3(0, 0, 0), new Vector3(0, 0, 0)),
                new Vector3(0, 0, 0));
        map.put(new NamedBody("Pallas", 2.14E20, new Vector3(0, 0, 0), new Vector3(0, 0, 0)),
                new Vector3(0, 0, 0));
        map.put(new NamedBody("Hygiea", 8.32E19, new Vector3(0, 0, 0), new Vector3(0, 0, 0)),
                new Vector3(0, 0, 0));
        map.put(new NamedBody("Ceres", 9.394E20, new Vector3(0, 0, 0), new Vector3(0, 0, 0)),
                new Vector3(0, 0, 0));


        //TODO: implementation of this method according to 'Aufgabenblatt8.md'.
        for (NamedBody m : map.keySet()) {
            try {
                ReadDataUtil.readConfiguration(m, path + "/" + m.getName() + ".txt", day);
            } catch (IOException e) {
                System.err.println(e.getMessage());
                return;
            }
        }

        // add sun after states have been read from files.
        map.put(new NamedBody("Sun", 1.989E30, new Vector3(0, 0, 0), new Vector3(0, 0, 0)),
                new Vector3(0, 0, 0));

        int seconds = -1;
        while (true) {
            seconds++;

            for (NamedBody massive : map.keySet()) {
                Vector3 force = new Vector3();
                for (Massive massive2 : map.keySet()) {
                    if (massive != massive2) {
                        force = force.plus(massive.gravitationalForce(massive2));
                    }
                }
                map.put(massive, force);
            }

            for (Massive massive : map.keySet()) {
                massive.move(map.get(massive));
            }

            if (seconds % 3600 == 0) {
                cd.clear(Color.BLACK);
                for (Massive m : map.keySet()) {
                    m.draw(cd);
                }
                cd.show();
            }
        }

    }

}
