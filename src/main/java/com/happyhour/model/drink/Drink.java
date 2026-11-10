package com.happyhour.model.drink;

import java.util.ArrayList;

public abstract class Drink {
    private String name;
    private int price;
    private Glass glass;
    private ArrayList<Ingredient> ingredients;
    private Technique technique;
    private GradingStrategy grading;

    public Drink(String name, int price, Glass glass, ArrayList<Ingredient> ingredients,
            Technique technique, GradingStrategy grading) {
        this.name = name;
        this.price = price;
        this.glass = glass;
        this.ingredients = ingredients;
        this.technique = technique;
        this.grading = grading;
    }

    public int grade(Preparation preparation) {
        return grading.grade(this, preparation);
    }

    protected double tipForGrade(int grade, double perfect, double good, double correct) {
        if (grade == 10) {
            return perfect;
        } else if (grade >= 7) {
            return good;
        } else if (grade >= 5) {
            return correct;
        }
        return 0;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public Glass getGlass() {
        return glass;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public Technique getTechnique() {
        return technique;
    }

    public abstract double getBaseTip(int grade);

    public abstract DrinkCategory getCategory();
}