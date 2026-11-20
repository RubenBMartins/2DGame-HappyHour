package com.happyhour.view.screen;

import com.happyhour.HappyHourApp;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public abstract class Screen {
    private static final String FONT = "-fx-font-family: 'Monospaced'; -fx-font-weight: bold; ";
    private static final String BACKGROUND = "-fx-background-color: linear-gradient(to bottom, "
            + "#993366 0%, #993366 86%, black 86%, black 87.5%, #a52763 87.5%, #a52763 100%); "
            + "-fx-padding: 40 40 110 40;";
    private static final String BUTTON = FONT + "-fx-font-size: 22px; -fx-text-fill: black; "
            + "-fx-background-color: #ff9900; -fx-background-radius: 0; "
            + "-fx-border-color: black; -fx-border-width: 4; -fx-cursor: hand;";
    private static final String BUTTON_HOVER = FONT + "-fx-font-size: 22px; -fx-text-fill: black; "
            + "-fx-background-color: #ffb84d; -fx-background-radius: 0; "
            + "-fx-border-color: black; -fx-border-width: 4; -fx-cursor: hand;";
    private static final String SECONDARY = FONT + "-fx-font-size: 22px; -fx-text-fill: #bf7fbf; "
            + "-fx-background-color: #531432; -fx-background-radius: 0; "
            + "-fx-border-color: black; -fx-border-width: 4; -fx-cursor: hand;";
    private static final String SECONDARY_HOVER = FONT + "-fx-font-size: 22px; -fx-text-fill: #bf7fbf; "
            + "-fx-background-color: #6e1c44; -fx-background-radius: 0; "
            + "-fx-border-color: black; -fx-border-width: 4; -fx-cursor: hand;";

    protected HappyHourApp app;

    public Screen(HappyHourApp app) {
        this.app = app;
    }

    protected VBox createLayout(String title) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle(BACKGROUND);

        Label titleLabel = new Label(title.toUpperCase());
        titleLabel.setStyle(FONT + "-fx-font-size: 64px; -fx-text-fill: #ff9900;");
        titleLabel.setEffect(new DropShadow(0, 5, 5, Color.BLACK));
        root.getChildren().add(titleLabel);
        return root;
    }

    protected Label createSubtitle(String text) {
        Label label = new Label(text);
        label.setStyle(FONT + "-fx-font-size: 22px; -fx-text-fill: white;");
        return label;
    }

    protected VBox createCard() {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setMaxWidth(900);
        card.setStyle("-fx-background-color: #531432; -fx-border-color: black; "
                + "-fx-border-width: 4; -fx-padding: 20 35 20 35;");
        card.setEffect(new DropShadow(0, 6, 6, Color.BLACK));
        return card;
    }

    protected Label createText(String text) {
        Label label = new Label(text);
        label.setStyle(FONT + "-fx-font-size: 20px; -fx-text-fill: white;");
        label.setWrapText(true);
        return label;
    }

    protected Label createHighlight(String text) {
        Label label = createText(text);
        label.setStyle(FONT + "-fx-font-size: 20px; -fx-text-fill: #ff9900;");
        return label;
    }

    protected Button createButton(String text) {
        return makeButton(text, BUTTON, BUTTON_HOVER);
    }

    protected Button createSecondaryButton(String text) {
        return makeButton(text, SECONDARY, SECONDARY_HOVER);
    }

    private Button makeButton(String text, String style, String hoverStyle) {
        Button button = new Button(text);
        button.setPrefWidth(280);
        button.setStyle(style);
        button.setEffect(new DropShadow(0, 5, 5, Color.BLACK));
        button.setOnMouseEntered(event -> {
            button.setStyle(hoverStyle);
            button.setTranslateX(-2);
            button.setTranslateY(-2);
        });
        button.setOnMouseExited(event -> {
            button.setStyle(style);
            button.setTranslateX(0);
            button.setTranslateY(0);
        });
        return button;
    }

    protected TextField createTextField(String placeholder) {
        TextField field = new TextField();
        field.setPromptText(placeholder);
        field.setMaxWidth(280);
        field.setStyle(FONT + "-fx-font-size: 20px; -fx-background-radius: 0; "
                + "-fx-border-color: black; -fx-border-width: 4;");
        return field;
    }

    protected void display(Stage stage, VBox root) {
        if (stage.getScene() == null) {
            stage.setScene(new Scene(root, 1280, 720));
        } else {
            stage.getScene().setRoot(root);
            stage.getScene().setOnKeyPressed(null);
        }
    }

    public abstract void start(Stage stage);
}
