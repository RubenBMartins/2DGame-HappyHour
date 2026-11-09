package com.happyhour.model.customer;

import com.happyhour.model.GameConfig;
import com.happyhour.model.drink.Drink;

public class GenerousCustomer extends Customer {

    public GenerousCustomer(Drink order) {
        super(order, GameConfig.LONG_PATIENCE);
    }

    @Override
    public double computeTip(double baseTip, double speedBonus) {
        return (baseTip + speedBonus) * 2;
    }

    @Override
    public double getDrinkingDuration() {
        return GameConfig.GENEROUS_DRINKING_TIME;
    }
}