package com.happyhour.view.screen;

import com.happyhour.HappyHourApp;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Tutoriel en 7 pages illustrées. On change de page avec les boutons
 * Précédent / Suivant ou les flèches du clavier.
 */
public class TutorialScreen extends Screen {
    private static final int PAGES = 7;
    private int page = 1;
    private Stage stage;
    private ImageView pageView;
    private Button previousButton;
    private Button nextButton;
    private EventHandler<KeyEvent> arrowKeys;

    /**
     * Crée le tutoriel.
     *
     * @param app l'application
     */
    public TutorialScreen(HappyHourApp app) {
        super(app);
    }

    /**
     * Construit et affiche l'écran.
     *
     * @param stage la fenêtre
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #993366; -fx-padding: 20;");

        pageView = new ImageView();
        pageView.setPreserveRatio(true);
        pageView.fitWidthProperty().bind(stage.widthProperty().multiply(0.95));
        pageView.fitHeightProperty().bind(stage.heightProperty().multiply(0.82));

        previousButton = createSecondaryButton("Précédent");
        previousButton.setOnAction(event -> previousPage());

        Button menuButton = createSecondaryButton("Menu");
        menuButton.setOnAction(event -> leave());

        nextButton = createButton("Suivant");
        nextButton.setOnAction(event -> nextPage());

        HBox buttons = new HBox(20, previousButton, menuButton, nextButton);
        buttons.setAlignment(Pos.CENTER);

        root.getChildren().addAll(pageView, buttons);
        display(stage, root);

        arrowKeys = event -> {
            if (event.getCode() == KeyCode.RIGHT) {
                nextPage();
                event.consume();
            } else if (event.getCode() == KeyCode.LEFT) {
                previousPage();
                event.consume();
            }
        };
        stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, arrowKeys);

        showPage();
    }

    private void showPage() {
        pageView.setImage(new Image(getClass().getResourceAsStream("/images/tutorialscreen/tuto" + page + ".png")));
        previousButton.setDisable(page == 1);
        if (page == PAGES) {
            nextButton.setText("Terminer");
        } else {
            nextButton.setText("Suivant");
        }
    }

    private void nextPage() {
        if (page < PAGES) {
            page = page + 1;
            showPage();
        } else {
            leave();
        }
    }

    private void previousPage() {
        if (page > 1) {
            page = page - 1;
            showPage();
        }
    }

    private void leave() {
        stage.getScene().removeEventFilter(KeyEvent.KEY_PRESSED, arrowKeys);
        app.showMenu();
    }
}

