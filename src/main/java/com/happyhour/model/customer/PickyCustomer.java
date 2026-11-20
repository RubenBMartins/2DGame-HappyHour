package com.happyhour.model.customer;

import com.happyhour.model.GameConfig;
import com.happyhour.model.drink.Drink;

/**
 * Client Difficile : commande toujours un cocktail shaken, divise le pourboire
 * par 2 et refuse toute boisson notée moins de 8/10.
 */
public class PickyCustomer extends Customer {

    /**
     * Crée un client difficile.
     *
     * @param order la boisson commandée (peut être null)
     */
    public PickyCustomer(Drink order) {
        super(order, GameConfig.NORMAL_PATIENCE);
    }

    /**
     * Pourboire = (pourboire de base + bonus vitesse) / 2.
     *
     * @param baseTip    le pourboire de base
     * @param speedBonus le bonus vitesse
     * @return le pourboire final en euros
     */
    @Override
    public double computeTip(double baseTip, double speedBonus) {
        return (baseTip + speedBonus) / 2;
    }

    /**
     * Refuse la boisson si sa note est inférieure à 8/10.
     * Le client part alors sans payer et compte comme perdu.
     *
     * @param grade la note de la boisson, sur 10
     * @return true si la note est d'au moins 8/10
     */
    @Override
    public boolean accepts(int grade) {
        return grade >= GameConfig.PICKY_MIN_GRADE;
    }
}
