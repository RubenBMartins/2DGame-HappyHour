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

        Button tutorialButton = createButton("Tutoriel");
        tutorialButton.setOnAction(event -> app.showTutorial());

        Button scoresButton = createButton("Scores");
        scoresButton.setOnAction(event -> app.showScores());

        Button quitButton = createSecondaryButton("Quitter");
        quitButton.setOnAction(event -> Platform.exit());

        root.getChildren().addAll(playButton, tutorialButton, scoresButton, quitButton);
        display(stage, root);
    }
}