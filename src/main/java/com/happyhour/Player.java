package com.happyhour;
import javafx.scene.image.Image;

public class Player {
    private Image arm;
    private float angle;
    final double playerX;
    final double playerY;
    private double mouseX;
    private double mouseY;
    public Player() {
        arm = new Image(getClass().getResourceAsStream("/images/arm.png"),320, 2560, true, false);
        mouseX = 0;
        mouseY = 0;
        playerX = 640;
        playerY = 720;
    }
    public double getX() {
        return mouseX;
    }
    public double getY() {
        return mouseY;
    }
    public Image getArm() {
        return arm;
    }
    public void updateMouse(double x, double y) {
        mouseX = x;
        mouseY = y;
    }
}
