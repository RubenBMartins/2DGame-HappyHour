package com.happyhour;

import com.happyhour.model.game.GameSession;
import com.happyhour.model.score.ScoreBoard;
import com.happyhour.view.audio.MusicPlayer;
import com.happyhour.view.audio.SoundManager;
import com.happyhour.view.game.GameScreen;
import com.happyhour.view.screen.MenuScreen;
import com.happyhour.view.screen.ScoreScreen;
import com.happyhour.view.screen.SummaryScreen;
import com.happyhour.view.screen.TutorialScreen;

import javafx.application.Application;
import javafx.stage.Stage;

public class HappyHourApp extends Application {
    private Stage stage;
    private ScoreBoard scoreBoard = new ScoreBoard("scores.txt");
    private MusicPlayer music;
    private MusicPlayer musicDisco;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle("Happy Hour");
        stage.setResizable(true);
        stage.setMaximized(true);

        music = new MusicPlayer("/sounds/la_suze_a_nova.mp3");
        musicDisco = new MusicPlayer("/sounds/le_rush_du_bistrot.mp3");
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
        musicDisco.stop();
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

    public MusicPlayer getMusic(boolean disco) {
        if (disco) {
            return musicDisco;
        }
        else {
            return music;
        }
    }

    public void switchMusic(boolean disco) {
        music.stop();
        musicDisco.stop();
        getMusic(disco).play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
