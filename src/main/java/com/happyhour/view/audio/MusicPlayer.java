package com.happyhour.view.audio;

import java.net.URL;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Joue une musique en boucle (fichier MP3 des ressources du jeu).
 */
public class MusicPlayer {
    private MediaPlayer mediaPlayer;

    /**
     * Charge une musique. Si le fichier est introuvable, un message est affiché
     * et la musique reste muette, sans bloquer le jeu.
     *
     * @param path le chemin de la musique dans les ressources (par exemple /sounds/musique.mp3)
     */
    public MusicPlayer(String path) {
        URL resource = getClass().getResource(path);

        if (resource == null) {
            System.err.println("Musique introuvable : " + path);
            return;
        }

        Media media = new Media(resource.toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(1.0);
    }

    /**
     * Lance la musique.
     */
    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    /**
     * Arrête la musique.
     */
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    /**
     * Change le volume.
     *
     * @param volume le volume, de 0 (muet) à 1 (maximum)
     */
    public void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    /**
     * Renvoie le volume actuel.
     *
     * @return le volume, de 0 à 1
     */
    public double getVolume() {
        if (mediaPlayer != null) {
            return mediaPlayer.getVolume();
        }
        return 0;
    }
}

