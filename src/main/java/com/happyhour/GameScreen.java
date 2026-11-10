package com.happyhour;

import com.happyhour.model.game.GameSession;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

public class GameScreen extends Canvas{
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    private Image table;
    private Image seats;
    private Image bg;
    private GraphicsContext batch;
    private double mouseX;
    private double mouseY;
    private Player melvin;
    private Image arm;
    public GameScreen() {
        super(WIDTH, HEIGHT); /* Before anything else in the constructor, because otherwise it just doesn't work */
        table = new Image(getClass().getResourceAsStream("/images/table.png"),WIDTH, HEIGHT, true, false);
        seats = new Image(getClass().getResourceAsStream("/images/seats.png"),960, 160, true, false);
        bg = new Image(getClass().getResourceAsStream("/images/bg.png"),WIDTH, 400, true, false);
        batch = getGraphicsContext2D();
        melvin = new Player();
        arm = melvin.getArm();
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

        GameLoop gameLoop = new GameLoop(session, infoLabel, this);
        gameLoop.start();

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setTitle("Happy Hour");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        setOnMouseMoved(event -> {
            melvin.updateMouse(event.getX(), event.getY());
        });
    }
    public void render() {
        batch.drawImage(bg, 0, 0, WIDTH, HEIGHT);
        batch.drawImage(seats, 160, 220, 960, 160);
        batch.drawImage(table, 0, 320, WIDTH, 400);
        double hand1X = melvin.getX() - 192;
        double hand1Y = melvin.getY();
        batch.save();
        batch.translate(hand1X, hand1Y);
        batch.drawImage(arm, -140, -160, 320, 2560);
        batch.restore();
    }
}
