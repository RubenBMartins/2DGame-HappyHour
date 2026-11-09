package com.happyhour.model.score;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.happyhour.model.customer.PickyCustomer;
import com.happyhour.model.game.FakeDrink;
import com.happyhour.model.game.GameSession;

public class CashRegisterTest {

    @Test
    void saleAddsPriceAndTip() {
        CashRegister register = new CashRegister();
        register.recordSale(new FakeDrink(10), 4);
        assertEquals(10.0, register.getRevenue());
        assertEquals(4.0, register.getTips());
        assertEquals(14.0, register.getTotal());
        assertEquals(1, register.getServedCustomers());
    }

    @Test
    void mostServedDrinkIsFound() {
        CashRegister register = new CashRegister();
        register.recordSale(new FakeDrink("Mojito", 10), 0);
        register.recordSale(new FakeDrink("Bière", 10), 0);
        register.recordSale(new FakeDrink("Mojito", 10), 0);
        assertEquals("Mojito", register.getMostServedDrink());
    }

    @Test
    void emptyRegisterHasNoMostServedDrink() {
        CashRegister register = new CashRegister();
        assertNull(register.getMostServedDrink());
    }

    @Test
    void refusedDrinkIsNotPaid() {
        GameSession session = new GameSession();
        session.placeCustomer(0, new PickyCustomer(new FakeDrink(7)));
        session.serve(0);
        assertEquals(0.0, session.getRegister().getTotal());
        assertEquals(0, session.getRegister().getServedCustomers());
    }
}