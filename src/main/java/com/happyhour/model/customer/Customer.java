package com.happyhour.model.customer;

import com.happyhour.model.GameConfig;
import com.happyhour.model.drink.Drink;

public abstract class Customer {
    private Drink order;
    private double patience;
    private double maxPatience;
    private double patienceSpeed = 1;
    private CustomerState state = new WaitingState();

    public Customer(Drink order, double patience) {
        this.order = order;
        this.patience = patience;
        this.maxPatience = patience;
    }

    public void update(double deltaTime) {
        state.update(this, deltaTime);
    }

    public void serve() {
        if (canBeServed()) {
            state = new DrinkingState(getDrinkingDuration());
        }
    }

    public void kick() {
        state = new LeftState(true);
    }

    public boolean canBeServed() {
        return state.canBeServed();
    }

    public boolean hasLeft() {
        return state.hasLeft();
    }

    public boolean isLost() {
        return state.isLost();
    }

    public boolean isServedQuickly() {
        return patience > maxPatience / 2;
    }

    public boolean accepts(int grade) {
        return true;
    }

    public boolean isChild() {
        return false;
    }

    public void reducePatience(double amount) {
        patience = patience - amount * patienceSpeed;
    }

    public void setPatienceSpeed(double patienceSpeed) {
        this.patienceSpeed = patienceSpeed;
    }

    public void setState(CustomerState state) {
        this.state = state;
    }

    public double getDrinkingDuration() {
        return GameConfig.DRINKING_TIME;
    }

    public Drink getOrder() {
        return order;
    }

    public void setOrder(Drink order) {
        this.order = order;
    }

    public double getPatience() {
        return patience;
    }

    public double getMaxPatience() {
        return maxPatience;
    }

    public CustomerState getState() {
        return state;
    }

    public abstract double computeTip(double baseTip, double speedBonus);
}