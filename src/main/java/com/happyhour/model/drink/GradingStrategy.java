package com.happyhour.model.drink;

/**
 * Façon de noter une boisson (pattern Strategy).
 * <p>
 * Chaque {@link Drink} reçoit une stratégie : {@link CocktailGrading} pour les
 * cocktails, {@link BeerWineGrading} pour la bière et le vin. On peut ainsi
 * changer de barème sans modifier les classes de boissons.
 */
public interface GradingStrategy {

    /**
     * Compare la préparation du joueur à la recette et donne une note.
     *
     * @param drink       la boisson commandée (la recette attendue)
     * @param preparation la boisson préparée par le joueur
     * @return la note sur 10
     */
    int grade(Drink drink, Preparation preparation);
}
