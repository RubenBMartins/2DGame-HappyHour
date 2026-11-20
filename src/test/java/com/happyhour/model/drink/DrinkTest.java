package com.happyhour.model.drink;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class DrinkTest {

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

    // Préparation

    @Test
    void newPreparationIsEmpty() {
        Preparation preparation = new Preparation();
        assertTrue(preparation.isEmpty());
        assertNull(preparation.getGlass());
        assertEquals(Technique.NONE, preparation.getTechnique());
    }

    @Test
    void ingredientsKeepTheirOrder() {
        Preparation preparation = perfectGinTonic();
        assertEquals(4, preparation.getIngredients().size());
        assertEquals(Ingredient.GIN, preparation.getIngredients().get(0));
        assertEquals(Ingredient.LIME, preparation.getIngredients().get(3));
    }

    @Test
    void glassMustBeChosenFirst() {
        Preparation preparation = new Preparation();
        preparation.addIngredient(Ingredient.BEER);
        preparation.useTechnique(Technique.SHAKE);
        assertTrue(preparation.getIngredients().isEmpty());
        assertEquals(Technique.NONE, preparation.getTechnique());
    }

    @Test
    void drinkIsReadyOnlyWithGlassAndLiquid() {
        Preparation preparation = new Preparation();
        preparation.chooseGlass(Glass.HIGHBALL);
        assertFalse(preparation.isReady());
        preparation.addIngredient(Ingredient.ICE);
        assertFalse(preparation.isReady());
        preparation.addIngredient(Ingredient.GIN);
        assertTrue(preparation.isReady());
    }

    @Test
    void garnishesAreNotLiquids() {
        assertFalse(Ingredient.ICE.isLiquid());
        assertFalse(Ingredient.LIME.isLiquid());
        assertTrue(Ingredient.TONIC.isLiquid());
        assertTrue(Ingredient.BEER.isLiquid());
    }

    // Notation des cocktails (pattern Strategy)

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
    void extraIngredientCostsOnePoint() {
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
    void ingredientScoreNeverGoesBelowZero() {
        Preparation preparation = new Preparation();
        preparation.chooseGlass(Glass.HIGHBALL);
        for (int i = 0; i < 10; i++) {
            preparation.addIngredient(Ingredient.BEER);
        }
        preparation.useTechnique(Technique.STIR);
        assertEquals(6, ginTonic().grade(preparation));
    }

    // Notation bière et vin

    @Test
    void perfectBeerGetsTen() {
        Preparation preparation = new Preparation();
        preparation.chooseGlass(Glass.PINT);
        preparation.addIngredient(Ingredient.BEER);
        assertEquals(10, new Beer().grade(preparation));
    }

    @Test
    void wineInWrongGlassLosesGlassPoints() {
        Preparation preparation = new Preparation();
        preparation.chooseGlass(Glass.PINT);
        preparation.addIngredient(Ingredient.WHITE_WINE);
        assertEquals(4, new Wine().grade(preparation));
    }

    @Test
    void wrongDrinkInRightGlassLosesDrinkPoints() {
        Preparation preparation = new Preparation();
        preparation.chooseGlass(Glass.WINE_GLASS);
        preparation.addIngredient(Ingredient.BEER);
        assertEquals(6, new Wine().grade(preparation));
    }

    // Pourboires et prix

    @Test
    void baseTipsFollowTheGrade() {
        Drink cocktail = ginTonic();
        assertEquals(3.0, cocktail.getBaseTip(10));
        assertEquals(2.0, cocktail.getBaseTip(7));
        assertEquals(1.0, cocktail.getBaseTip(5));
        assertEquals(0.0, cocktail.getBaseTip(4));
        assertEquals(1.5, new Beer().getBaseTip(10));
    }

    @Test
    void cocktailsCostMoreThanBeerAndWine() {
        assertEquals(7, new Beer().getPrice());
        assertEquals(5, new Wine().getPrice());
        assertEquals(10, ginTonic().getPrice());
    }

    // Livre de recettes

    @Test
    void recipeBookHasEightDrinks() {
        assertEquals(8, new RecipeBook().getDrinks().size());
    }

    @Test
    void randomDrinkHasTheAskedCategory() {
        RecipeBook book = new RecipeBook();
        for (int i = 0; i < 50; i++) {
            assertEquals(DrinkCategory.SHAKEN, book.randomDrink(DrinkCategory.SHAKEN).getCategory());
        }
    }
}
