package com.svalero.brawler.managers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import java.util.HashMap;
import java.util.Map;
import static com.svalero.brawler.utils.Constants.*;

public class AnimationManager {
    private static final Map<String, Animation<TextureRegion>> animations = new HashMap<>();

    public static void loadAllAnimations() {
        // KAIN
        TextureAtlas kainAtlas = ResourceManager.getAtlas(KAIN_ATLAS);

        animations.put(KAIN_IDLE_ANIMATION, new Animation<>(KAIN_IDLE_DURATION, kainAtlas.findRegions("idle"), Animation.PlayMode.LOOP));
        animations.put(KAIN_WALK_ANIMATION, new Animation<>(KAIN_WALK_DURATION, kainAtlas.findRegions("walk"), Animation.PlayMode.LOOP));
        animations.put(KAIN_ATTACK_ANIMATION, new Animation<>(KAIN_ATTACK_DURATION, kainAtlas.findRegions("attack"), Animation.PlayMode.NORMAL));
        animations.put(KAIN_CROUCH_DOWN_ANIMATION, new Animation<>(KAIN_CROUCH_DURATION, kainAtlas.findRegions("crouch"), Animation.PlayMode.NORMAL));
        animations.put(KAIN_CROUCH_UP_ANIMATION, new Animation<>(KAIN_CROUCH_DURATION, kainAtlas.findRegions("crouch"), Animation.PlayMode.REVERSED));
        animations.put(KAIN_JUMP_UP_ANIMATION, new Animation<>(KAIN_JUMP_UP_DURATION, kainAtlas.findRegions("jump_up"), Animation.PlayMode.NORMAL));
        animations.put(KAIN_JUMP_DOWN_ANIMATION, new Animation<>(KAIN_JUMP_DOWN_DURATION, kainAtlas.findRegions("jump_down"), Animation.PlayMode.NORMAL));
        animations.put(KAIN_LAND_ANIMATION, new Animation<>(KAIN_LAND_DURATION, kainAtlas.findRegions("land"), Animation.PlayMode.NORMAL));
    }

    public static Animation<TextureRegion> getAnimation(String key) { return animations.get(key); }

    public static void dispose() { animations.clear(); }
}
