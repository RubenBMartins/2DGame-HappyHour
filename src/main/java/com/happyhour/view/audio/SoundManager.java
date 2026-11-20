package com.happyhour.view.audio;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.media.AudioClip;

/**
 * Gère les bruitages du jeu (bouteille, liquide, caisse, coup de pied,
 * sirène, compte à rebours). Chaque son est chargé une fois, puis joué par son nom.
 */
public class SoundManager {

    private static final Map<String, AudioClip> sounds = new HashMap<>();
    private static double volume = 0.5;
    private static boolean muted = false;

    /**
     * Classe utilitaire : elle ne sert pas à créer des objets.
     */
    private SoundManager() {
    }

    /**
     * Charge un bruitage depuis le dossier /sounds des ressources.
     *
     * @param name le nom utilisé pour le jouer (par exemple « kick »)
     * @param file le nom du fichier (par exemple « kick.mp3 »)
     */
    public static void load(String name, String file) {
        URL resource = SoundManager.class.getResource("/sounds/" + file);

        if (resource == null) {
            System.err.println("Son introuvable : " + file);
            return;
        }

        sounds.put(name, new AudioClip(resource.toExternalForm()));
    }

    /**
     * Joue un bruitage, sauf si les bruitages sont coupés.
     *
     * @param name le nom du bruitage
     */
    public static void play(String name) {
        AudioClip sound = sounds.get(name);

        if (sound != null && !muted) {
            sound.setVolume(volume);
            sound.play();
        }
    }

    /**
     * Arrête un bruitage.
     *
     * @param name le nom du bruitage
     */
    public static void stop(String name) {
        AudioClip sound = sounds.get(name);

        if (sound != null) {
            sound.stop();
        }
    }

    /**
     * Change le volume des bruitages.
     *
     * @param value le volume, ramené entre 0 et 1
     */
    public static void setVolume(double value) {
        volume = Math.max(0, Math.min(1, value));
    }

    /**
     * Renvoie le volume des bruitages.
     *
     * @return le volume, de 0 à 1
     */
    public static double getVolume() {
        return volume;
    }

    /**
     * Active ou coupe les bruitages. Les couper arrête aussi ceux en cours.
     *
     * @param value true pour couper les bruitages
     */
    public static void setMuted(boolean value) {
        muted = value;

        if (muted) {
            stopAll();
        }
    }

    /**
     * Indique si les bruitages sont coupés.
     *
     * @return true si les bruitages sont coupés
     */
    public static boolean isMuted() {
        return muted;
    }

    /**
     * Arrête tous les bruitages en cours.
     */
    public static void stopAll() {
        for (AudioClip sound : sounds.values()) {
            sound.stop();
        }
    }
}

