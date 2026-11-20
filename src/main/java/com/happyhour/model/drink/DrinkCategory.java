package com.happyhour.model.drink;

/**
 * Catégories de boissons, utilisées pour tirer les commandes selon la phase.
 * L'ordre des valeurs correspond à l'ordre des pourcentages dans
 * {@code GameConfig} (bière, vin, stirred, shaken).
 */
public enum DrinkCategory {
    /** Bière blonde. */
    BEER,
    /** Vin blanc. */
    WINE,
    /** Cocktail mélangé à la cuillère. */
    STIRRED,
    /** Cocktail secoué au shaker. */
    SHAKEN
}
