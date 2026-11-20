package com.happyhour.model.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.happyhour.model.customer.ChildCustomer;
import com.happyhour.model.customer.Customer;
import com.happyhour.model.customer.NormalCustomer;
import com.happyhour.model.customer.PickyCustomer;
import com.happyhour.model.drink.Beer;
import com.happyhour.model.drink.Glass;
import com.happyhour.model.drink.Ingredient;

public class ServiceTest {

    private GameSession sessionWith(Customer customer) {
        GameSession session = new GameSession();
        session.placeCustomer(0, customer);
        return session;
    }

    // Bière parfaite : 10/10
    private void prepareDrink(GameSession session) {
        session.chooseGlass(Glass.PINT);
        session.addIngredient(Ingredient.BEER);
    }

    // Du vin dans une pinte au lieu d'une bière : 6/10
    private void prepareWineInPint(GameSession session) {
        session.chooseGlass(Glass.PINT);
        session.addIngredient(Ingredient.WHITE_WINE);
    }

    // De la bière dans un verre à vin : 4/10
    private void prepareBeerInWineGlass(GameSession session) {
        session.chooseGlass(Glass.WINE_GLASS);
        session.addIngredient(Ingredient.BEER);
    }

    // Servir

    @Test
    void servingGivesPriceAndTip() {
        GameSession session = sessionWith(new NormalCustomer(new Beer()));
        prepareDrink(session);
        session.serve(0);
        assertEquals(7.0, session.getRegister().getRevenue());
        assertEquals(2.5, session.getTips());
    }

    @Test
    void priceFollowsTheGrade() {
        GameSession session = sessionWith(new NormalCustomer(new Beer()));
        prepareWineInPint(session);
        session.serve(0);
        assertEquals(4.2, session.getRegister().getRevenue());
    }

    @Test
    void cannotServeWithoutGlassAndLiquid() {
        GameSession session = sessionWith(new NormalCustomer(new Beer()));
        session.serve(0);
        session.chooseGlass(Glass.PINT);
        session.serve(0);
        session.addIngredient(Ingredient.ICE);
        session.serve(0);
        assertTrue(session.getCustomer(0).canBeServed());
        assertEquals(0, session.getRegister().getServedCustomers());
    }

    @Test
    void servingAnEmptySeatKeepsTheDrink() {
        GameSession session = new GameSession();
        prepareDrink(session);
        session.serve(0);
        assertTrue(session.getPreparation().isReady());
    }

    @Test
    void servedCustomerKeepsTheDrinkAndHandsAreEmpty() {
        Customer customer = new NormalCustomer(new Beer());
        GameSession session = sessionWith(customer);
        prepareDrink(session);
        session.serve(0);
        assertTrue(session.getPreparation().isEmpty());
        assertEquals(Glass.PINT, customer.getServedPreparation().getGlass());
        assertNull(session.getOrder(0));
    }

    @Test
    void pickyCustomerRefusesBadDrink() {
        GameSession session = sessionWith(new PickyCustomer(new Beer()));
        prepareWineInPint(session);
        session.serve(0);
        assertEquals(1, session.getLostCustomers());
        assertEquals(0.0, session.getRegister().getTotal());
        assertNull(session.getCustomer(0));
    }

    @Test
    void pickyCustomerAcceptsGoodDrink() {
        GameSession session = sessionWith(new PickyCustomer(new Beer()));
        prepareDrink(session);
        session.serve(0);
        assertEquals(0, session.getLostCustomers());
        assertEquals(1.25, session.getTips());
    }

    // Messages après le service

    @Test
    void feedbackDependsOnTheGrade() {
        GameSession session = sessionWith(new NormalCustomer(new Beer()));
        prepareDrink(session);
        session.serve(0);
        assertEquals("PARFAIT ! 10/10", session.getFeedback());

        session.placeCustomer(1, new NormalCustomer(new Beer()));
        prepareWineInPint(session);
        session.serve(1);
        assertEquals("6/10 : 60 % DU PRIX", session.getFeedback());

        session.placeCustomer(2, new NormalCustomer(new Beer()));
        prepareBeerInWineGlass(session);
        session.serve(2);
        assertEquals("NUL ! 4/10", session.getFeedback());
    }

    @Test
    void feedbackDisappears() {
        GameSession session = sessionWith(new NormalCustomer(new Beer()));
        prepareDrink(session);
        session.serve(0);
        session.update(2);
        assertNull(session.getFeedback());
    }

    // Coup de pied

    @Test
    void kickedCustomerIsLost() {
        GameSession session = sessionWith(new NormalCustomer(null));
        session.kick(0);
        assertEquals(1, session.getLostCustomers());
        assertNull(session.getCustomer(0));
    }

    @Test
    void kickingDrinkingCustomerKeepsTheMoney() {
        GameSession session = sessionWith(new NormalCustomer(new Beer()));
        prepareDrink(session);
        session.serve(0);
        session.kick(0);
        assertEquals(1, session.getLostCustomers());
        assertEquals(2.5, session.getTips());
    }

    @Test
    void kickingChildIsNotLost() {
        GameSession session = sessionWith(new ChildCustomer(null));
        session.kick(0);
        assertEquals(0, session.getLostCustomers());
    }

    @Test
    void kickingAnEmptySeatDoesNothing() {
        GameSession session = new GameSession();
        session.kick(0);
        assertEquals(0, session.getLostCustomers());
    }

    // Police

    @Test
    void servingChildCallsThePolice() {
        GameSession session = sessionWith(new NormalCustomer(new Beer()));
        session.placeCustomer(1, new ChildCustomer(new Beer()));
        prepareDrink(session);
        session.serve(1);
        assertTrue(session.isPoliceRaidActive());
        assertNull(session.getCustomer(0));
        assertNull(session.getCustomer(1));
        assertFalse(session.isOver());

        session.update(1);
        session.update(3);
        assertTrue(session.isOver());
        assertTrue(session.isDefeat());
    }

    @Test
    void nothingWorksDuringPoliceRaid() {
        GameSession session = sessionWith(new ChildCustomer(new Beer()));
        prepareDrink(session);
        session.serve(0);
        session.chooseGlass(Glass.MARTINI);
        session.togglePause();
        session.spawnCustomer();
        assertEquals(Glass.PINT, session.getPreparation().getGlass());
        assertFalse(session.isPaused());
        assertEquals(0, session.getOccupiedSeats().size());
    }
}
