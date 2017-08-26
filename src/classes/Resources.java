package classes;

import java.util.Random;

public class Resources {
    public enum ResourceType {
        FOOD,
        MINERAL,
        UTILITY;

        private static ResourceType getRandom(Node.LandType lt) {
            ResourceType value;
            value = values()[(int) (Math.random() * values().length)];
            //Prevent water from having utility
            while ((value == ResourceType.UTILITY) && (lt == Node.LandType.WATER)) {
                value = values()[(int) (Math.random() * values().length)];
            }
            return values()[(int) (Math.random() * values().length)];
        }
    }

    private double amount;
    private double changeRate;
    private ResourceType type;

    public Resources (Node.LandType lt){
        type = ResourceType.getRandom(lt);
        amount = 1000 * resourceModifier(type) * landModifier(lt);
        changeRate = Math.random() * landModifier(lt);
        }

    public void tick() {
        amount +=  (100 * changeRate);
    }


    public double getAmount() {
        return this.amount;
    }

    public double getChangeRate() {
        return this.changeRate;
    }

    public ResourceType getType() {
        return type;
    }

    private int resourceModifier(ResourceType rt) {
        int modifier = (int)(Math.random() * 10);

        switch (rt) {
            case FOOD:
                modifier *= 3;
                break;
            case MINERAL:
                modifier *= 2;
                break;
            case UTILITY:
                modifier *= 1.5;
                break;
            default:
        }
        return modifier;
    }

    private double landModifier(Node.LandType lt) {
        double modifier = 1;
        switch (lt) {
            case LAND:
                switch (this.type) {
                    case FOOD:
                        modifier = 1;
                        break;
                    case MINERAL:
                        modifier = 1;
                        break;
                    case UTILITY:
                        modifier = 1;
                        break;
                }
                break;
            case WATER:
                switch (this.type) {
                    case FOOD:
                        modifier = 1.5;
                        break;
                    case MINERAL:
                        modifier = 0;
                        break;
                    case UTILITY:
                        modifier = 0.5;
                        break;
                }
                break;
            case FOREST:
                switch (this.type) {
                    case FOOD:
                        modifier = 1.8;
                        break;
                    case MINERAL:
                        modifier = 0.8;
                        break;
                    case UTILITY:
                        modifier = 1.2;
                        break;
                }
                break;
            case MOUNTAIN:
                switch (this.type) {
                    case FOOD:
                        modifier = 0.2;
                        break;
                    case MINERAL:
                        modifier = 1.9;
                        break;
                    case UTILITY:
                        modifier = 1;
                        break;
                }
                break;
            case COASTAL:
                switch (this.type) {
                    case FOOD:
                        modifier = 1.6;
                        break;
                    case MINERAL:
                        modifier = 0.6;
                        break;
                    case UTILITY:
                        modifier = 2;
                        break;
                }
                break;
        }
        return modifier;
    }

    }
