package com.happyhour.view.screen;

import com.happyhour.HappyHourApp;
import com.happyhour.view.game.GameScreen;

import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Menu pause : Continuer, Recommencer, Réglages, Retour au menu, Quitter.
 * La barre Espace reprend aussi la partie.
 */
public class PauseScreen extends Screen {
    private GameScreen game;

    /**
     * Crée le menu pause.
     *
     * @param app  l'application
     * @param game la partie en pause
     */
    public PauseScreen(HappyHourApp app, GameScreen game) {
        super(app);
        this.game = game;
    }

    /**
     * Construit et affiche l'écran.
     *
     * @param stage la fenêtre
     */
    @Override
    public void start(Stage stage) {
        VBox root = createLayout("Pause");
        root.getChildren().add(createSubtitle("Les clients attendent..."));

        Button continueButton = createButton("Continuer");
        continueButton.setOnAction(event -> game.resume());

        Button restartButton = createButton("Recommencer");
        restartButton.setOnAction(event -> game.restart());

        Button settingsButton = createSecondaryButton("Réglages");
        settingsButton.setOnAction(event -> new SettingsScreen(app, () -> start(stage)).start(stage));

        Button menuButton = createSecondaryButton("Retour au menu");
        menuButton.setOnAction(event -> game.backToMenu());

        Button quitButton = createSecondaryButton("Quitter");
        quitButton.setOnAction(event -> game.quit());

        root.getChildren().addAll(continueButton, restartButton, settingsButton, menuButton, quitButton);
        display(stage, root);

        stage.getScene().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                game.resume();
            }
        });
    }
}

