package classes;

import java.util.ArrayList;
import java.util.List;

public class Quadtree {

    private Location centre;
    private int halfSize;
    private static final int maxNodes = 16;
    private List<Node> nodes;

    private Quadtree NE;
    private Quadtree SE;
    private Quadtree SW;
    private Quadtree NW;

    public Quadtree(Location centre, int halfSize) {
        this.centre = centre;
        this.halfSize = halfSize;
        nodes = new ArrayList<>();
    }

    public Node getNode(Location location) {
        if (!inBounds(location)) {
            return null;
        }
        for (Node n: nodes) {
            if (n.getLocation().equals(location)) {
                return n;
            }
        }
        if (NE == null) {
            // We don't have any subtrees, give up
            return null;
        }
        // This will always be the correct subtree
        Quadtree subtree = getSubtree(location);
        return subtree.getNode(location);
    }

    public void addNode(Node node) {
        Location l = node.getLocation();
        if (!inBounds(l)) {
            // Not in this quadtree, the fuck are you doing
            return;
        } else if (NE != null) {
            // We have subtrees, put it there
            addToSubtree(node);
        } else if (nodes.size() < maxNodes) {
            nodes.add(node);
        } else {
            // Need to make subtrees
            int newSize = halfSize/2;
            NE = new Quadtree(centre.add(newSize, newSize), newSize);
            SE = new Quadtree(centre.add(newSize, -newSize), newSize);
            SW = new Quadtree(centre.add(-newSize, -newSize), newSize);
            NW = new Quadtree(centre.add(-newSize, newSize), newSize);
            nodes.add(node);
            for (Node n : nodes) {
                addToSubtree(n);
            }
            nodes.clear();
        }
    }

    private boolean inBounds(Location l) {
        return !(l.getX() < centre.getX() - halfSize ||
                l.getX() > centre.getX() + halfSize ||
                l.getY() < centre.getY() - halfSize ||
                l.getY() > centre.getY() + halfSize);
    }

    private void addToSubtree(Node n) {
        Quadtree subtree = getSubtree(n.getLocation());
        subtree.addNode(n);
    }

    private Quadtree getSubtree(Location l) {
        if (l.getX() >= centre.getX() && l.getY() >= centre.getY()) {
            return NE;
        } else if (l.getX() >= centre.getX() && l.getY() <= centre.getY()) {
            return SE;
        } else if (l.getX() <= centre.getX() && l.getY() <= centre.getY()) {
            return SW;
        } else {
            return NW;
        }
    }

}
