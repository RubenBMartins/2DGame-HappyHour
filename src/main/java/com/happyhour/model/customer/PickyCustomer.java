package com.happyhour.model.customer;

import com.happyhour.model.GameConfig;
import com.happyhour.model.drink.Drink;

import javafx.scene.image.Image;

public class PickyCustomer extends Customer {

    public PickyCustomer(Drink order) {
        super(order, GameConfig.NORMAL_PATIENCE);
    }

    @Override
    public double computeTip(double baseTip, double speedBonus) {
        return (baseTip + speedBonus) / 2;
    }

    @Override
    public boolean accepts(int grade) {
        return grade >= GameConfig.PICKY_MIN_GRADE;
    }
    
    @Override
    public Image getSprite() {
        return new Image(getClass().getResourceAsStream("/images/customerStrict.png"),160,350,true, false);
    }
}