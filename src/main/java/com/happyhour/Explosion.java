package com.happyhour;

import java.util.Random;

public class Explosion {
    private double x;
    private double y;
    private long creationTime;
    private double rotation;
    private double flip;
    private double scale;
    private double opacity;

    public Explosion(double x, double y) {
        this.x = x;
        this.y = y;
        this.creationTime = System.currentTimeMillis();
        Random rng = new Random();
        this.rotation = rng.nextInt(360);
        this.flip = rng.nextInt(2);
        this.scale = 0.5;
        this.opacity = 1.0;
    }

    public void update() {
        double progress = (getAge() - 150.0) / 350.0;
        double age = getAge();
        opacity = 1.0 - (age / 500.0);
        if (age < 150) {
            scale = 0.5 + (age / 150.0) * 0.8; /* 0.8 par rapport à la différence entre la taille de départ et d'arrivée (1.3 - 0.5) */
        }
        else {
            scale = 1.3 * (1.0 - progress);
        }
    }

    public double getX() {
    return x;
    }

    public double getY() {
        return y;
    }

    public double getRotation() {
        return rotation;
    }

    public double getFlipStatus() {
        return flip;
    }

    public double getScale() {
        return scale;
    }

    public double getOpacity() {
        return opacity;
    }

    public long getAge() {
        return System.currentTimeMillis() - creationTime;
    }
}
