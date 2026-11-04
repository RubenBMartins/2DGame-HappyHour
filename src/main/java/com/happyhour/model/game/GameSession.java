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
    public static final double SPAWN_DELAY = 5;
    public static final double SPEED_BONUS = 1;

    private Customer[] seats = new Customer[SEATS];
    private Preparation preparation = new Preparation();
    private ArrayList<GameListener> listeners = new ArrayList<>();
    private CustomerFactory factory = new CustomerFactory();
    private double tips = 0;
    private int lostCustomers = 0;
    private double spawnTimer = 0;
    private boolean over = false;

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
        if (customer == null || !customer.canBeServed()) {
            return;
        }
        Drink order = customer.getOrder();
        int grade = order.grade(preparation);
        double speedBonus = 0;
        if (customer.isServedQuickly()) {
            speedBonus = SPEED_BONUS;
        }
        tips = tips + customer.computeTip(order.getBaseTip(grade), speedBonus);
        customer.serve();
        preparation = new Preparation();
        for (GameListener listener : listeners) {
            listener.onScoreChanged(tips);
        }
    }

    public void kick(int seat) {
        Customer customer = seats[seat];
        if (customer == null) {
            return;
        }
        customer.kick();
        loseCustomer(seat);
    }

    public void update(double deltaTime) {
        if (over) {
            return;
        }
        for (int i = 0; i < SEATS; i++) {
            Customer customer = seats[i];
            if (customer != null) {
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

        spawnTimer = spawnTimer + deltaTime;
        if (spawnTimer >= SPAWN_DELAY) {
            spawnTimer = 0;
            int seat = findFreeSeat();
            if (seat != -1) {
                placeCustomer(seat, factory.createCustomer(null));
            }
        }
    }

    private void loseCustomer(int seat) {
        lostCustomers = lostCustomers + 1;
        freeSeat(seat);
        if (lostCustomers >= MAX_LOST) {
            over = true;
            for (GameListener listener : listeners) {
                listener.onEveningOver(true);
            }
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

    public boolean isOver() {
        return over;
    }
}