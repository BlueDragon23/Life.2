package classes;

public class QuadtreeMap extends Map {
    private Quadtree map;

    public QuadtreeMap() {
        super();
        map = new Quadtree(new Location(0, 0), halfMapSize);
    }

    @Override
    public Node getNode(Location l) {
        return map.getNode(l);
    }

    @Override
    public Node getNodeSafe(Location l) {
        Node n = map.getNode(l);
        if (n == null) {
            n = genNode(l);
        }
        return n;
    }

    @Override
    public void addNode(Node n) {
        Location l = n.getLocation();
        if (l.getX() > maxX) {
            maxX = l.getX();
        }
        if (l.getX() < minX) {
            minX = l.getX();
        }
        if (l.getY() > maxY) {
            maxY = l.getY();
        }
        if (l.getY() < minY) {
            minY = l.getY();
        }

        map.addNode(n);
    }

}
