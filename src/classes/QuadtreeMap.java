package classes;

public class QuadtreeMap implements Map {
    private Quadtree map;

    public QuadtreeMap() {
        map = new Quadtree(new Location(0, 0), halfMapSize);
    }

    @Override
    public Node getNode(Location l) {
        return map.getNode(l);
    }
}
