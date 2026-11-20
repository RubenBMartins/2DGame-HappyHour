package com.happyhour.view.game;

import com.happyhour.model.drink.Glass;
import com.happyhour.model.drink.Ingredient;
import com.happyhour.model.drink.Technique;

// Noms en français des verres, ingrédients et techniques, affichés dans le jeu
public class DrinkNames {

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

    public static String techniqueName(Technique technique) {
        if (technique == Technique.STIR) {
            return "Cuillère";
        }
        return "Shaker";
    }
}

