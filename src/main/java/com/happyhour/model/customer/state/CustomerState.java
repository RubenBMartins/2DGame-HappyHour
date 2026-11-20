package com.happyhour.model.customer.state;

import com.happyhour.model.customer.Customer;

public abstract class CustomerState {

    public abstract void update(Customer customer, double deltaTime);

    public boolean canBeServed() {
        return false;
    }

    public boolean hasLeft() {
        return false;
    }

    public boolean isLost() {
        return false;
    }
}
