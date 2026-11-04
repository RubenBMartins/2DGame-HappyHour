package com.happyhour.model.customer;

import com.happyhour.model.drink.Drink;

public class NormalCustomer extends Customer {

    public NormalCustomer(Drink order) {
        super(order, 35);
    }

    @Override
    public double computeTip(double baseTip, double speedBonus) {
        return baseTip + speedBonus;
    }
}