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
        this.birthRate = 1;
        this.explorationSpeed = 1 + explorePreference;
        this.agriculturalKnowledge = 1 + agriculturalPreference;
        this.militryPower = 1 + militaryPreference;
    }

    public void addNode(Node n) {
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


        //Spend the resources
    }

    public void explore() {

    }



    private boolean canMove() {

        //Double spread speed on plains


        //Can't cross mountain ranges


        //Can't habit water


        //Coastal and forest are "normal"








        return false;
    }




}