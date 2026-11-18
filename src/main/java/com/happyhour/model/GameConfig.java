package com.happyhour.model;

public class GameConfig {
    public static final int SEATS = 6;
    public static final int MAX_LOST = 10;

    public static final double FIRST_CUSTOMER_MIN = 1;
    public static final double FIRST_CUSTOMER_MAX = 3;
    public static final double HAPPY_HOUR_START = 60;
    public static final double FORCE_TIME = 150;
    public static final double RUSH_START = 180;
    public static final double EVENING_END = 300;

    public static final double OPENING_SPAWN_MIN = 5;
    public static final double OPENING_SPAWN_MAX = 10;
    public static final double HAPPY_HOUR_SPAWN_MIN = 3;
    public static final double HAPPY_HOUR_SPAWN_MAX = 7;
    public static final double RUSH_SPAWN_MIN = 2;
    public static final double RUSH_SPAWN_MAX = 5;

    public static final double SHORT_PATIENCE = 20;
    public static final double NORMAL_PATIENCE = 35;
    public static final double LONG_PATIENCE = 50;
    public static final double RUSH_PATIENCE_SPEED = 1.25;

    public static final double DRINKING_TIME = 3;
    public static final double GENEROUS_DRINKING_TIME = 6;

    public static final double SPEED_BONUS = 1;
    public static final double FEEDBACK_TIME = 1.2;
    public static final int PICKY_MIN_GRADE = 8;

    public static final int NORMAL_RATE = 60;
    public static final int RUSHED_RATE = 15;
    public static final int PICKY_RATE = 10;
    public static final int GENEROUS_RATE = 10;

    public static final int[] OPENING_ORDERS = { 30, 30, 15, 15 };
    public static final int[] HAPPY_HOUR_ORDERS = { 25, 25, 30, 20 };
    public static final int[] RUSH_ORDERS = { 15, 15, 35, 35 };
}