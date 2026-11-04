package com.happyhour.model.customer;

public class DrinkingState extends CustomerState {
    private double timeLeft;

    public DrinkingState(double duration) {
        timeLeft = duration;
    }

    @Override
    public void update(Customer customer, double deltaTime) {
        timeLeft = timeLeft - deltaTime;
        if (timeLeft <= 0) {
            customer.setState(new LeftState(false));
        }
    }
}