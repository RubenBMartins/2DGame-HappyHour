package com.happyhour.model.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.happyhour.model.customer.ChildCustomer;
import com.happyhour.model.customer.NormalCustomer;
import com.happyhour.model.customer.PickyCustomer;

public class GameRulesTest {

    @Test
    void servingGivesTips() {
        GameSession session = new GameSession();
        session.placeCustomer(0, new NormalCustomer(new FakeDrink(10)));
        session.serve(0);
        assertEquals(4.0, session.getTips());
    }

    @Test
    void pickyCustomerRefusesBadDrink() {
        GameSession session = new GameSession();
        session.placeCustomer(0, new PickyCustomer(new FakeDrink(7)));
        session.serve(0);
        assertEquals(1, session.getLostCustomers());
        assertEquals(0.0, session.getTips());
        assertNull(session.getCustomer(0));
    }

    @Test
    void pickyCustomerAcceptsGoodDrink() {
        GameSession session = new GameSession();
        session.placeCustomer(0, new PickyCustomer(new FakeDrink(8)));
        session.serve(0);
        assertEquals(0, session.getLostCustomers());
        assertEquals(2.0, session.getTips());
    }

    @Test
    void servingChildCallsThePolice() {
        GameSession session = new GameSession();
        session.placeCustomer(0, new NormalCustomer(new FakeDrink(10)));
        session.placeCustomer(1, new ChildCustomer(new FakeDrink(10)));
        session.serve(1);
        assertTrue(session.isOver());
        assertNull(session.getCustomer(0));
        assertNull(session.getCustomer(1));
    }

    @Test
    void kickingChildIsNotLost() {
        GameSession session = new GameSession();
        session.placeCustomer(0, new ChildCustomer(null));
        session.kick(0);
        assertEquals(0, session.getLostCustomers());
        assertNull(session.getCustomer(0));
    }

    @Test
    void kickingDrinkingCustomerKeepsTips() {
        GameSession session = new GameSession();
        session.placeCustomer(0, new NormalCustomer(new FakeDrink(10)));
        session.serve(0);
        session.kick(0);
        assertEquals(1, session.getLostCustomers());
        assertEquals(4.0, session.getTips());
    }
}