package com.happyhour.model.customer.state;

import com.happyhour.model.customer.Customer;

/**
 * État « boit » : le client a été servi et boit pendant un temps donné.
 * Ensuite, il part content ({@link LeftState}, pas perdu) et libère sa place.
 */
public class DrinkingState extends CustomerState {
    private double timeLeft;

    /**
     * Crée l'état « boit ».
     *
     * @param duration le temps que le client va passer à boire, en secondes
     */
    public DrinkingState(double duration) {
        timeLeft = duration;
    }

    /**
     * Fait baisser le temps restant ; à zéro, le client part sans être perdu.
     *
     * @param customer  le client concerné
     * @param deltaTime le temps écoulé, en secondes
     */
    @Override
    public void update(Customer customer, double deltaTime) {
        timeLeft = timeLeft - deltaTime;
        if (timeLeft <= 0) {
            customer.setState(new LeftState(false));
        }
    }
}
