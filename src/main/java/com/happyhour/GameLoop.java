package com.happyhour;

import com.happyhour.model.GameConfig;
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

        int remaining = (int) Math.ceil(GameConfig.EVENING_END - session.getElapsed());
        if (remaining < 0) {
            remaining = 0;
        }
        int minutes = remaining / 60;
        int seconds = remaining % 60;
        String time = minutes + ":";
        if (seconds < 10) {
            time = time + "0";
        }
        time = time + seconds;

        String text = "Temps : " + time + "   |   " + session.getPhase()
                + "   |   Score : " + session.getRegister().getTotal() + " €   |   Clients perdus : " + session.getLostCustomers();
        if (session.isPaused()) {
            text = text + "   |   PAUSE";
        }
        infoLabel.setText(text);
    }
}