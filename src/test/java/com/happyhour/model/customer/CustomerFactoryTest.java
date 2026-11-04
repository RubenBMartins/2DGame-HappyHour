package com.happyhour.model.customer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CustomerFactoryTest {

    @Test
    void factoryCreatesAWaitingCustomer() {
        CustomerFactory factory = new CustomerFactory();
        Customer customer = factory.createCustomer(null);
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
}