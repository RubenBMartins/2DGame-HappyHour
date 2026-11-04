package com.happyhour;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class HappyHourApp extends Application {
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: #3b1f5c;");

        Label timeLabel = new Label("Temps : 0 s");
        timeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px;");
        timeLabel.setLayoutX(20);
        timeLabel.setLayoutY(20);
        root.getChildren().add(timeLabel);

        GameLoop gameLoop = new GameLoop(timeLabel);
        gameLoop.start();

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setTitle("Happy Hour");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}