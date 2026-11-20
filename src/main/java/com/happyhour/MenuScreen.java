package com.happyhour;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuScreen extends Screen {

    public MenuScreen(HappyHourApp app) {
        super(app);
    }

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