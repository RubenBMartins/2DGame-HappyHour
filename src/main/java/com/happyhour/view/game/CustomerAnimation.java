package com.happyhour.view.game;

import javafx.scene.image.Image;

public class CustomerAnimation {
    private double x;
    private double y;
    private long creationTime;
    private double opacity;
    private double startY;
    private int seat;
    private boolean departing;
    private Image customerImage;
    private long startDelay;

    public CustomerAnimation(int seat, double x, double y, Image image, long startDelay) {
        this.x = x;
        this.y = y + 40;
        this.startY = this.y;
        this.creationTime = System.currentTimeMillis();
        this.seat = seat;
        this.customerImage = image;
        this.opacity = 0;
        this.startDelay = startDelay;
    }

    public static CustomerAnimation arrival(int seat, Image image, double x, double y) {
        return new CustomerAnimation(seat, x, y, image, 0);
    }

    public static CustomerAnimation departure(int seat, Image image, double x, double y) {
        CustomerAnimation animation = new CustomerAnimation(seat, x, y, image, 0);
        animation.departing = true;
        animation.y = y;
        animation.startY = y;
        animation.opacity = 1;
        return animation;
    }

    public void update() {
        double age = getAge();
        double progress = Math.max(0, Math.min(age / 500.0, 1.0));
        double easedProgress = 1 - Math.pow(1 - progress, 3);

        if (departing) {
            opacity = 1 - easedProgress;
            y = startY + (40 * easedProgress);
        } else {
            opacity = easedProgress;
            y = startY - (40 * easedProgress);
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getSeat() {
        return seat;
    }

    public double getOpacity() {
        return opacity;
    }

    public Image getCustomerImage() {
        return customerImage;
    }

    public long getAge() {
        return System.currentTimeMillis() - creationTime - startDelay;
    }
}

