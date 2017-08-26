package classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

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

    public List<Node> getAdjacentNodes(Node n) {
        List<Node> nodes = new ArrayList<>();
        Location l = n.getLocation();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                nodes.add(getNode(l.add(i, j)));
            }
        }
        return nodes;
    }

    public void initMap() {
        addCoastline();
        addInterior();
        addSea();
    }

    private void addCoastline() {
        SplineInterpolator sp = new SplineInterpolator();
        int size = (int) Math.ceil(2*Math.PI / 0.2);
        double[] ts = new double[size];
        double[] xs = new double[size];
        double[] ys = new double[size];
        Random random = new Random();
        int count = 0;
        for (double t = 0; t < 2*Math.PI; t += 0.2, count++) {
            ts[count] = t;
            xs[count] = (initialRadius - 10) * (random.nextGaussian()/20 + Math.cos(t));
            ys[count] = (initialRadius - 10) * (random.nextGaussian()/20 + Math.sin(t));
        }
        PolynomialSplineFunction fx = sp.interpolate(ts, xs);
        PolynomialSplineFunction fy = sp.interpolate(ts, ys);

        double t = 0;
        while (fx.isValidPoint(t)) {
            addNode(new Node((int) Math.floor(fx.value(t)), (int) Math.floor(fy.value(t)), Node.LandType.COASTAL));
            t += 0.01;
        }
    }

    private void addInterior() {
        Node n;
        Location l;
        for (int y = minY; y <= maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                l = new Location(x, y);
                n = getNode(l);
                if (n == null) {
                    addNode(new Node(x, y, Node.LandType.PLAINS));
                } else if (n.getLandType() == Node.LandType.COASTAL) {
                    break;
                }
            }
            for (int x = 0; x > minX; x--) {
                l = new Location(x, y);
                n = getNode(l);
                if (n == null) {
                    addNode(new Node(x, y, Node.LandType.PLAINS));
                } else if (n.getLandType() == Node.LandType.COASTAL) {
                    break;
                }
            }
        }
    }

    private void addSea() {
        Node n;
        Location l;
        for (int y = -initialRadius; y <= initialRadius; y++) {
            for (int x = -initialRadius; x <= initialRadius; x++) {
                l = new Location(x, y);
                n = getNode(l);
                if (n == null) {
                    addNode(new Node(x, y, Node.LandType.WATER));
                }
            }
        }
    }
}
