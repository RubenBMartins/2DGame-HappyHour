package com.happyhour.model.customer;

import com.happyhour.model.drink.Drink;

public class GenerousCustomer extends Customer {

    public GenerousCustomer(Drink order) {
        super(order, 30);
    }

    @Override
    public int getTip(int grade) {
        return grade;
    }

    @Override
    public double getDrinkingDuration() {
        return 6;
    }
}