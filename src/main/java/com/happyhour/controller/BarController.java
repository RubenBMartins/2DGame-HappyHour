package com.happyhour.controller;

import com.happyhour.model.GameConfig;
import com.happyhour.model.drink.Drink;
import com.happyhour.model.drink.Glass;
import com.happyhour.model.drink.Ingredient;
import com.happyhour.model.drink.Technique;
import com.happyhour.model.game.GameSession;
import com.happyhour.view.audio.SoundManager;
import com.happyhour.view.game.DrinkNames;
import com.happyhour.view.game.GameScreen;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

// Contrôleur : transforme les clics et les touches du joueur en actions sur le modèle (GameSession)
public class BarController {
    private GameSession session;
    private GameScreen screen;

    public BarController(GameSession session, GameScreen screen) {
        this.session = session;
        this.screen = screen;
    }

    // Crée toutes les zones cliquables : les 6 places, les postes du bar et les bulles de commande
    public void createZones(Pane pane) {
        for (int seat = 0; seat < GameConfig.SEATS; seat++) {
            Rectangle seatBox = new Rectangle(GameScreen.seatX(seat), 390, 160, 290);
            seatBox.setFill(Color.TRANSPARENT);
            setupSeat(seatBox, seat);
            pane.getChildren().add(seatBox);
        }
        createStations(pane);
        createBubbleZones(pane);
    }

    private void setupSeat(Rectangle box, int seat) {
        box.setOnMouseClicked(event -> {
            if (session.isPoliceRaidActive()) {
                return;
            }
            if (event.getButton() == MouseButton.PRIMARY) {
                session.serve(seat);
                if (session.isPoliceRaidActive()) {
                    SoundManager.play("siren");
                }
            } else if (event.getButton() == MouseButton.SECONDARY) {
                if (session.getCustomer(seat) != null) {
                    session.kick(seat);
                    SoundManager.play("kick");
                    screen.showKick(seat);
                }
            }
        });
    }

    private Rectangle createZone(Pane pane, double x, double y, double width, double height, String name) {
        Rectangle zone = new Rectangle(x, y, width, height);
        zone.setFill(Color.web("0xFFFFFF", 0));
        screen.installHover(zone, name);
        pane.getChildren().add(zone);
        return zone;
    }

    private void playSound(String name) {
        if (!session.isPoliceRaidActive()) {
            SoundManager.play(name);
        }
    }

    private void addGlassZone(Pane pane, double x, double y, double width, double height, Glass glass) {
        Rectangle zone = createZone(pane, x, y, width, height, DrinkNames.glassName(glass));
        zone.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                session.chooseGlass(glass);
                playSound("bottle");
            }
        });
    }

    private void addIngredientZone(Pane pane, double x, double y, double width, double height, Ingredient ingredient) {
        Rectangle zone = createZone(pane, x, y, width, height, DrinkNames.ingredientName(ingredient));
        zone.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                session.addIngredient(ingredient);
                if (session.getPreparation().getGlass() != null) {
                    playSound("water");
                }
            }
        });
    }

    private void addTechniqueZone(Pane pane, double x, double y, double width, double height, Technique technique) {
        Rectangle zone = createZone(pane, x, y, width, height, DrinkNames.techniqueName(technique));
        zone.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                session.useTechnique(technique);
                if (session.getPreparation().getGlass() != null) {
                    playSound("bottle");
                }
            }
        });
    }

    private void createStations(Pane pane) {
        addGlassZone(pane, 73, 945, 82, 120, Glass.PINT);
        addGlassZone(pane, 175, 915, 92, 150, Glass.MARTINI);
        addGlassZone(pane, 278, 915, 62, 150, Glass.HIGHBALL);
        addGlassZone(pane, 350, 915, 72, 150, Glass.WINE_GLASS);

        addIngredientZone(pane, 527, 885, 80, 180, Ingredient.WHITE_WINE);
        addIngredientZone(pane, 642, 885, 80, 180, Ingredient.VODKA);
        addIngredientZone(pane, 761, 885, 80, 180, Ingredient.GIN);
        addIngredientZone(pane, 880, 885, 80, 180, Ingredient.TEQUILA);
        addIngredientZone(pane, 1011, 885, 80, 180, Ingredient.TRIPLE_SEC);
        addIngredientZone(pane, 1101, 885, 80, 180, Ingredient.RASPBERRY_SYRUP);
        addIngredientZone(pane, 1190, 885, 80, 180, Ingredient.TONIC);
        addIngredientZone(pane, 1280, 885, 80, 180, Ingredient.LEMON_JUICE);
        addIngredientZone(pane, 1376, 885, 84, 180, Ingredient.CRANBERRY_JUICE);

        addIngredientZone(pane, 54, 540, 234, 277, Ingredient.BEER);
        addIngredientZone(pane, 1506, 882, 446, 178, Ingredient.ICE);
        addIngredientZone(pane, 696, 686, 92, 74, Ingredient.LIME);
        addIngredientZone(pane, 942, 686, 92, 88, Ingredient.ORANGE_ZEST);
        addIngredientZone(pane, 1195, 678, 86, 96, Ingredient.RASPBERRY);

        addTechniqueZone(pane, 1518, 759, 284, 58, Technique.STIR);
        addTechniqueZone(pane, 1799, 552, 107, 211, Technique.SHAKE);
    }

    private void createBubbleZones(Pane pane) {
        for (int seat = 0; seat < GameConfig.SEATS; seat++) {
            int currentSeat = seat;
            Rectangle zone = new Rectangle(GameScreen.seatX(seat) - 10, 170, 180, 220);
            zone.setFill(Color.web("0xFFFFFF", 0));
            zone.addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
                Drink order = session.getOrder(currentSeat);
                if (order != null) {
                    screen.moveHover(order.getName(), event);
                } else {
                    screen.stopHover();
                }
            });
            zone.addEventHandler(MouseEvent.MOUSE_EXITED, event -> screen.stopHover());
            pane.getChildren().add(zone);
        }
    }

    public void installKeys(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                if (!session.isPoliceRaidActive()) {
                    screen.pause();
                }
            } else if (event.getCode() == KeyCode.E) {
                session.spawnChild();
            } else if (event.getCode() == KeyCode.N) {
                session.skipPhase();
            } else if (event.getCode() == KeyCode.O) {
                session.spawnCustomer();
            } else if (event.getCode() == KeyCode.A) {
                session.countdownSkip();
            }
        });
    }
}

