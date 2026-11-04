package com.happyhour.model.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CustomerTypesTest {

    @Test
    void normalCustomerAddsBaseTipAndBonus() {
        Customer customer = new NormalCustomer(null);
        assertEquals(4.0, customer.computeTip(3, 1));
    }

    @Test
    void rushedCustomerDoublesSpeedBonus() {
        Customer customer = new RushedCustomer(null);
        assertEquals(5.0, customer.computeTip(3, 1));
    }

    @Test
    void pickyCustomerHalvesTipAndRefusesBelowEight() {
        Customer customer = new PickyCustomer(null);
        assertEquals(2.0, customer.computeTip(3, 1));
        assertFalse(customer.accepts(7));
        assertTrue(customer.accepts(8));
    }

    @Test
    void generousCustomerDoublesTip() {
        Customer customer = new GenerousCustomer(null);
        assertEquals(8.0, customer.computeTip(3, 1));
    }

    @Test
    void childIsRecognised() {
        Customer child = new ChildCustomer(null);
        assertTrue(child.isChild());
        assertFalse(new NormalCustomer(null).isChild());
    }

    @Test
    void speedBonusOnlyBeforeHalfPatience() {
        Customer customer = new NormalCustomer(null);
        assertTrue(customer.isServedQuickly());
        customer.update(20);
        assertFalse(customer.isServedQuickly());
    }
}