package com.happyhour.model.customer;

import com.happyhour.model.GameConfig;
import com.happyhour.model.customer.state.CustomerState;
import com.happyhour.model.customer.state.DrinkingState;
import com.happyhour.model.customer.state.LeftState;
import com.happyhour.model.customer.state.WaitingState;
import com.happyhour.model.drink.Drink;
import com.happyhour.model.drink.Preparation;

/**
 * Client assis au comptoir : il commande une boisson, attend avec une patience
 * limitée, boit puis s'en va.
 * <p>
 * Classe abstraite, mère des 5 types de clients. Son cycle de vie est géré par
 * un {@link CustomerState} (pattern State) : en attente, en train de boire, parti.
 * Chaque sous-classe redéfinit son propre comportement (pourboire, refus,
 * durée de consommation, enfant).
 */
public abstract class Customer {
    private Drink order;
    private double patience;
    private double maxPatience;
    private double patienceSpeed = 1;
    private CustomerState state = new WaitingState();
    private Preparation servedPreparation;

    /**
     * Crée un client en attente.
     *
     * @param order    la boisson commandée (peut être null, puis donnée avec {@link #setOrder})
     * @param patience la patience de départ, en secondes
     */
    public Customer(Drink order, double patience) {
        this.order = order;
        this.patience = patience;
        this.maxPatience = patience;
    }

    /**
     * Fait avancer le client dans le temps, selon son état actuel.
     *
     * @param deltaTime le temps écoulé depuis la dernière image, en secondes
     */
    public void update(double deltaTime) {
        state.update(this, deltaTime);
    }

    /**
     * Sert le client : s'il attendait, il passe à l'état « boit ».
     */
    public void serve() {
        if (canBeServed()) {
            state = new DrinkingState(getDrinkingDuration());
        }
    }

    /**
     * Expulse le client d'un coup de pied : il passe directement à l'état « parti »,
     * compté comme perdu.
     */
    public void kick() {
        state = new LeftState(true);
    }

    /**
     * Indique si le client attend encore sa boisson.
     *
     * @return true si le client peut être servi
     */
    public boolean canBeServed() {
        return state.canBeServed();
    }

    /**
     * Indique si le client a quitté le bar.
     *
     * @return true si le client est parti
     */
    public boolean hasLeft() {
        return state.hasLeft();
    }

    /**
     * Indique si le client est parti fâché (patience épuisée ou coup de pied).
     *
     * @return true si le client compte comme client perdu
     */
    public boolean isLost() {
        return state.isLost();
    }

    /**
     * Indique si le client est servi vite, c'est-à-dire avant que sa jauge
     * de patience passe à l'orange (moitié de la patience).
     *
     * @return true si le client a droit au bonus vitesse
     */
    public boolean isServedQuickly() {
        return patience > maxPatience / 2;
    }

    /**
     * Indique si le client accepte une boisson de cette note.
     * Par défaut, il accepte toujours ; le client Difficile redéfinit cette méthode.
     *
     * @param grade la note de la boisson, sur 10
     * @return true si le client accepte la boisson
     */
    public boolean accepts(int grade) {
        return true;
    }

    /**
     * Indique si le client est un enfant (qui ne doit jamais être servi).
     *
     * @return false par défaut, true pour {@link ChildCustomer}
     */
    public boolean isChild() {
        return false;
    }

    /**
     * Fait baisser la patience du client, en tenant compte de sa vitesse
     * (plus rapide pendant le rush).
     *
     * @param amount le temps écoulé, en secondes
     */
    public void reducePatience(double amount) {
        patience = patience - amount * patienceSpeed;
    }

    /**
     * Change la vitesse à laquelle la patience baisse.
     *
     * @param patienceSpeed 1 en temps normal, 1,25 pendant le rush
     */
    public void setPatienceSpeed(double patienceSpeed) {
        this.patienceSpeed = patienceSpeed;
    }

    /**
     * Change l'état du client. Appelée par les états eux-mêmes
     * pour passer à l'état suivant.
     *
     * @param state le nouvel état
     */
    public void setState(CustomerState state) {
        this.state = state;
    }

    /**
     * Renvoie le temps que le client passe à boire avant de partir.
     *
     * @return la durée en secondes (3 s par défaut)
     */
    public double getDrinkingDuration() {
        return GameConfig.DRINKING_TIME;
    }

    /**
     * Renvoie la boisson commandée.
     *
     * @return la boisson commandée
     */
    public Drink getOrder() {
        return order;
    }

    /**
     * Donne sa commande au client.
     *
     * @param order la boisson commandée
     */
    public void setOrder(Drink order) {
        this.order = order;
    }

    /**
     * Renvoie la boisson que le joueur a servie au client, pour l'afficher devant lui.
     *
     * @return la boisson servie, ou null s'il n'a pas encore été servi
     */
    public Preparation getServedPreparation() {
        return servedPreparation;
    }

    /**
     * Enregistre la boisson servie au client.
     *
     * @param servedPreparation la boisson servie
     */
    public void setServedPreparation(Preparation servedPreparation) {
        this.servedPreparation = servedPreparation;
    }

    /**
     * Renvoie la patience restante.
     *
     * @return la patience restante, en secondes
     */
    public double getPatience() {
        return patience;
    }

    /**
     * Renvoie la patience de départ, utilisée pour remplir la jauge.
     *
     * @return la patience de départ, en secondes
     */
    public double getMaxPatience() {
        return maxPatience;
    }

    /**
     * Calcule le pourboire final selon le type de client.
     *
     * @param baseTip    le pourboire de base, selon la note de la boisson
     * @param speedBonus le bonus vitesse (1 € si servi vite, sinon 0)
     * @return le pourboire final en euros
     */
    public abstract double computeTip(double baseTip, double speedBonus);
}
