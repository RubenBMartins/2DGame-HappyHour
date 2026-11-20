package com.happyhour.controller;

/**
 * Réglages choisis par le joueur, gardés pendant toute la session de jeu.
 */
public class GameSettings {

    /**
     * Classe de réglages partagés : elle ne sert pas à créer des objets.
     */
    private GameSettings() {
    }

    /**
     * Mode Disco : les couleurs du bar tournent en boucle et la musique change.
     */
    public static boolean discoMode = false;
    /**
     * Taille de l'écran choisie dans les réglages.
     */
    public static String screenSize = "Fenêtre agrandie";
}

