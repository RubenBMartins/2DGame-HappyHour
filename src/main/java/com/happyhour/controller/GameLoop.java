package com.happyhour.controller;

import com.happyhour.model.game.GameSession;
import com.happyhour.view.game.GameScreen;

import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer {
    private long lastTime = 0;
    private GameSession session;
    private GameScreen curscreen;

    public GameLoop(GameSession session, GameScreen gameScreen) {
        this.session = session;
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
            return;
        }
        curscreen.render();
    }
}
