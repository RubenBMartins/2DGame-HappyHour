package com.happyhour.model.customer;

import com.happyhour.model.GameConfig;
import com.happyhour.model.drink.Drink;

/**
 * Enfant déguisé qui s'est glissé au comptoir. Il commande comme les autres,
 * mais ne doit jamais être servi : le servir déclenche la descente de police.
 * Il faut l'expulser d'un coup de pied.
 */
public class ChildCustomer extends Customer {

    /**
     * Crée un enfant.
     *
     * @param order la boisson commandée (peut être null)
     */
    public ChildCustomer(Drink order) {
        super(order, GameConfig.NORMAL_PATIENCE);
    }

    /**
     * Un enfant ne laisse jamais de pourboire.
     *
     * @param baseTip    le pourboire de base
     * @param speedBonus le bonus vitesse
     * @return toujours 0
     */
    @Override
    public double computeTip(double baseTip, double speedBonus) {
        return 0;
    }

    /**
     * Indique que ce client est un enfant.
     *
     * @return toujours true
     */
    @Override
    public boolean isChild() {
        return true;
    }
}
