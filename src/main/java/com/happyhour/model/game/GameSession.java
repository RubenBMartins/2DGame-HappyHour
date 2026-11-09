package com.happyhour.model.game;

import java.util.ArrayList;

import com.happyhour.model.customer.Customer;
import com.happyhour.model.customer.CustomerFactory;
import com.happyhour.model.drink.Drink;
import com.happyhour.model.drink.Glass;
import com.happyhour.model.drink.Ingredient;
import com.happyhour.model.drink.Preparation;
import com.happyhour.model.drink.Technique;

public class GameSession {
    public static final int SEATS = 6;
    public static final int MAX_LOST = 10;
    public static final double SPEED_BONUS = 1;
    public static final double HAPPY_HOUR_START = 60;
    public static final double RUSH_START = 180;
    public static final double EVENING_END = 300;
    public static final double RUSH_PATIENCE_SPEED = 1.25;

    private Customer[] seats = new Customer[SEATS];
    private Preparation preparation = new Preparation();
    private ArrayList<GameListener> listeners = new ArrayList<>();
    private CustomerFactory factory = new CustomerFactory();
    private double tips = 0;
    private int lostCustomers = 0;
    private double elapsed = 0;
    private Phase phase = Phase.OPENING;
    private double spawnTimer = 0;
    private boolean closed = false;
    private boolean over = false;
    private boolean defeat = false;

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
        if (over || closed || customer == null || !customer.canBeServed()) {
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
            speedBonus = SPEED_BONUS;
        }
        tips = tips + customer.computeTip(order.getBaseTip(grade), speedBonus);
        customer.serve();
        for (GameListener listener : listeners) {
            listener.onScoreChanged(tips);
        }
    }

    public void kick(int seat) {
        Customer customer = seats[seat];
        if (over || customer == null) {
            return;
        }
        customer.kick();
        if (customer.isChild()) {
            freeSeat(seat);
        } else {
            loseCustomer(seat);
        }
    }

    public void update(double deltaTime) {
        if (over) {
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

        if (!closed && elapsed >= EVENING_END) {
            closeBar();
        }

        for (int i = 0; i < SEATS; i++) {
            Customer customer = seats[i];
            if (customer != null) {
                if (phase == Phase.RUSH) {
                    customer.setPatienceSpeed(RUSH_PATIENCE_SPEED);
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
                placeCustomer(seat, factory.createCustomer(null));
            }
        }
    }

    public Phase getPhase() {
        if (elapsed < HAPPY_HOUR_START) {
            return Phase.OPENING;
        } else if (elapsed < RUSH_START) {
            return Phase.HAPPY_HOUR;
        }
        return Phase.RUSH;
    }

    private double getSpawnDelay() {
        if (phase == Phase.OPENING) {
            return 12;
        } else if (phase == Phase.HAPPY_HOUR) {
            return 8;
        }
        return 5;
    }

    private void closeBar() {
        closed = true;
        for (int i = 0; i < SEATS; i++) {
            if (seats[i] != null && seats[i].canBeServed()) {
                freeSeat(i);
            }
        }
    }

    private boolean isBarEmpty() {
        for (int i = 0; i < SEATS; i++) {
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
        for (int i = 0; i < SEATS; i++) {
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
        if (lostCustomers >= MAX_LOST) {
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
        for (int i = 0; i < SEATS; i++) {
            if (seats[i] == null) {
                return i;
            }
        }
        return -1;
    }

    public Customer getCustomer(int seat) {
        return seats[seat];
    }

    public Preparation getPreparation() {
        return preparation;
    }

    public double getTips() {
        return tips;
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

    public boolean isOver() {
        return over;
    }

    public boolean isDefeat() {
        return defeat;
    }
}