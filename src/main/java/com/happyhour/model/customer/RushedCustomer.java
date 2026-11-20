package com.happyhour.model.customer;

import com.happyhour.model.GameConfig;
import com.happyhour.model.drink.Drink;

/**
 * Client Pressé : patience courte, mais bonus vitesse doublé.
 */
public class RushedCustomer extends Customer {

    /**
     * Crée un client pressé.
     *
     * @param order la boisson commandée (peut être null)
     */
    public RushedCustomer(Drink order) {
        super(order, GameConfig.SHORT_PATIENCE);
    }

    /**
     * Pourboire = pourboire de base + bonus vitesse × 2.
     *
     * @param baseTip    le pourboire de base
     * @param speedBonus le bonus vitesse
     * @return le pourboire final en euros
     */
    @Override
    public double computeTip(double baseTip, double speedBonus) {
        return baseTip + speedBonus * 2;
    }
}
