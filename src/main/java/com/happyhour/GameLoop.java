package com.happyhour;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;

public class GameLoop extends AnimationTimer {
    private long lastTime = 0;
    private double elapsed = 0;
    private Label timeLabel;

    public GameLoop(Label timeLabel) {
        this.timeLabel = timeLabel;
    }

    @Override
    public void handle(long now) {
        if (lastTime == 0) {
            lastTime = now;
            return;
        }
        double deltaTime = (now - lastTime) / 1000000000.0;
        lastTime = now;

        elapsed = elapsed + deltaTime;
        timeLabel.setText("Temps : " + (int) elapsed + " s");
    }
}