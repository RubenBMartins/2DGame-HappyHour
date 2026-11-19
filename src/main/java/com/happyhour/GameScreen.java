package com.happyhour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.happyhour.model.GameConfig;
import com.happyhour.model.customer.Customer;
import com.happyhour.model.drink.Drink;
import com.happyhour.model.drink.Glass;
import com.happyhour.model.drink.Ingredient;
import com.happyhour.model.drink.Preparation;
import com.happyhour.model.drink.Technique;
import com.happyhour.model.game.GameListener;
import com.happyhour.model.game.GameSession;
import com.happyhour.model.game.Phase;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

public class GameScreen extends Canvas implements GameListener {
    public static final int WIDTH = 1980;
    public static final int HEIGHT = 1080;
    private static final double POPUP_TIME = 1500;
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
    private Pane gamePane;
    private GameLoop gameLoop;
    private Label timeLabel;
    private Label moneyLabel;
    private Label lostLabel;
    private Label prepLabel;
    private Label feedbackLabel;
    private ImageView recipeView;
    private List<Explosion> explosions = new ArrayList<>();
    private ArrayList<CustomerAppears> customerAnimations = new ArrayList<>();
    private List<CustomerAppears> policeAnimations = new ArrayList<>();
    private Image policeImage;
    private HashMap<String, Image> tintCache = new HashMap<>();
    private String[] popupText = new String[GameConfig.SEATS];
    private long[] popupStart = new long[GameConfig.SEATS];
    private Label hoverLabel;
    private PauseTransition hoverTimer;
    private double hoverX;
    private double hoverY;

    @Override
    public void onPhaseChanged(Phase phase) {
    }

    @Override
    public void onScoreChanged(double tips) {
    }

    public GameScreen(HappyHourApp app) {
        super(WIDTH, HEIGHT);
        this.app = app;
        table = new Image(getClass().getResourceAsStream("/images/table.png"),WIDTH,540,true, false);
        seats = new Image(getClass().getResourceAsStream("/images/seats.png"),1360, 120, true, false);
        bg = new Image(getClass().getResourceAsStream("/images/bg.png"),WIDTH, HEIGHT, true, false);
        kick = new Image(getClass().getResourceAsStream("/images/kick.png"),400, 400, true, false);
        
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
        policeImage = new Image(getClass().getResourceAsStream("/images/police.png"),160, 350, true, false);
    }

    @Override
    public void onEveningOver(boolean defeat) {
        gameLoop.stop();
        endGame(session);
    }

    @Override
    public void onPoliceRaid() {
        for (int i = 0; i < 6; i++) {
            double x = 310 + (i * 240);
            double y = 390;
            CustomerAppears animation = new CustomerAppears(i, x, y, policeImage, i * 83L);
            policeAnimations.add(animation);
        }
    }

    @Override
    public void onCustomerArrived(int seat, Customer customer) {
        double x = 310 + (seat * 240);
        double y = 390;

        customerAnimations.add(
            new CustomerAppears(seat, x, y, customer)
        );
    }

    @Override
    public void onCustomerLeft(int seat, Customer customer) {
        double x = 310 + (seat * 240);
        double y = 390;

        customerAnimations.add(
            CustomerAppears.departure(seat, customer, x, y)
        );
    }

    @Override
    public void onCustomerServed(int seat, double amount) {
        popupText[seat] = "+" + String.format("%.0f", amount) + " €";
        popupStart[seat] = System.currentTimeMillis();
    }

    private void renderCustomers() {
        customerAnimations.removeIf(animation -> animation.getAge() >= 500);

        for (int i = 0; i < 6; i++) {
            Customer customer = session.getCustomer(i);

            if (customer != null) {
                double x = 310 + (i * 240);
                double y = 390;

                int currentSeat = i;

                CustomerAppears animation = customerAnimations.stream()
                    .filter(a -> a.getSeat() == currentSeat)
                    .filter(a -> a.getAge() < 500)
                    .findFirst()
                    .orElse(null);

                if (animation == null) {
                    batch.drawImage(customer.getSprite(), x, y, 160, 350);
                }
            }
        }

        for (CustomerAppears animation : policeAnimations) {
            animation.update();

            batch.save();
            batch.setGlobalAlpha(animation.getOpacity());
            batch.drawImage(
                animation.getCustomerImage(),
                animation.getX(),
                animation.getY(),
                160,
                350
            );
            batch.restore();
        }

        for (CustomerAppears animation : customerAnimations) {
            animation.update();

            batch.save();
            batch.setGlobalAlpha(animation.getOpacity());
            batch.drawImage(
                animation.getCustomerImage(),
                animation.getX(),
                animation.getY(),
                160,
                350
            );
            batch.restore();
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
        label.setPrefWidth((WIDTH - 60 - 40 - 30) / 3.0);
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
            parts.add(glassName(preparation.getGlass()));
        }
        for (Ingredient ingredient : preparation.getIngredients()) {
            parts.add(ingredientName(ingredient));
        }
        if (preparation.getTechnique() != Technique.NONE) {
            parts.add(techniqueName(preparation.getTechnique()));
        }

        if (parts.isEmpty()) {
            prepLabel.setText("EN MAIN : RIEN");
        } else {
            prepLabel.setText("EN MAIN : " + String.join(" + ", parts).toUpperCase());
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

    public void kickBoom(double x, double y) {
        explosions.add(new Explosion(x - 90, y - 50));
    }

    public void start(Stage stage) {
        this.stage = stage;
        gamePane = new Pane();
        gamePane.setPrefSize(WIDTH, HEIGHT);
        gamePane.setMinSize(WIDTH, HEIGHT);
        root = new Pane();
        root.setStyle("-fx-background-color: #3b1f5c;");

        hoverLabel = new Label();
        hoverLabel.setStyle("-fx-font-family: 'Monospaced';"
                + "-fx-font-weight: bold;"
                + "-fx-font-size: 26px;"
                + "-fx-text-fill: white;"
                + "-fx-background-color: #531432;"
                + "-fx-border-color: #ff9900;"
                + "-fx-border-width: 3;"
                + "-fx-padding: 4 10 4 10;");
        hoverLabel.setVisible(false);
        hoverLabel.setMouseTransparent(true);
        hoverTimer = new PauseTransition(Duration.millis(500));
        hoverTimer.setOnFinished(event -> {
            hoverLabel.setLayoutX(hoverX + 20);
            hoverLabel.setLayoutY(hoverY + 25);
            hoverLabel.setVisible(true);
        });

        timeLabel = createHudLabel(Pos.CENTER_LEFT);
        moneyLabel = createHudLabel(Pos.CENTER);
        lostLabel = createHudLabel(Pos.CENTER_RIGHT);

        ImageView bookIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/bookIcon.png")));
        bookIcon.setFitWidth(40);
        bookIcon.setFitHeight(40);
        bookIcon.setSmooth(false);
        bookIcon.setCursor(Cursor.HAND);
        installHover(bookIcon, "Livre de recettes");

        HBox hud = new HBox(10, bookIcon, timeLabel, moneyLabel, lostLabel);
        hud.setAlignment(Pos.CENTER_LEFT);
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

        recipeView = new ImageView(new Image(getClass().getResourceAsStream("/images/recipeBook.png")));
        double bookHeight = HEIGHT - 70;
        double bookWidth = bookHeight * 1920 / 1080;
        recipeView.setFitWidth(bookWidth);
        recipeView.setFitHeight(bookHeight);
        recipeView.setLayoutX((WIDTH - bookWidth) / 2);
        recipeView.setLayoutY(70);
        recipeView.setVisible(false);

        bookIcon.setOnMouseClicked(event -> {
            recipeView.setVisible(!recipeView.isVisible());
        });
        recipeView.setOnMouseClicked(event -> {
            recipeView.setVisible(false);
        });

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
        createBubbleZones(gamePane);
        gamePane.getChildren().add(drinksCanvas);
        gamePane.getChildren().add(kickCanvas);
        gamePane.getChildren().add(handsCanvas);
        gamePane.getChildren().add(prepLabel);
        gamePane.getChildren().add(feedbackLabel);
        gamePane.getChildren().add(recipeView);
        gamePane.getChildren().add(hud);
        gamePane.getChildren().add(hoverLabel);
        root.getChildren().add(gamePane);
        root.widthProperty().addListener((obs, oldVal, newVal) -> resizeGame(gamePane, root));
        root.heightProperty().addListener((obs, oldVal, newVal) -> resizeGame(gamePane, root));
        session = new GameSession();

        gameLoop = new GameLoop(session, this);
        session.addListener(this);
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

        setupSeat(seat1box, 0, 280);
        setupSeat(seat2box, 1, 520);
        setupSeat(seat3box, 2, 760);
        setupSeat(seat4box, 3, 1000);
        setupSeat(seat5box, 4, 1240);
        setupSeat(seat6box, 5, 1480);
        installKeys(scene);
    }

    private void setupSeat(Rectangle box, int seat, double kickX) {
        box.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                session.serve(seat);
            } else if (event.getButton() == MouseButton.SECONDARY) {
                if (session.getCustomer(seat) != null) {
                    session.kick(seat);
                    explosions.add(new Explosion(kickX - 90, 390 - 50));
                }
            }
        });
    }

    private void moveHover(String text, MouseEvent event) {
        Point2D point = gamePane.sceneToLocal(event.getSceneX(), event.getSceneY());
        double dx = point.getX() - hoverX;
        double dy = point.getY() - hoverY;
        if (dx * dx + dy * dy < 16) {
            return;
        }
        hoverLabel.setVisible(false);
        hoverLabel.setText(text);
        hoverX = point.getX();
        hoverY = point.getY();
        hoverTimer.playFromStart();
    }

    private void stopHover() {
        hoverTimer.stop();
        hoverLabel.setVisible(false);
    }

    private void installHover(Node node, String text) {
        node.addEventHandler(MouseEvent.MOUSE_MOVED, event -> moveHover(text, event));
        node.addEventHandler(MouseEvent.MOUSE_EXITED, event -> stopHover());
    }

    private Rectangle createZone(Pane pane, double x, double y, double width, double height, String name) {
        Rectangle zone = new Rectangle(x, y, width, height);
        zone.setFill(Color.web("0xFFFFFF", 0));
        installHover(zone, name);
        pane.getChildren().add(zone);
        return zone;
    }

    private void addGlassZone(Pane pane, double x, double y, double width, double height, Glass glass) {
        Rectangle zone = createZone(pane, x, y, width, height, glassName(glass));
        zone.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                session.chooseGlass(glass);
            }
        });
    }

    private void addIngredientZone(Pane pane, double x, double y, double width, double height, Ingredient ingredient) {
        Rectangle zone = createZone(pane, x, y, width, height, ingredientName(ingredient));
        zone.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                session.addIngredient(ingredient);
            }
        });
    }

    private void addTechniqueZone(Pane pane, double x, double y, double width, double height, Technique technique) {
        Rectangle zone = createZone(pane, x, y, width, height, techniqueName(technique));
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

    private void createBubbleZones(Pane pane) {
        for (int seat = 0; seat < GameConfig.SEATS; seat++) {
            int currentSeat = seat;
            Rectangle zone = new Rectangle(300 + seat * 240, 170, 180, 220);
            zone.setFill(Color.web("0xFFFFFF", 0));
            zone.addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
                Drink order = session.getOrder(currentSeat);
                if (order != null) {
                    moveHover(order.getName(), event);
                } else {
                    stopHover();
                }
            });
            zone.addEventHandler(MouseEvent.MOUSE_EXITED, event -> stopHover());
            pane.getChildren().add(zone);
        }
    }

    private String glassName(Glass glass) {
        switch (glass) {
            case PINT:
                return "Pinte";
            case MARTINI:
                return "Verre à martini";
            case HIGHBALL:
                return "Highball";
            default:
                return "Verre à vin";
        }
    }

    private String ingredientName(Ingredient ingredient) {
        switch (ingredient) {
            case VODKA:
                return "Vodka";
            case TEQUILA:
                return "Tequila";
            case GIN:
                return "Gin";
            case TRIPLE_SEC:
                return "Triple sec";
            case RASPBERRY_SYRUP:
                return "Sirop de framboise";
            case TONIC:
                return "Tonic";
            case LEMON_JUICE:
                return "Jus de citron";
            case CRANBERRY_JUICE:
                return "Jus de cranberry";
            case ICE:
                return "Glaçons";
            case LIME:
                return "Citron vert";
            case ORANGE_ZEST:
                return "Zeste d'orange";
            case RASPBERRY:
                return "Framboise";
            case BEER:
                return "Bière";
            default:
                return "Vin blanc";
        }
    }

    private String techniqueName(Technique technique) {
        if (technique == Technique.STIR) {
            return "Cuillère";
        }
        return "Shaker";
    }

    private void installKeys(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                if (!session.isPoliceRaidActive()) {
                    pause();
                }
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
                    drawPatienceBar(customer, seat);
                } else if (customer.getServedPreparation() != null) {
                    drawPreparation(customer.getServedPreparation(), centerX, 675, 0.6);
                }
            }
        }
        drawMoneyPopups();
    }

    private void drawPatienceBar(Customer customer, int seat) {
        double ratio = customer.getPatience() / customer.getMaxPatience();
        if (ratio < 0) {
            ratio = 0;
        }
        double x = 300 + seat * 240 + 20;
        double y = 150;
        double width = 140;
        double height = 14;

        Color color = Color.web("#9BE15D");
        if (ratio < 0.25) {
            color = Color.web("#ff4d4d");
        } else if (ratio < 0.5) {
            color = Color.web("#ff9900");
        }

        drinkBatch.setFill(Color.BLACK);
        drinkBatch.fillRect(x - 3, y - 3, width + 6, height + 6);
        drinkBatch.setFill(Color.web("#531432"));
        drinkBatch.fillRect(x, y, width, height);
        drinkBatch.setFill(color);
        drinkBatch.fillRect(x, y, width * ratio, height);
    }

    private void drawMoneyPopups() {
        long now = System.currentTimeMillis();
        drinkBatch.setFont(Font.font("Monospaced", FontWeight.BOLD, 44));
        drinkBatch.setTextAlign(TextAlignment.CENTER);
        for (int seat = 0; seat < GameConfig.SEATS; seat++) {
            if (popupText[seat] != null) {
                double age = now - popupStart[seat];
                if (age > POPUP_TIME) {
                    popupText[seat] = null;
                } else {
                    double progress = age / POPUP_TIME;
                    double x = 310 + 80 + seat * 240;
                    double y = 330 - progress * 100;
                    drinkBatch.setGlobalAlpha(1 - progress);
                    drinkBatch.setFill(Color.BLACK);
                    drinkBatch.fillText(popupText[seat], x + 3, y + 3);
                    drinkBatch.setFill(Color.web("#ff9900"));
                    drinkBatch.fillText(popupText[seat], x, y);
                    drinkBatch.setGlobalAlpha(1);
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
        stopHover();
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

    public void backToMenu() {
        gameLoop.stop();
        app.showMenu();
    }

    public void quit() {
        gameLoop.stop();
        Platform.exit();
    }

    public void endGame(GameSession session) {
        app.showSummary(session);
    }

    private void drawPreparation(Preparation preparation, double centerX, double centerY, double scale) {
        Glass glass = preparation.getGlass();
        if (glass == null) {
            return;
        }

        double width = 100 * scale;
        if (glass == Glass.WINE_GLASS) {
            width = 80 * scale;
        } else if (glass == Glass.HIGHBALL) {
            width = 60 * scale;
        }
        double height = 150 * scale;
        double x = centerX - width / 2;
        double y = centerY - height / 2;

        ArrayList<Ingredient> liquids = getLiquids(preparation.getIngredients());
        Drink match = findMatchingDrink(preparation);

        if (!liquids.isEmpty()) {
            if (match != null && (glass == Glass.PINT || glass == Glass.WINE_GLASS)) {
                drinkBatch.drawImage(getLiquidImage(glass), x, y, width, height);
            } else {
                Color color;
                if (match != null) {
                    color = getDrinkColor(match.getName());
                } else {
                    color = mixColors(liquids);
                }
                drinkBatch.drawImage(getTintedLiquid(glass, color), x, y, width, height);
            }
        }

        switch (glass) {
            case HIGHBALL:
                drinkBatch.drawImage(highballGlass, x, y, width, height);
                break;
            case MARTINI:
                drinkBatch.drawImage(martiniGlass, x, y, width, height);
                break;
            case PINT:
                drinkBatch.drawImage(beerGlass, x, y, width, height);
                if (match != null) {
                    drinkBatch.drawImage(beerFoam, x, y, width, height);
                }
                break;
            case WINE_GLASS:
                drinkBatch.drawImage(wineGlass, x, y, width, height);
                break;
        }
    }

    private ArrayList<Ingredient> getLiquids(ArrayList<Ingredient> ingredients) {
        ArrayList<Ingredient> liquids = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            if (ingredient.isLiquid()) {
                liquids.add(ingredient);
            }
        }
        return liquids;
    }

    private Drink findMatchingDrink(Preparation preparation) {
        ArrayList<Ingredient> made = getLiquids(preparation.getIngredients());
        for (Drink drink : session.getRecipeBook().getDrinks()) {
            if (drink.getGlass() == preparation.getGlass()
                    && getLiquids(drink.getIngredients()).equals(made)) {
                return drink;
            }
        }
        return null;
    }

    private Image getLiquidImage(Glass glass) {
        if (glass == Glass.PINT) {
            return beer;
        } else if (glass == Glass.WINE_GLASS) {
            return wine;
        } else if (glass == Glass.HIGHBALL) {
            return highball;
        }
        return martini;
    }

    private Color getDrinkColor(String name) {
        switch (name) {
            case "Gin Tonic":
                return Color.web("#00c7e6");
            case "Tequila Tonic":
                return Color.web("#ff9100");
            case "Pink Cape Cod":
                return Color.web("#FF7EB9");
            case "Margarita":
                return Color.web("#9BE15D");
            case "Clover Club":
                return Color.web("#cd07ff");
            case "Cosmopolitan":
                return Color.web("#D7263D");
        }
        return Color.WHITE;
    }

    private Color getIngredientColor(Ingredient ingredient) {
        switch (ingredient) {
            case VODKA:
                return Color.web("#e8f4ff");
            case GIN:
                return Color.web("#dff7f2");
            case TEQUILA:
                return Color.web("#f5d76e");
            case TRIPLE_SEC:
                return Color.web("#ffa733");
            case RASPBERRY_SYRUP:
                return Color.web("#e0115f");
            case TONIC:
                return Color.web("#c9f0ff");
            case LEMON_JUICE:
                return Color.web("#f7f06d");
            case CRANBERRY_JUICE:
                return Color.web("#b5122e");
            case BEER:
                return Color.web("#f2b134");
            case WHITE_WINE:
                return Color.web("#f3e9a6");
            default:
                return Color.WHITE;
        }
    }

    private Color mixColors(ArrayList<Ingredient> liquids) {
        double red = 0;
        double green = 0;
        double blue = 0;
        for (Ingredient ingredient : liquids) {
            Color color = getIngredientColor(ingredient);
            red = red + color.getRed();
            green = green + color.getGreen();
            blue = blue + color.getBlue();
        }
        int count = liquids.size();
        return Color.color(red / count, green / count, blue / count);
    }

    private Image getTintedLiquid(Glass glass, Color color) {
        String key = glass + color.toString();
        if (!tintCache.containsKey(key)) {
            tintCache.put(key, tintImage(getLiquidImage(glass), color));
        }
        return tintCache.get(key);
    }

    public void render() {
        updateHud();
        batch.drawImage(bg, 0, 0, WIDTH, HEIGHT);
        batch.drawImage(seats, 310, 590, 1360, 120);
        renderCustomers();
        batch.drawImage(table, 0, 540, WIDTH, 540);
        renderOrders();
        explosions.removeIf(explosion -> explosion.getAge() > 500);
        kickBatch.clearRect(0, 0, WIDTH, HEIGHT);
        for (Explosion explosion : explosions) {
            kickBatch.save();
            explosion.update();
            kickBatch.setGlobalAlpha(explosion.getOpacity());
            kickBatch.translate(explosion.getX() + 200, explosion.getY() + 200);
            kickBatch.scale(explosion.getScale(), explosion.getScale());
            kickBatch.rotate(explosion.getRotation());
            if (explosion.getFlipStatus() == 1) {
                kickBatch.scale(-1,1);
            }
            kickBatch.drawImage(kick, -200, -200, 400, 400);
            kickBatch.restore();
        }

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
        drawPreparation(session.getPreparation(), melvin.getX(), melvin.getY(), 1);
    }
}