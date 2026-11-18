package com.happyhour.model.customer;

import com.happyhour.model.GameConfig;
import com.happyhour.model.drink.Drink;

import javafx.scene.image.Image;

public class GenerousCustomer extends Customer {

    public GenerousCustomer(Drink order) {
        super(order, GameConfig.LONG_PATIENCE);
    }

    @Override
    public double computeTip(double baseTip, double speedBonus) {
        return (baseTip + speedBonus) * 2;
    }

    @Override
    public double getDrinkingDuration() {
        return GameConfig.GENEROUS_DRINKING_TIME;
    }
    
    @Override
    public Image getSprite() {
        return new Image(getClass().getResourceAsStream("/images/customerGenerous.png"),160,350,true, false);
    }
}