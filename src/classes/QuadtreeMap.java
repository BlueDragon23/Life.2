package classes;

import java.util.ArrayList;
import java.util.List;

public class QuadtreeMap implements Map {
    private Quadtree map;

    public QuadtreeMap() {
        map = new Quadtree(new Location(0, 0), halfMapSize);
    }

    @Override
    public Node getNode(Location l) {
        return map.getNode(l);
    }

    @Override
    public void addNode(Node n) {
        map.addNode(n);
    }

    @Override
    public List<Node> getAdjacentNodes(Node n) {
        return new ArrayList<>();
    }
}
