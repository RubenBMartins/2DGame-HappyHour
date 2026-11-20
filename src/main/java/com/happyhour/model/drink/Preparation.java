package com.happyhour.model.drink;

import java.util.ArrayList;

/**
 * Boisson en cours de préparation dans les mains de Melvin.
 * <p>
 * Elle enregistre le verre, les ingrédients dans l'ordre où ils sont ajoutés
 * et la technique utilisée. Le verre doit être pris en premier :
 * sans verre, les ingrédients et la technique sont ignorés.
 */
public class Preparation {
    private Glass glass;
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private Technique technique = Technique.NONE;
    private boolean glassFirst = false;

    /**
     * Crée une préparation vide : pas de verre, pas d'ingrédient.
     */
    public Preparation() {
    }

    /**
     * Prend un verre. Si un verre est déjà en main, il est simplement remplacé
     * (le contenu est gardé).
     *
     * @param glass le verre choisi
     */
    public void chooseGlass(Glass glass) {
        if (this.glass == null && ingredients.isEmpty()) {
            glassFirst = true;
        }
        this.glass = glass;
    }

    /**
     * Ajoute un ingrédient, seulement si un verre est en main.
     *
     * @param ingredient l'ingrédient ajouté
     */
    public void addIngredient(Ingredient ingredient) {
        if (glass == null) {
            return;
        }
        ingredients.add(ingredient);
    }

    /**
     * Utilise la cuillère ou le shaker, seulement si un verre est en main.
     *
     * @param technique la technique utilisée
     */
    public void useTechnique(Technique technique) {
        if (glass == null) {
            return;
        }
        this.technique = technique;
    }

    /**
     * Indique si la boisson peut être servie : il faut un verre
     * et au moins un liquide (les glaçons et la garnish ne comptent pas).
     *
     * @return true si la boisson peut être servie
     */
    public boolean isReady() {
        if (glass == null) {
            return false;
        }
        for (Ingredient ingredient : ingredients) {
            if (ingredient.isLiquid()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Renvoie le verre en main, ou null s'il n'y en a pas.
     *
     * @return le verre en main, ou null s'il n'y en a pas
     */
    public Glass getGlass() {
        return glass;
    }

    /**
     * Renvoie les ingrédients ajoutés, dans l'ordre.
     *
     * @return les ingrédients ajoutés, dans l'ordre
     */
    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * Renvoie la technique utilisée, {@link Technique#NONE} par défaut.
     *
     * @return la technique utilisée, {@link Technique#NONE} par défaut
     */
    public Technique getTechnique() {
        return technique;
    }

    /**
     * Indique si le verre a été pris avant tout ingrédient.
     *
     * @return true si le verre a été pris avant tout ingrédient
     */
    public boolean isGlassFirst() {
        return glassFirst;
    }

    /**
     * Indique si rien n'a encore été pris (ni verre ni ingrédient).
     *
     * @return true si rien n'a encore été pris (ni verre ni ingrédient)
     */
    public boolean isEmpty() {
        return glass == null && ingredients.isEmpty();
    }
}
