package com.happyhour.model.score;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.happyhour.model.drink.Beer;
import com.happyhour.model.drink.Wine;

public class CashRegisterTest {

    @Test
    void saleAddsPriceAndTip() {
        CashRegister register = new CashRegister();
        register.recordSale(new Beer(), 7, 2.5);
        assertEquals(7.0, register.getRevenue());
        assertEquals(2.5, register.getTips());
        assertEquals(9.5, register.getTotal());
        assertEquals(1, register.getServedCustomers());
    }

    @Test
    void mostServedDrinkIsFound() {
        CashRegister register = new CashRegister();
        register.recordSale(new Beer(), 7, 0);
        register.recordSale(new Wine(), 5, 0);
        register.recordSale(new Beer(), 7, 0);
        assertEquals("Bière blonde", register.getMostServedDrink());
    }

    @Test
    void emptyRegisterHasNoMostServedDrink() {
        assertNull(new CashRegister().getMostServedDrink());
    }
}
