package com.happyhour.model.drink;

public class BeerWineGrading implements GradingStrategy {

    @Override
    public int grade(Drink drink, Preparation preparation) {
        int score = 0;
        if (preparation.getGlass() == drink.getGlass()) {
            score = score + 6;
        }
        if (preparation.isGlassFirst() && preparation.getIngredients().equals(drink.getIngredients())) {
            score = score + 4;
        }
        return score;
    }
}