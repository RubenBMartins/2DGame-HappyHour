package com.happyhour.model.customer;

import com.happyhour.model.GameConfig;
import com.happyhour.model.drink.Drink;
import javafx.scene.image.Image;

public class ChildCustomer extends Customer {

    public ChildCustomer(Drink order) {
        super(order, GameConfig.NORMAL_PATIENCE);
    }

    @Override
    public double computeTip(double baseTip, double speedBonus) {
        return 0;
    }

    @Override
    public boolean isChild() {
        return true;
    }

    @Override
    public Image getSprite() {
        return new Image(getClass().getResourceAsStream("/images/customerChild.png"));
    }
}