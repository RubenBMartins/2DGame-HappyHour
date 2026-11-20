package com.happyhour.view.game;

import com.happyhour.model.drink.Glass;
import com.happyhour.model.drink.Ingredient;
import com.happyhour.model.drink.Technique;

/**
 * Noms en français des verres, ingrédients et techniques, affichés dans le jeu
 * (ligne « EN MAIN » et texte au survol des postes).
 */
public class DrinkNames {

    /**
     * Classe utilitaire : elle ne sert pas à créer des objets.
     */
    private DrinkNames() {
    }

    /**
     * Renvoie le nom d'un verre.
     *
     * @param glass le verre
     * @return son nom en français
     */
    public static String glassName(Glass glass) {
        switch (glass) {
            case PINT:
                return "Pinte";
            case MARTINI:
                return "Verre à martini";
            case HIGHBALL:
                return "Highball";
            default:
                return "Verre à vin";
        }
    }

    /**
     * Renvoie le nom d'un ingrédient.
     *
     * @param ingredient l'ingrédient
     * @return son nom en français
     */
    public static String ingredientName(Ingredient ingredient) {
        switch (ingredient) {
            case VODKA:
                return "Vodka";
            case TEQUILA:
                return "Tequila";
            case GIN:
                return "Gin";
            case TRIPLE_SEC:
                return "Triple sec";
            case RASPBERRY_SYRUP:
                return "Sirop de framboise";
            case TONIC:
                return "Tonic";
            case LEMON_JUICE:
                return "Jus de citron";
            case CRANBERRY_JUICE:
                return "Jus de cranberry";
            case ICE:
                return "Glaçons";
            case LIME:
                return "Citron vert";
            case ORANGE_ZEST:
                return "Zeste d'orange";
            case RASPBERRY:
                return "Framboise";
            case BEER:
                return "Bière";
            default:
                return "Vin blanc";
        }
    }

    /**
     * Renvoie le nom de l'outil d'une technique.
     *
     * @param technique la technique
     * @return « Cuillère » ou « Shaker »
     */
    public static String techniqueName(Technique technique) {
        if (technique == Technique.STIR) {
            return "Cuillère";
        }
        return "Shaker";
    }
}


