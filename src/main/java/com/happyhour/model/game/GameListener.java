package com.happyhour.model.game;

import com.happyhour.model.customer.Customer;

public interface GameListener {
    void onScoreChanged(double tips);
    
    void onCustomerArrived(int seat, Customer customer);

    void onCustomerLeft(int seat);

    void onPoliceRaid();

    void onEveningOver(boolean defeat);

    void onPhaseChanged(Phase phase);
}