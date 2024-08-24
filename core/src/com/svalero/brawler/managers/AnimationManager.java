package com.svalero.brawler.managers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.HashMap;
import java.util.Map;
import static com.svalero.brawler.utils.Constants.*;

public class AnimationManager {
    private static final Map<String, Animation<TextureRegion>> animations = new HashMap<>();

    public static void loadAllAnimations() {
        // KAIN
        TextureAtlas kainAtlas = ResourceManager.getAtlas(KAIN_ATLAS);

        animations.put(KAIN_IDLE, new Animation<>(KAIN_IDLE_DURATION, kainAtlas.findRegions("idle"), Animation.PlayMode.LOOP));
        animations.put(KAIN_TURN, new Animation<>(KAIN_TURN_DURATION, kainAtlas.findRegions("turn"), Animation.PlayMode.REVERSED));
        animations.put(KAIN_WALK, new Animation<>(KAIN_WALK_DURATION, kainAtlas.findRegions("walk"), Animation.PlayMode.LOOP));
        animations.put(KAIN_BLOCK_UP, new Animation<>(KAIN_WALK_DURATION, kainAtlas.findRegions("block"), Animation.PlayMode.NORMAL));
        animations.put(KAIN_BLOCK_DOWN, new Animation<>(KAIN_WALK_DURATION, kainAtlas.findRegions("block"), Animation.PlayMode.REVERSED));
        animations.put(KAIN_CROUCH_DOWN, new Animation<>(KAIN_CROUCH_DURATION, kainAtlas.findRegions("crouch"), Animation.PlayMode.NORMAL));
        animations.put(KAIN_CROUCH_UP, new Animation<>(KAIN_CROUCH_DURATION, kainAtlas.findRegions("crouch"), Animation.PlayMode.REVERSED));
        animations.put(KAIN_JUMP_UP, new Animation<>(KAIN_JUMP_UP_DURATION, kainAtlas.findRegions("jump_up"), Animation.PlayMode.NORMAL));
        animations.put(KAIN_JUMP, new Animation<>(KAIN_JUMP_DOWN_DURATION, kainAtlas.findRegions("jump_down"), Animation.PlayMode.NORMAL));
        animations.put(KAIN_LAND, new Animation<>(KAIN_LAND_DURATION, kainAtlas.findRegions("land"), Animation.PlayMode.NORMAL));
        animations.put(KAIN_ATTACK, new Animation<>(KAIN_ATTACK_DURATION, kainAtlas.findRegions("attack"), Animation.PlayMode.NORMAL));
        animations.put(KAIN_JUMP_ATTACK, new Animation<>(KAIN_ATTACK_DURATION, kainAtlas.findRegions("jump_attack"), Animation.PlayMode.NORMAL));

        // BISHAMON
        TextureAtlas bishamonAtlas = ResourceManager.getAtlas(BISHAMON_ATLAS);

        animations.put(BISHAMON_IDLE, new Animation<>(BISHAMON_IDLE_DURATION, bishamonAtlas.findRegions("idle"), Animation.PlayMode.LOOP));
    }

    public static Animation<TextureRegion> getAnimation(String key) { return animations.get(key); }

    public static void dispose() { animations.clear(); }
}
