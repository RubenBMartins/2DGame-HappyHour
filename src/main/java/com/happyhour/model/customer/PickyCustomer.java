package com.happyhour.model.customer;

import com.happyhour.model.drink.Drink;

public class PickyCustomer extends Customer {

    public PickyCustomer(Drink order) {
        super(order, 35);
    }

    @Override
    public double computeTip(double baseTip, double speedBonus) {
        return (baseTip + speedBonus) / 2;
    }

    @Override
    public boolean accepts(int grade) {
        return grade >= 8;
    }
}