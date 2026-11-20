package com.happyhour.model.drink;

import java.util.ArrayList;

/**
 * Cocktail mélangé à la cuillère (Gin Tonic, Tequila Tonic, Pink Cape Cod).
 * Servi dans un verre highball, au prix de 10 €.
 */
public class StirredCocktail extends Cocktail {

    /**
     * Crée un cocktail stirred.
     *
     * @param name        le nom du cocktail
     * @param ingredients les ingrédients attendus, dans l'ordre
     */
    public StirredCocktail(String name, ArrayList<Ingredient> ingredients) {
        super(name, 10, Glass.HIGHBALL, ingredients, Technique.STIR);
    }

    /**
     * Renvoie la catégorie de cette boisson.
     *
     * @return {@link DrinkCategory#STIRRED}
     */
    @Override
    public DrinkCategory getCategory() {
        return DrinkCategory.STIRRED;
    }
}
