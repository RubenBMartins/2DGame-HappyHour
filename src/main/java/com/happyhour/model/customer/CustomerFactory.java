package com.happyhour.model.customer;

import java.util.Random;

import com.happyhour.model.GameConfig;
import com.happyhour.model.drink.Drink;

public class CustomerFactory {
    private Random random = new Random();
    private boolean pickyArrived = false;
    private boolean rushedArrived = false;

    public Customer createCustomer(Drink order, double elapsed, boolean childrenAllowed) {
        if (elapsed >= GameConfig.FORCE_TIME && !pickyArrived) {
            pickyArrived = true;
            return new PickyCustomer(order);
        }
        if (elapsed >= GameConfig.FORCE_TIME && !rushedArrived) {
            rushedArrived = true;
            return new RushedCustomer(order);
        }

        int roll = random.nextInt(100);
        int limit = GameConfig.NORMAL_RATE;
        if (roll < limit) {
            return new NormalCustomer(order);
        }
        limit = limit + GameConfig.RUSHED_RATE;
        if (roll < limit) {
            rushedArrived = true;
            return new RushedCustomer(order);
        }
        limit = limit + GameConfig.PICKY_RATE;
        if (roll < limit) {
            pickyArrived = true;
            return new PickyCustomer(order);
        }
        limit = limit + GameConfig.GENEROUS_RATE;
        if (roll < limit) {
            return new GenerousCustomer(order);
        }

        if (!childrenAllowed) {
            return new NormalCustomer(order);
        }
        return new ChildCustomer(order);
    }
}