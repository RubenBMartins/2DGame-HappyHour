package com.happyhour.view.screen;

import com.happyhour.HappyHourApp;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Menu principal : Jouer, Tutoriel, Scores, Réglages, Quitter.
 */
public class MenuScreen extends Screen {

    /**
     * Crée le menu principal.
     *
     * @param app l'application
     */
    public MenuScreen(HappyHourApp app) {
        super(app);
    }

    /**
     * Construit et affiche l'écran.
     *
     * @param stage la fenêtre
     */
    @Override
    public void start(Stage stage) {
        VBox root = createLayout("Happy Hour");
        root.getChildren().add(createSubtitle("Cinq minutes pour faire fortune."));

        Button playButton = createButton("Jouer");
        playButton.setOnAction(event -> app.showGame());

        Button tutorialButton = createSecondaryButton("Tutoriel");
        tutorialButton.setOnAction(event -> app.showTutorial());

        Button scoresButton = createSecondaryButton("Scores");
        scoresButton.setOnAction(event -> app.showScores());

        Button settingsButton = createSecondaryButton("Réglages");
        settingsButton.setOnAction(event -> new SettingsScreen(app, () -> app.showMenu()).start(stage));

        Button quitButton = createSecondaryButton("Quitter");
        quitButton.setOnAction(event -> Platform.exit());

        root.getChildren().addAll(playButton, tutorialButton, scoresButton, settingsButton, quitButton);
        display(stage, root);
    }
}

