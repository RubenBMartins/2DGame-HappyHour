package com.happyhour.model.drink;

import java.util.ArrayList;

public class Beer extends Drink {

    public Beer() {
        super("Bière blonde", 7, Glass.PINT, createIngredients(), Technique.NONE, new BeerWineGrading());
    }

    private static ArrayList<Ingredient> createIngredients() {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(Ingredient.BEER);
        return ingredients;
    }

    @Override
    public double getBaseTip(int grade) {
        return tipForGrade(grade, 1.5, 1, 0.5);
    }

    @Override
    public DrinkCategory getCategory() {
        return DrinkCategory.BEER;
    }
}