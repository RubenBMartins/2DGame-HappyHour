package com.happyhour.model.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.happyhour.model.customer.NormalCustomer;

public class PauseAndDemoTest {

    @Test
    void pauseFreezesTime() {
        GameSession session = new GameSession();
        session.togglePause();
        session.update(10);
        assertEquals(0.0, session.getElapsed());

        session.togglePause();
        session.update(10);
        assertEquals(10.0, session.getElapsed());
    }

    @Test
    void cannotKickDuringPause() {
        GameSession session = new GameSession();
        session.placeCustomer(0, new NormalCustomer(null));
        session.togglePause();
        session.kick(0);
        assertEquals(0, session.getLostCustomers());
    }

    @Test
    void demoKeySpawnsAChild() {
        GameSession session = new GameSession();
        session.spawnChild();
        assertTrue(session.getCustomer(0).isChild());
    }

    @Test
    void demoKeySkipsToNextPhase() {
        GameSession session = new GameSession();
        session.skipPhase();
        assertEquals(Phase.HAPPY_HOUR, session.getPhase());
        session.update(0);
        session.skipPhase();
        assertEquals(Phase.RUSH, session.getPhase());
    }
}