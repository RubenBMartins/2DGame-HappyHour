package com.happyhour.model.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class CustomerFactoryTest {

    @Test
    void factoryCreatesAWaitingCustomer() {
        CustomerFactory factory = new CustomerFactory();
        Customer customer = factory.createCustomer(null);
        assertNotNull(customer);
        assertEquals(CustomerState.WAITING, customer.getState());
    }

    @Test
    void generousCustomerDrinksLonger() {
        Customer customer = new GenerousCustomer(null);
        customer.startDrinking();
        customer.update(3);
        assertEquals(CustomerState.DRINKING, customer.getState());
        customer.update(3);
        assertEquals(CustomerState.LEFT_HAPPY, customer.getState());
    }
}