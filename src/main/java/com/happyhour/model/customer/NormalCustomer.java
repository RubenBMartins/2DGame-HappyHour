package com.happyhour.model.customer;

import com.happyhour.model.GameConfig;
import com.happyhour.model.drink.Drink;

import javafx.scene.image.Image;

public class NormalCustomer extends Customer {

    public NormalCustomer(Drink order) {
        super(order, GameConfig.NORMAL_PATIENCE);
    }

    @Override
    public double computeTip(double baseTip, double speedBonus) {
        return baseTip + speedBonus;
    }
    
    @Override
    public Image getSprite() {
        return new Image(getClass().getResourceAsStream("/images/customerRegular.png"),160,350,true, false);
    }
}