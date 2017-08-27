package gui;

import classes.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        URL location = getClass().getResource("Game.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);

        Parent root = fxmlLoader.load(location.openStream());
        Game controller = fxmlLoader.getController();

        Scene scene = new Scene(root, 1200, 1200);

        primaryStage.setTitle("Life.2");
        primaryStage.setScene(scene);

        Map m = new QuadtreeMap();
        m.initMap();

        GraphicsContext tribeG = controller.tribeCanvas.getGraphicsContext2D();

        controller.tribeCanvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Display a window with more details
                Location l = controller.getLocationForScreen(event.getX(), event.getY());
                Node n = m.getNode(l);
                if (n.hasTribe()) {
                    Popup popup = new Popup();
                    popup.setX(controller.resourceCanvas.getWidth() - 50);
                    popup.setY(controller.resourceCanvas.getTranslateY());
                    GridPane gp = new GridPane();
                    gp.setStyle("-fx-border-color: black");
                    gp.setStyle("-fx-background-color: white");
                    popup.getContent().addAll(gp);
                    gp.setHgap(10);
                    gp.setVgap(10);
                    gp.addColumn(0, new Text("Colour"), new Text("Type"), new Text("Exploration"), new Text("Agriculture"),
                            new Text("Military"), new Text("Battles Won"), new Text("Food"), new Text("Minerals"), new Text("Utility"));
                    Tribe t = n.getTribe();
                    gp.addColumn(1, new Text(t.getColour().toString()), new Text(t.getType()), new Text(Integer.toString(t.explorationSpeed)),
                            new Text(Integer.toString(t.agriculturalKnowledge)), new Text(Integer.toString(t.militryPower)),
                            new Text(Integer.toString(t.battles)),new Text(Integer.toString((int)t.getFood())),
                            new Text(Integer.toString((int)t.getMinerals())),new Text(Integer.toString((int)t.getUtility())));
                    Button b = new Button("Close");
                    b.setDefaultButton(true);
                    b.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            popup.hide();
                        }
                    });
                    gp.addRow(6, b);
                    popup.show(primaryStage);
                }
            }
        });

        controller.tribeCanvas.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Highlight the square
                GraphicsContext g = controller.resourceCanvas.getGraphicsContext2D();
                g.setFill(Color.WHITE);
                Location l = controller.getLocationForScreen(event.getX(), event.getY());
                g.fillRect(l.getX(), l.getY(), controller.viewSize, controller.viewSize);
                controller.selected = l;
            }
        });

        controller.scroll.viewportBoundsProperty().addListener((ChangeListener<Bounds>) (observable, oldValue, newValue) -> controller.showBounds( controller.scroll));
        controller.scroll.hvalueProperty().addListener((ChangeListener<Number>) (observable, oldValue, newValue) -> controller.showBounds( controller.scroll));
        controller.scroll.vvalueProperty().addListener((ChangeListener<Number>) (observable, oldValue, newValue) -> controller.showBounds( controller.scroll));


        new AnimationTimer() {
            @Override
            public void handle(long now) {
                controller.drawMap(m);
            }

        }.start();

        //Start game loop
        Executor exe = Executors.newSingleThreadExecutor();
        exe.execute(new GameLoop(m.getInitialRadius(),m));

        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if(event.equals(WindowEvent.WINDOW_CLOSE_REQUEST)) {
                    //Kill?
                }
            }
        });
    }
}
