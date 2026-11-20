package com.happyhour.model.customer;

import java.util.Random;

import com.happyhour.model.GameConfig;

/**
 * Fabrique des clients au hasard selon les proportions de {@link GameConfig}
 * (pattern Factory) : 60 % Normal, 15 % Pressé, 10 % Difficile, 10 % Généreux,
 * 5 % Enfant.
 * <p>
 * Elle garantit aussi au moins un client Difficile et un Pressé par soirée :
 * s'ils ne sont pas encore venus à 2:30, le prochain client est forcé.
 */
public class CustomerFactory {
    private Random random = new Random();
    private boolean pickyArrived = false;
    private boolean rushedArrived = false;

    /**
     * Crée la fabrique, au début d'une soirée.
     */
    public CustomerFactory() {
    }

    /**
     * Crée un nouveau client, sans commande (elle est donnée ensuite).
     *
     * @param elapsed         le temps écoulé depuis le début de la soirée, en secondes
     * @param childrenAllowed false pendant l'ouverture : un enfant tiré est
     *                        alors remplacé par un client normal
     * @return le client créé
     */
    public Customer createCustomer(double elapsed, boolean childrenAllowed) {
        if (elapsed >= GameConfig.FORCE_TIME && !pickyArrived) {
            pickyArrived = true;
            return new PickyCustomer(null);
        }
        if (elapsed >= GameConfig.FORCE_TIME && !rushedArrived) {
            rushedArrived = true;
            return new RushedCustomer(null);
        }

        int roll = random.nextInt(100);
        int limit = GameConfig.NORMAL_RATE;
        if (roll < limit) {
            return new NormalCustomer(null);
        }
        limit = limit + GameConfig.RUSHED_RATE;
        if (roll < limit) {
            rushedArrived = true;
            return new RushedCustomer(null);
        }
        limit = limit + GameConfig.PICKY_RATE;
        if (roll < limit) {
            pickyArrived = true;
            return new PickyCustomer(null);
        }
        limit = limit + GameConfig.GENEROUS_RATE;
        if (roll < limit) {
            return new GenerousCustomer(null);
        }

        if (!childrenAllowed) {
            return new NormalCustomer(null);
        }
        return new ChildCustomer(null);
    }
}
