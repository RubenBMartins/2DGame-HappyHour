package com.happyhour.model.game;

import com.happyhour.model.drink.Drink;
import com.happyhour.model.drink.Glass;
import com.happyhour.model.drink.Preparation;

public class FakeDrink extends Drink {
    private int fixedGrade;

    public FakeDrink(int fixedGrade) {
        super("Test", 10, Glass.HIGHBALL);
        this.fixedGrade = fixedGrade;
    }

    @Override
    public int grade(Preparation preparation) {
        return fixedGrade;
    }

    @Override
    public double getBaseTip(int grade) {
        return 3;
    }
}