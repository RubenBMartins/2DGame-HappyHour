package com.happyhour.view.game;

import java.util.Random;

/**
 * Impact cartoon affiché lors d'un coup de pied : il grossit pendant 150 ms,
 * puis rétrécit et s'efface jusqu'à 500 ms. Sa rotation est tirée au hasard.
 */
public class Explosion {
    private double x;
    private double y;
    private long creationTime;
    private double rotation;
    private double flip;
    private double scale;
    private double opacity;

    /**
     * Crée un impact à une position donnée.
     *
     * @param x la position horizontale
     * @param y la position verticale
     */
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

    /**
     * Met à jour la taille et la transparence selon le temps écoulé.
     */
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

    /**
     * Renvoie la position horizontale.
     *
     * @return la position x
     */
    public double getX() {
    return x;
    }

    /**
     * Renvoie la position verticale.
     *
     * @return la position y
     */
    public double getY() {
        return y;
    }

    /**
     * Renvoie l'angle de rotation.
     *
     * @return l'angle, en degrés
     */
    public double getRotation() {
        return rotation;
    }

    /**
     * Indique si l'image est retournée horizontalement.
     *
     * @return 1 si l'image est retournée, 0 sinon
     */
    public double getFlipStatus() {
        return flip;
    }

    /**
     * Renvoie la taille actuelle.
     *
     * @return l'échelle de l'image
     */
    public double getScale() {
        return scale;
    }

    /**
     * Renvoie l'opacité actuelle.
     *
     * @return l'opacité, de 0 à 1
     */
    public double getOpacity() {
        return opacity;
    }

    /**
     * Renvoie le temps écoulé depuis la création de l'impact.
     *
     * @return le temps écoulé, en millisecondes
     */
    public long getAge() {
        return System.currentTimeMillis() - creationTime;
    }
}


