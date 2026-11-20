package com.happyhour.model.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.happyhour.model.GameConfig;
import com.happyhour.model.customer.NormalCustomer;
import com.happyhour.model.drink.Beer;
import com.happyhour.model.drink.DrinkCategory;
import com.happyhour.model.drink.Glass;
import com.happyhour.model.drink.Ingredient;

public class EveningTest {

    // Phases

    @Test
    void phasesFollowTheClock() {
        GameSession session = new GameSession();
        assertEquals(Phase.OPENING, session.getPhase());
        session.update(61);
        assertEquals(Phase.HAPPY_HOUR, session.getPhase());
        session.update(120);
        assertEquals(Phase.RUSH, session.getPhase());
    }

    @Test
    void patienceDropsFasterDuringRush() {
        GameSession session = new GameSession();
        session.update(181);
        NormalCustomer customer = new NormalCustomer(null);
        session.placeCustomer(0, customer);
        session.update(4);
        assertEquals(30.0, customer.getPatience());
    }

    // Arrivée des clients

    @Test
    void firstCustomerArrivesBetweenOneAndThreeSeconds() {
        GameSession session = new GameSession();
        session.update(0.9);
        assertEquals(0, session.getOccupiedSeats().size());
        session.update(2.2);
        assertEquals(1, session.getOccupiedSeats().size());
    }

    @Test
    void customerReceivesAnOrder() {
        GameSession session = new GameSession();
        session.spawnCustomer();
        int seat = session.getOccupiedSeats().get(0);
        assertNotNull(session.getOrder(seat));
    }

    @Test
    void cocktailsCanBeOrderedDuringOpening() {
        OrderGenerator generator = new OrderGenerator();
        boolean cocktail = false;
        for (int i = 0; i < 500; i++) {
            DrinkCategory category = generator.pickCategory(Phase.OPENING, false);
            if (category == DrinkCategory.STIRRED || category == DrinkCategory.SHAKEN) {
                cocktail = true;
            }
        }
        assertTrue(cocktail);
    }

    @Test
    void pickyCustomerAlwaysOrdersShaken() {
        OrderGenerator generator = new OrderGenerator();
        for (int i = 0; i < 100; i++) {
            assertEquals(DrinkCategory.SHAKEN, generator.pickCategory(Phase.RUSH, true));
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

    // Fin de soirée

    @Test
    void eveningEndsAtFiveMinutesWithoutDefeat() {
        GameSession session = new GameSession();
        session.update(301);
        assertTrue(session.isClosed());
        assertTrue(session.isOver());
        assertFalse(session.isDefeat());
    }

    @Test
    void waitingCustomerLeavesAtClosingWithoutBeingLost() {
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
        session.placeCustomer(0, new NormalCustomer(new Beer()));
        session.chooseGlass(Glass.PINT);
        session.addIngredient(Ingredient.BEER);
        session.serve(0);
        session.update(2);
        assertNotNull(session.getCustomer(0));
        assertFalse(session.isOver());
        session.update(2);
        assertTrue(session.isOver());
    }

    @Test
    void impatientCustomerIsLost() {
        GameSession session = new GameSession();
        session.placeCustomer(0, new NormalCustomer(null));
        session.update(36);
        assertEquals(1, session.getLostCustomers());
    }

    @Test
    void tenLostCustomersIsADefeat() {
        GameSession session = new GameSession();
        for (int i = 0; i < 10; i++) {
            session.placeCustomer(0, new NormalCustomer(null));
            session.kick(0);
        }
        assertTrue(session.isOver());
        assertTrue(session.isDefeat());
    }

    // Pause et touches de démo

    @Test
    void pauseFreezesTimeAndKicks() {
        GameSession session = new GameSession();
        session.placeCustomer(0, new NormalCustomer(null));
        session.togglePause();
        session.update(10);
        session.kick(0);
        assertEquals(0.0, session.getElapsed());
        assertEquals(0, session.getLostCustomers());

        session.togglePause();
        session.update(10);
        assertEquals(10.0, session.getElapsed());
    }

    @Test
    void demoKeySpawnsAChild() {
        GameSession session = new GameSession();
        session.spawnChild();
        int seat = session.getOccupiedSeats().get(0);
        assertTrue(session.getCustomer(seat).isChild());
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

    @Test
    void demoKeyJumpsToTheLastFifteenSeconds() {
        GameSession session = new GameSession();
        session.countdownSkip();
        assertEquals(285.0, session.getElapsed());
    }
}
