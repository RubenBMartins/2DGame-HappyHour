package com.happyhour.model.score;

import java.util.HashMap;

import com.happyhour.model.drink.Drink;

/**
 * Caisse du bar : additionne le chiffre d'affaires et les pourboires,
 * et compte les boissons servies pour le bilan de fin de soirée.
 */
public class CashRegister {
    private double revenue = 0;
    private double tips = 0;
    private int servedCustomers = 0;
    private HashMap<String, Integer> drinkCounts = new HashMap<>();

    /**
     * Crée une caisse vide, au début d'une soirée.
     */
    public CashRegister() {
    }

    /**
     * Enregistre une vente.
     *
     * @param drink la boisson servie
     * @param price le prix payé (prix × note / 10), en euros
     * @param tip   le pourboire, en euros
     */
    public void recordSale(Drink drink, double price, double tip) {
        revenue = revenue + price;
        tips = tips + tip;
        servedCustomers = servedCustomers + 1;

        String name = drink.getName();
        if (drinkCounts.containsKey(name)) {
            drinkCounts.put(name, drinkCounts.get(name) + 1);
        } else {
            drinkCounts.put(name, 1);
        }
    }

    /**
     * Renvoie la boisson la plus servie de la soirée.
     *
     * @return son nom, ou null si aucune boisson n'a été servie
     */
    public String getMostServedDrink() {
        String bestName = null;
        int bestCount = 0;
        for (String name : drinkCounts.keySet()) {
            if (drinkCounts.get(name) > bestCount) {
                bestName = name;
                bestCount = drinkCounts.get(name);
            }
        }
        return bestName;
    }

    /**
     * Renvoie le score : chiffre d'affaires + pourboires.
     *
     * @return le total, en euros
     */
    public double getTotal() {
        return revenue + tips;
    }

    /**
     * Renvoie le chiffre d'affaires (prix des boissons).
     *
     * @return le chiffre d'affaires, en euros
     */
    public double getRevenue() {
        return revenue;
    }

    /**
     * Renvoie le total des pourboires.
     *
     * @return les pourboires, en euros
     */
    public double getTips() {
        return tips;
    }

    /**
     * Renvoie le nombre de clients servis.
     *
     * @return le nombre de clients servis
     */
    public int getServedCustomers() {
        return servedCustomers;
    }
}

