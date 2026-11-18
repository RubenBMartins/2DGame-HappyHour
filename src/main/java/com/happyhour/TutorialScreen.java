package com.happyhour;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TutorialScreen extends Screen {

    public TutorialScreen(HappyHourApp app) {
        super(app);
    }

    @Override
    public void start(Stage stage) {
        VBox root = createLayout("Tutoriel");

        VBox card = createCard();
        card.getChildren().add(createText("Tu es Melvin, barman du Happy Hour. Sers un maximum de clients en 5 minutes."));
        card.getChildren().add(createText("Préparer : clique sur le verre, puis les ingrédients dans l'ordre (alcool, sirop, jus, glaçons), la technique et la garnish."));
        card.getChildren().add(createText("Servir : clic gauche sur le client. Coup de pied : clic droit."));
        card.getChildren().add(createText("Attention : ne sers jamais un enfant, sinon c'est la descente de police !"));
        card.getChildren().add(createText("10 clients perdus et la soirée est finie. Pause : barre Espace."));
        root.getChildren().add(card);

        Button backButton = createSecondaryButton("Retour");
        backButton.setOnAction(event -> app.showMenu());
        root.getChildren().add(backButton);

        display(stage, root);
    }
}