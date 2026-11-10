package com.happyhour.model.drink;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class DrinkGradingTest {

    private Drink ginTonic() {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(Ingredient.GIN);
        ingredients.add(Ingredient.TONIC);
        ingredients.add(Ingredient.ICE);
        ingredients.add(Ingredient.LIME);
        return new StirredCocktail("Gin Tonic", ingredients);
    }

    private Preparation perfectGinTonic() {
        Preparation preparation = new Preparation();
        preparation.chooseGlass(Glass.HIGHBALL);
        preparation.addIngredient(Ingredient.GIN);
        preparation.addIngredient(Ingredient.TONIC);
        preparation.addIngredient(Ingredient.ICE);
        preparation.useTechnique(Technique.STIR);
        preparation.addIngredient(Ingredient.LIME);
        return preparation;
    }

    @Test
    void perfectCocktailGetsTen() {
        assertEquals(10, ginTonic().grade(perfectGinTonic()));
    }

    @Test
    void missingIngredientCostsOnePoint() {
        Preparation preparation = new Preparation();
        preparation.chooseGlass(Glass.HIGHBALL);
        preparation.addIngredient(Ingredient.GIN);
        preparation.addIngredient(Ingredient.TONIC);
        preparation.addIngredient(Ingredient.ICE);
        preparation.useTechnique(Technique.STIR);
        assertEquals(9, ginTonic().grade(preparation));
    }

    @Test
    void ingredientAddedTwiceCostsOnePoint() {
        Preparation preparation = perfectGinTonic();
        preparation.addIngredient(Ingredient.LIME);
        assertEquals(9, ginTonic().grade(preparation));
    }

    @Test
    void wrongOrderCostsTwoPoints() {
        Preparation preparation = new Preparation();
        preparation.chooseGlass(Glass.HIGHBALL);
        preparation.addIngredient(Ingredient.TONIC);
        preparation.addIngredient(Ingredient.GIN);
        preparation.addIngredient(Ingredient.ICE);
        preparation.useTechnique(Technique.STIR);
        preparation.addIngredient(Ingredient.LIME);
        assertEquals(8, ginTonic().grade(preparation));
    }

    @Test
    void wrongGlassCostsTwoPoints() {
        Preparation preparation = perfectGinTonic();
        preparation.chooseGlass(Glass.MARTINI);
        assertEquals(8, ginTonic().grade(preparation));
    }

    @Test
    void wrongTechniqueCostsTwoPoints() {
        Preparation preparation = perfectGinTonic();
        preparation.useTechnique(Technique.SHAKE);
        assertEquals(8, ginTonic().grade(preparation));
    }

    @Test
    void perfectBeerGetsTen() {
        Preparation preparation = new Preparation();
        preparation.chooseGlass(Glass.PINT);
        preparation.addIngredient(Ingredient.BEER);
        assertEquals(10, new Beer().grade(preparation));
    }

    @Test
    void beerPouredBeforeGlassLosesOrderPoints() {
        Preparation preparation = new Preparation();
        preparation.addIngredient(Ingredient.BEER);
        preparation.chooseGlass(Glass.PINT);
        assertEquals(6, new Beer().grade(preparation));
    }

    @Test
    void wineInWrongGlassLosesGlassPoints() {
        Preparation preparation = new Preparation();
        preparation.chooseGlass(Glass.PINT);
        preparation.addIngredient(Ingredient.WHITE_WINE);
        assertEquals(4, new Wine().grade(preparation));
    }

    @Test
    void baseTipsFollowTheGrade() {
        Drink cocktail = ginTonic();
        assertEquals(3.0, cocktail.getBaseTip(10));
        assertEquals(2.0, cocktail.getBaseTip(8));
        assertEquals(1.0, cocktail.getBaseTip(5));
        assertEquals(0.0, cocktail.getBaseTip(4));
        assertEquals(1.5, new Beer().getBaseTip(10));
    }
}