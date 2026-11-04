package com.happyhour.model.drink;

public abstract class Drink {
    private String name;
    private int price;
    private Glass glass;

    public Drink(String name, int price, Glass glass) {
        this.name = name;
        this.price = price;
        this.glass = glass;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public Glass getGlass() {
        return glass;
    }

    public abstract int grade(Preparation preparation);

    public abstract double getBaseTip(int grade);
}