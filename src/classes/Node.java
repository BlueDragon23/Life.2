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
        for (int i=0; i < 5; i++) {
            resources.add(new Resources(landType));
        }
    }

    public void tick() {
        for (Resources r : resources) {
            r.tick();
        }
    }

    public double getFoodTotal() {
        double value = 0;
        for (Resources r : resources) {
            if (r.getType() == Resources.ResourceType.FOOD) {
                value += r.getAmount();
            }
        }
        return value;
    }
    public double getMineralTotal() {
        double value = 0;
        for (Resources r : resources) {
            if (r.getType() == Resources.ResourceType.MINERAL) {
                value += r.getAmount();
            }
        }
        return value;
    }
    public double getUtilityTotal() {
        double value = 0;
        for (Resources r : resources) {
            if (r.getType() == Resources.ResourceType.UTILITY) {
                value += r.getAmount();
            }
        }
        return value;
    }

    public boolean hasTribe() {
        return (!(tribe == null));
    }

    public Location getLocation() {
        return location;
    }

}