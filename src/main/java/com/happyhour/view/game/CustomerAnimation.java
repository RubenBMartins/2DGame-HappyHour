package com.happyhour.view.game;

import javafx.scene.image.Image;

/**
 * Animation d'un personnage qui apparaît ou disparaît en fondu à une place
 * du comptoir (en 0,5 seconde), en glissant légèrement vers le haut ou le bas.
 * Utilisée pour l'arrivée et le départ des clients, et pour l'entrée des policiers.
 */
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

    /**
     * Crée une animation d'arrivée.
     *
     * @param seat       la place concernée
     * @param x          la position horizontale du personnage
     * @param y          la position verticale finale du personnage
     * @param image      l'image du personnage
     * @param startDelay le délai avant le début de l'animation, en millisecondes
     */
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

    /**
     * Crée l'animation d'un client qui arrive (apparition en fondu).
     *
     * @param seat  la place du client
     * @param image l'image du client
     * @param x     la position horizontale
     * @param y     la position verticale
     * @return l'animation d'arrivée
     */
    public static CustomerAnimation arrival(int seat, Image image, double x, double y) {
        return new CustomerAnimation(seat, x, y, image, 0);
    }

    /**
     * Crée l'animation d'un client qui part (disparition en fondu).
     *
     * @param seat  la place du client
     * @param image l'image du client
     * @param x     la position horizontale
     * @param y     la position verticale
     * @return l'animation de départ
     */
    public static CustomerAnimation departure(int seat, Image image, double x, double y) {
        CustomerAnimation animation = new CustomerAnimation(seat, x, y, image, 0);
        animation.departing = true;
        animation.y = y;
        animation.startY = y;
        animation.opacity = 1;
        return animation;
    }

    /**
     * Met à jour la position et la transparence selon le temps écoulé.
     */
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

    /**
     * Renvoie la position horizontale.
     *
     * @return la position x
     */
    public double getX() {
        return x;
    }

    /**
     * Renvoie la position verticale actuelle.
     *
     * @return la position y
     */
    public double getY() {
        return y;
    }

    /**
     * Renvoie la place concernée.
     *
     * @return la place (0 à 5)
     */
    public int getSeat() {
        return seat;
    }

    /**
     * Renvoie l'opacité actuelle.
     *
     * @return l'opacité, de 0 (invisible) à 1
     */
    public double getOpacity() {
        return opacity;
    }

    /**
     * Renvoie l'image du personnage.
     *
     * @return l'image
     */
    public Image getCustomerImage() {
        return customerImage;
    }

    /**
     * Renvoie le temps écoulé depuis le début de l'animation.
     *
     * @return le temps écoulé, en millisecondes
     */
    public long getAge() {
        return System.currentTimeMillis() - creationTime - startDelay;
    }
}


