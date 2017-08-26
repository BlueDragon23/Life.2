package classes;

import java.util.List;


public class Tribe {
    private List<Node> tribeNodes;
    private long population;

    //Stats for the tribe
    private double brithRate;
    private double lifeExpectancy;


    public Tribe(Node n, double initalFood, double initalMinerals, double initalUtility, long initalPopulation) {
        tribeNodes.add(n);
        this.population = initalPopulation;
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



    private boolean canMove() {

        //Double spread speed on plains


        //Can't cross mountain ranges


        //Can't habit water


        //Coastal and forest are "normal"








        return false;
    }




}