package com.happyhour.model.customer;

import com.happyhour.model.GameConfig;
import com.happyhour.model.drink.Drink;

import javafx.scene.image.Image;

public class RushedCustomer extends Customer {

    public RushedCustomer(Drink order) {
        super(order, GameConfig.SHORT_PATIENCE);
    }

    @Override
    public double computeTip(double baseTip, double speedBonus) {
        return baseTip + speedBonus * 2;
    }
    
    @Override
    public Image getSprite() {
        return new Image(getClass().getResourceAsStream("/images/customerHurried.png"),160,350,true, false);
    }
}