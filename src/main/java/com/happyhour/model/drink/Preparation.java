package com.happyhour.model.drink;

import java.util.ArrayList;

public class Preparation {
    private Glass glass;
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private Technique technique = Technique.NONE;
    private boolean glassFirst = false;

    public void chooseGlass(Glass glass) {
        if (this.glass == null && ingredients.isEmpty()) {
            glassFirst = true;
        }
        this.glass = glass;
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    public void useTechnique(Technique technique) {
        this.technique = technique;
    }

    public boolean isReady() {
        if (glass == null) {
            return false;
        }
        for (Ingredient ingredient : ingredients) {
            if (ingredient.isLiquid()) {
                return true;
            }
        }
        return false;
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

    public boolean isGlassFirst() {
        return glassFirst;
    }

    public boolean isEmpty() {
        return glass == null && ingredients.isEmpty();
    }
}