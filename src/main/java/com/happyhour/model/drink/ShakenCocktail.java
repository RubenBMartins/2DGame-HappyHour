package com.happyhour.model.drink;

import java.util.ArrayList;

public class ShakenCocktail extends Cocktail {

    public ShakenCocktail(String name, ArrayList<Ingredient> ingredients) {
        super(name, 12, Glass.MARTINI, ingredients, Technique.SHAKE);
    }

    @Override
    public DrinkCategory getCategory() {
        return DrinkCategory.SHAKEN;
    }
}