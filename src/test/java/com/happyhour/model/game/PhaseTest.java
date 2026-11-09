package com.happyhour.model.game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.happyhour.model.customer.Customer;
import com.happyhour.model.customer.NormalCustomer;

public class PhaseTest {

    @Test
    void eveningStartsWithOpening() {
        GameSession session = new GameSession();
        assertEquals(Phase.OPENING, session.getPhase());
    }

    @Test
    void happyHourStartsAfterOneMinute() {
        GameSession session = new GameSession();
        session.update(61);
        assertEquals(Phase.HAPPY_HOUR, session.getPhase());
    }

    @Test
    void rushStartsAfterThreeMinutes() {
        GameSession session = new GameSession();
        session.update(61);
        session.update(120);
        assertEquals(Phase.RUSH, session.getPhase());
    }

    @Test
    void rushMakesPatienceDropFaster() {
        Customer customer = new NormalCustomer(null);
        customer.setPatienceSpeed(1.25);
        customer.update(20);
        assertEquals(10.0, customer.getPatience());
    }
}