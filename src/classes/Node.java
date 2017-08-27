package classes;

import java.util.ArrayList;
import java.util.List;

public class Node {
    //Basic Node information
    private Location location;
    private Tribe tribe;
    private static int resourceCount = 5;

    //Land type
    public enum LandType {
        PLAINS,
        WATER,
        MOUNTAIN,
        COASTAL,
        FOREST
    }

    //Basic Resource information
    private List<Resources> nodeResources;
    private LandType landType;


    public Node (int x, int y, LandType lt) {
        this.location = new Location(x,y);
        this.tribe = null;
        this.landType = lt;
        nodeResources = new ArrayList<>();
        //Add the resources based on land type
        for (int i=0; i < resourceCount; i++) {
            nodeResources.add(new Resources(landType));
        }
    }

    public void tick() {
        for (Resources r : nodeResources) {
            r.tick();
        }
    }

    public double getResourceTotal(Resources.ResourceType rt) {
        double value = 0;
        for (Resources r : nodeResources) {
            if (r.getType() == rt) {
                value += r.getAmount();
            }
        }
        return value;
    }

    public double getFoodTotal() {
        return getResourceTotal(Resources.ResourceType.FOOD);
    }
    public double getMineralTotal() {
        return getResourceTotal(Resources.ResourceType.MINERAL);
    }
    public double getUtilityTotal() {
        return getResourceTotal(Resources.ResourceType.UTILITY);
    }

    private int countResourceType(Resources.ResourceType rt) {
        int count = 0;
        for (Resources r: nodeResources) {
            if (r.getType() == rt) {
                count++;
            }
        }
        return count;
    }

    private int countFoodResources() {
        return countResourceType(Resources.ResourceType.FOOD);
    }
    private int countMineralesources() {
        return countResourceType(Resources.ResourceType.MINERAL);
    }
    private int countUtilityResources() {
        return countResourceType(Resources.ResourceType.UTILITY);
    }

    private double removeResourceType(double amount, int distribute, Resources.ResourceType rt) {
        for (Resources r: nodeResources) {
            if(r.getType() == rt) {
                if ((amount / distribute) > r.getAmount()) {
                    r.takeAmount(r.getAmount());
                    amount -= r.getAmount();
                    distribute--;
                } else {
                    r.takeAmount((amount / distribute)); //negatives handled in resources
                    amount -= (amount / distribute);
                    distribute--;
                }
            }
        }
        //returns leftover food
        return (amount);
    }

    public double takeFood(double amount) {
        return removeResourceType(amount, countFoodResources(), Resources.ResourceType.FOOD);
    }
    public void takeMineral(double amount) {
        removeResourceType(amount, countMineralesources(), Resources.ResourceType.MINERAL);
    }
    public void takeUtility(double amount) {
        removeResourceType(amount, countUtilityResources(), Resources.ResourceType.UTILITY);
    }

    public LandType getLandType() {
        return landType;
    }

    public boolean hasTribe() {
        return (!(tribe == null));
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Node)) {
            return false;
        }
        Node other = (Node) o;
        return this.location.equals(other.location);
    }

    public String toString() {
        return location + " - " + landType;
    }

    public void setTribe(Tribe t) {
        this.tribe = t;
    }

    public Tribe getTribe() {
        return tribe;
    }
}