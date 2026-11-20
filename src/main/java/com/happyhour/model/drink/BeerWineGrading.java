package com.happyhour.model.drink;

/**
 * Notation de la bière et du vin, sur 10 points :
 * <ul>
 *   <li>bon verre : 6 points ;</li>
 *   <li>bonne boisson, versée dans un verre déjà choisi : 4 points.</li>
 * </ul>
 */
public class BeerWineGrading implements GradingStrategy {

    /**
     * Crée la stratégie de notation de la bière et du vin.
     */
    public BeerWineGrading() {
    }

    /**
     * Note une bière ou un vin selon les deux critères ci-dessus.
     *
     * @param drink       la boisson commandée
     * @param preparation la boisson préparée par le joueur
     * @return la note sur 10
     */
    @Override
    public int grade(Drink drink, Preparation preparation) {
        int score = 0;
        if (preparation.getGlass() == drink.getGlass()) {
            score = score + 6;
        }
        if (preparation.isGlassFirst() && preparation.getIngredients().equals(drink.getIngredients())) {
            score = score + 4;
        }
        return score;
    }
}
