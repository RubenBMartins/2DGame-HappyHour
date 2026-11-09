package com.happyhour.model.score;

public class ScoreEntry {
    private String name;
    private double score;
    private boolean defeat;

    public ScoreEntry(String name, double score, boolean defeat) {
        this.name = name;
        this.score = score;
        this.defeat = defeat;
    }

    public String getName() {
        return name;
    }

    public double getScore() {
        return score;
    }

    public boolean isDefeat() {
        return defeat;
    }
}