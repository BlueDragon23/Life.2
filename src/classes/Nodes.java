package classes;

public class Node {
    //Basic Node information
    public Location location;
    public Tribe tribe;

    //Basic Resource information



    public Node (int x, int y) {
        this.location = new Location(x,y);
        this.tribe = null;
    }

    public hasTribe() {
        return (!(tribe == null));
    }


}