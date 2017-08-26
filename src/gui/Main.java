package gui;

import classes.Map;
import classes.Node;
import classes.QuadtreeMap;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

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

        Scene scene = new Scene(root, 300, 300);
        controller.setHeightChangeListener(controller.getRootPane().heightProperty());
        controller.setWidthChangeListener(controller.getRootPane().widthProperty());

        primaryStage.setTitle("Life.2");
        primaryStage.setScene(scene);

        Map m = new QuadtreeMap();
        m.initMap();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                controller.drawMap(m);
            }
        }.start();

        primaryStage.show();
    }
}
