package com.happyhour.view.game;

import java.util.ArrayList;
import java.util.List;

import com.happyhour.HappyHourApp;
import com.happyhour.controller.BarController;
import com.happyhour.controller.GameLoop;
import com.happyhour.controller.GameSettings;
import com.happyhour.model.GameConfig;
import com.happyhour.model.customer.ChildCustomer;
import com.happyhour.model.customer.Customer;
import com.happyhour.model.customer.GenerousCustomer;
import com.happyhour.model.customer.PickyCustomer;
import com.happyhour.model.customer.RushedCustomer;
import com.happyhour.model.drink.Ingredient;
import com.happyhour.model.drink.Preparation;
import com.happyhour.model.drink.Technique;
import com.happyhour.model.game.GameListener;
import com.happyhour.model.game.GameSession;
import com.happyhour.model.game.Phase;
import com.happyhour.view.audio.SoundManager;
import com.happyhour.view.screen.PauseScreen;
import com.happyhour.view.screen.SettingsScreen;

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
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;

/**
 * Écran de jeu (la vue du MVC) : dessine le bar, les clients, les commandes,
 * les mains de Melvin, le HUD et tous les effets visuels.
 * <p>
 * Il écoute la {@link GameSession} (pattern Observer) pour lancer les animations
 * quand un client arrive, part ou est servi, et pour la descente de police.
 * Le jeu est dessiné en 1980 × 1080, puis mis à l'échelle de la fenêtre
 * en gardant le format 16:9.
 */
public class GameScreen extends Canvas implements GameListener {
    /**
     * Largeur de référence de l'écran de jeu, en pixels.
     */
    public static final int WIDTH = 1980;
    /**
     * Hauteur de référence de l'écran de jeu, en pixels.
     */
    public static final int HEIGHT = 1080;
    private static final double POPUP_TIME = 1500;
    private Image table;
    private Image seats;
    private Image lights;
    private ImageView lightsView;
    private Image bg;

    private Image orderBubble;

    
    private GraphicsContext batch;
    private Canvas backgroundCanvas;
    private GraphicsContext backgroundBatch;
    private GraphicsContext drinkBatch;
    private GraphicsContext kickBatch;
    private Rectangle policeLightLeft;
    private Rectangle policeLightRight;
    private ColorAdjust discoHue = new ColorAdjust();
    private Canvas drinksCanvas;
    private ImageView arm1View;
    private ImageView arm2View;
    private final Translate arm1Pos = new Translate();
    private final Translate arm2Pos = new Translate();
    private final Rotate arm1Rot = new Rotate();
    private final Rotate arm2Rot = new Rotate();
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
    private BarController controller;
    private DrinkPainter painter;
    private Label timeLabel;
    private Label moneyLabel;
    private Label lostLabel;
    private Label prepLabel;
    private Label feedbackLabel;
    private ImageView recipeView;
    private List<Explosion> explosions = new ArrayList<>();
    private ArrayList<CustomerAnimation> customerAnimations = new ArrayList<>();
    private List<CustomerAnimation> policeAnimations = new ArrayList<>();
    private Image policeImage;
    private Image normalSprite;
    private Image rushedSprite;
    private Image pickySprite;
    private Image generousSprite;
    private Image childSprite;
    private String[] popupText = new String[GameConfig.SEATS];
    private long[] popupStart = new long[GameConfig.SEATS];
    private Label hoverLabel;
    private PauseTransition hoverTimer;
    private double hoverX;
    private double hoverY;
    private int lastCountdownSecond = -1;
    private boolean hasGlass = false;

    /**
     * Rien à afficher au changement de phase.
     *
     * @param phase la nouvelle phase
     */
    @Override
    public void onPhaseChanged(Phase phase) {
    }

    /**
     * Rien à faire : la caisse du HUD est mise à jour à chaque image.
     *
     * @param tips le nouveau total de la caisse
     */
    @Override
    public void onScoreChanged(double tips) {
    }

    /**
     * Crée l'écran de jeu et charge les images du décor et des personnages.
     *
     * @param app l'application, pour changer d'écran
     */
    public GameScreen(HappyHourApp app) {
        super(WIDTH, HEIGHT);
        this.app = app;
        table = new Image(getClass().getResourceAsStream("/images/table.png"),WIDTH,540,true, false);
        seats = new Image(getClass().getResourceAsStream("/images/seats.png"),1360, 120, true, false);
        bg = new Image(getClass().getResourceAsStream("/images/bg.png"),WIDTH, HEIGHT, true, false);
        lights = new Image(getClass().getResourceAsStream("/images/happyHourLights.png"),1850, 370, true, false);
        lightsView = new ImageView(lights);
        lightsView.setX(30);
        lightsView.setY(100);
        lightsView.setFitWidth(1850);
        lightsView.setFitHeight(370);
        lightsView.setSmooth(false);
        lightsView.setMouseTransparent(true);
        kick = new Image(getClass().getResourceAsStream("/images/kick.png"),400, 400, true, false);
        
        orderBubble = new Image(getClass().getResourceAsStream("/images/orderBubble.png"),180, 260, true, false);


        backgroundCanvas = new Canvas(WIDTH, HEIGHT);
        backgroundBatch = backgroundCanvas.getGraphicsContext2D();
        backgroundCanvas.setMouseTransparent(true);
        batch = getGraphicsContext2D();
        drinksCanvas = new Canvas(WIDTH, HEIGHT);
        drinkBatch = drinksCanvas.getGraphicsContext2D();
        kickCanvas = new Canvas(WIDTH, HEIGHT);
        kickBatch = kickCanvas.getGraphicsContext2D();
        melvin = new Player();
        arm = melvin.getArm(false);
        drinksCanvas.setMouseTransparent(true);
        kickCanvas.setMouseTransparent(true);
        policeImage = new Image(getClass().getResourceAsStream("/images/police.png"),160, 350, true, false);
        normalSprite = new Image(getClass().getResourceAsStream("/images/customerRegular.png"), 160, 350, true, false);
        rushedSprite = new Image(getClass().getResourceAsStream("/images/customerHurried.png"), 160, 350, true, false);
        pickySprite = new Image(getClass().getResourceAsStream("/images/customerStrict.png"), 160, 350, true, false);
        generousSprite = new Image(getClass().getResourceAsStream("/images/customerGenerous.png"), 160, 350, true, false);
        childSprite = new Image(getClass().getResourceAsStream("/images/customerChild.png"), 160, 350, true, false);
    }

    private Image getSprite(Customer customer) {
        if (customer instanceof ChildCustomer) {
            return childSprite;
        } else if (customer instanceof RushedCustomer) {
            return rushedSprite;
        } else if (customer instanceof PickyCustomer) {
            return pickySprite;
        } else if (customer instanceof GenerousCustomer) {
            return generousSprite;
        }
        return normalSprite;
    }

    /**
     * Arrête la boucle de jeu et affiche le bilan.
     *
     * @param defeat true si la partie est perdue
     */
    @Override
    public void onEveningOver(boolean defeat) {
        gameLoop.stop();
        endGame(session);
    }

    private ImageView createArmView(Translate pos, Rotate rot, boolean flipped) {
        ImageView view = new ImageView(arm);
        view.setX(-1410);
        view.setY(-170);
        view.setFitWidth(1580);
        view.setFitHeight(320);
        view.setSmooth(false);
        view.setMouseTransparent(true);
        if (flipped) {
            view.getTransforms().setAll(pos, rot, new Scale(1, -1));
        } else {
            view.getTransforms().setAll(pos, rot);
        }
        return view;
    }

    /**
     * Fait entrer six policiers un par un, à chaque place du comptoir.
     */
    @Override
    public void onPoliceRaid() {
        for (int i = 0; i < 6; i++) {
            double x = seatX(i);
            double y = 390;
            CustomerAnimation animation = new CustomerAnimation(i, x, y, policeImage, i * 83L);
            policeAnimations.add(animation);
        }
    }

    /**
     * Lance l'animation d'arrivée du client.
     *
     * @param seat     la place du client
     * @param customer le client arrivé
     */
    @Override
    public void onCustomerArrived(int seat, Customer customer) {
        double x = seatX(seat);
        double y = 390;

        customerAnimations.add(CustomerAnimation.arrival(seat, getSprite(customer), x, y));
    }

    /**
     * Lance l'animation de départ du client.
     *
     * @param seat     la place libérée
     * @param customer le client parti
     */
    @Override
    public void onCustomerLeft(int seat, Customer customer) {
        double x = seatX(seat);
        double y = 390;

        customerAnimations.add(CustomerAnimation.departure(seat, getSprite(customer), x, y));
    }

    /**
     * Affiche le gain « +X € » au-dessus du client et joue le bruit de la caisse.
     *
     * @param seat   la place du client
     * @param amount le gain (prix + pourboire), en euros
     */
    @Override
    public void onCustomerServed(int seat, double amount) {
        popupText[seat] = "+" + String.format("%.0f", amount) + " €";
        popupStart[seat] = System.currentTimeMillis();
        SoundManager.play("cash");
    }

    private void renderCustomers() {
        customerAnimations.removeIf(animation -> animation.getAge() >= 500);

        for (int i = 0; i < 6; i++) {
            Customer customer = session.getCustomer(i);

            if (customer != null && !isAnimated(i)) {
                double x = seatX(i);
                double y = 390;
                batch.drawImage(getSprite(customer), x, y, 160, 350);
            }
        }

        for (CustomerAnimation animation : policeAnimations) {
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

        for (CustomerAnimation animation : customerAnimations) {
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

    /**
     * Calcule la position horizontale d'une place du comptoir.
     *
     * @param seat la place (0 à 5)
     * @return la position x du bord gauche de la place
     */
    public static double seatX(int seat) {
        return 310 + seat * 240;
    }

    private boolean isAnimated(int seat) {
        for (CustomerAnimation animation : customerAnimations) {
            if (animation.getSeat() == seat) {
                return true;
            }
        }
        return false;
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
        label.setPrefWidth((WIDTH - 60 - 80 - 40) / 3.0);
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
        int secondsLeft = (int) Math.ceil(remaining);

        if (secondsLeft <= 10 && secondsLeft > 0
                && secondsLeft != lastCountdownSecond) {

            SoundManager.play("countdown");
            lastCountdownSecond = secondsLeft;
        }
        timeLabel.setText("TEMPS " + String.format("%d:%02d", minutes, seconds));
        moneyLabel.setText("CAISSE " + String.format("%.0f €", session.getRegister().getTotal()));
        lostLabel.setText("CLIENTS PERDUS " + session.getLostCustomers() + "/" + GameConfig.MAX_LOST);

        Preparation preparation = session.getPreparation();
        boolean currentHasGlass = preparation.getGlass() != null;

        if (currentHasGlass != hasGlass) {
            hasGlass = currentHasGlass;
            arm = melvin.getArm(hasGlass);
            arm1View.setImage(arm);
            arm2View.setImage(arm);
        }
        ArrayList<String> parts = new ArrayList<>();
        if (preparation.getGlass() != null) {
            parts.add(DrinkNames.glassName(preparation.getGlass()));
        }
        for (Ingredient ingredient : preparation.getIngredients()) {
            parts.add(DrinkNames.ingredientName(ingredient));
        }
        if (preparation.getTechnique() != Technique.NONE) {
            parts.add(DrinkNames.techniqueName(preparation.getTechnique()));
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

    /**
     * Lance une nouvelle partie : crée la soirée, le contrôleur, le HUD,
     * le livre de recettes et les gyrophares, puis démarre la boucle de jeu.
     *
     * @param stage la fenêtre du jeu
     */
    public void start(Stage stage) {
        this.stage = stage;
        session = new GameSession();
        controller = new BarController(session, this);
        painter = new DrinkPainter(drinkBatch, session.getRecipeBook());
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

        ImageView settingsIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/settingsIcon.png")));
        settingsIcon.setFitWidth(40);
        settingsIcon.setFitHeight(40);
        settingsIcon.setSmooth(false);
        settingsIcon.setCursor(Cursor.HAND);
        installHover(settingsIcon, "Réglages");
        settingsIcon.setOnMouseClicked(event -> openSettings());

        HBox hud = new HBox(10, bookIcon, timeLabel, moneyLabel, lostLabel, settingsIcon);
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

        policeLightLeft = new Rectangle(0, 0, WIDTH / 2.0, HEIGHT);
        policeLightRight = new Rectangle(WIDTH / 2.0, 0, WIDTH / 2.0, HEIGHT);
        policeLightLeft.setMouseTransparent(true);
        policeLightRight.setMouseTransparent(true);
        policeLightLeft.setVisible(false);
        policeLightRight.setVisible(false);
        policeLightLeft.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.BLUE), new Stop(1, Color.TRANSPARENT)));
        policeLightRight.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.TRANSPARENT), new Stop(1, Color.RED)));
        backgroundBatch.drawImage(bg, 0, 0, WIDTH, HEIGHT);
        gamePane.getChildren().add(backgroundCanvas);
        gamePane.getChildren().add(lightsView);
        gamePane.getChildren().add(this);
        gamePane.getChildren().add(policeLightLeft);
        gamePane.getChildren().add(policeLightRight);

        controller.createZones(gamePane);
        gamePane.getChildren().add(drinksCanvas);
        gamePane.getChildren().add(kickCanvas);
        arm1View = createArmView(arm1Pos, arm1Rot, false);
        arm2View = createArmView(arm2Pos, arm2Rot, true);
        gamePane.getChildren().add(arm1View);
        gamePane.getChildren().add(arm2View);
        gamePane.getChildren().add(prepLabel);
        gamePane.getChildren().add(feedbackLabel);
        gamePane.getChildren().add(recipeView);
        gamePane.getChildren().add(hud);
        gamePane.getChildren().add(hoverLabel);
        root.getChildren().add(gamePane);
        root.widthProperty().addListener((obs, oldVal, newVal) -> resizeGame(gamePane, root));
        root.heightProperty().addListener((obs, oldVal, newVal) -> resizeGame(gamePane, root));

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

        controller.installKeys(scene);
    }

    private void renderDisco() {
        if (!GameSettings.discoMode) {
            setEffect(null);
            lightsView.setEffect(null);
            return;
        }
        double time = System.currentTimeMillis() / 3000.0;
        discoHue.setHue(0.16 + Math.sin(time) * 0.5);
        setEffect(discoHue);
        lightsView.setEffect(discoHue);
    }

    /**
     * Prépare le texte de survol : il s'affiche après 0,5 seconde sans bouger la souris.
     *
     * @param text  le texte à afficher
     * @param event l'événement de la souris
     */
    public void moveHover(String text, MouseEvent event) {
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

    /**
     * Cache le texte de survol.
     */
    public void stopHover() {
        hoverTimer.stop();
        hoverLabel.setVisible(false);
    }

    /**
     * Affiche un texte quand la souris reste sur un élément.
     *
     * @param node l'élément survolé
     * @param text le texte à afficher
     */
    public void installHover(Node node, String text) {
        node.addEventHandler(MouseEvent.MOUSE_MOVED, event -> moveHover(text, event));
        node.addEventHandler(MouseEvent.MOUSE_EXITED, event -> stopHover());
    }

    /**
     * Dessine les bulles de commande, les jauges de patience, les verres servis et les gains.
     */
    public void renderOrders() {
        drinkBatch.clearRect(0, 0, WIDTH, HEIGHT);
        for (int seat = 0; seat < GameConfig.SEATS; seat++) {
            Customer customer = session.getCustomer(seat);
            if (customer != null && customer.getOrder() != null) {
                double centerX = seatX(seat) + 80;
                if (customer.canBeServed()) {
                    drinkBatch.drawImage(orderBubble, seatX(seat) - 10, 170, 180, 260);
                    painter.drawDrink(customer.getOrder(), centerX, 190, 1);
                    drawPatienceBar(customer, seat);
                } else if (customer.getServedPreparation() != null) {
                    painter.drawPreparation(customer.getServedPreparation(), centerX, 675, 0.6);
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
        double x = seatX(seat) + 10;
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
                    double x = seatX(seat) + 80;
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

    /**
     * Affiche l'impact du coup de pied à une place.
     *
     * @param seat la place du client expulsé
     */
    public void showKick(int seat) {
        explosions.add(new Explosion(seatX(seat) - 120, 340));
    }

    /**
     * Met la partie en pause et affiche le menu pause.
     */
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

    /**
     * Met la partie en pause et ouvre les réglages.
     */
    public void openSettings() {
        if (session.isOver() || session.isPoliceRaidActive()) {
            return;
        }
        if (!session.isPaused()) {
            session.togglePause();
        }
        stopHover();
        new SettingsScreen(app, () -> resume()).start(stage);
    }

    /**
     * Revient à la partie et la reprend.
     */
    public void resume() {
        stage.getScene().setRoot(root);
        controller.installKeys(stage.getScene());
        if (session.isPaused()) {
            session.togglePause();
        }
    }

    /**
     * Arrête la partie et en lance une nouvelle.
     */
    public void restart() {
        gameLoop.stop();
        app.showGame();
    }

    /**
     * Arrête la partie et revient au menu.
     */
    public void backToMenu() {
        gameLoop.stop();
        app.showMenu();
    }

    /**
     * Arrête la partie et ferme le jeu.
     */
    public void quit() {
        gameLoop.stop();
        Platform.exit();
    }

    /**
     * Affiche le bilan de la soirée.
     *
     * @param session la soirée terminée
     */
    public void endGame(GameSession session) {
        app.showSummary(session);
    }

    private void renderPoliceLights() {
        boolean raid = session.isPoliceRaidActive();
        policeLightLeft.setVisible(raid);
        policeLightRight.setVisible(raid);
        if (!raid) {
            return;
        }
        boolean redFlash = System.currentTimeMillis() % 1500 < 750;
        if (redFlash) {
            policeLightLeft.setOpacity(0);
            policeLightRight.setOpacity(0.7);
        } else {
            policeLightLeft.setOpacity(0.7);
            policeLightRight.setOpacity(0);
        }
    }

    /**
     * Redessine tout l'écran. Appelée à chaque image par la boucle de jeu :
     * HUD, Mode Disco, gyrophares, clients, comptoir, commandes, impacts,
     * bras de Melvin et verre en main.
     */
    public void render() {
        updateHud();
        renderDisco();
        renderPoliceLights();
        batch.clearRect(0, 0, WIDTH, HEIGHT);

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

        double angle = Math.atan2(melvin.getY() - 1280, melvin.getX() - 990) * (180 / Math.PI);

        arm1Pos.setX(melvin.getX() - 192);
        arm1Pos.setY(melvin.getY());
        arm1Rot.setAngle(angle);

        arm2Pos.setX(melvin.getX() + 192);
        arm2Pos.setY(melvin.getY());
        arm2Rot.setAngle(angle);

        painter.drawPreparation(session.getPreparation(), melvin.getX(), melvin.getY(), 1);
    }
}


