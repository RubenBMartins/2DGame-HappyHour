package com.happyhour.model.customer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CustomerTest {

    @Test
    void newCustomerCanBeServed() {
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
}