package gui;

import classes.Location;
import classes.Map;
import classes.QuadtreeMap;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
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

        Scene scene = new Scene(root, 600, 600);

        primaryStage.setTitle("Life.2");
        primaryStage.setScene(scene);

        GraphicsContext tribeG = controller.tribeCanvas.getGraphicsContext2D();

        controller.tribeCanvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Display a window with more details

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

        Map m = new QuadtreeMap();
        m.initMap();

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
