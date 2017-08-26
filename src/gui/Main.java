package gui;

import classes.Map;
import classes.Node;
import classes.QuadtreeMap;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

        primaryStage.show();

        Executor exe = Executors.newCachedThreadPool();
        exe.execute(new GameLoop(m.getInitialRadius()));

    }
}
