package com.happyhour.model.drink;

import java.util.ArrayList;

/**
 * Bière blonde servie à la pression dans une pinte, au prix de 7 €.
 * Notée avec {@link BeerWineGrading}.
 */
public class Beer extends Drink {

    /**
     * Crée la bière de la carte.
     */
    public Beer() {
        super("Bière blonde", 7, Glass.PINT, createIngredients(), Technique.NONE, new BeerWineGrading());
    }

    private static ArrayList<Ingredient> createIngredients() {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(Ingredient.BEER);
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
     * @return {@link DrinkCategory#BEER}
     */
    @Override
    public DrinkCategory getCategory() {
        return DrinkCategory.BEER;
    }
}
