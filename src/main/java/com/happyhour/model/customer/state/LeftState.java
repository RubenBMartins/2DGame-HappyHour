package com.happyhour.model.customer.state;

import com.happyhour.model.customer.Customer;

/**
 * État « parti » : le client a quitté le bar. C'est l'état final.
 * Il retient si le client est parti fâché (perdu) ou content.
 */
public class LeftState extends CustomerState {
    private boolean lost;

    /**
     * Crée l'état « parti ».
     *
     * @param lost true si le client est parti fâché (patience épuisée ou coup de pied)
     */
    public LeftState(boolean lost) {
        this.lost = lost;
    }

    /**
     * Ne fait rien : le client est déjà parti.
     *
     * @param customer  le client concerné
     * @param deltaTime le temps écoulé, en secondes
     */
    @Override
    public void update(Customer customer, double deltaTime) {
    }

    /**
     * Le client est parti.
     *
     * @return toujours true
     */
    @Override
    public boolean hasLeft() {
        return true;
    }

    /**
     * Indique si le client est parti fâché.
     *
     * @return true si le client compte comme client perdu
     */
    @Override
    public boolean isLost() {
        return lost;
    }
}
