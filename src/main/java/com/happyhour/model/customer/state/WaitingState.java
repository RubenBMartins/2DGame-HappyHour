package com.happyhour.model.customer.state;

import com.happyhour.model.customer.Customer;

/**
 * État « en attente » : le client attend sa boisson et sa patience baisse.
 * Quand elle arrive à zéro, il part fâché ({@link LeftState}, perdu).
 * C'est le seul état où le client peut être servi.
 */
public class WaitingState extends CustomerState {

    /**
     * Crée l'état « en attente ».
     */
    public WaitingState() {
    }

    /**
     * Fait baisser la patience ; à zéro, le client part et compte comme perdu.
     *
     * @param customer  le client concerné
     * @param deltaTime le temps écoulé, en secondes
     */
    @Override
    public void update(Customer customer, double deltaTime) {
        customer.reducePatience(deltaTime);
        if (customer.getPatience() <= 0) {
            customer.setState(new LeftState(true));
        }
    }

    /**
     * Le client en attente peut être servi.
     *
     * @return toujours true
     */
    @Override
    public boolean canBeServed() {
        return true;
    }
}
