package com.happyhour.model.customer.state;

import com.happyhour.model.customer.Customer;

/**
 * État d'un client dans son cycle de vie (pattern State) :
 * {@link WaitingState} (en attente), {@link DrinkingState} (boit),
 * {@link LeftState} (parti).
 * <p>
 * Chaque état décide de ce qui se passe à chaque image et de l'état suivant.
 * Par défaut, un état ne peut pas être servi, n'est pas parti et n'est pas perdu.
 */
public abstract class CustomerState {

    /**
     * Crée un état.
     */
    public CustomerState() {
    }

    /**
     * Fait avancer le client dans cet état.
     *
     * @param customer  le client concerné
     * @param deltaTime le temps écoulé depuis la dernière image, en secondes
     */
    public abstract void update(Customer customer, double deltaTime);

    /**
     * Indique si le client peut être servi dans cet état.
     *
     * @return false par défaut
     */
    public boolean canBeServed() {
        return false;
    }

    /**
     * Indique si le client a quitté le bar.
     *
     * @return false par défaut
     */
    public boolean hasLeft() {
        return false;
    }

    /**
     * Indique si le client compte comme client perdu.
     *
     * @return false par défaut
     */
    public boolean isLost() {
        return false;
    }
}
