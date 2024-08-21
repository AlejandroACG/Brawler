package com.svalero.brawler.managers;

import com.badlogic.gdx.audio.Sound;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private static final Map<String, Long> longSoundIds = new HashMap<>();

    public static void playSound(String path) {
        if (!ConfigurationManager.mute) {
            Sound sound = ResourceManager.getSound(path);
            if (sound != null) {
                sound.play();
            }
        }
    }

    public static void stopSound(String path) {
        Sound sound = ResourceManager.getSound(path);
        if (sound != null) {
            sound.stop();
        }
    }

    public static void playLongSound(String path, String instanceKey) {
        if (!ConfigurationManager.mute) {
            Sound sound = ResourceManager.getSound(path);
            if (sound != null && !longSoundIds.containsKey(instanceKey)) {
                long soundId = sound.loop();
                longSoundIds.put(instanceKey, soundId);
            }
        }
    }

    public static void stopLongSound(String path, String instanceKey) {
        Sound sound = ResourceManager.getSound(path);
        Long soundId = longSoundIds.get(instanceKey);
        if (sound != null && soundId != null) {
            sound.stop(soundId);  // Detiene solo esa instancia
            longSoundIds.remove(instanceKey);
        }
    }
}
