package com.happyhour.model.customer;

import com.happyhour.model.drink.Drink;

public abstract class Customer {
    private Drink order;
    private double patience;
    private double drinkingTime = 0;
    private CustomerState state = CustomerState.WAITING;

    public Customer(Drink order, double patience) {
        this.order = order;
        this.patience = patience;
    }

    public void update(double deltaTime) {
        if (state == CustomerState.WAITING) {
            patience = patience - deltaTime;
            if (patience <= 0) {
                state = CustomerState.LEFT_ANGRY;
            }
        } else if (state == CustomerState.DRINKING) {
            drinkingTime = drinkingTime - deltaTime;
            if (drinkingTime <= 0) {
                state = CustomerState.LEFT_HAPPY;
            }
        }
    }

    public void startDrinking() {
        state = CustomerState.DRINKING;
        drinkingTime = getDrinkingDuration();
    }

    public double getDrinkingDuration() {
        return 3;
    }

    public Drink getOrder() {
        return order;
    }

    public double getPatience() {
        return patience;
    }

    public CustomerState getState() {
        return state;
    }

    public abstract int getTip(int grade);
}