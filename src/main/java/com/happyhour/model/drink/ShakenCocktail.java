package com.happyhour.model.drink;

import java.util.ArrayList;

/**
 * Cocktail secoué au shaker (Margarita, Clover Club, Cosmopolitan).
 * Servi dans un verre martini, au prix de 12 €.
 * <p>
 * Plus difficile à réussir qu'un stirred, il redéfinit son pourboire
 * pour rapporter davantage.
 */
public class ShakenCocktail extends Cocktail {

    /**
     * Crée un cocktail shaken.
     *
     * @param name        le nom du cocktail
     * @param ingredients les ingrédients attendus, dans l'ordre
     */
    public ShakenCocktail(String name, ArrayList<Ingredient> ingredients) {
        super(name, 12, Glass.MARTINI, ingredients, Technique.SHAKE);
    }

    /**
     * Pourboire de base d'un shaken : 4 € pour 10/10, 3 € de 7 à 9, 1,50 € de 5 à 6.
     *
     * @param grade la note sur 10
     * @return le pourboire de base en euros
     */
    @Override
    public double getBaseTip(int grade) {
        return tipForGrade(grade, 4, 3, 1.5);
    }

    /**
     * Renvoie la catégorie de cette boisson.
     *
     * @return {@link DrinkCategory#SHAKEN}
     */
    @Override
    public DrinkCategory getCategory() {
        return DrinkCategory.SHAKEN;
    }
}
