package gui;

import classes.Map;
import classes.Node;
import classes.QuadtreeMap;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
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

        Scene scene = new Scene(root, 300, 275);
        primaryStage.setTitle("Life.2");
        primaryStage.setScene(scene);

        Map m = new QuadtreeMap();
        m.addNode(new Node(0, 0, Node.LandType.PLAINS));
        m.addNode(new Node(0, 1, Node.LandType.PLAINS));
        m.addNode(new Node(1, 1, Node.LandType.WATER));
        m.addNode(new Node(1, 0, Node.LandType.PLAINS));

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                controller.drawMap(m);
            }
        }.start();

        primaryStage.show();
    }
}
