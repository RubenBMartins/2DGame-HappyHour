package com.happyhour.model.score;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Tableau des 10 meilleurs scores, trié du plus grand au plus petit
 * et sauvegardé dans un fichier texte (une ligne par score : pseudo;score;défaite).
 */
public class ScoreBoard {
    /**
     * Nombre maximum de scores gardés.
     */
    public static final int MAX_ENTRIES = 10;

    private String filePath;
    private ArrayList<ScoreEntry> entries = new ArrayList<>();

    /**
     * Crée le tableau et charge les scores déjà enregistrés dans le fichier.
     *
     * @param filePath le chemin du fichier de sauvegarde
     */
    public ScoreBoard(String filePath) {
        this.filePath = filePath;
        load();
    }

    /**
     * Ajoute un score à sa place dans le classement, garde les 10 meilleurs
     * et sauvegarde le fichier.
     *
     * @param name   le pseudo du joueur (les « ; » sont retirés)
     * @param score  le score total, en euros
     * @param defeat true si la partie a été perdue
     */
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

    /**
     * Renvoie les scores, du meilleur au moins bon.
     *
     * @return la liste des scores
     */
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
