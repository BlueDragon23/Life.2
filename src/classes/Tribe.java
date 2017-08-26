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
    private int battles;

    //Retained Resources (loot)
    private double foodLoot;
    private double materialLoot;
    private double utilityLoot;

    public Tribe(Node n, long initialPopulation) {
        tribeNodes = new ArrayList<>();
        exploredNodes = new ArrayList<>();

        tribeNodes.add(n);
        this.population = initialPopulation;

        //Set the traits of the tribe
        setPreferences();

        //Set initial stats
        this.birthRate = 0.1;
        this.explorationSpeed = 1 + explorePreference;
        this.agriculturalKnowledge = 1 + agriculturalPreference;
        this.militryPower = 1 + militaryPreference;
    }

    private void setPreferences() {
        double total = 0.97;
        explorePreference = 0.01;
        agriculturalPreference = 0.01;
        militaryPreference = 0.01;
        List<Integer> preferences = new ArrayList<>();
        preferences.add(0);
        preferences.add(1);
        preferences.add(2);

        while(preferences.size() > 0) {
            int trait = preferences.get(Helpers.randBetween(0, preferences.size() -1));
            switch (trait) {
                case 0:
                    explorePreference +=Helpers.randBetween(0, total);
                    total -= explorePreference - 0.01;
                    break;
                case 1:
                    agriculturalPreference +=Helpers.randBetween(0, total);
                    total -= agriculturalPreference - 0.01;
                break;
                case 2:
                    militaryPreference +=Helpers.randBetween(0, total);
                    total -= militaryPreference - 0.01;
                    break;
            }
            preferences.remove(preferences.indexOf(trait));
        }
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
    public void addBattleWin(double foodLoot, double materialLoot, double utilityLoot) {
        this.battles++;
        this.foodLoot += foodLoot;
        this.materialLoot += materialLoot;
        this.utilityLoot += utilityLoot;
    }

    public void turnCollection() {
        //Eat the food
        for (Node n: tribeNodes) {
            n.takeFood((population / tribeNodes.size()));
        }

        //Birth the people
        population += (int)(birthRate * population);

        //Spend the resources
        research();
    }

    private void research() {
        for(int rc=0; rc < 5; rc++) {
            double rVal = Helpers.randBetween(0.0,1.0);
            //Placed in order - explore/agri/mil
            if (rVal < explorePreference) {

            } else if ((rVal > explorePreference) && (rVal < explorePreference + agriculturalPreference)) {

            } else {

            }
        }
    }

    private double researchCost(double traitDiscount, double currentAmount) {
        return 0;
    }

    public double forGloryAndHonour() {
        //This method determines what they spend this time investing in
        return (population * militaryPreference * militryPower + battles);
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

    public void explore(List<Node> visibleNodes) {
        //given list of adjacent nodes from the map
        //for each of the adj nodes if it is a water
        //node than ignore it.

        List<Node> possibleExploration = new ArrayList<>();
        List<Node> adjNodes = new ArrayList<>();

        boolean canVisit;

        //Todo Aidan to steal for game
        //For each of the possible nodes to return to the tribe to explore
        for (int i = 0; i < visibleNodes.size(); i++) {
            //For each of the nodes look at what surrounds it.
            //adjNodes = Map.getAdjacentNodes(visibleNodes.get(i));
            canVisit = false;
            //We don't care about tiles which are only connected to a tribe by water or mountain
            for (int j = 0; j < adjNodes.size(); j++) {
                Node adj = adjNodes.get(j);
                //For each adjacent Node to us make sure it is connected by something other than water or mountain
                if((adj.getLandType() != Node.LandType.WATER) && (adj.getLandType() != Node.LandType.MOUNTAIN)) {
                    canVisit = true;
                }
            }
            //Remove it if we can't visit it
            if (!canVisit) {
                visibleNodes.remove(i);
            }
        }
        //todo end

        //There is the possibility of no searching
        if (possibleExploration.size() > 0) {
            int searchSize = (int)(explorationSpeed * Helpers.randBetween(0,(explorationSpeed + 1)));
            if (searchSize > possibleExploration.size()) {
                for (Node n: possibleExploration) {
                    exploredNodes.add(n);
                }
            } else if (searchSize > 0) {
                int randNodePosition;
                for (int i = 0; i < searchSize; i ++) {
                    randNodePosition = Helpers.randBetween(0,possibleExploration.size() - 1);
                    exploredNodes.add(possibleExploration.get(randNodePosition));
                    possibleExploration.remove(randNodePosition);
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