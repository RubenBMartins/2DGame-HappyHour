package com.happyhour;

import javafx.application.Application;
import javafx.stage.Stage;

public class HappyHourApp extends Application {
    private GameScreen curscreen;
    @Override
    public void start(Stage stage) {
        curscreen = new GameScreen();
        curscreen.start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}