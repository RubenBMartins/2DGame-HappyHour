package com.happyhour.model.drink;

import java.util.ArrayList;

public class Wine extends Drink {

    public Wine() {
        super("Vin blanc", 5, Glass.WINE_GLASS, createIngredients(), Technique.NONE, new BeerWineGrading());
    }

    private static ArrayList<Ingredient> createIngredients() {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(Ingredient.WHITE_WINE);
        return ingredients;
    }

    @Override
    public double getBaseTip(int grade) {
        return tipForGrade(grade, 1.5, 1, 0.5);
    }

    @Override
    public DrinkCategory getCategory() {
        return DrinkCategory.WINE;
    }
}