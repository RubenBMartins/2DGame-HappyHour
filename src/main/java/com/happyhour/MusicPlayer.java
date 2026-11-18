package com.happyhour;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicPlayer {
    private MediaPlayer player;

    public MusicPlayer(String path) {
        Media media = new Media(getClass().getResource(path).toExternalForm());
        player = new MediaPlayer(media);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.setVolume(0.5);
    }

    public void play() {
        player.play();
    }

    public void stop() {
        player.stop();
    }

    public void setVolume(double volume) {
        player.setVolume(volume);
    }
}