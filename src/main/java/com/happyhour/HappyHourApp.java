package com.happyhour;

import com.happyhour.model.game.GameSession;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class HappyHourApp extends Application {
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: #3b1f5c;");

        Label infoLabel = new Label();
        infoLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px;");
        infoLabel.setLayoutX(20);
        infoLabel.setLayoutY(20);
        root.getChildren().add(infoLabel);

        GameSession session = new GameSession();

        GameLoop gameLoop = new GameLoop(session, infoLabel);
        gameLoop.start();

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                session.togglePause();
            } else if (event.getCode() == KeyCode.E) {
                session.spawnChild();
            } else if (event.getCode() == KeyCode.N) {
                session.skipPhase();
            }
        });

        stage.setTitle("Happy Hour");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}