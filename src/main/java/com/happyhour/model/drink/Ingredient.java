package com.happyhour.model.drink;

public enum Ingredient {
    VODKA,
    TEQUILA,
    GIN,
    TRIPLE_SEC,
    RASPBERRY_SYRUP,
    TONIC,
    LEMON_JUICE,
    CRANBERRY_JUICE,
    ICE,
    LIME,
    ORANGE_ZEST,
    RASPBERRY,
    BEER,
    WHITE_WINE;

    public boolean isLiquid() {
        return this != ICE && this != LIME && this != ORANGE_ZEST && this != RASPBERRY;
    }
}