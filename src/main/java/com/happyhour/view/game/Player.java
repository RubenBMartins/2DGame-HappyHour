package com.happyhour.view.game;
import javafx.scene.image.Image;

/**
 * Melvin, le barman : ses deux bras suivent la souris.
 * Les mains sont ouvertes, ou serrées quand il tient un verre.
 */
public class Player {
    private Image arm;
    private Image armClench;
    private double mouseX;
    private double mouseY;
    /**
     * Crée Melvin et charge les images de ses bras.
     */
    public Player() {
        arm = new Image(getClass().getResourceAsStream("/images/arm.png"),1580, 320, true, false);
        armClench = new Image(getClass().getResourceAsStream("/images/armClench.png"),1580, 320, true, false);
        mouseX = 0;
        mouseY = 0;
    }
    /**
     * Renvoie la position horizontale de la souris.
     *
     * @return la position x
     */
    public double getX() {
        return mouseX;
    }
    /**
     * Renvoie la position verticale de la souris.
     *
     * @return la position y
     */
    public double getY() {
        return mouseY;
    }
    /**
     * Renvoie l'image du bras.
     *
     * @param status true si Melvin tient un verre
     * @return le bras main serrée si status vaut true, main ouverte sinon
     */
    public Image getArm(boolean status) {
        if (status == false) {
            return arm;
        }
        else {
            return armClench;
        }
    }
    /**
     * Enregistre la nouvelle position de la souris.
     *
     * @param x la position horizontale
     * @param y la position verticale
     */
    public void updateMouse(double x, double y) {
        mouseX = x;
        mouseY = y;
    }
}



