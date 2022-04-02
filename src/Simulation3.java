import codedraw.CodeDraw;

import java.awt.*;
import java.util.Random;

// Simulates the formation of a massive solar system.
//
public class Simulation3 {

    // The main simulation method using instances of other classes.
    public static void main(String[] args) {

        // simulation
        CodeDraw cd = new CodeDraw();
        // Body[] bodies = new Body[NUMBER_OF_BODIES];
        BodyLinkedList list = new BodyLinkedList();
        BodyForceTreeMap tree = new BodyForceTreeMap();

        Random random = new Random(2022);
        for (int i = 0; i < Simulation.NUMBER_OF_BODIES; i++) {
            double mass = Math.abs(random.nextGaussian()) * Simulation.OVERALL_SYSTEM_MASS / Simulation.NUMBER_OF_BODIES; // kg
            Vector3 massCenter = new Vector3(0.2 * random.nextGaussian() * Simulation.AU, 0.2 * random.nextGaussian() * Simulation.AU, 0.2 * random.nextGaussian() * Simulation.AU);
            Vector3 currentMovement = new Vector3(0 + random.nextGaussian() * 5e3, 0 + random.nextGaussian() * 5e3, 0 + random.nextGaussian() * 5e3);
            Body b = new Body(mass, massCenter, currentMovement);
            list.addFirst(b);
            tree.put(b, new Vector3());
        }

        double seconds = 0;

        // simulation loop
        while (true) {
            seconds++; // each iteration computes the movement of the celestial bodies within one second.

            BodyLinkedList mergeList = new BodyLinkedList(list);
            Body el = mergeList.pollFirst();
            while (el != null) {
                if (list.indexOf(el) == -1) {
                    el = mergeList.pollFirst();
                    continue;
                }
                BodyLinkedList merge = list.removeCollidingWith(el);
                boolean removedElements = false;
                double oldMass = 0;
                if (merge.size() > 0) {
                    removedElements = true;
                    oldMass = el.mass();
                }
                for (int i = 0; i < merge.size(); ++i) {
                    el.merge(merge.get(i));
                }
                if (removedElements) {
                    tree.reorder(el, oldMass);
                    // restart again
                    mergeList = new BodyLinkedList(list);
                }
                el = mergeList.pollFirst();
            }

            BodyLinkedList outerLoop = new BodyLinkedList(list);
            el = outerLoop.pollFirst();
            while (el != null) {
                BodyLinkedList innerLoop = new BodyLinkedList(list);
                Body el2 = innerLoop.pollFirst();
                while (el2 != null) {
                    if (el != el2) {
                        Vector3 forceToAdd = el.gravitationalForce(el2);
                        tree.put(el, tree.get(el).plus(forceToAdd));
                    }
                    el2 = innerLoop.pollFirst();
                }
                el = outerLoop.pollFirst();
            }

            BodyLinkedList reset = new BodyLinkedList(list);
            el = reset.pollFirst();
            while (el != null) {
                // move body, then reset to 0
                el.move(tree.get(el));
                tree.put(el, new Vector3());
                el = reset.pollFirst();
            }

            // show all movements in the canvas only every hour (to speed up the simulation)
            if (seconds % (3600) == 0) {
                // clear old positions (exclude the following line if you want to draw orbits).
                cd.clear(Color.BLACK);

                // draw new positions
                for (int i = 0; i < list.size(); ++i) {
                    Body b = list.get(i);
                    b.draw(cd);
                }

                // show new positions
                cd.show();
            }

        }
    }
}
