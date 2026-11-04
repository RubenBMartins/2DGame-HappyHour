package com.happyhour.model.drink;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PreparationTest {

    @Test
    void newPreparationIsEmpty() {
        Preparation preparation = new Preparation();
        assertTrue(preparation.isEmpty());
        assertNull(preparation.getGlass());
        assertEquals(Technique.NONE, preparation.getTechnique());
    }

    @Test
    void ingredientsKeepTheirOrder() {
        Preparation preparation = new Preparation();
        preparation.addIngredient(Ingredient.GIN);
        preparation.addIngredient(Ingredient.TONIC);
        preparation.addIngredient(Ingredient.ICE);

        assertEquals(3, preparation.getIngredients().size());
        assertEquals(Ingredient.GIN, preparation.getIngredients().get(0));
        assertEquals(Ingredient.ICE, preparation.getIngredients().get(2));
    }

    @Test
    void glassAndTechniqueAreRemembered() {
        Preparation preparation = new Preparation();
        preparation.chooseGlass(Glass.COUPETTE);
        preparation.useTechnique(Technique.SHAKE);

        assertFalse(preparation.isEmpty());
        assertEquals(Glass.COUPETTE, preparation.getGlass());
        assertEquals(Technique.SHAKE, preparation.getTechnique());
    }
}