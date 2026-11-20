package com.happyhour.view.audio;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.media.AudioClip;

public class SoundManager {

    private static final Map<String, AudioClip> sounds = new HashMap<>();
    private static double volume = 0.5;
    private static boolean muted = false;

    public static void load(String name, String file) {
        URL resource = SoundManager.class.getResource("/sounds/" + file);

        if (resource == null) {
            System.err.println("Son introuvable : " + file);
            return;
        }

        sounds.put(name, new AudioClip(resource.toExternalForm()));
    }

    public static void play(String name) {
        AudioClip sound = sounds.get(name);

        if (sound != null && !muted) {
            sound.setVolume(volume);
            sound.play();
        }
    }

    public static void stop(String name) {
        AudioClip sound = sounds.get(name);

        if (sound != null) {
            sound.stop();
        }
    }

    public static void setVolume(double value) {
        volume = Math.max(0, Math.min(1, value));
    }

    public static double getVolume() {
        return volume;
    }

    public static void setMuted(boolean value) {
        muted = value;

        if (muted) {
            stopAll();
        }
    }

    public static boolean isMuted() {
        return muted;
    }

    public static void stopAll() {
        for (AudioClip sound : sounds.values()) {
            sound.stop();
        }
    }
}
