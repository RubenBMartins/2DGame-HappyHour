package com.happyhour.model.game;

import com.happyhour.model.customer.Customer;

/**
 * Écouteur des événements de la partie (pattern Observer).
 * <p>
 * La {@link GameSession} prévient ses écouteurs à chaque événement important.
 * L'affichage ({@code GameScreen}) implémente cette interface : le modèle
 * n'a donc pas besoin de connaître JavaFX.
 */
public interface GameListener {
    /**
     * Appelée quand la caisse change, après un service.
     *
     * @param tips le nouveau total de la caisse, en euros
     */
    void onScoreChanged(double tips);

    /**
     * Appelée quand un client s'assoit au comptoir.
     *
     * @param seat     la place du client (0 à 5)
     * @param customer le client arrivé
     */
    void onCustomerArrived(int seat, Customer customer);

    /**
     * Appelée quand un client quitte sa place (content, fâché ou expulsé).
     *
     * @param seat     la place libérée
     * @param customer le client parti
     */
    void onCustomerLeft(int seat, Customer customer);

    /**
     * Appelée quand un client est servi et paie.
     *
     * @param seat   la place du client
     * @param amount le gain total (prix + pourboire), en euros
     */
    void onCustomerServed(int seat, double amount);

    /**
     * Appelée quand la police entre, après qu'un enfant a été servi.
     */
    void onPoliceRaid();

    /**
     * Appelée à la fin de la soirée.
     *
     * @param defeat true si la partie est perdue (10 clients perdus ou enfant servi)
     */
    void onEveningOver(boolean defeat);

    /**
     * Appelée quand la soirée change de phase.
     *
     * @param phase la nouvelle phase
     */
    void onPhaseChanged(Phase phase);
}
