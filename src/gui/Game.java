package gui;

import classes.Location;
import classes.Map;
import classes.Node;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;

public class Game {
    @FXML
    public Canvas resourceCanvas;

    @FXML
    public Canvas tribeCanvas;

    @FXML
    public ScrollPane scroll;

    private Location bottomLeft = new Location(-50, -50);
    private Location topRight = new Location(50, 50);
    int viewSize = 10;
    Location selected;

    public int getXOffset() {
        return -(bottomLeft.getX() * viewSize);
    }

    public int getYOffset() {
        return -(bottomLeft.getY() * viewSize);
    }

    public void drawMap(Map m) {
        resourceCanvas.setWidth((m.getMaxX() - m.getMinX()) * viewSize);
        resourceCanvas.setHeight((m.getMaxY() - m.getMinY()) * viewSize);
        tribeCanvas.setWidth((m.getMaxX() - m.getMinX()) * viewSize);
        tribeCanvas.setHeight((m.getMaxY() - m.getMinY()) * viewSize);
        GraphicsContext g = resourceCanvas.getGraphicsContext2D();
        g.clearRect(0, 0, resourceCanvas.getWidth(), resourceCanvas.getHeight());
        // Make sure the bottomLeft is the bottomLeft of the screen
        int xOffset = getXOffset();
        int yOffset = getYOffset();
        for (int x = bottomLeft.getX(); x < topRight.getX(); x++) {
            for (int y = bottomLeft.getY(); y < topRight.getY(); y++) {
                Location l = new Location(x, y);
                if (l.equals(selected)) {
                    continue;
                }
                Node n = m.getNode(l);
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
                g.fillRect(x * viewSize + xOffset, y * viewSize + yOffset, viewSize, viewSize);
                /*
                Tribe t = (m.getNode(new Location(x,y)).getTribe());
                BlendMode bm;
                if(t == null) {
                    bm = g.getGlobalBlendMode();
                } else {
                    bm = t.getBlendMode();
                }
                g.setGlobalBlendMode(bm);
                g.setGlobalBlendMode(bm);
                */
            }
        }
    }

    private class WidthChangeListener implements ChangeListener<Number> {

        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            resourceCanvas.setWidth((double) newValue - 30);
        }
    }

    private class HeightChangeListener implements ChangeListener<Number> {

        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            resourceCanvas.setHeight((double) newValue);
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

    // https://stackoverflow.com/a/35331595
    public void showBounds(ScrollPane scrollPane) {

        double hValue = scrollPane.getHvalue();
        double vValue = scrollPane.getVvalue();
        double width = scrollPane.viewportBoundsProperty().get().getWidth();
        double height = scrollPane.viewportBoundsProperty().get().getHeight();

        double x = (scrollPane.getContent().getBoundsInParent().getWidth() - width) * hValue;
        double y = (scrollPane.getContent().getBoundsInParent().getHeight() - height) * vValue;

    }

    Location getLocationForScreen(double x, double y) {
        double xRound = x - x % viewSize;
        double yRound = y - y % viewSize;
        return new Location((int) (xRound - getXOffset()) / viewSize, (int) (yRound - getYOffset()) / viewSize);
    }

}
