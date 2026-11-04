package com.happyhour.model.customer;

import java.util.Random;

import com.happyhour.model.drink.Drink;

public class CustomerFactory {
    private Random random = new Random();

    public Customer createCustomer(Drink order) {
        int type = random.nextInt(2);
        if (type == 0) {
            return new NormalCustomer(order);
        }
        return new GenerousCustomer(order);
    }
}