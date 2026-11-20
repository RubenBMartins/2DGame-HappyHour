package com.happyhour.model.drink;

import java.util.ArrayList;

/**
 * Boisson de la carte du bar : sa recette, son prix et sa façon d'être notée.
 * <p>
 * Classe abstraite, mère de toutes les boissons ({@link Beer}, {@link Wine}
 * et {@link Cocktail}). La notation est déléguée à une {@link GradingStrategy}
 * (pattern Strategy), et chaque sous-classe définit son pourboire et sa catégorie.
 */
public abstract class Drink {
    private String name;
    private int price;
    private Glass glass;
    private ArrayList<Ingredient> ingredients;
    private Technique technique;
    private GradingStrategy grading;

    /**
     * Crée une boisson.
     *
     * @param name        le nom affiché (par exemple « Gin Tonic »)
     * @param price       le prix en euros pour une boisson parfaite
     * @param glass       le verre attendu
     * @param ingredients les ingrédients attendus, dans l'ordre de la recette
     * @param technique   la technique attendue (cuillère, shaker ou aucune)
     * @param grading     la stratégie utilisée pour noter la boisson
     */
    public Drink(String name, int price, Glass glass, ArrayList<Ingredient> ingredients,
            Technique technique, GradingStrategy grading) {
        this.name = name;
        this.price = price;
        this.glass = glass;
        this.ingredients = ingredients;
        this.technique = technique;
        this.grading = grading;
    }

    /**
     * Note la préparation du joueur en la comparant à cette recette.
     *
     * @param preparation la boisson préparée par le joueur
     * @return la note sur 10
     */
    public int grade(Preparation preparation) {
        return grading.grade(this, preparation);
    }

    /**
     * Choisit un pourboire selon le palier de la note.
     * Utilisée par les sous-classes pour définir leur propre barème.
     *
     * @param grade   la note sur 10
     * @param perfect le pourboire pour 10/10
     * @param good    le pourboire de 7 à 9
     * @param correct le pourboire de 5 à 6
     * @return le pourboire en euros, 0 si la note est inférieure à 5 (boisson ratée)
     */
    protected double tipForGrade(int grade, double perfect, double good, double correct) {
        if (grade == 10) {
            return perfect;
        } else if (grade >= 7) {
            return good;
        } else if (grade >= 5) {
            return correct;
        }
        return 0;
    }

    /**
     * Renvoie le nom de la boisson.
     *
     * @return le nom de la boisson
     */
    public String getName() {
        return name;
    }

    /**
     * Renvoie le prix en euros pour une boisson parfaite.
     *
     * @return le prix en euros pour une boisson parfaite
     */
    public int getPrice() {
        return price;
    }

    /**
     * Renvoie le verre attendu.
     *
     * @return le verre attendu
     */
    public Glass getGlass() {
        return glass;
    }

    /**
     * Renvoie les ingrédients attendus, dans l'ordre de la recette.
     *
     * @return les ingrédients attendus, dans l'ordre de la recette
     */
    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * Renvoie la technique attendue.
     *
     * @return la technique attendue
     */
    public Technique getTechnique() {
        return technique;
    }

    /**
     * Calcule le pourboire de base, avant l'effet du type de client.
     *
     * @param grade la note de la boisson, sur 10
     * @return le pourboire de base en euros
     */
    public abstract double getBaseTip(int grade);

    /**
     * Renvoie la catégorie de la boisson, utilisée pour tirer les commandes au hasard.
     *
     * @return la catégorie de la boisson, utilisée pour tirer les commandes au hasard
     */
    public abstract DrinkCategory getCategory();
}
