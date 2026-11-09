package com.happyhour.model.customer;

import com.happyhour.model.GameConfig;
import com.happyhour.model.drink.Drink;

public class ChildCustomer extends Customer {

    public ChildCustomer(Drink order) {
        super(order, GameConfig.NORMAL_PATIENCE);
    }

    @Override
    public double computeTip(double baseTip, double speedBonus) {
        return 0;
    }

    @Override
    public boolean isChild() {
        return true;
    }
}