package com.happyhour.model.customer;

import java.util.Random;

import com.happyhour.model.GameConfig;

public class CustomerFactory {
    private Random random = new Random();
    private boolean pickyArrived = false;
    private boolean rushedArrived = false;

    public Customer createCustomer(double elapsed, boolean childrenAllowed) {
        if (elapsed >= GameConfig.FORCE_TIME && !pickyArrived) {
            pickyArrived = true;
            return new PickyCustomer(null);
        }
        if (elapsed >= GameConfig.FORCE_TIME && !rushedArrived) {
            rushedArrived = true;
            return new RushedCustomer(null);
        }

        int roll = random.nextInt(100);
        int limit = GameConfig.NORMAL_RATE;
        if (roll < limit) {
            return new NormalCustomer(null);
        }
        limit = limit + GameConfig.RUSHED_RATE;
        if (roll < limit) {
            rushedArrived = true;
            return new RushedCustomer(null);
        }
        limit = limit + GameConfig.PICKY_RATE;
        if (roll < limit) {
            pickyArrived = true;
            return new PickyCustomer(null);
        }
        limit = limit + GameConfig.GENEROUS_RATE;
        if (roll < limit) {
            return new GenerousCustomer(null);
        }

        if (!childrenAllowed) {
            return new NormalCustomer(null);
        }
        return new ChildCustomer(null);
    }
}