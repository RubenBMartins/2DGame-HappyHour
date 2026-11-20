package com.happyhour.model.drink;

import java.util.ArrayList;

/**
 * Cocktail : classe abstraite intermédiaire entre {@link Drink} et les cocktails
 * {@link StirredCocktail} et {@link ShakenCocktail} (héritage à deux niveaux).
 * <p>
 * Elle apporte ce qui est commun à tous les cocktails : la notation détaillée
 * ({@link CocktailGrading}) et le barème de pourboire des cocktails.
 */
public abstract class Cocktail extends Drink {

    /**
     * Crée un cocktail noté avec {@link CocktailGrading}.
     *
     * @param name        le nom du cocktail
     * @param price       le prix en euros
     * @param glass       le verre attendu
     * @param ingredients les ingrédients attendus, dans l'ordre
     * @param technique   la technique attendue (cuillère ou shaker)
     */
    public Cocktail(String name, int price, Glass glass, ArrayList<Ingredient> ingredients, Technique technique) {
        super(name, price, glass, ingredients, technique, new CocktailGrading());
    }

    /**
     * Pourboire de base d'un cocktail : 3 € pour 10/10, 2 € de 7 à 9, 1 € de 5 à 6.
     *
     * @param grade la note sur 10
     * @return le pourboire de base en euros
     */
    @Override
    public double getBaseTip(int grade) {
        return tipForGrade(grade, 3, 2, 1);
    }
}
