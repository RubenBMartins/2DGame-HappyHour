package com.happyhour.model.drink;

import java.util.ArrayList;

/**
 * Notation des cocktails, sur 10 points :
 * <ul>
 *   <li>bons ingrédients : 4 points, moins 1 par ingrédient manquant ou en trop ;</li>
 *   <li>ordre des ingrédients respecté : 2 points ;</li>
 *   <li>bon verre : 2 points ;</li>
 *   <li>bonne technique (cuillère ou shaker) : 2 points.</li>
 * </ul>
 */
public class CocktailGrading implements GradingStrategy {

    /**
     * Crée la stratégie de notation des cocktails.
     */
    public CocktailGrading() {
    }

    /**
     * Note un cocktail selon les quatre critères ci-dessus.
     *
     * @param drink       le cocktail commandé
     * @param preparation la boisson préparée par le joueur
     * @return la note sur 10
     */
    @Override
    public int grade(Drink drink, Preparation preparation) {
        ArrayList<Ingredient> expected = drink.getIngredients();
        ArrayList<Ingredient> made = preparation.getIngredients();

        int missing = 0;
        for (Ingredient ingredient : expected) {
            if (!made.contains(ingredient)) {
                missing = missing + 1;
            }
        }
        int extra = made.size() - (expected.size() - missing);

        int score = 4 - missing - extra;
        if (score < 0) {
            score = 0;
        }

        if (isInOrder(expected, made)) {
            score = score + 2;
        }
        if (preparation.getGlass() == drink.getGlass()) {
            score = score + 2;
        }
        if (preparation.getTechnique() == drink.getTechnique()) {
            score = score + 2;
        }
        return score;
    }

    // Vérifie que les ingrédients de la recette ont été ajoutés dans le bon ordre
    // (les ingrédients en trop sont ignorés)
    private boolean isInOrder(ArrayList<Ingredient> expected, ArrayList<Ingredient> made) {
        int lastIndex = -1;
        for (Ingredient ingredient : made) {
            int index = expected.indexOf(ingredient);
            if (index != -1) {
                if (index < lastIndex) {
                    return false;
                }
                lastIndex = index;
            }
        }
        return true;
    }
}
