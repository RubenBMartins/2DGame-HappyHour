package com.happyhour.model.customer;

import com.happyhour.model.drink.Drink;

public class RushedCustomer extends Customer {

    public RushedCustomer(Drink order) {
        super(order, 20);
    }

    @Override
    public double computeTip(double baseTip, double speedBonus) {
        return baseTip + speedBonus * 2;
    }
}