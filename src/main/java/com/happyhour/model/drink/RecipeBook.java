package com.happyhour.model.drink;

import java.util.ArrayList;
import java.util.Random;

public class RecipeBook {
    private ArrayList<Drink> drinks = new ArrayList<>();
    private Random random = new Random();

    public RecipeBook() {
        drinks.add(new Beer());
        drinks.add(new Wine());

        ArrayList<Ingredient> ginTonic = new ArrayList<>();
        ginTonic.add(Ingredient.GIN);
        ginTonic.add(Ingredient.TONIC);
        ginTonic.add(Ingredient.ICE);
        ginTonic.add(Ingredient.LIME);
        drinks.add(new StirredCocktail("Gin Tonic", ginTonic));

        ArrayList<Ingredient> tequilaTonic = new ArrayList<>();
        tequilaTonic.add(Ingredient.TEQUILA);
        tequilaTonic.add(Ingredient.TONIC);
        tequilaTonic.add(Ingredient.ICE);
        tequilaTonic.add(Ingredient.ORANGE_ZEST);
        drinks.add(new StirredCocktail("Tequila Tonic", tequilaTonic));

        ArrayList<Ingredient> pinkCapeCod = new ArrayList<>();
        pinkCapeCod.add(Ingredient.VODKA);
        pinkCapeCod.add(Ingredient.RASPBERRY_SYRUP);
        pinkCapeCod.add(Ingredient.CRANBERRY_JUICE);
        pinkCapeCod.add(Ingredient.ICE);
        pinkCapeCod.add(Ingredient.RASPBERRY);
        drinks.add(new StirredCocktail("Pink Cape Cod", pinkCapeCod));

        ArrayList<Ingredient> margarita = new ArrayList<>();
        margarita.add(Ingredient.TEQUILA);
        margarita.add(Ingredient.TRIPLE_SEC);
        margarita.add(Ingredient.LEMON_JUICE);
        margarita.add(Ingredient.ICE);
        margarita.add(Ingredient.LIME);
        drinks.add(new ShakenCocktail("Margarita", margarita));

        ArrayList<Ingredient> cloverClub = new ArrayList<>();
        cloverClub.add(Ingredient.GIN);
        cloverClub.add(Ingredient.RASPBERRY_SYRUP);
        cloverClub.add(Ingredient.LEMON_JUICE);
        cloverClub.add(Ingredient.ICE);
        cloverClub.add(Ingredient.RASPBERRY);
        drinks.add(new ShakenCocktail("Clover Club", cloverClub));

        ArrayList<Ingredient> cosmopolitan = new ArrayList<>();
        cosmopolitan.add(Ingredient.VODKA);
        cosmopolitan.add(Ingredient.TRIPLE_SEC);
        cosmopolitan.add(Ingredient.CRANBERRY_JUICE);
        cosmopolitan.add(Ingredient.ICE);
        cosmopolitan.add(Ingredient.ORANGE_ZEST);
        drinks.add(new ShakenCocktail("Cosmopolitan", cosmopolitan));
    }

    public Drink randomDrink(DrinkCategory category) {
        ArrayList<Drink> matching = new ArrayList<>();
        for (Drink drink : drinks) {
            if (drink.getCategory() == category) {
                matching.add(drink);
            }
        }
        return matching.get(random.nextInt(matching.size()));
    }

    public ArrayList<Drink> getDrinks() {
        return drinks;
    }
}