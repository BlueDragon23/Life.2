package gui;

import classes.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameLoop implements Runnable {
    private int radius;
    private Map map;
    public boolean running = true;
    private int generation;

    public GameLoop(int radius, Map m) {
        this.radius = radius;
        this.map = m;
        generation = 0;
    }

    private void tick() {
        java.util.Map<Node, List<Tribe>> expansionNodes = new HashMap<>();
        for (Tribe tribe : map.getTribes()) {
            tribe.turnCollection();

            List<Node> visibleNodes = tribe.getSeenNodes();
            List<Node> adjNodes;
            boolean canVisit;

            List<Node> newVisibleNodes = new ArrayList<>();
            //For each of the possible nodes to return to the tribe to explore
            for (int i = 0; i < visibleNodes.size(); i++) {
                //For each of the nodes look at what surrounds it.
                adjNodes = map.getAdjacentNodes(visibleNodes.get(i).getLocation());
                //We don't care about tiles which are only connected to a tribe by water or mountain
                for (int j = 0; j < adjNodes.size(); j++) {
                    canVisit = false;
                    Node adj = adjNodes.get(j);
                    //For each adjacent Node to us make sure it is connected by something other than water or mountain
                    if((adj.getLandType() != Node.LandType.WATER) && (adj.getLandType() != Node.LandType.MOUNTAIN)) {
                        canVisit = true;
                    }
                    //Remove it if we can't visit it
                    if (canVisit) {
                        newVisibleNodes.add(adj);
                    }
                }

            }
            visibleNodes.addAll(newVisibleNodes);
            tribe.turnExplore(visibleNodes);

            List<Node> expansion = tribe.turnExpand();
            for (Node node : expansion) {
                List<Tribe> expandingTribe = expansionNodes.getOrDefault(node, new ArrayList<>());
                expandingTribe.add(tribe);
                expansionNodes.put(node, expandingTribe);
            }
        }
        for (Node n : expansionNodes.keySet()) {
            List<Tribe> tribes = expansionNodes.get(n);
            if (tribes.size() == 1) {
                n.setTribe(tribes.get(0));
                tribes.get(0).addNode(n);
            } else {
                // FIGHT
                Tribe tribe1;
                Tribe tribe2;

                //Do all the fighting and looting
                while(tribes.size() > 1) {
                    tribe1 = tribes.get(0);
                    tribe2 = tribes.get(1);
                    if (tribe1.forGloryAndHonour() >= tribe2.forGloryAndHonour()) {
                        //Tribe 1 wins
                        double foodLoot = ((tribe2.getFood() / tribe2.nodeCount()) / 10);
                        double mineralLoot = ((tribe2.getFood() / tribe2.nodeCount()) / 10);
                        double utilityLoot = ((tribe2.getFood() / tribe2.nodeCount()) / 10);

                        tribe1.addBattleResult(foodLoot,mineralLoot,utilityLoot);
                        tribe1.addBattleLog(new BattleLog(generation,tribe2.getColour(),true));
                        tribe2.addBattleLog(new BattleLog(generation,tribe1.getColour(),false));
                        tribe2.addBattleResult((-1 * foodLoot),(-1 * mineralLoot),(-1 * utilityLoot));
                        tribes.remove(tribe2);
                    } else {
                        //Tribe 2 wins
                        double foodLoot = ((tribe1.getFood() / tribe1.nodeCount()) / 10);
                        double mineralLoot = ((tribe1.getFood() / tribe1.nodeCount()) / 10);
                        double utilityLoot = ((tribe1.getFood() / tribe1.nodeCount()) / 10);

                        tribe2.addBattleResult(foodLoot,mineralLoot,utilityLoot);
                        tribe2.addBattleLog(new BattleLog(generation,tribe1.getColour(),true));
                        tribe1.addBattleLog(new BattleLog(generation,tribe2.getColour(),false));
                        tribe1.addBattleResult((-1 * foodLoot),(-1 * mineralLoot),(-1 * utilityLoot));
                        tribes.remove(tribe1);
                    }
                }
                //Remaining tribe wins
                tribes.get(0).addNode(n);
                n.setTribe(tribes.get(0));
            }
        }
    }
    private void init() {

        int count = 10;
        while (count > 0) {
            boolean validAdd = map.addNewTribe(Helpers.randBetween(-1 * (radius),radius),Helpers.randBetween(-1 * (radius),radius));
            if (validAdd) {
                count--;
            }
        }
    }



    @Override
    public void run() {
        //Game inital startup (map already made)
        init();

        while(true) {
            try {
                if(running) {
                   tick();
                    generation++;
                }
                Thread.sleep(1000);
            } catch(InterruptedException e) {
                System.out.println("broke!");
            }
        }
    }
}
