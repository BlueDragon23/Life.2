package classes;

import java.util.List;
import java.util.Random;

public abstract class Map {

    int halfMapSize = (int) Math.pow(2, 9);
    private int initialRadius = 50;

    // The current minimum/maximum tiles generated
    protected int maxY;
    protected int minY;
    protected int maxX;
    protected int minX;

    public abstract Node getNode(Location l);
    public abstract void addNode(Node n);

    public void initMap() {
        addCoastline();
        addInterior();
    }

    private void addCoastline() {
        double x, y, noise;
        Random random = new Random();
        Node node;
        for (double t = 0; t < 2*Math.PI; t += 0.01) {
            noise = random.nextGaussian() / 2;
            x = (initialRadius + noise) * Math.cos(t);
            y = (initialRadius + noise) * Math.sin(t);
            node = new Node((int) x, (int) y, Node.LandType.COASTAL);
            addNode(node);
        }
    }

    private void addInterior() {
        Node n;
        Location l;
        for (int y = maxY; y >= minY; y--) {
            for (int x = minX; x <= maxX; x++) {
                l = new Location(x, y);
                n = getNode(l);
                if (n == null) {
                    addNode(new Node(x, y, Node.LandType.PLAINS));
                }
            }
        }
    }
}
