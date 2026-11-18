package com.happyhour;

import com.happyhour.model.GameConfig;
import com.happyhour.model.customer.Customer;
import com.happyhour.model.drink.Drink;
import com.happyhour.model.game.GameSession;

import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
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

    private Image orderBubble;
    private Image beerFoam;
    private Image beerGlass;
    private Image beer;
    private Image highballGlass;
    private Image highball;
    private Image martiniGlass;
    private Image martini;
    private Image wineGlass;
    private Image wine;

    private WritableImage ginTonic;
    private WritableImage tequilaTonic;
    private WritableImage pinkCapeCod;
    private WritableImage margarita;
    private WritableImage cloverClub;
    private WritableImage cosmopolitan;
    
    private GraphicsContext batch;
    private GraphicsContext drinkBatch;
    private GraphicsContext handsBatch;
    private GraphicsContext kickBatch;
    private Canvas handsCanvas;
    private Canvas drinksCanvas;
    private Canvas kickCanvas;
    private Player melvin;
    private Image arm;
    private Image kick;
    private GameSession session;
    private HappyHourApp app;
    private Stage stage;
    private Pane root;
    private GameLoop gameLoop;
    private Label timeLabel;
    private Label moneyLabel;
    private Label lostLabel;
    private PauseTransition delay;

    public GameScreen(HappyHourApp app) {
        super(WIDTH, HEIGHT);
        this.app = app;
        table = new Image(getClass().getResourceAsStream("/images/table.png"),WIDTH,540,true, false);
        seats = new Image(getClass().getResourceAsStream("/images/seats.png"),1360, 120, true, false);
        bg = new Image(getClass().getResourceAsStream("/images/bg.png"),WIDTH, HEIGHT, true, false);
        kick = new Image(getClass().getResourceAsStream("/images/kick.png"),200, 200, true, false);
        
        orderBubble = new Image(getClass().getResourceAsStream("/images/orderBubble.png"),180, 260, true, false);
        beerFoam = new Image(getClass().getResourceAsStream("/images/beerFoam.png"),100, 150, true, false);
        beerGlass = new Image(getClass().getResourceAsStream("/images/beerGlass.png"),100, 150, true, false);
        beer = new Image(getClass().getResourceAsStream("/images/beer.png"),100, 150, true, false);
        highballGlass = new Image(getClass().getResourceAsStream("/images/highballGlass.png"),60, 150, true, false);
        highball = new Image(getClass().getResourceAsStream("/images/highball.png"),60, 150, true, false);
        martiniGlass = new Image(getClass().getResourceAsStream("/images/martiniGlass.png"),90, 150, true, false);
        martini = new Image(getClass().getResourceAsStream("/images/martini.png"),90, 150, true, false);
        wineGlass = new Image(getClass().getResourceAsStream("/images/wineGlass.png"),70, 150, true, false);
        wine = new Image(getClass().getResourceAsStream("/images/wine.png"),70, 150, true, false);

        ginTonic = tintImage(highball, Color.web("#00c7e6"));
        tequilaTonic = tintImage(highball, Color.web("#ff9100"));
        pinkCapeCod = tintImage(highball, Color.web("#FF7EB9"));
        margarita = tintImage(martini, Color.web("#9BE15D"));
        cloverClub = tintImage(martini, Color.web("#cd07ff"));
        cosmopolitan = tintImage(martini, Color.web("#D7263D"));

        batch = getGraphicsContext2D();
        handsCanvas = new Canvas(WIDTH, HEIGHT);
        handsBatch = handsCanvas.getGraphicsContext2D();
        drinksCanvas = new Canvas(WIDTH, HEIGHT);
        drinkBatch = drinksCanvas.getGraphicsContext2D();
        kickCanvas = new Canvas(WIDTH, HEIGHT);
        kickBatch = kickCanvas.getGraphicsContext2D();
        melvin = new Player();
        arm = melvin.getArm();
        handsCanvas.setMouseTransparent(true);
        drinksCanvas.setMouseTransparent(true);
        kickCanvas.setMouseTransparent(true);
        delay = new PauseTransition(Duration.seconds(0.5));
        
    }
    private void renderCustomers() {
        for (int i = 0; i < 6; i++) {
            Customer customer = session.getCustomer(i);

            if (customer != null) {
                double x = 310 + (i * 240);
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

    private Label createHudLabel(Pos position) {
        Label label = new Label();
        label.setStyle("-fx-text-fill: #ffd9ec;"
                + "-fx-font-family: 'Monospaced';"
                + "-fx-font-weight: bold;"
                + "-fx-font-size: 26px;");
        label.setPrefWidth((WIDTH - 60) / 3.0);
        label.setAlignment(position);
        return label;
    }

    private void updateHud() {
        double remaining = GameConfig.EVENING_END - session.getElapsed();
        if (remaining < 0) {
            remaining = 0;
        }
        int minutes = (int) remaining / 60;
        int seconds = (int) remaining % 60;

        timeLabel.setText("TEMPS " + String.format("%d:%02d", minutes, seconds));
        moneyLabel.setText("CAISSE " + String.format("%.0f €", session.getRegister().getTotal()));
        lostLabel.setText("CLIENTS PERDUS " + session.getLostCustomers() + "/" + GameConfig.MAX_LOST);
    }

    public void kickBoom(double x, double y) {
        kickBatch.drawImage(kick, x, y, 200, 200);
        delay.setOnFinished(event -> {
            kickBatch.clearRect(x, y, 200, 200);
        });
        delay.playFromStart();
    }
    public void start(Stage stage) {
        this.stage = stage;
        Pane gamePane = new Pane();
        gamePane.setPrefSize(WIDTH, HEIGHT);
        gamePane.setMinSize(WIDTH, HEIGHT);
        root = new Pane();
        root.setStyle("-fx-background-color: #3b1f5c;");

        timeLabel = createHudLabel(Pos.CENTER_LEFT);
        moneyLabel = createHudLabel(Pos.CENTER);
        lostLabel = createHudLabel(Pos.CENTER_RIGHT);

        HBox hud = new HBox(timeLabel, moneyLabel, lostLabel);
        hud.setPrefWidth(WIDTH);
        hud.setStyle("-fx-background-color: rgba(0, 0, 0, 0.35); -fx-padding: 12 30 12 30;");

        Rectangle seat1box = new Rectangle(310, 390, 160, 290);
        Rectangle seat2box = new Rectangle(550, 390, 160, 290);
        Rectangle seat3box = new Rectangle(790, 390, 160, 290);
        Rectangle seat4box = new Rectangle(1030, 390, 160, 290);
        Rectangle seat5box = new Rectangle(1270, 390, 160, 290);
        Rectangle seat6box = new Rectangle(1510, 390, 160, 290);
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
        gamePane.getChildren().add(drinksCanvas);
        gamePane.getChildren().add(kickCanvas);
        gamePane.getChildren().add(handsCanvas);
        gamePane.getChildren().add(hud);
        root.getChildren().add(gamePane);
        root.widthProperty().addListener((obs, oldVal, newVal) -> resizeGame(gamePane, root));
        root.heightProperty().addListener((obs, oldVal, newVal) -> resizeGame(gamePane, root));
        session = new GameSession();

        gameLoop = new GameLoop(session, this);
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
                kickBoom(310,390);
        });
        seat2box.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY)
                session.kick(1);
                kickBoom(510,390);
        });
        seat3box.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY)
                session.kick(2);
                kickBoom(750,390);
        });
        seat4box.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY)
                session.kick(3);
                kickBoom(990,390);
        });
        seat5box.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY)
                session.kick(4);
                kickBoom(1230,390);
        });
        seat6box.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY)
                session.kick(5);
                kickBoom(1470,390);
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

    private WritableImage tintImage(Image source, Color tint) {
        int width = (int) source.getWidth();
        int height = (int) source.getHeight();
        PixelReader reader = source.getPixelReader();
        WritableImage result = new WritableImage(width, height);
        PixelWriter writer = result.getPixelWriter();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);
                double brightness = color.getBrightness();
                Color tintedColor = Color.color(tint.getRed() * brightness, tint.getGreen() * brightness, tint.getBlue() * brightness, color.getOpacity());
                writer.setColor(x, y, tintedColor);
            }
        }
        return result;
    }
    public void renderOrders() {
        drinkBatch.clearRect(0, 0, WIDTH, HEIGHT);
        for (int seat = 0; seat < GameConfig.SEATS; seat++) {
            Customer customer = session.getCustomer(seat);
            if (customer != null) {
                Drink order = customer.getOrder();
                if (order != null) {
                    drinkBatch.drawImage(orderBubble, 310 - 10 + seat*240, 170, 180, 260);
                    switch (order.getGlass()) {
                        case PINT:
                            drinkBatch.drawImage(beer, 310 + 30 + seat*240, 190, 100, 150);
                            drinkBatch.drawImage(beerGlass, 310 + 30 + seat*240, 190, 100, 150);
                            drinkBatch.drawImage(beerFoam, 310 + 30 + seat*240, 190, 100, 150);
                            break;
                        case WINE_GLASS:
                            drinkBatch.drawImage(wine, 310 + 40 + seat*240, 190, 80, 150);
                            drinkBatch.drawImage(wineGlass, 310 + 40 + seat*240, 190, 80, 150);
                            break;
                        case HIGHBALL:
                            switch (order.getName()) {
                                case "Gin Tonic":
                                    drinkBatch.drawImage(ginTonic, 310 + 50 + seat*240, 190, 60, 150);
                                    break;
                                case "Tequila Tonic":
                                    drinkBatch.drawImage(tequilaTonic, 310 + 50 + seat*240, 190, 60, 150);
                                    break;
                                case "Pink Cape Cod":
                                    drinkBatch.drawImage(pinkCapeCod, 310 + 50 + seat*240, 190, 60, 150);
                                    break;
                            }
                            drinkBatch.drawImage(highballGlass, 310 + 50 + seat*240, 190, 60, 150);
                            break;
                        case MARTINI:  
                            switch (order.getName()) {
                                case "Margarita":
                                    drinkBatch.drawImage(margarita, 310 + 30 + seat*240, 190, 100, 150);
                                    break;
                                case "Clover Club":
                                    drinkBatch.drawImage(cloverClub, 310 + 30 + seat*240, 190, 100, 150);
                                    break;
                                case "Cosmopolitan":
                                    drinkBatch.drawImage(cosmopolitan, 310 + 30 + seat*240, 190, 100, 150);
                                    break;
                            }
                            drinkBatch.drawImage(martiniGlass, 310 + 30 + seat*240, 190, 100, 150);
                            break;
                    }
                }
            }
        }
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
        updateHud();
        batch.drawImage(bg, 0, 0, WIDTH, HEIGHT);
        batch.drawImage(seats, 310, 590, 1360, 120);
        renderCustomers();
        batch.drawImage(table, 0, 540, WIDTH, 540);
        renderOrders();

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