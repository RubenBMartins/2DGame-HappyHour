package com.happyhour.model.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CustomerTest {

    // États d'un client (pattern State)

    @Test
    void newCustomerIsWaiting() {
        Customer customer = new NormalCustomer(null);
        assertTrue(customer.canBeServed());
        assertFalse(customer.hasLeft());
    }

    @Test
    void customerIsLostWhenPatienceRunsOut() {
        Customer customer = new NormalCustomer(null);
        customer.update(36);
        assertTrue(customer.hasLeft());
        assertTrue(customer.isLost());
    }

    @Test
    void customerLeavesHappyAfterDrinking() {
        Customer customer = new NormalCustomer(null);
        customer.serve();
        assertFalse(customer.canBeServed());
        assertFalse(customer.hasLeft());
        customer.update(3);
        assertTrue(customer.hasLeft());
        assertFalse(customer.isLost());
    }

    @Test
    void kickedCustomerIsLost() {
        Customer customer = new NormalCustomer(null);
        customer.kick();
        assertTrue(customer.hasLeft());
        assertTrue(customer.isLost());
    }

    @Test
    void patienceDropsFasterDuringRush() {
        Customer customer = new NormalCustomer(null);
        customer.setPatienceSpeed(1.25);
        customer.update(20);
        assertEquals(10.0, customer.getPatience());
    }

    @Test
    void speedBonusOnlyBeforeHalfPatience() {
        Customer customer = new NormalCustomer(null);
        assertTrue(customer.isServedQuickly());
        customer.update(20);
        assertFalse(customer.isServedQuickly());
    }

    // Les 5 types de clients

    @Test
    void normalCustomerAddsBaseTipAndBonus() {
        assertEquals(4.0, new NormalCustomer(null).computeTip(3, 1));
    }

    @Test
    void rushedCustomerDoublesSpeedBonusButIsImpatient() {
        Customer customer = new RushedCustomer(null);
        assertEquals(5.0, customer.computeTip(3, 1));
        assertEquals(20.0, customer.getMaxPatience());
    }

    @Test
    void pickyCustomerHalvesTipAndRefusesBelowEight() {
        Customer customer = new PickyCustomer(null);
        assertEquals(2.0, customer.computeTip(3, 1));
        assertFalse(customer.accepts(7));
        assertTrue(customer.accepts(8));
    }

    @Test
    void generousCustomerDoublesTipAndDrinksLonger() {
        Customer customer = new GenerousCustomer(null);
        assertEquals(8.0, customer.computeTip(3, 1));
        customer.serve();
        customer.update(3);
        assertFalse(customer.hasLeft());
        customer.update(3);
        assertTrue(customer.hasLeft());
    }

    @Test
    void childGivesNoTip() {
        Customer child = new ChildCustomer(null);
        assertTrue(child.isChild());
        assertFalse(new NormalCustomer(null).isChild());
        assertEquals(0.0, child.computeTip(3, 1));
    }

    // Génération des clients (pattern Factory)

    @Test
    void factoryCreatesAWaitingCustomer() {
        Customer customer = new CustomerFactory().createCustomer(0, true);
        assertTrue(customer.canBeServed());
    }

    @Test
    void noChildDuringOpening() {
        CustomerFactory factory = new CustomerFactory();
        for (int i = 0; i < 500; i++) {
            assertFalse(factory.createCustomer(0, false).isChild());
        }
    }

    @Test
    void pickyThenRushedAreForcedAtHalfPastTwo() {
        CustomerFactory factory = new CustomerFactory();
        assertTrue(factory.createCustomer(150, true) instanceof PickyCustomer);
        assertTrue(factory.createCustomer(150, true) instanceof RushedCustomer);
    }
}
