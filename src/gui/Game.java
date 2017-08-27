package gui;

import classes.Location;
import classes.Map;
import classes.Node;
import classes.Tribe;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Game {
    @FXML
    private Canvas resourceCanvas;
    private Canvas tribeCanvas;

    @FXML
    public ScrollPane scroll;

    private Location bottomLeft = new Location(-50, -50);
    private Location topRight = new Location(50, 50);
    private int viewSize = 10;

    public void drawMap(Map m) {
        resourceCanvas.setWidth((m.getMaxX() - m.getMinX()) * viewSize);
        resourceCanvas.setHeight((m.getMaxY() - m.getMinY()) * viewSize);
        tribeCanvas.setHeight(resourceCanvas.getHeight());
        tribeCanvas.setWidth(resourceCanvas.getWidth());
        GraphicsContext resourceLayer = resourceCanvas.getGraphicsContext2D();
        resourceLayer.clearRect(0, 0, resourceCanvas.getWidth(), resourceCanvas.getHeight());
        // Make sure the bottomLeft is the bottomLeft of the screen
        // Add resource objects
        int xOffset = -(bottomLeft.getX() * viewSize);
        int yOffset = -(bottomLeft.getY() * viewSize);
        for (int x = bottomLeft.getX(); x < topRight.getX(); x++) {
            for (int y = bottomLeft.getY(); y < topRight.getY(); y++) {
                Node n = m.getNode(new Location(x, y));
                if (n == null) {
                    resourceLayer.setFill(Color.GREY);
                } else {
                    switch (n.getLandType()) {
                        case PLAINS:
                            resourceLayer.setFill(Color.GREEN);
                            break;
                        case WATER:
                            resourceLayer.setFill(Color.BLUE);
                            break;
                        case COASTAL:
                            resourceLayer.setFill(Color.YELLOW);
                            break;
                        default:
                            resourceLayer.setFill(Color.RED);
                            break;
                    }

                }
                resourceLayer.fillRect(x * viewSize + xOffset, y * viewSize + yOffset, viewSize, viewSize);
            }
        }
        //Tribe Layer
        GraphicsContext tribeLayer = resourceCanvas.getGraphicsContext2D();
        tribeLayer.clearRect(0, 0, resourceCanvas.getWidth(), resourceCanvas.getHeight());
        for (int x = bottomLeft.getX(); x < topRight.getX(); x++) {
            for (int y = bottomLeft.getY(); y < topRight.getY(); y++) {
                Node n = m.getNode(new Location(x, y));
                if (n != null) {
                    if (n.hasTribe()) {
                        //Add a rectangle for the tribe using tribe rec colour
                        Tribe t = (m.getNode(new Location(x,y)).getTribe());
                        tribeLayer.setFill(t.getColour());
                        tribeLayer.fillOval(x * viewSize + xOffset,y * viewSize + yOffset, viewSize, viewSize);
                    }
                }
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

}
