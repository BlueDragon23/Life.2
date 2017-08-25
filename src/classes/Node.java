package classes;

import java.util.List;

public class Node {
    //Basic Node information
    private Location location;
    private Tribe tribe;

    //Land type
    public enum LandType {
        LAND,
        WATER,
        MOUNTAIN,
        COASTAL,
        FOREST
        //Todo add more land types!
    }

    //Basic Resource information
    private List<Resources> resources;
    private LandType landType;

    public Node (int x, int y, LandType lt) {
        this.location = new Location(x,y);
        this.tribe = null;
        this.landType = lt;

        //Add the resources based on land type
    }

    public boolean hasTribe() {
        return (!(tribe == null));
    }

}