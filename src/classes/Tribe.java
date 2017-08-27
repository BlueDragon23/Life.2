package classes;

import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;

import javax.tools.JavaCompiler;
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
    private Color colour;

    public Tribe(Node n, long initialPopulation, Color c) {
        tribeNodes = new ArrayList<>();
        exploredNodes = new ArrayList<>();
        this.colour = c;
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

    private double getResource(Resources.ResourceType rt) {
        double resources = 0;
        for (Node n : tribeNodes) {
            resources += n.getResourceTotal(rt);
        }
        return resources;
    }

    public double getFood() {
        return getResource(Resources.ResourceType.FOOD);
    }

    public double getMinerals() {
        return getResource(Resources.ResourceType.MINERAL);
    }

    public double getUtility() {
        return getResource(Resources.ResourceType.UTILITY);
    }

    public long getPopulation() {
        return population;
    }
    public int nodeCount() {
        return tribeNodes.size();
    }
    public void addBattleWin(double foodLoot, double materialLoot, double utilityLoot, double exploreRLoot,
                             double agriRLoot) {
        this.battles++;
        this.foodLoot += foodLoot;
        this.materialLoot += materialLoot;
        this.utilityLoot += utilityLoot;
        this.explorationSpeed += exploreRLoot;
        this.agriculturalKnowledge += agriRLoot;
    }

    public void turnCollection() {
        //Eat the food
        int nodesRemaining = tribeNodes.size();
        long allotedFood = population / nodesRemaining;
        double deficitFood = 0;
        for (Node n: tribeNodes) {
            deficitFood += n.takeFood(allotedFood);
            nodesRemaining--;
            if ((deficitFood != 0) && (nodesRemaining != 0)) {
                //distribute the overflowing food out to the rest of the nodes
                allotedFood += (deficitFood / nodesRemaining);
                deficitFood = 0;
            }
        }
        population -= deficitFood;

        //Birth the people
        population += (int)(birthRate * population);

        //Spend the resources
        research();
    }

    private void research() {
        for(int rc=0; rc < 5; rc++) {
            double rVal = Helpers.randBetween(0.0,1.0);
            //Placed in order - explore/agri/mil
            double spendMin;
            double spendUtil;
            if (rVal < explorePreference) {
                //Spend on explore points
                spendMin = 0;
                spendUtil = 0;
                if((canSpend(spendMin, Resources.ResourceType.MINERAL)) &&
                        (canSpend(spendUtil, Resources.ResourceType.UTILITY))) {
                    //Spend and Add
                }
            } else if ((rVal > explorePreference) && (rVal < explorePreference + agriculturalPreference)) {
                //Spend on agri
                //Spend on explore points
                spendMin = 0;
                spendUtil = 0;
                if((canSpend(spendMin, Resources.ResourceType.MINERAL)) &&
                        (canSpend(spendUtil, Resources.ResourceType.UTILITY))) {
                    //Spend and Add
                }
            } else {
                //Spend on milit
                //Spend on explore points
                spendMin = 0;
                spendUtil = 0;
                if((canSpend(spendMin, Resources.ResourceType.MINERAL)) &&
                        (canSpend(spendUtil, Resources.ResourceType.UTILITY))) {
                    //Spend and Add
                }
            }
        }
    }

    private boolean canSpend(double amount, Resources.ResourceType rt) {
        return (amount < getResource(rt));
    }


    private double researchCost(double traitDiscount, double currentAmount) {
        return 0;
    }

    public double forGloryAndHonour() {
        //This method determines what they spend this time investing in
        return (population * (militaryPreference * militryPower + battles));
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

    public void turnExplore(List<Node> possibleExploration) {
        //given list of adjacent nodes from the map
        //for each of the adj nodes if it is a water
        //node than ignore it.

        //There is the possibility of no searching
        if (possibleExploration.size() > 0) {
            int searchSize = (int)(population % (explorationSpeed * Helpers.randBetween(0,(explorationSpeed * 10)))) + 1;
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

    public List<Node> turnExpand() {
        List<Node> expand = new ArrayList<>();
        int expandSize = (int)(population%(militaryPreference * Helpers.randBetween(0,(militaryPreference * 10))));
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

    public Color getColour(){
        return colour;
    }




}