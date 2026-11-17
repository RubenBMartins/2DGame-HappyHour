package com.happyhour.model.drink;

import java.util.ArrayList;

public abstract class Cocktail extends Drink {

    public Cocktail(String name, int price, Glass glass, ArrayList<Ingredient> ingredients, Technique technique) {
        super(name, price, glass, ingredients, technique, new CocktailGrading());
    }

    @Override
    public double getBaseTip(int grade) {
        return tipForGrade(grade, 3, 2, 1);
    }
}