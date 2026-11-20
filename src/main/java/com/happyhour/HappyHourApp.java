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

/**
 * Point d'entrée du jeu Happy Hour (application JavaFX).
 * <p>
 * Elle ouvre la fenêtre, charge la musique et les bruitages,
 * puis passe d'un écran à l'autre : menu, tutoriel, jeu, bilan, scores.
 */
public class HappyHourApp extends Application {
    private Stage stage;
    private ScoreBoard scoreBoard = new ScoreBoard("scores.txt");
    private MusicPlayer music;
    private MusicPlayer musicDisco;

    /**
     * Crée l'application. Appelé par JavaFX au lancement.
     */
    public HappyHourApp() {
    }

    /**
     * Lance le jeu : ouvre la fenêtre agrandie, charge les sons,
     * démarre la musique et affiche le menu.
     *
     * @param stage la fenêtre principale
     */
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

    /**
     * Appelée à la fermeture du jeu : arrête les musiques.
     */
    @Override
    public void stop() {
        music.stop();
        musicDisco.stop();
    }

    /**
     * Affiche le menu principal.
     */
    public void showMenu() {
        new MenuScreen(this).start(stage);
    }

    /**
     * Affiche le tutoriel.
     */
    public void showTutorial() {
        new TutorialScreen(this).start(stage);
    }

    /**
     * Lance une nouvelle partie.
     */
    public void showGame() {
        new GameScreen(this).start(stage);
    }

    /**
     * Affiche le bilan de fin de soirée.
     *
     * @param session la soirée qui vient de se terminer
     */
    public void showSummary(GameSession session) {
        new SummaryScreen(this, session, scoreBoard).start(stage);
    }

    /**
     * Affiche le tableau des meilleurs scores.
     */
    public void showScores() {
        new ScoreScreen(this, scoreBoard).start(stage);
    }

    /**
     * Renvoie l'une des deux musiques du jeu.
     *
     * @param disco true pour la musique du Mode Disco
     * @return la musique demandée
     */
    public MusicPlayer getMusic(boolean disco) {
        if (disco) {
            return musicDisco;
        }
        else {
            return music;
        }
    }

    /**
     * Change de musique selon le Mode Disco.
     *
     * @param disco true pour jouer la musique du Mode Disco
     */
    public void switchMusic(boolean disco) {
        music.stop();
        musicDisco.stop();
        getMusic(disco).play();
    }

    /**
     * Démarre l'application.
     *
     * @param args les arguments de la ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        launch(args);
    }
}

