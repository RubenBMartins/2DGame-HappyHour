package com.happyhour.model.drink;

import java.util.ArrayList;

public class StirredCocktail extends Drink {

    public StirredCocktail(String name, ArrayList<Ingredient> ingredients) {
        super(name, 10, Glass.HIGHBALL, ingredients, Technique.STIR, new CocktailGrading());
    }

    @Override
    public double getBaseTip(int grade) {
        return tipForGrade(grade, 3, 2, 1);
    }

    @Override
    public DrinkCategory getCategory() {
        return DrinkCategory.STIRRED;
    }
}