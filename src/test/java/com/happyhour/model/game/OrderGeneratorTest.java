package com.happyhour.model.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.happyhour.model.GameConfig;
import com.happyhour.model.drink.DrinkCategory;

public class OrderGeneratorTest {

    @Test
    void pickyCustomerAlwaysOrdersShaken() {
        OrderGenerator generator = new OrderGenerator();
        for (int i = 0; i < 100; i++) {
            assertEquals(DrinkCategory.SHAKEN, generator.pickCategory(Phase.OPENING, true));
        }
    }

    @Test
    void onlyBeerAndWineDuringOpening() {
        OrderGenerator generator = new OrderGenerator();
        for (int i = 0; i < 500; i++) {
            DrinkCategory category = generator.pickCategory(Phase.OPENING, false);
            assertTrue(category == DrinkCategory.BEER || category == DrinkCategory.WINE);
        }
    }

    @Test
    void orderRatesAddUpToOneHundred() {
        assertEquals(100, sum(GameConfig.OPENING_ORDERS));
        assertEquals(100, sum(GameConfig.HAPPY_HOUR_ORDERS));
        assertEquals(100, sum(GameConfig.RUSH_ORDERS));
    }

    private int sum(int[] rates) {
        int total = 0;
        for (int rate : rates) {
            total = total + rate;
        }
        return total;
    }
}