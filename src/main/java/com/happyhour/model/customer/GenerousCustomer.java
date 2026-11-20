package com.happyhour.model.customer;

import com.happyhour.model.GameConfig;
import com.happyhour.model.drink.Drink;

/**
 * Client Généreux : patience longue, pourboire doublé,
 * mais il boit plus longtemps (6 s au lieu de 3 s).
 */
public class GenerousCustomer extends Customer {

    /**
     * Crée un client généreux.
     *
     * @param order la boisson commandée (peut être null)
     */
    public GenerousCustomer(Drink order) {
        super(order, GameConfig.LONG_PATIENCE);
    }

    /**
     * Pourboire = (pourboire de base + bonus vitesse) × 2.
     *
     * @param baseTip    le pourboire de base
     * @param speedBonus le bonus vitesse
     * @return le pourboire final en euros
     */
    @Override
    public double computeTip(double baseTip, double speedBonus) {
        return (baseTip + speedBonus) * 2;
    }

    /**
     * Le client généreux boit plus longtemps et occupe sa place plus longtemps.
     *
     * @return la durée en secondes (6 s)
     */
    @Override
    public double getDrinkingDuration() {
        return GameConfig.GENEROUS_DRINKING_TIME;
    }
}
