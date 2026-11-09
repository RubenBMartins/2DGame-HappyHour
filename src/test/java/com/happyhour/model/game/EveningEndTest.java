package com.happyhour.model.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.happyhour.model.customer.NormalCustomer;

public class EveningEndTest {

    @Test
    void eveningEndsAtFiveMinutesWithoutDefeat() {
        GameSession session = new GameSession();
        session.update(301);
        assertTrue(session.isClosed());
        assertTrue(session.isOver());
        assertFalse(session.isDefeat());
    }

    @Test
    void waitingCustomerLeavesAtClosing() {
        GameSession session = new GameSession();
        session.update(299);
        session.placeCustomer(0, new NormalCustomer(null));
        session.update(2);
        assertNull(session.getCustomer(0));
        assertEquals(0, session.getLostCustomers());
    }

    @Test
    void drinkingCustomerFinishesBeforeTheEnd() {
        GameSession session = new GameSession();
        session.update(299);
        session.placeCustomer(0, new NormalCustomer(new FakeDrink(10)));
        session.serve(0);
        session.update(2);
        assertTrue(session.isClosed());
        assertNotNull(session.getCustomer(0));
        assertFalse(session.isOver());
        session.update(2);
        assertTrue(session.isOver());
    }

    @Test
    void tenLostCustomersIsADefeat() {
        GameSession session = new GameSession();
        for (int i = 0; i < 10; i++) {
            session.placeCustomer(0, new NormalCustomer(null));
            session.kick(0);
        }
        assertTrue(session.isDefeat());
    }
}