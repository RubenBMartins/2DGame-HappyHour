
package com.happyhour;

import com.happyhour.model.customer.Customer;
import javafx.scene.image.Image;

public class CustomerAppears {
    private double x;
    private double y;
    private long creationTime;
    private double opacity;
    private double startY;
    private int seat;
    private boolean departing;
    private Image customerImage;
    private long startDelay;

    public CustomerAppears(int seat, double x, double y, Customer customer) {
        this.x = x;
        this.y = y + 40;
        this.startY = this.y;
        this.creationTime = System.currentTimeMillis();
        this.seat = seat;
        this.customerImage = customer.getSprite();
        this.opacity = 0;
    }

    public static CustomerAppears departure(int seat, Customer customer, double x, double y) {
        CustomerAppears animation = new CustomerAppears(seat, x, y, customer);
        animation.departing = true;
        animation.y = y;
        animation.startY = y;
        animation.opacity = 1;
        return animation;
    }

    public CustomerAppears(int seat, double x, double y, Image image, long startDelay) {
        this.x = x;
        this.y = y + 40;
        this.startY = this.y;
        this.creationTime = System.currentTimeMillis();
        this.seat = seat;
        this.customerImage = image;
        this.opacity = 0;
        this.startDelay = startDelay;
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
