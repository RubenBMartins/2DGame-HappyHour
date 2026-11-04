package com.happyhour.model.drink;

import java.util.ArrayList;

public class Preparation {
    private Glass glass;
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private Technique technique = Technique.NONE;

    public void chooseGlass(Glass glass) {
        this.glass = glass;
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    public void useTechnique(Technique technique) {
        this.technique = technique;
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

    public boolean isEmpty() {
        return glass == null && ingredients.isEmpty();
    }
}