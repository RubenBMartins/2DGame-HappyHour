package com.happyhour.model.customer;

import java.util.Random;

import com.happyhour.model.drink.Drink;

public class CustomerFactory {
    private Random random = new Random();

    public Customer createCustomer(Drink order) {
        int roll = random.nextInt(100);
        if (roll < 60) {
            return new NormalCustomer(order);
        } else if (roll < 75) {
            return new RushedCustomer(order);
        } else if (roll < 85) {
            return new PickyCustomer(order);
        } else if (roll < 95) {
            return new GenerousCustomer(order);
        }
        return new ChildCustomer(order);
    }
}