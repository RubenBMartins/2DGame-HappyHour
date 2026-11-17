package com.happyhour.model.drink;

import java.util.ArrayList;

public class StirredCocktail extends Cocktail {

    public StirredCocktail(String name, ArrayList<Ingredient> ingredients) {
        super(name, 10, Glass.HIGHBALL, ingredients, Technique.STIR);
    }

    @Override
    public DrinkCategory getCategory() {
        return DrinkCategory.STIRRED;
    }
}