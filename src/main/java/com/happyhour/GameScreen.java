package com.happyhour;

import com.happyhour.model.game.GameSession;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

public class GameScreen extends Canvas{
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    private Image table;
    private Image seats;
    private Image bg;
    private GraphicsContext batch;
    public GameScreen() {
        super(WIDTH, HEIGHT); /* Before anything else in the constructor, because otherwise it just doesn't work */
        table = new Image("table.png");
        seats = new Image("seats.png");
        bg = new Image("bg.png");
        batch = getGraphicsContext2D();
    }
    public void start(Stage stage) {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: #3b1f5c;");

        Label infoLabel = new Label();
        infoLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px;");
        infoLabel.setLayoutX(20);
        infoLabel.setLayoutY(20);
        root.getChildren().add(this);
        root.getChildren().add(infoLabel);

        GameSession session = new GameSession();

        GameLoop gameLoop = new GameLoop(session, infoLabel);
        gameLoop.start();

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setTitle("Happy Hour");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    public void render() {
        batch.drawImage(bg, 0, 0, WIDTH, HEIGHT);
    }
}
