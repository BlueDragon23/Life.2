package classes;

import javafx.scene.paint.Color;

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
    public int explorationSpeed;
    public int militryPower;
    public int agriculturalKnowledge;
    public int battles;

    //Retained Resources (loot)
    private double foodLoot;
    private double mineralLoot;
    private double utilityLoot;
    private Color colour;

    //
    private List<BattleLog> battleLog;

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
        this.explorationSpeed = 1 + (int)(explorePreference * 10);
        this.agriculturalKnowledge = 1 + (int)(agriculturalPreference * 10);
        this.militryPower = 1 + (int)(militaryPreference * 10);

        this.battleLog = new ArrayList<>();
    }

    private void setPreferences() {
        double total = 0.7;
        explorePreference = 0.10;
        agriculturalPreference = 0.10;
        militaryPreference = 0.10;
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

    public String getType() {
        if ((explorePreference > militaryPreference) && (explorePreference > agriculturalPreference)) {
            //Get second pref
            if (agriculturalPreference > militaryPreference) {
                //Explore/Ag
                return "Exploring Agriculture";
            } else {
                //Explore/Mil
                return "Exploring Aggressive ";
            }
        } else if ((agriculturalPreference > explorePreference) && (agriculturalPreference > militaryPreference)) {
            //Get second pref
            if (explorePreference > militaryPreference) {
                //Ag/Exp
                return "Agricultural Exploration";
            } else {
                //Ag/Mil
                return "Agriculture Enforced ";
            }
        } else {
            //Mil pref is highest
            //Get second pref
            if (agriculturalPreference > explorePreference) {
                //Mil/Ag
                return "Aggressive Agriculture";
            } else {
                //Mil/Exp
                return "Aggressive Exploration";
            }
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
    public void addBattleResult(double foodLoot, double mineralLoot, double utilityLoot) {
        this.battles++;
        this.foodLoot += foodLoot;
        this.mineralLoot += mineralLoot;
        this.utilityLoot += utilityLoot;
    }

    public void addBattleLog(BattleLog bl) {
        this.battleLog.add(bl);
    }
    public List<BattleLog> getBattleLog(){
        return battleLog;
    }

    public void turnCollection() {
        //Eat the food
        int nodesRemaining = tribeNodes.size();
        long allotedFood = (population / nodesRemaining); //Todo use agri power to reduce this number
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

    private void spendResource(double amount, Resources.ResourceType rt) {
        //Check if we are trying to take more than we should
        if (canSpend(amount,rt)) {
            for (Node n: tribeNodes) {
                switch (rt) {
                    case FOOD:
                        if(foodLoot > amount) {
                            foodLoot -= amount;
                        } else {
                            amount -= foodLoot;
                            foodLoot = 0;
                            n.takeFood(amount);
                        }
                        break;
                    case MINERAL:
                        if(mineralLoot > amount) {
                            mineralLoot -= amount;
                        } else {
                            amount -= mineralLoot;
                            mineralLoot = 0;
                            n.takeMineral(amount);
                        }
                        break;
                    case UTILITY:
                        if(utilityLoot > amount) {
                            utilityLoot -= amount;
                        } else {
                            amount -= utilityLoot;
                            utilityLoot = 0;
                            n.takeUtility(amount);
                        }
                }
            }
        }
    }

    private void research() {
        for(int rc=0; rc < 5; rc++) {
            double rVal = Helpers.randBetween(0.0,1.0);
            //Placed in order - explore/agri/mil
            double spendMin;
            double spendUtil;
            //Todo lower based on the amount of preference towards resource making it cheaper
            if (rVal < explorePreference) {
                //Spend on explore points
                spendMin = (explorationSpeed * 100) * (1 - explorePreference);
                spendUtil = (explorationSpeed * 100) * (1 - explorePreference) ;
                if((canSpend(spendMin, Resources.ResourceType.MINERAL)) &&
                        (canSpend(spendUtil, Resources.ResourceType.UTILITY))) {
                    //Spend
                    spendResource(spendMin, Resources.ResourceType.MINERAL);
                    spendResource(spendUtil, Resources.ResourceType.UTILITY);
                    // Add
                    explorationSpeed++;
                }
            } else if ((rVal > explorePreference) && (rVal < explorePreference + agriculturalPreference)) {
                //Spend on agri
                //Spend on explore points
                spendMin = (agriculturalKnowledge * 80) * (1 - agriculturalPreference);
                spendUtil = (agriculturalKnowledge * 120) * (1 - agriculturalPreference);
                if((canSpend(spendMin, Resources.ResourceType.MINERAL)) &&
                        (canSpend(spendUtil, Resources.ResourceType.UTILITY))) {
                    //Spend
                    spendResource(spendMin, Resources.ResourceType.MINERAL);
                    spendResource(spendUtil, Resources.ResourceType.UTILITY);
                    // Add
                    agriculturalKnowledge++;
                }
            } else {
                //Spend on milit
                //Spend on explore points
                spendMin = (militryPower * 120) * (1 - militaryPreference);
                spendUtil = (militryPower * 80) * (1 - militaryPreference);
                if((canSpend(spendMin, Resources.ResourceType.MINERAL)) &&
                        (canSpend(spendUtil, Resources.ResourceType.UTILITY))) {
                    //Spend
                    spendResource(spendMin, Resources.ResourceType.MINERAL);
                    spendResource(spendUtil, Resources.ResourceType.UTILITY);
                    // Add
                    militryPower++;
                }
            }
        }
    }

    private boolean canSpend(double amount, Resources.ResourceType rt) {
        return (amount < (getResource(rt) + getLoot(rt)));
    }
    private double getLoot(Resources.ResourceType rt) {
        switch (rt){
            case FOOD:
                return foodLoot;
            case MINERAL:
                return mineralLoot;
            case UTILITY:
                return utilityLoot;
            default:
                return 0;
        }
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

        for (Node n : tribeNodes) {
            possibleExploration.remove(n);
        }
        //There is the possibility of no searching
        if (possibleExploration.size() > 0) {
            int searchSize = (int)(population % (explorationSpeed * Helpers.randBetween(0,(explorationSpeed * 10)) + 1)) ;
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
        double expandCost = (((nodeCount() * 10) * (1 - agriculturalPreference)) % agriculturalKnowledge);
        int expandSize = (int)(population%(militaryPreference * Helpers.randBetween(0,(militaryPreference * 10)) + 1));
        if (exploredNodes.size() > 0) {
            if (expandSize > exploredNodes.size()) {
                for (Node n: exploredNodes) {
                    if(canSpend(expandCost, Resources.ResourceType.FOOD)) {
                        expand.add(n);
                        spendResource(expandCost, Resources.ResourceType.FOOD);
                    }
                }
            } else if (expandSize > 0) {
                int randNodePosition;
                for (int i = 0; i < expandSize; i ++) {
                    //If they are aggressive they will go for other people by default
                    if (militaryPreference > 0.50) {
                        List<Node> otherTribeNodes = new ArrayList<>();
                        for(Node n: exploredNodes) {
                            if(n.hasTribe() && (expandSize > 0)) {
                                if (canSpend(expandCost, Resources.ResourceType.FOOD)) {
                                    expand.add(n);
                                    spendResource(expandCost, Resources.ResourceType.FOOD);
                                    i++;
                                }
                            }
                        }
                    }
                    randNodePosition = Helpers.randBetween(0, exploredNodes.size() - 1);
                    if (canSpend(expandCost, Resources.ResourceType.FOOD)) {
                        expand.add(exploredNodes.get(randNodePosition));
                        spendResource(expandCost, Resources.ResourceType.FOOD);
                    }
                }
            }
        }
        return expand;
    }

    public Color getColour(){
        return colour;
    }








}