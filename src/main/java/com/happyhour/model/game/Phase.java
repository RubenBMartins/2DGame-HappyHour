package com.happyhour.model.game;

/**
 * Les trois phases d'une soirée, qui changent le rythme des clients
 * et le type de commandes.
 */
public enum Phase {
    /** Ouverture, de 0:00 à 1:00 : peu de clients, pas d'enfant. */
    OPENING,
    /** Happy hour, de 1:00 à 3:00 : plus de clients et de cocktails, les enfants arrivent. */
    HAPPY_HOUR,
    /** Rush, de 3:00 à 5:00 : le comptoir est plein et la patience baisse plus vite. */
    RUSH
}
