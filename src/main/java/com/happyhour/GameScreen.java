
package com.happyhour;

import com.happyhour.model.game.GameSession;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class GameScreen extends Canvas{
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    private Image table;
    private Image seats;
    private Image bg;
    private GraphicsContext batch;
    private GraphicsContext handsBatch;
    private Canvas handsCanvas;
    private Player melvin;
    private Image arm;

    public GameScreen() {
        super(WIDTH, HEIGHT);
        table = new Image(getClass().getResourceAsStream("/images/table.png"),WIDTH,400,true, false);
        seats = new Image(getClass().getResourceAsStream("/images/seats.png"),960, 160, true, false);
        bg = new Image(getClass().getResourceAsStream("/images/bg.png"),WIDTH, HEIGHT, true, false);
        batch = getGraphicsContext2D();
        handsCanvas = new Canvas(WIDTH, HEIGHT);
        handsBatch = handsCanvas.getGraphicsContext2D();
        melvin = new Player();
        arm = melvin.getArm();
        handsCanvas.setMouseTransparent(true);
    }

    public void start(Stage stage) {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: #3b1f5c;");

        Label infoLabel = new Label();
        infoLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px;");
        infoLabel.setLayoutX(20);
        infoLabel.setLayoutY(20);
        Rectangle seat1box = new Rectangle(160, 70, 160, 290);
        Rectangle seat2box = new Rectangle(320, 70, 160, 290);
        Rectangle seat3box = new Rectangle(480, 70, 160, 290);
        Rectangle seat4box = new Rectangle(640, 70, 160, 290);
        Rectangle seat5box = new Rectangle(800, 70, 160, 290);
        Rectangle seat6box = new Rectangle(960, 70, 160, 290);
        seat1box.setFill(Color.web("0xFF0000",0.5));
        seat2box.setFill(Color.web("0xFF9900",0.5));
        seat3box.setFill(Color.web("0xFFFF00",0.5));
        seat4box.setFill(Color.web("0x09FF00",0.5));
        seat5box.setFill(Color.web("0x0051FF",0.5));
        seat6box.setFill(Color.web("0xAE00FF",0.5));

        root.getChildren().add(this);
        root.getChildren().add(seat1box);
        root.getChildren().add(seat2box);
        root.getChildren().add(seat3box);
        root.getChildren().add(seat4box);
        root.getChildren().add(seat5box);
        root.getChildren().add(seat6box);
        root.getChildren().add(handsCanvas);
        root.getChildren().add(infoLabel);

        GameSession session = new GameSession();

        GameLoop gameLoop = new GameLoop(session, infoLabel, this);
        gameLoop.start();

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setTitle("Happy Hour");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        root.setOnMouseMoved(event -> {
            melvin.updateMouse(event.getX(), event.getY());
        });
    }

    public void render() {
        batch.drawImage(bg, 0, 0, WIDTH, HEIGHT);
        batch.drawImage(seats, 160, 260, 960, 160);
        batch.drawImage(table, 0, 360, WIDTH, 400);

        double hand1X = melvin.getX() - 192;
        double hand1Y = melvin.getY();
        double dx = melvin.getX()-640;
        double dy = melvin.getY()-920;
        double angle = (Math.atan2(dy, dx)) * (180 / Math.PI);
        double hand2X = melvin.getX() + 192;
        double hand2Y = melvin.getY();

        handsBatch.clearRect(0, 0, WIDTH, HEIGHT);

        handsBatch.save();
        handsBatch.translate(hand1X, hand1Y);
        handsBatch.rotate(angle);
        handsBatch.drawImage(arm, -2390, -170, 2560, 320);
        handsBatch.restore();

        handsBatch.save();
        handsBatch.translate(hand2X, hand2Y);
        handsBatch.rotate(angle);
        handsBatch.drawImage(arm, -2390, 170, 2560, -320);
        handsBatch.restore();
    }
}
