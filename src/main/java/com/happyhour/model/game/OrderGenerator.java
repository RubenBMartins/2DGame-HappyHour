package com.happyhour.model.game;

import java.util.Random;

import com.happyhour.model.GameConfig;
import com.happyhour.model.drink.DrinkCategory;

/**
 * Tire au hasard la catégorie de boisson commandée par un client,
 * selon les pourcentages de la phase en cours (voir {@link GameConfig}).
 */
public class OrderGenerator {
    private Random random = new Random();

    /**
     * Crée le générateur de commandes.
     */
    public OrderGenerator() {
    }

    /**
     * Choisit la catégorie de la prochaine commande.
     *
     * @param phase la phase en cours
     * @param picky true pour un client Difficile, qui commande toujours un shaken
     * @return la catégorie tirée (bière, vin, stirred ou shaken)
     */
    public DrinkCategory pickCategory(Phase phase, boolean picky) {
        if (picky) {
            return DrinkCategory.SHAKEN;
        }

        int[] rates = GameConfig.OPENING_ORDERS;
        if (phase == Phase.HAPPY_HOUR) {
            rates = GameConfig.HAPPY_HOUR_ORDERS;
        } else if (phase == Phase.RUSH) {
            rates = GameConfig.RUSH_ORDERS;
        }

        DrinkCategory[] categories = DrinkCategory.values();
        int roll = random.nextInt(100);
        int limit = 0;
        for (int i = 0; i < categories.length; i++) {
            limit = limit + rates[i];
            if (roll < limit) {
                return categories[i];
            }
        }
        return DrinkCategory.BEER;
    }
}
