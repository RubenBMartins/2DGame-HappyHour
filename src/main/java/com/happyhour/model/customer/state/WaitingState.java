package com.happyhour.model.customer.state;

import com.happyhour.model.customer.Customer;

public class WaitingState extends CustomerState {

    @Override
    public void update(Customer customer, double deltaTime) {
        customer.reducePatience(deltaTime);
        if (customer.getPatience() <= 0) {
            customer.setState(new LeftState(true));
        }
    }

    @Override
    public boolean canBeServed() {
        return true;
    }
}
