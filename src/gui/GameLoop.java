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

        //Game inital startup (map already made)
        int count = 10;
        while (count > 0) {
            boolean validAdd = map.addNewTribe(Helpers.randBetween(-50,50),Helpers.randBetween(-50,50));
            if (validAdd) {
                count--;
            }
        }



        while(true) {
            try {






                Thread.sleep(1000);

            } catch(InterruptedException e) {
                System.out.println("broke!");
            }
        }
    }
}
