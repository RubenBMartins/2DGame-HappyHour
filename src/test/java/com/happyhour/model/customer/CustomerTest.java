package com.happyhour.model.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CustomerTest {

    @Test
    void newCustomerIsWaiting() {
        Customer customer = new NormalCustomer(null);
        assertEquals(CustomerState.WAITING, customer.getState());
    }

    @Test
    void customerLeavesAngryWhenPatienceRunsOut() {
        Customer customer = new NormalCustomer(null);
        customer.update(31);
        assertEquals(CustomerState.LEFT_ANGRY, customer.getState());
    }

    @Test
    void customerLeavesHappyAfterDrinking() {
        Customer customer = new NormalCustomer(null);
        customer.startDrinking();
        assertEquals(CustomerState.DRINKING, customer.getState());
        customer.update(3);
        assertEquals(CustomerState.LEFT_HAPPY, customer.getState());
    }
}