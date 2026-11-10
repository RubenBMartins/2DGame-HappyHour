package com.happyhour.model.customer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CustomerFactoryTest {

    @Test
    void factoryCreatesAWaitingCustomer() {
        CustomerFactory factory = new CustomerFactory();
        Customer customer = factory.createCustomer(0, true);
        assertNotNull(customer);
        assertTrue(customer.canBeServed());
    }

    @Test
    void generousCustomerDrinksLonger() {
        Customer customer = new GenerousCustomer(null);
        customer.serve();
        customer.update(3);
        assertFalse(customer.hasLeft());
        customer.update(3);
        assertTrue(customer.hasLeft());
    }

    @Test
    void noChildDuringOpening() {
        CustomerFactory factory = new CustomerFactory();
        for (int i = 0; i < 500; i++) {
            Customer customer = factory.createCustomer(0, false);
            assertFalse(customer.isChild());
        }
    }

    @Test
    void pickyThenRushedAreForcedAtHalfPastTwo() {
        CustomerFactory factory = new CustomerFactory();
        Customer first = factory.createCustomer(150, true);
        Customer second = factory.createCustomer(150, true);
        assertTrue(first instanceof PickyCustomer);
        assertTrue(second instanceof RushedCustomer);
    }
}