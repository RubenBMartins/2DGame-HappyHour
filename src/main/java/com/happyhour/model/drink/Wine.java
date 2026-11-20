package com.happyhour.model.drink;

import java.util.ArrayList;

/**
 * Vin blanc servi dans un verre à vin, au prix de 5 €.
 * Notée avec {@link BeerWineGrading}.
 */
public class Wine extends Drink {

    /**
     * Crée le vin de la carte.
     */
    public Wine() {
        super("Vin blanc", 5, Glass.WINE_GLASS, createIngredients(), Technique.NONE, new BeerWineGrading());
    }

    private static ArrayList<Ingredient> createIngredients() {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(Ingredient.WHITE_WINE);
        return ingredients;
    }

    /**
     * Pourboire de base : 1,50 € pour 10/10, 1 € de 7 à 9, 0,50 € de 5 à 6.
     *
     * @param grade la note sur 10
     * @return le pourboire de base en euros
     */
    @Override
    public double getBaseTip(int grade) {
        return tipForGrade(grade, 1.5, 1, 0.5);
    }

    /**
     * Renvoie la catégorie de cette boisson.
     *
     * @return {@link DrinkCategory#WINE}
     */
    @Override
    public DrinkCategory getCategory() {
        return DrinkCategory.WINE;
    }
}
