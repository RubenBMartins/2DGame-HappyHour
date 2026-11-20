package com.happyhour.view.screen;

import com.happyhour.HappyHourApp;
import com.happyhour.controller.GameSettings;
import com.happyhour.view.audio.MusicPlayer;
import com.happyhour.view.audio.SoundManager;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SettingsScreen extends Screen {
    private static final String TEXT_STYLE = "-fx-text-fill: white;"
            + "-fx-font-family: 'Monospaced';"
            + "-fx-font-weight: bold;"
            + "-fx-font-size: 26px;";
    private Runnable onBack;

    public SettingsScreen(HappyHourApp app, Runnable onBack) {
        super(app);
        this.onBack = onBack;
    }

    @Override
    public void start(Stage stage) {
        VBox root = createLayout("Réglages");
        MusicPlayer music = app.getMusic(false);

        Label musicLabel = new Label();
        musicLabel.setStyle(TEXT_STYLE);
        Slider musicSlider = new Slider(0, 100, music.getVolume() * 100);
        musicSlider.setMaxWidth(500);
        musicLabel.setText("MUSIQUE : " + (int) musicSlider.getValue() + " %");
        musicSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            app.getMusic(false).setVolume(newValue.doubleValue() / 100);
            app.getMusic(true).setVolume(newValue.doubleValue() / 100);
            musicLabel.setText("MUSIQUE : " + newValue.intValue() + " %");
        });

        CheckBox soundBox = new CheckBox("BRUITAGES");
        soundBox.setStyle(TEXT_STYLE);
        soundBox.setSelected(!SoundManager.isMuted());
        soundBox.setOnAction(event -> SoundManager.setMuted(!soundBox.isSelected()));

        Label sizeLabel = new Label("TAILLE DE L'ÉCRAN");
        sizeLabel.setStyle(TEXT_STYLE);
        ComboBox<String> sizeBox = new ComboBox<>();
        sizeBox.getItems().addAll("Plein écran", "Fenêtre agrandie", "1920 x 1080", "1600 x 900", "1280 x 720");
        sizeBox.setValue(GameSettings.screenSize);
        sizeBox.setStyle("-fx-font-family: 'Monospaced'; -fx-font-weight: bold; -fx-font-size: 20px;");
        sizeBox.setOnAction(event -> {
            GameSettings.screenSize = sizeBox.getValue();
            applyScreenSize(stage, sizeBox.getValue());
        });

        CheckBox discoBox = new CheckBox("MODE DISCO");
        discoBox.setStyle(TEXT_STYLE);
        discoBox.setSelected(GameSettings.discoMode);
        discoBox.setOnAction(event -> {
            GameSettings.discoMode = discoBox.isSelected();
            app.switchMusic(GameSettings.discoMode);
        });

        Button backButton = createButton("Retour");
        backButton.setOnAction(event -> onBack.run());

        root.getChildren().addAll(musicLabel, musicSlider, soundBox, sizeLabel, sizeBox, discoBox, backButton);
        display(stage, root);
    }

    private void applyScreenSize(Stage stage, String choice) {
        stage.setFullScreen(false);
        stage.setMaximized(false);
        if (choice.equals("Plein écran")) {
            stage.setFullScreen(true);
        } else if (choice.equals("Fenêtre agrandie")) {
            stage.setMaximized(true);
        } else {
            String[] parts = choice.split(" x ");
            stage.setWidth(Integer.parseInt(parts[0]));
            stage.setHeight(Integer.parseInt(parts[1]));
            stage.centerOnScreen();
        }
    }
}
