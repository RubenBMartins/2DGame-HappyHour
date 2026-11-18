package com.happyhour;

import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PauseScreen extends Screen {
    private GameScreen game;

    public PauseScreen(HappyHourApp app, GameScreen game) {
        super(app);
        this.game = game;
    }

    @Override
    public void start(Stage stage) {
        VBox root = createLayout("Pause");
        root.getChildren().add(createSubtitle("Les clients attendent..."));

        Button continueButton = createButton("Continuer");
        continueButton.setOnAction(event -> game.resume());

        Button restartButton = createButton("Recommencer");
        restartButton.setOnAction(event -> game.restart());

        Button quitButton = createSecondaryButton("Quitter");
        quitButton.setOnAction(event -> game.quit());

        root.getChildren().addAll(continueButton, restartButton, quitButton);
        display(stage, root);

        stage.getScene().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                game.resume();
            }
        });
    }
}