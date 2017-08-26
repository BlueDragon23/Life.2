package gui;

import classes.*;

public class GameLoop implements Runnable {
    private int radius;
    private Map map;

    public GameLoop(int radius, Map m) {
        this.radius = radius;
    }



    @Override
    public void run() {

        while(true) {
            try {

                Thread.sleep(1000);
                map.




            } catch(InterruptedException e) {
                System.out.println("broke!");
            }
        }
    }
}
