package com.happyhour.model.drink;

/**
 * Ingrédients disponibles au bar : alcools, sirops, jus et sodas, glaçons,
 * garnish, et boissons servies telles quelles (bière, vin).
 */
public enum Ingredient {
    /** Alcool : vodka. */
    VODKA,
    /** Alcool : tequila. */
    TEQUILA,
    /** Alcool : gin. */
    GIN,
    /** Sirop : triple sec (liqueur d'orange). */
    TRIPLE_SEC,
    /** Sirop de framboise. */
    RASPBERRY_SYRUP,
    /** Soda : tonic. */
    TONIC,
    /** Jus de citron. */
    LEMON_JUICE,
    /** Jus de cranberry. */
    CRANBERRY_JUICE,
    /** Glaçons. */
    ICE,
    /** Garnish : citron vert. */
    LIME,
    /** Garnish : zeste d'orange. */
    ORANGE_ZEST,
    /** Garnish : framboise. */
    RASPBERRY,
    /** Bière blonde à la pression. */
    BEER,
    /** Vin blanc. */
    WHITE_WINE;

    /**
     * Indique si l'ingrédient est un liquide. Les glaçons et la garnish
     * n'en sont pas : ils ne suffisent pas pour servir une boisson.
     *
     * @return true si l'ingrédient est un liquide
     */
    public boolean isLiquid() {
        return this != ICE && this != LIME && this != ORANGE_ZEST && this != RASPBERRY;
    }
}
