package com.happyhour.model.drink;

import java.util.ArrayList;

public class ShakenCocktail extends Drink {

    public ShakenCocktail(String name, ArrayList<Ingredient> ingredients) {
        super(name, 12, Glass.MARTINI, ingredients, Technique.SHAKE, new CocktailGrading());
    }

    @Override
    public double getBaseTip(int grade) {
        return tipForGrade(grade, 3, 2, 1);
    }

    @Override
    public DrinkCategory getCategory() {
        return DrinkCategory.SHAKEN;
    }
}