package gui;

import classes.Location;
import classes.Map;
import classes.Node;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Game {
    @FXML
    private Canvas canvas;

    @FXML
    private Pane pane;

    private Location bottomLeft = new Location(-50, -50);
    private Location topRight = new Location(50, 50);
    private int viewSize = 10;

    public void drawMap(Map m) {
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        // Make sure the bottomLeft is the bottomLeft of the screen
        int xOffset = -(bottomLeft.getX() * viewSize);
        int yOffset = -(bottomLeft.getY() * viewSize);
        for (int x = bottomLeft.getX(); x < topRight.getX(); x++) {
            for (int y = bottomLeft.getY(); y < topRight.getY(); y++) {
                Node n = m.getNode(new Location(x, y));
                if (n == null) {
                    g.setFill(Color.GREY);

                } else {
                    switch (n.getLandType()) {
                        case PLAINS:
                            g.setFill(Color.GREEN);
                            break;
                        case WATER:
                            g.setFill(Color.BLUE);
                            break;
                        case COASTAL:
                            g.setFill(Color.YELLOW);
                            break;
                        default:
                            g.setFill(Color.RED);
                            break;
                    }

                }
                BlendMode bm = g.getGlobalBlendMode();
                g.setGlobalBlendMode(m.getNode(new Location(x,y)).getTribe().getBlendMode());
                g.fillRect(x * viewSize + xOffset, y * viewSize + yOffset, viewSize, viewSize);
                g.setGlobalBlendMode(bm);
            }
        }
    }

    public void setWidthChangeListener(ReadOnlyDoubleProperty prop) {
        prop.addListener(new WidthChangeListener());
    }

    public void setHeightChangeListener(ReadOnlyDoubleProperty prop) {
        prop.addListener(new HeightChangeListener());
    }

    public Pane getRootPane() {
        return pane;
    }

    private class WidthChangeListener implements ChangeListener<Number> {

        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            canvas.setWidth((double) newValue - 30);
        }
    }

    private class HeightChangeListener implements ChangeListener<Number> {

        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            canvas.setHeight((double) newValue);
        }
    }

    @FXML
    protected void zoomIn(ActionEvent event) {
        viewSize += 5;
    }

    @FXML
    protected void zoomOut(ActionEvent event) {
        if (viewSize > 5) {
            viewSize -= 5;
        }
    }

}
