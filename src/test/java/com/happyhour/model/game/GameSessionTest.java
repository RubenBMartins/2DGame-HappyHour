package com.happyhour.model.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.happyhour.model.customer.NormalCustomer;

public class GameSessionTest {

    @Test
    void impatientCustomerIsLost() {
        GameSession session = new GameSession();
        session.placeCustomer(0, new NormalCustomer(null));
        session.update(31);
        assertEquals(1, session.getLostCustomers());
        assertNull(session.getCustomer(0));
    }

    @Test
    void kickedCustomerIsLost() {
        GameSession session = new GameSession();
        session.placeCustomer(2, new NormalCustomer(null));
        session.kick(2);
        assertEquals(1, session.getLostCustomers());
        assertNull(session.getCustomer(2));
    }

    @Test
    void tenLostCustomersEndTheEvening() {
        GameSession session = new GameSession();
        for (int i = 0; i < 10; i++) {
            session.placeCustomer(0, new NormalCustomer(null));
            session.kick(0);
        }
        assertTrue(session.isOver());
    }
}