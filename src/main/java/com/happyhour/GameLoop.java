package com.happyhour;

import com.happyhour.model.game.GameSession;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;

public class GameLoop extends AnimationTimer {
    private long lastTime = 0;
    private GameSession session;
    private Label infoLabel;

    public GameLoop(GameSession session, Label infoLabel) {
        this.session = session;
        this.infoLabel = infoLabel;
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
        int seconds = (int) session.getElapsed();
        infoLabel.setText("Heure : " + seconds / 60 + ":" + seconds % 60 + "   |   " + session.getPhase()
                + "   |   Pourboires : " + session.getTips() + " €   |   Clients perdus : " + session.getLostCustomers());    }
}