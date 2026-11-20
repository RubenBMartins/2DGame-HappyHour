package com.happyhour.model.customer.state;

import com.happyhour.model.customer.Customer;

public class LeftState extends CustomerState {
    private boolean lost;

    public LeftState(boolean lost) {
        this.lost = lost;
    }

    @Override
    public void update(Customer customer, double deltaTime) {
    }

    @Override
    public boolean hasLeft() {
        return true;
    }

    @Override
    public boolean isLost() {
        return lost;
    }
}
