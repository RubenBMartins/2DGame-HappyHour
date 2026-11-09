package com.happyhour.model.score;

import java.util.HashMap;

import com.happyhour.model.drink.Drink;

public class CashRegister {
    private double revenue = 0;
    private double tips = 0;
    private int servedCustomers = 0;
    private HashMap<String, Integer> drinkCounts = new HashMap<>();

    public void recordSale(Drink drink, double tip) {
        revenue = revenue + drink.getPrice();
        tips = tips + tip;
        servedCustomers = servedCustomers + 1;

        String name = drink.getName();
        if (drinkCounts.containsKey(name)) {
            drinkCounts.put(name, drinkCounts.get(name) + 1);
        } else {
            drinkCounts.put(name, 1);
        }
    }

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

    public double getTotal() {
        return revenue + tips;
    }

    public double getRevenue() {
        return revenue;
    }

    public double getTips() {
        return tips;
    }

    public int getServedCustomers() {
        return servedCustomers;
    }
}