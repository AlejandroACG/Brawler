package com.svalero.brawler.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import java.util.Objects;
import static com.svalero.brawler.managers.ResourceManager.getMusic;
import static com.svalero.brawler.utils.Constants.MENU_MUSIC;

public class MusicManager {
    private static Music backgroundMusic;

    public static void startMusic(String path) {
        if (!ConfigurationManager.mute) {
            if (backgroundMusic != null && backgroundMusic.isPlaying()) {
                return;
            }

            backgroundMusic = getMusic(path);
            if (backgroundMusic != null) {
                backgroundMusic.setLooping(true);
                // TODO Hacer más limpio
                if (Objects.equals(path, "level-2-music.mp3")) {
                    backgroundMusic.setVolume(0.5f);
                }
                backgroundMusic.play();
            }
        }
    }

    public static void stopMusic() {
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.stop();
            backgroundMusic.dispose();
            backgroundMusic = null;
        }
    }

    public static void setSplashMusic() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(MENU_MUSIC));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.5f);
        backgroundMusic.play();
    }
}
