package classes;

public interface Map {

    int halfMapSize = (int) Math.pow(2, 9);

    Node getNode(Location l);
    void addNode(Node n);

}
