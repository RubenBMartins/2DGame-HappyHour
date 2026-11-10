package com.happyhour.model.drink;

public interface GradingStrategy {
    int grade(Drink drink, Preparation preparation);
}