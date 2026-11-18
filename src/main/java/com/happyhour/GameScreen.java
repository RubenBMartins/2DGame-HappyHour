package com.happyhour;

import java.util.ArrayList;

import com.happyhour.model.GameConfig;
import com.happyhour.model.customer.Customer;
import com.happyhour.model.drink.Drink;
import com.happyhour.model.drink.Glass;
import com.happyhour.model.drink.Ingredient;
import com.happyhour.model.drink.Preparation;
import com.happyhour.model.drink.Technique;
import com.happyhour.model.game.GameSession;

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
    private Canvas handsCanvas;
    private Canvas drinksCanvas;
    private Player melvin;
    private Image arm;
    private GameSession session;
    private HappyHourApp app;
    private Stage stage;
    private Pane root;
    private GameLoop gameLoop;
    private Label timeLabel;
    private Label moneyLabel;
    private Label lostLabel;
    private Label prepLabel;
    private Label feedbackLabel;

    public GameScreen(HappyHourApp app) {
        super(WIDTH, HEIGHT);
        this.app = app;
        table = new Image(getClass().getResourceAsStream("/images/table.png"),WIDTH,540,true, false);
        seats = new Image(getClass().getResourceAsStream("/images/seats.png"),1360, 120, true, false);
        bg = new Image(getClass().getResourceAsStream("/images/bg.png"),WIDTH, HEIGHT, true, false);
        
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
        melvin = new Player();
        arm = melvin.getArm();
        handsCanvas.setMouseTransparent(true);
        drinksCanvas.setMouseTransparent(true);
        
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

        Preparation preparation = session.getPreparation();
        ArrayList<String> parts = new ArrayList<>();
        if (preparation.getGlass() != null) {
            parts.add(preparation.getGlass().toString());
        }
        for (Ingredient ingredient : preparation.getIngredients()) {
            parts.add(ingredient.toString());
        }
        if (preparation.getTechnique() != Technique.NONE) {
            parts.add(preparation.getTechnique().toString());
        }

        if (parts.isEmpty()) {
            prepLabel.setText("EN MAIN : RIEN");
        } else {
            prepLabel.setText("EN MAIN : " + String.join(" + ", parts));
        }

        String feedback = session.getFeedback();
        if (feedback == null) {
            feedbackLabel.setText("");
        } else {
            String color = "#ffffff";
            if (session.getFeedbackGrade() == 10) {
                color = "#ff9900";
            } else if (session.getFeedbackGrade() < 5) {
                color = "#ff4d4d";
            }
            feedbackLabel.setStyle("-fx-text-fill: " + color + ";"
                    + "-fx-font-family: 'Monospaced';"
                    + "-fx-font-weight: bold;"
                    + "-fx-font-size: 48px;");
            feedbackLabel.setText(feedback);
        }
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

        prepLabel = createHudLabel(Pos.CENTER);
        prepLabel.setPrefWidth(WIDTH);
        prepLabel.setLayoutY(70);
        prepLabel.setMouseTransparent(true);

        feedbackLabel = new Label();
        feedbackLabel.setPrefWidth(WIDTH);
        feedbackLabel.setAlignment(Pos.CENTER);
        feedbackLabel.setLayoutY(140);
        feedbackLabel.setMouseTransparent(true);

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
        createStations(gamePane);
        gamePane.getChildren().add(drinksCanvas);
        gamePane.getChildren().add(handsCanvas);
        gamePane.getChildren().add(hud);
        gamePane.getChildren().add(prepLabel);
        gamePane.getChildren().add(feedbackLabel);
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

        setupSeat(seat1box, 0);
        setupSeat(seat2box, 1);
        setupSeat(seat3box, 2);
        setupSeat(seat4box, 3);
        setupSeat(seat5box, 4);
        setupSeat(seat6box, 5);
        installKeys(scene);
    }

    private void setupSeat(Rectangle box, int seat) {
        box.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                session.serve(seat);
            } else if (event.getButton() == MouseButton.SECONDARY) {
                session.kick(seat);
            }
        });
    }

    private Rectangle createZone(Pane pane, double x, double y, double width, double height) {
        Rectangle zone = new Rectangle(x, y, width, height);
        zone.setFill(Color.web("0xFFFFFF", 0));
        pane.getChildren().add(zone);
        return zone;
    }

    private void addGlassZone(Pane pane, double x, double y, double width, double height, Glass glass) {
        Rectangle zone = createZone(pane, x, y, width, height);
        zone.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                session.chooseGlass(glass);
            }
        });
    }

    private void addIngredientZone(Pane pane, double x, double y, double width, double height, Ingredient ingredient) {
        Rectangle zone = createZone(pane, x, y, width, height);
        zone.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                session.addIngredient(ingredient);
            }
        });
    }

    private void addTechniqueZone(Pane pane, double x, double y, double width, double height, Technique technique) {
        Rectangle zone = createZone(pane, x, y, width, height);
        zone.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                session.useTechnique(technique);
            }
        });
    }

    private void createStations(Pane pane) {
        addGlassZone(pane, 73, 945, 82, 120, Glass.PINT);
        addGlassZone(pane, 175, 915, 92, 150, Glass.MARTINI);
        addGlassZone(pane, 278, 915, 62, 150, Glass.HIGHBALL);
        addGlassZone(pane, 350, 915, 72, 150, Glass.WINE_GLASS);

        addIngredientZone(pane, 527, 885, 80, 180, Ingredient.WHITE_WINE);
        addIngredientZone(pane, 642, 885, 80, 180, Ingredient.VODKA);
        addIngredientZone(pane, 761, 885, 80, 180, Ingredient.GIN);
        addIngredientZone(pane, 880, 885, 80, 180, Ingredient.TEQUILA);
        addIngredientZone(pane, 1011, 885, 80, 180, Ingredient.TRIPLE_SEC);
        addIngredientZone(pane, 1101, 885, 80, 180, Ingredient.RASPBERRY_SYRUP);
        addIngredientZone(pane, 1190, 885, 80, 180, Ingredient.TONIC);
        addIngredientZone(pane, 1280, 885, 80, 180, Ingredient.LEMON_JUICE);
        addIngredientZone(pane, 1376, 885, 84, 180, Ingredient.CRANBERRY_JUICE);

        addIngredientZone(pane, 54, 540, 234, 277, Ingredient.BEER);
        addIngredientZone(pane, 1506, 882, 446, 178, Ingredient.ICE);
        addIngredientZone(pane, 696, 686, 92, 74, Ingredient.LIME);
        addIngredientZone(pane, 942, 686, 92, 88, Ingredient.ORANGE_ZEST);
        addIngredientZone(pane, 1195, 678, 86, 96, Ingredient.RASPBERRY);

        addTechniqueZone(pane, 1518, 759, 284, 58, Technique.STIR);
        addTechniqueZone(pane, 1799, 552, 107, 211, Technique.SHAKE);
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
            if (customer != null && customer.getOrder() != null) {
                double centerX = 310 + 80 + seat * 240;
                if (customer.canBeServed()) {
                    drinkBatch.drawImage(orderBubble, 300 + seat * 240, 170, 180, 260);
                    drawDrink(customer.getOrder(), centerX, 190, 1);
                } else {
                    drawDrink(customer.getOrder(), centerX, 630, 0.6);
                }
            }
        }
    }

    private void drawDrink(Drink order, double centerX, double y, double scale) {
        double width = 100 * scale;
        if (order.getGlass() == Glass.WINE_GLASS) {
            width = 80 * scale;
        } else if (order.getGlass() == Glass.HIGHBALL) {
            width = 60 * scale;
        }
        double height = 150 * scale;
        double x = centerX - width / 2;

        switch (order.getGlass()) {
            case PINT:
                drinkBatch.drawImage(beer, x, y, width, height);
                drinkBatch.drawImage(beerGlass, x, y, width, height);
                drinkBatch.drawImage(beerFoam, x, y, width, height);
                break;
            case WINE_GLASS:
                drinkBatch.drawImage(wine, x, y, width, height);
                drinkBatch.drawImage(wineGlass, x, y, width, height);
                break;
            case HIGHBALL:
                switch (order.getName()) {
                    case "Gin Tonic":
                        drinkBatch.drawImage(ginTonic, x, y, width, height);
                        break;
                    case "Tequila Tonic":
                        drinkBatch.drawImage(tequilaTonic, x, y, width, height);
                        break;
                    case "Pink Cape Cod":
                        drinkBatch.drawImage(pinkCapeCod, x, y, width, height);
                        break;
                }
                drinkBatch.drawImage(highballGlass, x, y, width, height);
                break;
            case MARTINI:
                switch (order.getName()) {
                    case "Margarita":
                        drinkBatch.drawImage(margarita, x, y, width, height);
                        break;
                    case "Clover Club":
                        drinkBatch.drawImage(cloverClub, x, y, width, height);
                        break;
                    case "Cosmopolitan":
                        drinkBatch.drawImage(cosmopolitan, x, y, width, height);
                        break;
                }
                drinkBatch.drawImage(martiniGlass, x, y, width, height);
                break;
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