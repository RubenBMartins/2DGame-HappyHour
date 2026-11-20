package com.happyhour.model;

/**
 * Toutes les valeurs d'équilibrage du jeu, regroupées au même endroit.
 * Les durées sont en secondes et les proportions en pourcentages.
 */
public class GameConfig {

    /**
     * Classe de constantes : elle ne sert pas à créer des objets.
     */
    private GameConfig() {
    }

    /** Nombre de places au comptoir. */
    public static final int SEATS = 6;
    /** Nombre de clients perdus qui entraîne la défaite. */
    public static final int MAX_LOST = 10;

    /** Délai minimum avant l'arrivée du premier client. */
    public static final double FIRST_CUSTOMER_MIN = 1;
    /** Délai maximum avant l'arrivée du premier client. */
    public static final double FIRST_CUSTOMER_MAX = 3;
    /** Début de l'happy hour (1:00). */
    public static final double HAPPY_HOUR_START = 60;
    /** Moment (2:30) où un client Difficile ou Pressé est forcé s'il n'est pas encore venu. */
    public static final double FORCE_TIME = 150;
    /** Début du rush (3:00). */
    public static final double RUSH_START = 180;
    /** Fermeture du bar (5:00). */
    public static final double EVENING_END = 300;
    /** Moment où saute la touche de démo A : 15 secondes avant la fin. */
    public static final double COUNTDOWN_SKIP_TIME = 285;
    /** Délai entre le service d'un enfant et l'entrée de la police. */
    public static final double POLICE_DELAY = 0.5;
    /** Durée de l'animation de la police avant l'écran de défaite. */
    public static final double POLICE_ARRIVAL_TIME = 2.915;

    /** Délai minimum entre deux clients pendant l'ouverture. */
    public static final double OPENING_SPAWN_MIN = 5;
    /** Délai maximum entre deux clients pendant l'ouverture. */
    public static final double OPENING_SPAWN_MAX = 10;
    /** Délai minimum entre deux clients pendant l'happy hour. */
    public static final double HAPPY_HOUR_SPAWN_MIN = 3;
    /** Délai maximum entre deux clients pendant l'happy hour. */
    public static final double HAPPY_HOUR_SPAWN_MAX = 7;
    /** Délai minimum entre deux clients pendant le rush. */
    public static final double RUSH_SPAWN_MIN = 2;
    /** Délai maximum entre deux clients pendant le rush. */
    public static final double RUSH_SPAWN_MAX = 5;

    /** Patience courte (client Pressé). */
    public static final double SHORT_PATIENCE = 20;
    /** Patience normale (clients Normal, Difficile et enfant). */
    public static final double NORMAL_PATIENCE = 35;
    /** Patience longue (client Généreux). */
    public static final double LONG_PATIENCE = 50;
    /** Pendant le rush, la patience baisse 25 % plus vite. */
    public static final double RUSH_PATIENCE_SPEED = 1.25;

    /** Temps passé à boire avant de partir. */
    public static final double DRINKING_TIME = 3;
    /** Temps passé à boire par un client Généreux. */
    public static final double GENEROUS_DRINKING_TIME = 6;

    /** Bonus vitesse, en euros, si le client est servi avant que sa jauge passe à l'orange. */
    public static final double SPEED_BONUS = 1;
    /** Durée d'affichage du message de note après un service. */
    public static final double FEEDBACK_TIME = 1.2;
    /** Note minimum (sur 10) acceptée par le client Difficile. */
    public static final int PICKY_MIN_GRADE = 8;

    /** Proportion de clients Normaux. */
    public static final int NORMAL_RATE = 60;
    /** Proportion de clients Pressés. */
    public static final int RUSHED_RATE = 15;
    /** Proportion de clients Difficiles. */
    public static final int PICKY_RATE = 10;
    /** Proportion de clients Généreux. Le reste (5 %) sont des enfants. */
    public static final int GENEROUS_RATE = 10;

    /** Commandes pendant l'ouverture : bière, vin, stirred, shaken. */
    public static final int[] OPENING_ORDERS = { 30, 30, 25, 15 };
    /** Commandes pendant l'happy hour : bière, vin, stirred, shaken. */
    public static final int[] HAPPY_HOUR_ORDERS = { 25, 25, 30, 20 };
    /** Commandes pendant le rush : bière, vin, stirred, shaken. */
    public static final int[] RUSH_ORDERS = { 15, 15, 35, 35 };
}

