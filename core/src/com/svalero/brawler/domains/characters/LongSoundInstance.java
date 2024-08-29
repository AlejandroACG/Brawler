package com.svalero.brawler.domains.characters;

import com.badlogic.gdx.audio.Sound;

public class LongSoundInstance {
    private Sound sound;
    private long soundId;
    private String path;

    public LongSoundInstance(Sound sound, long soundId, String path) {
        this.sound = sound;
        this.soundId = soundId;
        this.path = path;
    }

    public Sound getSound() {
        return sound;
    }

    public long getSoundId() {
        return soundId;
    }

    public String getPath() {
        return path;
    }
}
