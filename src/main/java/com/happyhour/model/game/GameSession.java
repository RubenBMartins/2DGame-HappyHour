package com.happyhour.model.game;

import java.util.ArrayList;

import com.happyhour.model.GameConfig;
import com.happyhour.model.customer.ChildCustomer;
import com.happyhour.model.customer.Customer;
import com.happyhour.model.customer.CustomerFactory;
import com.happyhour.model.customer.PickyCustomer;
import com.happyhour.model.drink.Drink;
import com.happyhour.model.drink.DrinkCategory;
import com.happyhour.model.drink.Glass;
import com.happyhour.model.drink.Ingredient;
import com.happyhour.model.drink.Preparation;
import com.happyhour.model.drink.RecipeBook;
import com.happyhour.model.drink.Technique;
import com.happyhour.model.score.CashRegister;
import java.util.Random;
import java.util.ArrayList;

public class GameSession {
    private Customer[] seats = new Customer[GameConfig.SEATS];
    private Preparation preparation = new Preparation();
    private ArrayList<GameListener> listeners = new ArrayList<>();
    private CustomerFactory factory = new CustomerFactory();
    private OrderGenerator orders = new OrderGenerator();
    private RecipeBook recipeBook = new RecipeBook();
    private CashRegister register = new CashRegister();
    private int lostCustomers = 0;
    private double elapsed = 0;
    private Phase phase = Phase.OPENING;
    private double spawnTimer = 0;
    private boolean closed = false;
    private boolean paused = false;
    private boolean over = false;
    private boolean defeat = false;
    private ArrayList<Integer> openSeats = new ArrayList<Integer>();
    private Random rand = new Random();

    public void addListener(GameListener listener) {
        listeners.add(listener);
    }

    public void chooseGlass(Glass glass) {
        preparation.chooseGlass(glass);
    }

    public void addIngredient(Ingredient ingredient) {
        preparation.addIngredient(ingredient);
    }

    public void useTechnique(Technique technique) {
        preparation.useTechnique(technique);
    }

    public void placeCustomer(int seat, Customer customer) {
        seats[seat] = customer;
        for (GameListener listener : listeners) {
            listener.onCustomerArrived(seat, customer);
        }
    }

    public void serve(int seat) {
        Customer customer = seats[seat];
        if (over || closed || paused || customer == null || !customer.canBeServed()) {
            return;
        }

        if (customer.isChild()) {
            policeRaid();
            return;
        }

        Drink order = customer.getOrder();
        int grade = order.grade(preparation);
        preparation = new Preparation();

        if (!customer.accepts(grade)) {
            loseCustomer(seat);
            return;
        }

        double speedBonus = 0;
        if (customer.isServedQuickly()) {
            speedBonus = GameConfig.SPEED_BONUS;
        }
        double tip = customer.computeTip(order.getBaseTip(grade), speedBonus);
        register.recordSale(order, tip);
        customer.serve();
        for (GameListener listener : listeners) {
            listener.onScoreChanged(register.getTotal());
        }
    }

    public void kick(int seat) {
        Customer customer = seats[seat];
        if (over || paused || customer == null) {
            return;
        }
        customer.kick();
        if (customer.isChild()) {
            freeSeat(seat);
        } else {
            loseCustomer(seat);
        }
    }

    public void togglePause() {
        if (!over) {
            paused = !paused;
        }
    }

    public void spawnChild() {
        int seat = findFreeSeat();
        if (!over && !closed && seat != -1) {
            Customer child = new ChildCustomer(null);
            giveOrder(child);
            placeCustomer(seat, child);
        }
    }

    public void skipPhase() {
        if (phase == Phase.OPENING) {
            elapsed = GameConfig.HAPPY_HOUR_START;
        } else if (phase == Phase.HAPPY_HOUR) {
            elapsed = GameConfig.RUSH_START;
        } else {
            elapsed = GameConfig.EVENING_END;
        }
    }

    public void update(double deltaTime) {
        if (over || paused) {
            return;
        }

        elapsed = elapsed + deltaTime;
        Phase newPhase = getPhase();
        if (newPhase != phase) {
            phase = newPhase;
            for (GameListener listener : listeners) {
                listener.onPhaseChanged(phase);
            }
        }

        if (!closed && elapsed >= GameConfig.EVENING_END) {
            closeBar();
        }

        for (int i = 0; i < GameConfig.SEATS; i++) {
            Customer customer = seats[i];
            if (customer != null) {
                if (phase == Phase.RUSH) {
                    customer.setPatienceSpeed(GameConfig.RUSH_PATIENCE_SPEED);
                }
                customer.update(deltaTime);
                if (customer.hasLeft()) {
                    if (customer.isLost()) {
                        loseCustomer(i);
                    } else {
                        freeSeat(i);
                    }
                }
            }
        }

        if (closed) {
            if (!over && isBarEmpty()) {
                endEvening(false);
            }
            return;
        }

        spawnTimer = spawnTimer + deltaTime;
        if (spawnTimer >= getSpawnDelay()) {
            spawnTimer = 0;
            int seat = findFreeSeat();
            if (seat != -1) {
                Customer customer = factory.createCustomer(elapsed, phase != Phase.OPENING);
                giveOrder(customer);
                placeCustomer(seat, customer);
            }
        }
    }

    private void giveOrder(Customer customer) {
        boolean picky = customer instanceof PickyCustomer;
        DrinkCategory category = orders.pickCategory(phase, picky);
        customer.setOrder(recipeBook.randomDrink(category));
    }

    public Phase getPhase() {
        if (elapsed < GameConfig.HAPPY_HOUR_START) {
            return Phase.OPENING;
        } else if (elapsed < GameConfig.RUSH_START) {
            return Phase.HAPPY_HOUR;
        }
        return Phase.RUSH;
    }

    private double getSpawnDelay() {
        if (phase == Phase.OPENING) {
            return GameConfig.OPENING_SPAWN_DELAY;
        } else if (phase == Phase.HAPPY_HOUR) {
            return GameConfig.HAPPY_HOUR_SPAWN_DELAY;
        }
        return GameConfig.RUSH_SPAWN_DELAY;
    }

    private void closeBar() {
        closed = true;
        for (int i = 0; i < GameConfig.SEATS; i++) {
            if (seats[i] != null && seats[i].canBeServed()) {
                freeSeat(i);
            }
        }
    }

    private boolean isBarEmpty() {
        for (int i = 0; i < GameConfig.SEATS; i++) {
            if (seats[i] != null) {
                return false;
            }
        }
        return true;
    }

    private void endEvening(boolean lost) {
        over = true;
        defeat = lost;
        for (GameListener listener : listeners) {
            listener.onEveningOver(lost);
        }
    }

    private void policeRaid() {
        for (int i = 0; i < GameConfig.SEATS; i++) {
            if (seats[i] != null) {
                freeSeat(i);
            }
        }
        for (GameListener listener : listeners) {
            listener.onPoliceRaid();
        }
        endEvening(true);
    }

    private void loseCustomer(int seat) {
        lostCustomers = lostCustomers + 1;
        freeSeat(seat);
        if (lostCustomers >= GameConfig.MAX_LOST) {
            endEvening(true);
        }
    }

    private void freeSeat(int seat) {
        seats[seat] = null;
        for (GameListener listener : listeners) {
            listener.onCustomerLeft(seat);
        }
    }

    private int findFreeSeat() {
        openSeats.clear();
        for (int i = 0; i < GameConfig.SEATS; i++) {
            if (seats[i] == null) {
                openSeats.add(i);
            }
        }
        if (openSeats.size() != 0) {
            int n = rand.nextInt(openSeats.size());
            return openSeats.get(n);
        }
        else {
            return -1;
        }
    }

    public Customer getCustomer(int seat) {
        return seats[seat];
    }

    public Preparation getPreparation() {
        return preparation;
    }

    public RecipeBook getRecipeBook() {
        return recipeBook;
    }

    public CashRegister getRegister() {
        return register;
    }

    public double getTips() {
        return register.getTips();
    }

    public int getLostCustomers() {
        return lostCustomers;
    }

    public double getElapsed() {
        return elapsed;
    }

    public boolean isClosed() {
        return closed;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isOver() {
        return over;
    }

    public boolean isDefeat() {
        return defeat;
    }
}