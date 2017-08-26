package classes;

import java.util.ArrayList;
import java.util.List;


public class Tribe {
    private List<Node> tribeNodes;
    private List<Node> exploredNodes;
    private long population;

    //innate stats for a tribe (randomly allocated currently)
    private double explorePreference;
    private double agriculturalPreference;
    private double militaryPreference;

    //Stats which allocated at start but can be improved
    private double birthRate;
    private double explorationSpeed;
    private double militryPower;
    private double agriculturalKnowledge;

    public Tribe(Node n, long initialPopulation) {
        tribeNodes = new ArrayList<>();
        exploredNodes = new ArrayList<>();

        tribeNodes.add(n);
        this.population = initialPopulation;

        //Set the traits of the tribe
        explorePreference = 1 - Math.random();
        agriculturalPreference = 1 - Math.random();
        militaryPreference = 1 - Math.random();

        //Set initial stats
        this.birthRate = 0.1;
        this.explorationSpeed = 1 + explorePreference;
        this.agriculturalKnowledge = 1 + agriculturalPreference;
        this.militryPower = 1 + militaryPreference;
    }

    public void addNode(Node n) {
        exploredNodes.remove(n); //Remove the item if it is in the
        tribeNodes.add(n);
    }

    public void removeNode(Node n) {
        tribeNodes.remove(n);
    }

    public boolean hasNode(Node n) {
        return tribeNodes.contains(n);
    }

    public double getFood() {
        double food = 0;
        for (Node n : tribeNodes) {
            food += n.getFoodTotal();
        }
        return food;
    }

    public double getMinerals() {
        double minerals = 0;
        for (Node n : tribeNodes) {
            minerals += n.getMineralTotal();
        }
        return minerals;
    }

    public double getUtility() {
        double utility = 0;
        for (Node n : tribeNodes) {
            utility += n.getUtilityTotal();
        }
        return utility;
    }

    public long getPopulation() {
        return population;
    }
    public int nodeCount() {
        return tribeNodes.size();
    }

    public void turnCollection() {
        //Eat the food
        for (Node n: tribeNodes) {
            n.takeFood((population / tribeNodes.size()));
        }

        //Birth the people
        population += (int)(birthRate * population);

        //Spend the resources
    }

    public List<Node> getSeenNodes() {
        //From the current seen grids look at others
        List<Node> seen = new ArrayList<>();
        for (Node n: tribeNodes) {
            seen.add(n);
        }
        for (Node n: exploredNodes) {
            seen.add(n);
        }
        return seen;
    }

    public void explore(List<Node> adjNodes) {
        //given list of adjacent nodes from the map
        //for each of the adj nodes if it is a water
        //node than ignore it.

        List<Node> possibleExploriation = new ArrayList<>();
        for (Node n: adjNodes) {
            if(n.getLandType() != Node.LandType.WATER) {
                possibleExploriation.add(n);
            }
        }

        //There is the possibility of no searching
        if (possibleExploriation.size() > 0) {
            int searchSize = (int)(explorationSpeed * Helpers.randBetween(0,(explorationSpeed + 1)));
            if (searchSize > possibleExploriation.size()) {
                for (Node n: possibleExploriation) {
                    exploredNodes.add(n);
                }
            } else if (searchSize > 0) {
                int randNodePosition;
                for (int i = 0; i < searchSize; i ++) {
                    randNodePosition = Helpers.randBetween(0,possibleExploriation.size() - 1);
                    exploredNodes.add(possibleExploriation.get(randNodePosition));
                    possibleExploriation.remove(randNodePosition);
                }
            }
        }
    }

    public List<Node> expand() {
        List<Node> expand = new ArrayList<>();
        int expandSize = (int)(militaryPreference * Helpers.randBetween(0,(militaryPreference + 1)));
        if (exploredNodes.size() > 0) {
            if (expandSize > exploredNodes.size()) {
                for (Node n: exploredNodes) {
                    expand.add(n);
                }
            } else if (expandSize > 0) {
                int randNodePosition;
                for (int i = 0; i < expandSize; i ++) {
                    randNodePosition = Helpers.randBetween(0,exploredNodes.size() - 1);
                    expand.add(exploredNodes.get(randNodePosition));
                }
            }
        }
        return expand;
    }




}