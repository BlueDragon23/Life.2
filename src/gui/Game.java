package gui;

import classes.Location;
import classes.Map;
import classes.Node;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Game {
    @FXML
    private Canvas canvas;

    private Location bottomLeft = new Location(-10, -10);
    private Location topRight = new Location(10, 10);
    private int viewSize = 50;

    public void drawMap(Map m) {
        GraphicsContext g = canvas.getGraphicsContext2D();
        for (int x = bottomLeft.getX(); x < topRight.getX(); x++) {
            for (int y = bottomLeft.getY(); y < topRight.getY(); y++) {
                Node n = m.getNode(new Location(x, y));
                if (n == null) {
                    g.setFill(Color.GREY);
                    g.fillRect(x * viewSize, y * viewSize, viewSize, viewSize);
                    continue;
                }
                switch (n.getLandType()) {
                    case PLAINS:
                        g.setFill(Color.GREEN);
                        break;
                    case WATER:
                        g.setFill(Color.BLUE);
                        break;
                    default:
                        g.setFill(Color.RED);
                        break;
                }
                g.fillRect(x * viewSize, y * viewSize, viewSize, viewSize);

            }
        }
    }

}
