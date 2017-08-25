package classes;

import java.util.Random;

public class Resources {
    public enum ResourceType {
        FOOD,
        MINERAL,
        UTILITY;

        private static ResourceType getRandom() {
            return values()[(int) (Math.random() * values().length)];
        }
    }

    private double amount;
    private double changeRate;
    private String name;
    private ResourceType type;

    public Resources (Node.LandType lt){
        type = ResourceType.getRandom();
        amount = 1000 * resourceModifier(type) * landModifier(lt);
        changeRate = Math.random() * landModifier(lt);

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

    private String resource_name(Node.LandType lt) {
        String name = "";
        Random rand = new Random();
        int index = rand.nextInt(3);
        switch (lt) {
            case LAND:
                switch (this.type) {
                    case FOOD:
                        switch (index) {
                            case 0:
                                name = "";
                                break;
                            case 1:
                                name = "";
                                break;
                            case 2:
                                name = "";
                                break;
                        }
                        break;
                    case MINERAL:
                        switch (index) {
                            case 0:
                                name = "";
                                break;
                            case 1:
                                name = "";
                                break;
                            case 2:
                                name = "";
                                break;
                        }
                        break;
                    case UTILITY:
                        switch (index) {
                            case 0:
                                name = "";
                                break;
                            case 1:
                                name = "";
                                break;
                            case 2:
                                name = "";
                                break;
                        }
                        break;
                }
                break;
            case WATER:
                switch (this.type) {
                    case FOOD:
                        switch (index) {
                            case 0:
                                name = "";
                                break;
                            case 1:
                                name = "";
                                break;
                            case 2:
                                name = "";
                                break;
                        }
                        break;
                    case MINERAL:
                        switch (index) {
                            case 0:
                                name = "";
                                break;
                            case 1:
                                name = "";
                                break;
                            case 2:
                                name = "";
                                break;
                        }
                        break;
                    case UTILITY:
                        switch (index) {
                            case 0:
                                name = "";
                                break;
                            case 1:
                                name = "";
                                break;
                            case 2:
                                name = "";
                                break;
                        }
                        break;
                }
                break;
            case FOREST:
                switch (this.type) {
                    case FOOD:
                        switch (index) {
                            case 0:
                                name = "";
                                break;
                            case 1:
                                name = "";
                                break;
                            case 2:
                                name = "";
                                break;
                        }
                        break;
                    case MINERAL:
                        switch (index) {
                            case 0:
                                name = "";
                                break;
                            case 1:
                                name = "";
                                break;
                            case 2:
                                name = "";
                                break;
                        }
                        break;
                    case UTILITY:
                        switch (index) {
                            case 0:
                                name = "";
                                break;
                            case 1:
                                name = "";
                                break;
                            case 2:
                                name = "";
                                break;
                        }
                        break;
                }
                break;
            case MOUNTAIN:
                switch (this.type) {
                    case FOOD:
                        switch (index) {
                            case 0:
                                name = "";
                                break;
                            case 1:
                                name = "";
                                break;
                            case 2:
                                name = "";
                                break;
                        }
                        break;
                    case MINERAL:
                        switch (index) {
                            case 0:
                                name = "";
                                break;
                            case 1:
                                name = "";
                                break;
                            case 2:
                                name = "";
                                break;
                        }
                        break;
                    case UTILITY:
                        switch (index) {
                            case 0:
                                name = "";
                                break;
                            case 1:
                                name = "";
                                break;
                            case 2:
                                name = "";
                                break;
                        }
                        break;
                }
                break;
            case COASTAL:
                switch (this.type) {
                    case FOOD:
                        switch (index) {
                            case 0:
                                name = "";
                                break;
                            case 1:
                                name = "";
                                break;
                            case 2:
                                name = "";
                                break;
                        }
                        break;
                    case MINERAL:
                        switch (index) {
                            case 0:
                                name = "";
                                break;
                            case 1:
                                name = "";
                                break;
                            case 2:
                                name = "";
                                break;
                        }
                        break;
                    case UTILITY:
                        switch (index) {
                            case 0:
                                name = "";
                                break;
                            case 1:
                                name = "";
                                break;
                            case 2:
                                name = "";
                                break;
                        }
                        break;
                }
                break;
        }
        return name;
    }


    }
