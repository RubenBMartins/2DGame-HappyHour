package com.happyhour.model.customer;

import com.happyhour.model.drink.Drink;

public class NormalCustomer extends Customer {

    public NormalCustomer(Drink order) {
        super(order, 30);
    }

    @Override
    public int getTip(int grade) {
        return grade / 2;
    }
}