package com.happyhour.model.score;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ScoreBoard {
    public static final int MAX_ENTRIES = 10;

    private String filePath;
    private ArrayList<ScoreEntry> entries = new ArrayList<>();

    public ScoreBoard(String filePath) {
        this.filePath = filePath;
        load();
    }

    public void addScore(String name, double score, boolean defeat) {
        String cleanName = name.replace(";", "");
        ScoreEntry entry = new ScoreEntry(cleanName, score, defeat);

        int index = 0;
        while (index < entries.size() && entries.get(index).getScore() >= score) {
            index = index + 1;
        }
        entries.add(index, entry);

        if (entries.size() > MAX_ENTRIES) {
            entries.remove(entries.size() - 1);
        }
        save();
    }

    public ArrayList<ScoreEntry> getEntries() {
        return entries;
    }

    private void save() {
        try {
            FileWriter writer = new FileWriter(filePath);
            for (ScoreEntry entry : entries) {
                writer.write(entry.getName() + ";" + entry.getScore() + ";" + entry.isDefeat() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Impossible de sauvegarder les scores.");
        }
    }

    private void load() {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        try {
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String[] parts = reader.nextLine().split(";");
                String name = parts[0];
                double score = Double.parseDouble(parts[1]);
                boolean defeat = Boolean.parseBoolean(parts[2]);
                entries.add(new ScoreEntry(name, score, defeat));
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Impossible de lire les scores.");
        }
    }
}