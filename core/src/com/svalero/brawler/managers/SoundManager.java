package com.svalero.brawler.managers;

import com.badlogic.gdx.audio.Sound;
import com.svalero.brawler.domains.LongSoundInstance;

import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private static final Map<String, LongSoundInstance> longSoundData = new HashMap<>();

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
            if (sound != null && !longSoundData.containsKey(instanceKey)) {
                long soundId = sound.loop();
                longSoundData.put(instanceKey, new LongSoundInstance(sound, soundId, path));
            }
        }
    }

    public static void stopLongSound(String path, String instanceKey) {
        LongSoundInstance soundInstance = longSoundData.get(instanceKey);
        if (soundInstance != null) {
            Sound sound = soundInstance.getSound();
            long soundId = soundInstance.getSoundId();
            if (sound != null) {
                sound.stop(soundId);
            }
            longSoundData.remove(instanceKey);
        }
    }

    public static void stopAllLongSounds() {
        for (Map.Entry<String, LongSoundInstance> entry : new HashMap<>(longSoundData).entrySet()) {
            LongSoundInstance soundInstance = entry.getValue();
            Sound sound = soundInstance.getSound();
            long soundId = soundInstance.getSoundId();

            if (sound != null && soundId != 0) {
                sound.stop(soundId);
            }
        }
        longSoundData.clear();
    }
}
