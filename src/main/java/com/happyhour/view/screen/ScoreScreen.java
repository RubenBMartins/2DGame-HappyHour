package com.happyhour.view.screen;

import com.happyhour.HappyHourApp;
import com.happyhour.model.score.ScoreBoard;
import com.happyhour.model.score.ScoreEntry;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ScoreScreen extends Screen {
    private ScoreBoard scoreBoard;

    public ScoreScreen(HappyHourApp app, ScoreBoard scoreBoard) {
        super(app);
        this.scoreBoard = scoreBoard;
    }

    @Override
    public void start(Stage stage) {
        VBox root = createLayout("Meilleurs scores");

        VBox card = createCard();
        if (scoreBoard.getEntries().isEmpty()) {
            card.getChildren().add(createText("Aucun score pour l'instant."));
        }

        int rank = 1;
        for (ScoreEntry entry : scoreBoard.getEntries()) {
            String line = rank + ". " + entry.getName() + " : " + String.format("%.2f €", entry.getScore());
            if (entry.isDefeat()) {
                line = line + " (défaite)";
            }
            card.getChildren().add(createText(line));
            rank = rank + 1;
        }
        root.getChildren().add(card);

        Button menuButton = createSecondaryButton("Retour au menu");
        menuButton.setOnAction(event -> app.showMenu());
        root.getChildren().add(menuButton);

        display(stage, root);
    }
}
