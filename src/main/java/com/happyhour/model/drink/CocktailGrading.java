package com.happyhour.model.drink;

import java.util.ArrayList;

public class CocktailGrading implements GradingStrategy {

    @Override
    public int grade(Drink drink, Preparation preparation) {
        ArrayList<Ingredient> expected = drink.getIngredients();
        ArrayList<Ingredient> made = preparation.getIngredients();

        int missing = 0;
        for (Ingredient ingredient : expected) {
            if (!made.contains(ingredient)) {
                missing = missing + 1;
            }
        }
        int extra = made.size() - (expected.size() - missing);

        int score = 4 - missing - extra;
        if (score < 0) {
            score = 0;
        }

        if (isInOrder(expected, made)) {
            score = score + 2;
        }
        if (preparation.getGlass() == drink.getGlass()) {
            score = score + 2;
        }
        if (preparation.getTechnique() == drink.getTechnique()) {
            score = score + 2;
        }
        return score;
    }

    private boolean isInOrder(ArrayList<Ingredient> expected, ArrayList<Ingredient> made) {
        int lastIndex = -1;
        for (Ingredient ingredient : made) {
            int index = expected.indexOf(ingredient);
            if (index != -1) {
                if (index < lastIndex) {
                    return false;
                }
                lastIndex = index;
            }
        }
        return true;
    }
}