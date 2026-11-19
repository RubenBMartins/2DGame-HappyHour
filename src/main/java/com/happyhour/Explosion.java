package com.happyhour;

import java.util.Random;

public class Explosion {
    private double x;
    private double y;
    private long creationTime;
    private double rotation;
    private double flip;

    public Explosion(double x, double y) {
        this.x = x;
        this.y = y;
        this.creationTime = System.currentTimeMillis();
        Random rng = new Random();
        this.rotation = rng.nextInt(360);
        this.flip = rng.nextInt(2);
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

    public long getAge() {
        return System.currentTimeMillis() - creationTime;
    }
}
