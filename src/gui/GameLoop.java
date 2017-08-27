package gui;

import classes.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameLoop implements Runnable {
    private int radius;
    private Map map;
    public boolean running = true;

    public GameLoop(int radius, Map m) {
        this.radius = radius;
        this.map = m;
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
        for (Node node : expansionNodes.keySet()) {
            List<Tribe> tribes = expansionNodes.get(node);
            if (tribes.size() == 1) {
                node.setTribe(tribes.get(0));
                tribes.get(0).addNode(node);
            } else {
                // FIGHT
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
                }
                Thread.sleep(100);
            } catch(InterruptedException e) {
                System.out.println("broke!");
            }
        }
    }
}
