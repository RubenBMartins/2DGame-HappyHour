package com.happyhour.view.game;
import javafx.scene.image.Image;

public class Player {
    private Image arm;
    private Image armClench;
    private double mouseX;
    private double mouseY;
    public Player() {
        arm = new Image(getClass().getResourceAsStream("/images/arm.png"),1580, 320, true, false);
        armClench = new Image(getClass().getResourceAsStream("/images/armClench.png"),1580, 320, true, false);
        mouseX = 0;
        mouseY = 0;
    }
    public double getX() {
        return mouseX;
    }
    public double getY() {
        return mouseY;
    }
    public Image getArm(boolean status) {
        if (status == false) {
            return arm;
        }
        else {
            return armClench;
        }
    }
    public void updateMouse(double x, double y) {
        mouseX = x;
        mouseY = y;
    }
}


