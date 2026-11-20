package com.happyhour.model.game;

import java.util.ArrayList;
import java.util.Random;

import com.happyhour.SoundManager;
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
    private double nextSpawnDelay;
    private boolean closed = false;
    private boolean paused = false;
    private boolean over = false;
    private boolean defeat = false;
    private ArrayList<Integer> openSeats = new ArrayList<Integer>();
    private Random rand = new Random();
    private String feedback = null;
    private int feedbackGrade = 0;
    private double feedbackTimer = 0;
    private boolean policeRaidInProgress = false;
    private double policeRaidTimer = 0;
    private boolean policeArrivalInProgress = false;
    private double policeArrivalTimer = 0;

    public GameSession() {
        nextSpawnDelay = randomBetween(GameConfig.FIRST_CUSTOMER_MIN, GameConfig.FIRST_CUSTOMER_MAX);
    }

    public void addListener(GameListener listener) {
        listeners.add(listener);
    }

    public void chooseGlass(Glass glass) {
        if (isPoliceRaidActive()) {
            return;
        }
        preparation.chooseGlass(glass);
        SoundManager.play("bottle");
    }

    public void addIngredient(Ingredient ingredient) {
        if (isPoliceRaidActive()) {
            return;
        }
        preparation.addIngredient(ingredient);
        SoundManager.play("water");
    }

    public void useTechnique(Technique technique) {
        if (isPoliceRaidActive()) {
            return;
        }
        preparation.useTechnique(technique);
        SoundManager.play("bottle");
    }

    public void placeCustomer(int seat, Customer customer) {
        seats[seat] = customer;
        for (GameListener listener : listeners) {
            listener.onCustomerArrived(seat, customer);
        }
    }

    private void removeAllCustomers() {
        for (int i = 0; i < GameConfig.SEATS; i++) {
            if (seats[i] != null) {
                freeSeat(i);
            }
        }
    }

    public boolean isPoliceRaidActive() {
        return policeRaidInProgress || policeArrivalInProgress;
    }

    public void serve(int seat) {
        if (isPoliceRaidActive()) {
            return;
        }
        Customer customer = seats[seat];
        if (over || closed || paused || customer == null || !customer.canBeServed()) {
            return;
        }

        if (!preparation.isReady()) {
            return;
        }

        if (customer.isChild()) {
            policeRaid();
            return;
        }

        Drink order = customer.getOrder();
        int grade = order.grade(preparation);
        Preparation served = preparation;
        preparation = new Preparation();
        showFeedback(grade);

        if (!customer.accepts(grade)) {
            loseCustomer(seat);
            return;
        }

        double speedBonus = 0;
        if (customer.isServedQuickly()) {
            speedBonus = GameConfig.SPEED_BONUS;
        }
        double tip = customer.computeTip(order.getBaseTip(grade), speedBonus);
        double price = order.getPrice() * grade / 10.0;
        register.recordSale(order, price, tip);
        for (GameListener listener : listeners) {
            listener.onCustomerServed(seat, price + tip);
        }
        customer.setServedPreparation(served);
        customer.serve();
        for (GameListener listener : listeners) {
            listener.onScoreChanged(register.getTotal());
        }
    }

    private void showFeedback(int grade) {
        if (grade == 10) {
            feedback = "PARFAIT ! 10/10";
        } else if (grade >= 5) {
            feedback = grade + "/10 : " + (grade * 10) + " % DU PRIX";
        } else {
            feedback = "NUL ! " + grade + "/10";
        }
        feedbackGrade = grade;
        feedbackTimer = GameConfig.FEEDBACK_TIME;
    }

    public void kick(int seat) {
        if (isPoliceRaidActive()) {
            return;
        }
        Customer customer = seats[seat];
        if (over || paused || customer == null) {
            return;
        }
        customer.kick();
        SoundManager.play("kick");
        if (customer.isChild()) {
            freeSeat(seat);
        } else {
            loseCustomer(seat);
        }
    }

    public void togglePause() {
        if (!over && !isPoliceRaidActive()) {
            paused = !paused;
        }
    }

    public void spawnChild() {
        if (isPoliceRaidActive()) {
            return;
        }
        int seat = findFreeSeat();
        if (!over && !closed && seat != -1) {
            Customer child = new ChildCustomer(null);
            giveOrder(child);
            placeCustomer(seat, child);
        }
    }

    public void spawnCustomer() {
        if (isPoliceRaidActive()) {
            return;
        }
        int seat = findFreeSeat();
        if (!over && !closed && seat != -1) {
            Customer customer = factory.createCustomer(elapsed, phase != Phase.OPENING);
            giveOrder(customer);
            placeCustomer(seat, customer);
        }
    }

    public void skipPhase() {
        if (isPoliceRaidActive()) {
            return;
        }
        if (phase == Phase.OPENING) {
            elapsed = GameConfig.HAPPY_HOUR_START;
        } else if (phase == Phase.HAPPY_HOUR) {
            elapsed = GameConfig.RUSH_START;
        } else {
            elapsed = GameConfig.EVENING_END;
        }
    }

    public void countdownSkip() {
        if (isPoliceRaidActive()) {
            return;
        }
        elapsed = 285;
    }

    public void update(double deltaTime) {
        if (over) {
            return;
        }

        if (policeRaidInProgress) {
            policeRaidTimer -= deltaTime;

            if (policeRaidTimer <= 0) {
                policeRaidInProgress = false;
                policeArrivalInProgress = true;

                for (GameListener listener : listeners) {
                    listener.onPoliceRaid();
                }

                policeArrivalTimer = 2.915;
            }
            return;
        }

        if (policeArrivalInProgress) {
            policeArrivalTimer -= deltaTime;

            if (policeArrivalTimer <= 0) {
                policeArrivalInProgress = false;
                endEvening(true);
            }
            return;
        }

        if (paused) {
            return;
        }

        if (feedbackTimer > 0) {
            feedbackTimer = feedbackTimer - deltaTime;
            if (feedbackTimer <= 0) {
                feedback = null;
            }
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
        if (spawnTimer >= nextSpawnDelay) {
            spawnTimer = 0;
            nextSpawnDelay = randomSpawnDelay();
            spawnCustomer();
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

    private double randomSpawnDelay() {
        if (phase == Phase.OPENING) {
            return randomBetween(GameConfig.OPENING_SPAWN_MIN, GameConfig.OPENING_SPAWN_MAX);
        } else if (phase == Phase.HAPPY_HOUR) {
            return randomBetween(GameConfig.HAPPY_HOUR_SPAWN_MIN, GameConfig.HAPPY_HOUR_SPAWN_MAX);
        }
        return randomBetween(GameConfig.RUSH_SPAWN_MIN, GameConfig.RUSH_SPAWN_MAX);
    }

    private double randomBetween(double min, double max) {
        return min + rand.nextDouble() * (max - min);
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
        if (isPoliceRaidActive()) {
            return;
        }

        policeRaidInProgress = true;
        policeRaidTimer = 0.5;

        removeAllCustomers();
        SoundManager.play("siren");
    }

    private void loseCustomer(int seat) {
        lostCustomers = lostCustomers + 1;
        freeSeat(seat);
        if (lostCustomers >= GameConfig.MAX_LOST) {
            endEvening(true);
        }
    }

    private void freeSeat(int seat) {
        if (seats[seat] != null) {
            Customer customer = this.getCustomer(seat);
            seats[seat] = null;
            for (GameListener listener : listeners) {
                listener.onCustomerLeft(seat, customer);
            }
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
        } else {
            return -1;
        }
    }

    public Customer getCustomer(int seat) {
        return seats[seat];
    }

    public Drink getOrder(int seat) {
        Customer customer = seats[seat];
        if (customer == null || !customer.canBeServed()) {
            return null;
        }
        return customer.getOrder();
    }

    public ArrayList<Integer> getOccupiedSeats() {
        ArrayList<Integer> occupied = new ArrayList<>();
        for (int seat = 0; seat < GameConfig.SEATS; seat++) {
            if (seats[seat] != null) {
                occupied.add(seat);
            }
        }
        return occupied;
    }

    public String getFeedback() {
        return feedback;
    }

    public int getFeedbackGrade() {
        return feedbackGrade;
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