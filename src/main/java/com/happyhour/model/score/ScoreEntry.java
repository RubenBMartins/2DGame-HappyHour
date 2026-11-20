package com.happyhour.model.score;

/**
 * Une ligne du tableau des scores : pseudo, score et résultat de la partie.
 */
public class ScoreEntry {
    private String name;
    private double score;
    private boolean defeat;

    /**
     * Crée une ligne de score.
     *
     * @param name   le pseudo du joueur
     * @param score  le score total, en euros
     * @param defeat true si la partie a été perdue
     */
    public ScoreEntry(String name, double score, boolean defeat) {
        this.name = name;
        this.score = score;
        this.defeat = defeat;
    }

    /**
     * Renvoie le pseudo du joueur.
     *
     * @return le pseudo
     */
    public String getName() {
        return name;
    }

    /**
     * Renvoie le score total.
     *
     * @return le score, en euros
     */
    public double getScore() {
        return score;
    }

    /**
     * Indique si la partie a été perdue.
     *
     * @return true en cas de défaite
     */
    public boolean isDefeat() {
        return defeat;
    }
}
