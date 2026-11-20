package com.happyhour.controller;

import com.happyhour.model.game.GameSession;
import com.happyhour.view.game.GameScreen;

import javafx.animation.AnimationTimer;

/**
 * Boucle de jeu, appelée par JavaFX à chaque image (environ 60 fois par seconde).
 * <p>
 * À chaque image, elle calcule le temps écoulé (delta time), fait avancer
 * le modèle puis redessine l'écran.
 */
public class GameLoop extends AnimationTimer {
    private long lastTime = 0;
    private GameSession session;
    private GameScreen curscreen;

    /**
     * Crée la boucle de jeu.
     *
     * @param session    la soirée à faire avancer
     * @param gameScreen l'écran à redessiner
     */
    public GameLoop(GameSession session, GameScreen gameScreen) {
        this.session = session;
        this.curscreen = gameScreen;
    }

    /**
     * Appelée à chaque image : met à jour la soirée avec le temps écoulé
     * puis redessine l'écran, tant que la partie n'est pas finie.
     *
     * @param now l'heure actuelle, en nanosecondes
     */
    @Override
    public void handle(long now) {
        if (lastTime == 0) {
            lastTime = now;
            return;
        }
        double deltaTime = (now - lastTime) / 1000000000.0;
        lastTime = now;

        session.update(deltaTime);
        if (session.isOver()) {
            return;
        }
        curscreen.render();
    }
}

