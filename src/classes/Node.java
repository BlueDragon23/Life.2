package classes;

public class Node {
    //Basic Node information
    private Location location;
    private Tribe tribe;

    //Basic Resource information



    public Node (int x, int y) {
        this.location = new Location(x,y);
        this.tribe = null;
    }

    public boolean hasTribe() {
        return (!(tribe == null));
    }


}