package com.happyhour;

import com.happyhour.model.game.GameSession;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;

public class GameLoop extends AnimationTimer {
    private long lastTime = 0;
    private GameSession session;
    private Label infoLabel;
    private GameScreen curscreen;

    public GameLoop(GameSession session, Label infoLabel, GameScreen gameScreen) {
        this.session = session;
        this.infoLabel = infoLabel;
        this.curscreen = gameScreen;
    }

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
            stop();
            curscreen.endGame(session);
            return;
        }

        curscreen.render();
        int seconds = (int) session.getElapsed();
        infoLabel.setText("Heure : " + seconds / 60 + ":" + seconds % 60 + "   |   " + session.getPhase()
                + "   |   Pourboires : " + session.getTips() + " €   |   Clients perdus : " + session.getLostCustomers());
    }
}