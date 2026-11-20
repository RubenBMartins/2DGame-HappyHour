package com.happyhour.model.customer;

import com.happyhour.model.GameConfig;
import com.happyhour.model.drink.Drink;

/**
 * Client Normal : patience normale, aucun effet sur le pourboire.
 * C'est le client le plus fréquent.
 */
public class NormalCustomer extends Customer {

    /**
     * Crée un client normal.
     *
     * @param order la boisson commandée (peut être null)
     */
    public NormalCustomer(Drink order) {
        super(order, GameConfig.NORMAL_PATIENCE);
    }

    /**
     * Pourboire = pourboire de base + bonus vitesse.
     *
     * @param baseTip    le pourboire de base
     * @param speedBonus le bonus vitesse
     * @return le pourboire final en euros
     */
    @Override
    public double computeTip(double baseTip, double speedBonus) {
        return baseTip + speedBonus;
    }
}
