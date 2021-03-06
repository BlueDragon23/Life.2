package classes;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sun.org.apache.bcel.internal.generic.LAND;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

public abstract class Map {

    int halfMapSize = (int) Math.pow(2, 9);
    private int initialRadius = 50;
    private List<Tribe> tribes;

    // The current minimum/maximum tiles generated
    protected int maxY;
    protected int minY;
    protected int maxX;
    protected int minX;

    //Keep a list of colours
    private List<List<Integer>> cList;

    public abstract Node getNode(Location l);
    public abstract Node getNodeSafe(Location l);
    public abstract void addNode(Node n);

    public Map() {
        tribes = new ArrayList<>();
        cList = new ArrayList<>();
    }

    public List<Node> getAdjacentNodes(Location l) {
        List<Node> nodes = new ArrayList<>();
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

    public int getMaxY() {
        return maxY;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMinX() {
        return minX;
    }

    public void initMap() {
        addCoastline();
        addInterior();
        addSea();
    }

    private void addCoastline() {
        SplineInterpolator sp = new SplineInterpolator();
        int size = (int) Math.ceil(2*Math.PI / 0.2) + 1;
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
        ts[count] = ts[0] + 2*Math.PI;
        xs[count] = xs[0];
        ys[count] = ys[0];
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
                    addNode(genNode(l));
                } else if (n.getLandType() == Node.LandType.COASTAL) {
                    break;
                }
            }
            for (int x = 0; x > minX; x--) {
                l = new Location(x, y);
                n = getNode(l);
                if (n == null) {
                    addNode(genNode(l));
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

    public boolean addNewTribe(int x, int y) {
        Node n = getNode(new Location(x, y));
        if((n.getLandType() != Node.LandType.WATER) && (n.getLandType() != Node.LandType.MOUNTAIN)){
            //get random colour
            int rVal;
            int gVal;
            int bVal;
            List<Integer> rgbAdd = new ArrayList<>();
            rVal = Helpers.randBetween(0,255);
            gVal = Helpers.randBetween(0,255);
            bVal = Helpers.randBetween(0,255);
            rgbAdd.add(rVal);
            rgbAdd.add(gVal);
            rgbAdd.add(bVal);
            while (cList.contains(rgbAdd)) {
                rgbAdd = new ArrayList<>();
                rVal = Helpers.randBetween(0,255);
                gVal = Helpers.randBetween(0,255);
                bVal = Helpers.randBetween(0,255);
                rgbAdd.add(rVal);
                rgbAdd.add(gVal);
                rgbAdd.add(bVal);
            }
            cList.add(rgbAdd);
            javafx.scene.paint.Color c = javafx.scene.paint.Color.rgb(rVal,gVal,bVal);
            Tribe tribe = new Tribe(n,Helpers.randBetween(90,110),c);
            n.setTribe(tribe);
            tribes.add(tribe);
            return true;
        } else {
            return false;
        }

    }

    public int getInitialRadius() {
        return initialRadius;
    }

    public List<Tribe> getTribes() {
        return new ArrayList<>(tribes);
    }

    protected Node genNode(Location l) {
        List<Node> nodes = getAdjacentNodes(l);
        nodes = nodes.stream().filter(Objects::nonNull).collect(Collectors.toList());
        // TODO: make this actually generate nodes
        if (nodes.size() == 0) {
            // Initial behaviour
            return new Node(l.getX(), l.getY(), Node.LandType.PLAINS);
        }
        java.util.Map<Node.LandType, Integer> types = new HashMap<>();
        for (Node n : nodes) {
            types.put(n.getLandType(), types.getOrDefault(n.getLandType(), 0) + 1);
        }
        int largestVal = 0;
        Node.LandType largest = Node.LandType.PLAINS;
        for (Node.LandType type : types.keySet()) {
            if (types.get(type) > largestVal) {
                largest = type;
                largestVal = types.get(type);
            }
        }
        Random r = new Random();
        Node.LandType actual = largest;
        double test = r.nextDouble();
        if (largest == Node.LandType.PLAINS) {
            if (test < 0.05) {
                actual = Node.LandType.MOUNTAIN;
            } else if (test > 0.9) {
                actual = Node.LandType.FOREST;
            }
        } else if (largest == Node.LandType.COASTAL) {
            actual = Node.LandType.PLAINS;
        } else if (largest == Node.LandType.WATER) {
            if (test < 0.2) {
                actual = Node.LandType.COASTAL;
            }
        } else if (largest == Node.LandType.MOUNTAIN) {
            if (test < 0.6) {
                actual = Node.LandType.PLAINS;
            }
        } else if (largest == Node.LandType.FOREST) {
            if (test < 0.2) {
                actual = Node.LandType.PLAINS;
            } else if (test > 0.95) {
                actual = Node.LandType.MOUNTAIN;
            }
        }
        return new Node(l.getX(), l.getY(), actual);
    }
}
