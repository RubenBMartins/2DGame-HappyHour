package com.happyhour.model.game;

import java.util.ArrayList;

import com.happyhour.model.drink.Drink;
import com.happyhour.model.drink.DrinkCategory;
import com.happyhour.model.drink.Glass;
import com.happyhour.model.drink.Preparation;
import com.happyhour.model.drink.Technique;

public class FakeDrink extends Drink {
    private int fixedGrade;

    public FakeDrink(int fixedGrade) {
        super("Test", 10, Glass.HIGHBALL, new ArrayList<>(), Technique.NONE, null);
        this.fixedGrade = fixedGrade;
    }

    public FakeDrink(String name, int fixedGrade) {
        super(name, 10, Glass.HIGHBALL, new ArrayList<>(), Technique.NONE, null);
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

    @Override
    public DrinkCategory getCategory() {
        return DrinkCategory.STIRRED;
    }
}