package com.happyhour;

import com.happyhour.model.game.GameSession;
import com.happyhour.model.score.ScoreBoard;

import javafx.application.Application;
import javafx.stage.Stage;

public class HappyHourApp extends Application {
    private Stage stage;
    private ScoreBoard scoreBoard = new ScoreBoard("scores.txt");
    private MusicPlayer music;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle("Happy Hour");
        stage.setResizable(true);
        stage.setMaximized(true);

        music = new MusicPlayer("/sounds/la_suze_a_nova.mp3");
        SoundManager.load("kick", "kick.mp3");
        SoundManager.load("bottle", "bottle.mp3");
        SoundManager.load("water", "water.mp3");
        SoundManager.load("countdown", "countdown.mp3");
        SoundManager.load("siren", "siren.mp3");
        SoundManager.load("cash", "cash.mp3");
        music.play();

        showMenu();
        stage.show();
    }

    @Override
    public void stop() {
        music.stop();
    }

    public void showMenu() {
        new MenuScreen(this).start(stage);
    }

    public void showTutorial() {
        new TutorialScreen(this).start(stage);
    }

    public void showGame() {
        new GameScreen(this).start(stage);
    }

    public void showSummary(GameSession session) {
        new SummaryScreen(this, session, scoreBoard).start(stage);
    }

    public void showScores() {
        new ScoreScreen(this, scoreBoard).start(stage);
    }

    public MusicPlayer getMusic() {
        return music;
    }

    public static void main(String[] args) {
        launch(args);
    }
}