package com.happyhour.view.screen;

import com.happyhour.HappyHourApp;
import com.happyhour.model.game.GameSession;
import com.happyhour.model.score.CashRegister;
import com.happyhour.model.score.ScoreBoard;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Bilan de fin de soirée : chiffre d'affaires, pourboires, clients servis,
 * boisson la plus servie et score total. Le joueur peut entrer son pseudo
 * pour enregistrer son score.
 */
public class SummaryScreen extends Screen {
    private GameSession session;
    private ScoreBoard scoreBoard;

    /**
     * Crée l'écran de bilan.
     *
     * @param app        l'application
     * @param session    la soirée terminée
     * @param scoreBoard le tableau des scores, pour enregistrer le score
     */
    public SummaryScreen(HappyHourApp app, GameSession session, ScoreBoard scoreBoard) {
        super(app);
        this.session = session;
        this.scoreBoard = scoreBoard;
    }

    /**
     * Construit et affiche l'écran.
     *
     * @param stage la fenêtre
     */
    @Override
    public void start(Stage stage) {
        String title = "Fin de la soirée";
        if (session.isDefeat()) {
            title = "Défaite !";
        }
        VBox root = createLayout(title);

        CashRegister register = session.getRegister();
        String mostServed = register.getMostServedDrink();
        if (mostServed == null) {
            mostServed = "aucune";
        }

        VBox card = createCard();
        card.getChildren().add(createText("Chiffre d'affaires : " + formatMoney(register.getRevenue())));
        card.getChildren().add(createText("Pourboires : " + formatMoney(register.getTips())));
        card.getChildren().add(createText("Clients servis : " + register.getServedCustomers()));
        card.getChildren().add(createText("Boisson la plus servie : " + mostServed));
        card.getChildren().add(createHighlight("Score total : " + formatMoney(register.getTotal())));
        root.getChildren().add(card);

        TextField nameField = createTextField("Ton pseudo");

        Button saveButton = createButton("Enregistrer");
        Button skipButton = createSecondaryButton("Passer");
        skipButton.setOnAction(event -> app.showMenu());
        saveButton.setOnAction(event -> {
            String name = nameField.getText();
            if (name.isEmpty()) {
                name = "Anonyme";
            }
            scoreBoard.addScore(name, register.getTotal(), session.isDefeat());
            app.showScores();
        });

        root.getChildren().addAll(nameField, saveButton, skipButton);
        display(stage, root);
    }

    private String formatMoney(double amount) {
        return String.format("%.2f €", amount);
    }
}

