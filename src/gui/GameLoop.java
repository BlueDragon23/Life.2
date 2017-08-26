package gui;

import classes.*;

public class GameLoop implements Runnable {
    private int radius;
    private Map map;

    public GameLoop(int radius, Map m) {
        this.radius = radius;
        this.map = m;
    }



    @Override
    public void run() {

        while(true) {
            try {

                Thread.sleep(1000);
                map.addNewTribe(Helpers.randBetween(-50,50),Helpers.randBetween(-50,50));

            } catch(InterruptedException e) {
                System.out.println("broke!");
            }
        }
    }
}
