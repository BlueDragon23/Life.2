package classes;

import java.util.List;

public interface Map {

    int halfMapSize = (int) Math.pow(2, 9);

    Node getNode(Location l);
    void addNode(Node n);

    public List<Node> getAdjacentNodes(Node n);
}
