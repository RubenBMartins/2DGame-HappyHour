package com.happyhour;

import com.happyhour.model.customer.Customer;
import com.happyhour.model.game.GameSession;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.transform.Scale;

public class GameScreen extends Canvas{
    public static final int WIDTH = 1980;
    public static final int HEIGHT = 1080;
    private Image table;
    private Image seats;
    private Image bg;
    private GraphicsContext batch;
    private GraphicsContext handsBatch;
    private Canvas handsCanvas;
    private Player melvin;
    private Image arm;
    private GameSession session;
    private HappyHourApp app;
    private Stage stage;
    private Pane root;
    private GameLoop gameLoop;

    public GameScreen(HappyHourApp app) {
        super(WIDTH, HEIGHT);
        this.app = app;
        table = new Image(getClass().getResourceAsStream("/images/table.png"),WIDTH,540,true, false);
        seats = new Image(getClass().getResourceAsStream("/images/seats.png"),1360, 120, true, false);
        bg = new Image(getClass().getResourceAsStream("/images/bg.png"),WIDTH, HEIGHT, true, false);
        batch = getGraphicsContext2D();
        handsCanvas = new Canvas(WIDTH, HEIGHT);
        handsBatch = handsCanvas.getGraphicsContext2D();
        melvin = new Player();
        arm = melvin.getArm();
        handsCanvas.setMouseTransparent(true);
        
    }
    private void renderCustomers() {
        for (int i = 0; i < 6; i++) {
            Customer customer = session.getCustomer(i);

            if (customer != null) {
                double x = 280 + (i * 240);
                double y = 390;

                batch.drawImage(customer.getSprite(), x, y, 160, 350);
            }
        }
    }
    private void resizeGame(Pane gamePane, Pane root) {
        double scaleX = root.getWidth() / WIDTH;
        double scaleY = root.getHeight() / HEIGHT;

        double scale = Math.min(scaleX, scaleY);

        double x = (root.getWidth() - WIDTH * scale) / 2;
        double y = (root.getHeight() - HEIGHT * scale) / 2;

        Scale transformation = new Scale(scale, scale, 0, 0);

        gamePane.getTransforms().setAll(transformation);

        gamePane.setLayoutX(x);
        gamePane.setLayoutY(y);
    }
    public void start(Stage stage) {
        this.stage = stage;
        Pane gamePane = new Pane();
        gamePane.setPrefSize(WIDTH, HEIGHT);
        gamePane.setMinSize(WIDTH, HEIGHT);
        root = new Pane();
        root.setStyle("-fx-background-color: #3b1f5c;");

        Label infoLabel = new Label();
        infoLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px;");
        infoLabel.setLayoutX(20);
        infoLabel.setLayoutY(20);
        Rectangle seat1box = new Rectangle(280, 390, 160, 290);
        Rectangle seat2box = new Rectangle(520, 390, 160, 290);
        Rectangle seat3box = new Rectangle(760, 390, 160, 290);
        Rectangle seat4box = new Rectangle(1000, 390, 160, 290);
        Rectangle seat5box = new Rectangle(1240, 390, 160, 290);
        Rectangle seat6box = new Rectangle(1480, 390, 160, 290);
        seat1box.setFill(Color.web("0xFF0000",0));
        seat2box.setFill(Color.web("0xFF9900",0));
        seat3box.setFill(Color.web("0xFFFF00",0));
        seat4box.setFill(Color.web("0x09FF00",0));
        seat5box.setFill(Color.web("0x0051FF",0));
        seat6box.setFill(Color.web("0xAE00FF",0));

        gamePane.getChildren().add(this);
        gamePane.getChildren().add(seat1box);
        gamePane.getChildren().add(seat2box);
        gamePane.getChildren().add(seat3box);
        gamePane.getChildren().add(seat4box);
        gamePane.getChildren().add(seat5box);
        gamePane.getChildren().add(seat6box);
        gamePane.getChildren().add(handsCanvas);
        gamePane.getChildren().add(infoLabel);
        root.getChildren().add(gamePane);
        root.widthProperty().addListener((obs, oldVal, newVal) -> resizeGame(gamePane, root));
        root.heightProperty().addListener((obs, oldVal, newVal) -> resizeGame(gamePane, root));
        session = new GameSession();

        gameLoop = new GameLoop(session, infoLabel, this);
        gameLoop.start();

        Scene scene = stage.getScene();
        scene.setRoot(root);
        resizeGame(gamePane, root);
        root.setOnMouseMoved(event -> {
            var point = gamePane.sceneToLocal(event.getSceneX(), event.getSceneY());
            melvin.updateMouse(point.getX(), point.getY());
        });

        root.setOnMouseDragged(event -> {
            var point = gamePane.sceneToLocal(event.getSceneX(), event.getSceneY());
            melvin.updateMouse(point.getX(), point.getY());
        });

        seat1box.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY)
                session.kick(0);
        });
        seat2box.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY)
                session.kick(1);
        });
        seat3box.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY)
                session.kick(2);
        });
        seat4box.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY)
                session.kick(3);
        });
        seat5box.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY)
                session.kick(4);
        });
        seat6box.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY)
                session.kick(5);
        });
        installKeys(scene);
    }

    private void installKeys(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                pause();
            } else if (event.getCode() == KeyCode.E) {
                session.spawnChild();
            } else if (event.getCode() == KeyCode.N) {
                session.skipPhase();
            } else if (event.getCode() == KeyCode.O) {
                session.spawnCustomer();
            }
        });
    }

    public void pause() {
        if (session.isOver()) {
            return;
        }
        if (!session.isPaused()) {
            session.togglePause();
        }
        new PauseScreen(app, this).start(stage);
    }

    public void resume() {
        stage.getScene().setRoot(root);
        installKeys(stage.getScene());
        if (session.isPaused()) {
            session.togglePause();
        }
    }

    public void restart() {
        gameLoop.stop();
        app.showGame();
    }

    public void quit() {
        gameLoop.stop();
        Platform.exit();
    }

    public void endGame(GameSession session) {
        app.showSummary(session);
    }

    public void render() {
        batch.drawImage(bg, 0, 0, WIDTH, HEIGHT);
        batch.drawImage(seats, 280, 590, 1360, 120);
        renderCustomers();
        batch.drawImage(table, 0, 540, WIDTH, 540);

        double hand1X = melvin.getX() - 192;
        double hand1Y = melvin.getY();
        double dx = melvin.getX()-990;
        double dy = melvin.getY()-1280;
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