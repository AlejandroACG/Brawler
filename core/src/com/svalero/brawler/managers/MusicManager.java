package com.svalero.brawler.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class MusicManager {
    private static Music backgroundMusic;

    public static void startMusic(String path) {
        if (backgroundMusic != null) {
            if (backgroundMusic.isPlaying()) {
                backgroundMusic.stop();
            }
            backgroundMusic.dispose();
        }
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(path));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }

    public static void stopMusic() {
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.stop();
            backgroundMusic.dispose();
            backgroundMusic = null;
        }
    }
}
