package com.happyhour.view.game;

import java.util.ArrayList;
import java.util.HashMap;

import com.happyhour.model.drink.Drink;
import com.happyhour.model.drink.Glass;
import com.happyhour.model.drink.Ingredient;
import com.happyhour.model.drink.Preparation;
import com.happyhour.model.drink.RecipeBook;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

// Dessine les verres (commandes, verre de Melvin, verre servi) avec la couleur de leur contenu
public class DrinkPainter {
    private GraphicsContext gc;
    private RecipeBook recipeBook;
    private HashMap<String, Image> tintCache = new HashMap<>();
    private Image beerFoam;
    private Image beerGlass;
    private Image beer;
    private Image highballGlass;
    private Image highball;
    private Image martiniGlass;
    private Image martini;
    private Image wineGlass;
    private Image wine;

    public DrinkPainter(GraphicsContext gc, RecipeBook recipeBook) {
        this.gc = gc;
        this.recipeBook = recipeBook;
        beerFoam = load("beerFoam.png", 100);
        beerGlass = load("beerGlass.png", 100);
        beer = load("beer.png", 100);
        highballGlass = load("highballGlass.png", 60);
        highball = load("highball.png", 60);
        martiniGlass = load("martiniGlass.png", 90);
        martini = load("martini.png", 90);
        wineGlass = load("wineGlass.png", 70);
        wine = load("wine.png", 70);
    }

    private Image load(String file, double width) {
        return new Image(getClass().getResourceAsStream("/images/" + file), width, 150, true, false);
    }

    public void drawDrink(Drink order, double centerX, double topY, double scale) {
        Color color;
        if (order.getGlass() == Glass.PINT) {
            color = getIngredientColor(Ingredient.BEER);
        } else if (order.getGlass() == Glass.WINE_GLASS) {
            color = getIngredientColor(Ingredient.WHITE_WINE);
        } else {
            color = getDrinkColor(order.getName());
        }
        double height = 150 * scale;
        drawGlass(order.getGlass(), color, order.getGlass() == Glass.PINT, centerX, topY + height / 2, scale);
    }

    public void drawPreparation(Preparation preparation, double centerX, double centerY, double scale) {
        Glass glass = preparation.getGlass();
        if (glass == null) {
            return;
        }
        ArrayList<Ingredient> liquids = getLiquids(preparation.getIngredients());
        Color color = null;
        if (!liquids.isEmpty()) {
            Drink match = findMatchingDrink(preparation);
            if (match != null && (glass == Glass.HIGHBALL || glass == Glass.MARTINI)) {
                color = getDrinkColor(match.getName());
            } else {
                color = mixColors(liquids);
            }
        }
        boolean foam = glass == Glass.PINT && preparation.getIngredients().contains(Ingredient.BEER);
        drawGlass(glass, color, foam, centerX, centerY, scale);
    }

    // Dessine un verre centré sur (centerX, centerY), avec son liquide si color n'est pas null
    private void drawGlass(Glass glass, Color color, boolean foam, double centerX, double centerY, double scale) {
        double width = 100 * scale;
        if (glass == Glass.WINE_GLASS) {
            width = 80 * scale;
        } else if (glass == Glass.HIGHBALL) {
            width = 60 * scale;
        }
        double height = 150 * scale;
        double x = centerX - width / 2;
        double y = centerY - height / 2;

        if (color != null) {
            gc.drawImage(getTintedLiquid(glass, color), x, y, width, height);
        }
        gc.drawImage(getGlassImage(glass), x, y, width, height);
        if (foam) {
            gc.drawImage(beerFoam, x, y, width, height);
        }
    }

    private Image getGlassImage(Glass glass) {
        if (glass == Glass.PINT) {
            return beerGlass;
        } else if (glass == Glass.WINE_GLASS) {
            return wineGlass;
        } else if (glass == Glass.HIGHBALL) {
            return highballGlass;
        }
        return martiniGlass;
    }

    private ArrayList<Ingredient> getLiquids(ArrayList<Ingredient> ingredients) {
        ArrayList<Ingredient> liquids = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            if (ingredient.isLiquid()) {
                liquids.add(ingredient);
            }
        }
        return liquids;
    }

    private Drink findMatchingDrink(Preparation preparation) {
        ArrayList<Ingredient> made = getLiquids(preparation.getIngredients());
        for (Drink drink : recipeBook.getDrinks()) {
            if (drink.getGlass() == preparation.getGlass()
                    && getLiquids(drink.getIngredients()).equals(made)) {
                return drink;
            }
        }
        return null;
    }

    private Image getLiquidImage(Glass glass) {
        if (glass == Glass.PINT) {
            return beer;
        } else if (glass == Glass.WINE_GLASS) {
            return wine;
        } else if (glass == Glass.HIGHBALL) {
            return highball;
        }
        return martini;
    }

    private Color getDrinkColor(String name) {
        switch (name) {
            case "Gin Tonic":
                return Color.web("#00c7e6");
            case "Tequila Tonic":
                return Color.web("#ff9100");
            case "Pink Cape Cod":
                return Color.web("#FF7EB9");
            case "Margarita":
                return Color.web("#9BE15D");
            case "Clover Club":
                return Color.web("#cd07ff");
            case "Cosmopolitan":
                return Color.web("#D7263D");
        }
        return Color.WHITE;
    }

    private Color getIngredientColor(Ingredient ingredient) {
        switch (ingredient) {
            case VODKA:
                return Color.web("#e8f4ff");
            case GIN:
                return Color.web("#dff7f2");
            case TEQUILA:
                return Color.web("#f5d76e");
            case TRIPLE_SEC:
                return Color.web("#ffa733");
            case RASPBERRY_SYRUP:
                return Color.web("#e0115f");
            case TONIC:
                return Color.web("#c9f0ff");
            case LEMON_JUICE:
                return Color.web("#f7f06d");
            case CRANBERRY_JUICE:
                return Color.web("#b5122e");
            case BEER:
                return Color.web("#f2b134");
            case WHITE_WINE:
                return Color.web("#f3e9a6");
            default:
                return Color.WHITE;
        }
    }

    private Color mixColors(ArrayList<Ingredient> liquids) {
        double red = 0;
        double green = 0;
        double blue = 0;
        for (Ingredient ingredient : liquids) {
            Color color = getIngredientColor(ingredient);
            red = red + color.getRed();
            green = green + color.getGreen();
            blue = blue + color.getBlue();
        }
        int count = liquids.size();
        return Color.color(red / count, green / count, blue / count);
    }

    private Image getTintedLiquid(Glass glass, Color color) {
        String key = glass + color.toString();
        if (!tintCache.containsKey(key)) {
            tintCache.put(key, tintImage(getLiquidImage(glass), color));
        }
        return tintCache.get(key);
    }

    private WritableImage tintImage(Image source, Color tint) {
        int width = (int) source.getWidth();
        int height = (int) source.getHeight();
        PixelReader reader = source.getPixelReader();
        WritableImage result = new WritableImage(width, height);
        PixelWriter writer = result.getPixelWriter();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);
                double brightness = color.getBrightness();
                Color tintedColor = Color.color(tint.getRed() * brightness, tint.getGreen() * brightness, tint.getBlue() * brightness, color.getOpacity());
                writer.setColor(x, y, tintedColor);
            }
        }
        return result;
    }
}

