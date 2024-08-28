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
        animations.put(KAIN_RUN, new Animation<>(KAIN_RUN_DURATION, kainAtlas.findRegions("run"), Animation.PlayMode.LOOP));
        animations.put(KAIN_BLOCK_UP, new Animation<>(KAIN_WALK_DURATION, kainAtlas.findRegions("block"), Animation.PlayMode.NORMAL));
        animations.put(KAIN_BLOCK_DOWN, new Animation<>(KAIN_WALK_DURATION, kainAtlas.findRegions("block"), Animation.PlayMode.REVERSED));
        animations.put(KAIN_CROUCH_DOWN, new Animation<>(KAIN_CROUCH_DURATION, kainAtlas.findRegions("crouch"), Animation.PlayMode.NORMAL));
        animations.put(KAIN_CROUCH_UP, new Animation<>(KAIN_CROUCH_DURATION, kainAtlas.findRegions("crouch"), Animation.PlayMode.REVERSED));
        animations.put(KAIN_JUMP_UP, new Animation<>(KAIN_JUMP_UP_DURATION, kainAtlas.findRegions("jump_up"), Animation.PlayMode.NORMAL));
        animations.put(KAIN_JUMP_DOWN, new Animation<>(KAIN_JUMP_DOWN_DURATION, kainAtlas.findRegions("jump_down"), Animation.PlayMode.NORMAL));
        animations.put(KAIN_LAND, new Animation<>(KAIN_LAND_DURATION, kainAtlas.findRegions("land"), Animation.PlayMode.NORMAL));
        animations.put(KAIN_ATTACK, new Animation<>(KAIN_ATTACK_DURATION, kainAtlas.findRegions("attack"), Animation.PlayMode.NORMAL));
        animations.put(KAIN_JUMP_ATTACK, new Animation<>(KAIN_ATTACK_DURATION, kainAtlas.findRegions("jump_attack"), Animation.PlayMode.NORMAL));
        animations.put(KAIN_HIT, new Animation<>(KAIN_HIT_DURATION, kainAtlas.findRegions("hit"), Animation.PlayMode.NORMAL));
        animations.put(KAIN_DEAD, new Animation<>(KAIN_DEAD_DURATION, kainAtlas.findRegions("knockdown"), Animation.PlayMode.NORMAL));
        animations.put(KAIN_VICTORY, new Animation<>(KAIN_VICTORY_DURATION, kainAtlas.findRegions("victory"), Animation.PlayMode.NORMAL));

        // BISHAMON
        TextureAtlas bishamonAtlas = ResourceManager.getAtlas(BISHAMON_ATLAS);

        animations.put(BISHAMON_IDLE, new Animation<>(BISHAMON_IDLE_DURATION, bishamonAtlas.findRegions("idle"), Animation.PlayMode.LOOP));
        animations.put(BISHAMON_WALK, new Animation<>(BISHAMON_WALK_DURATION, bishamonAtlas.findRegions("walk"), Animation.PlayMode.LOOP));
        animations.put(BISHAMON_TURN, new Animation<>(BISHAMON_TURN_DURATION, bishamonAtlas.findRegions("turn"), Animation.PlayMode.REVERSED));
        animations.put(BISHAMON_ATTACK, new Animation<>(BISHAMON_ATTACK_DURATION, bishamonAtlas.findRegions("attack"), Animation.PlayMode.NORMAL));
        animations.put(BISHAMON_SPECIAL_ATTACK_PREP, new Animation<>(BISHAMON_SPECIAL_ATTACK_PREP_DURATION, bishamonAtlas.findRegions("special_prep"), Animation.PlayMode.LOOP));
        animations.put(BISHAMON_SPECIAL_ATTACK, new Animation<>(BISHAMON_SPECIAL_ATTACK_DURATION, bishamonAtlas.findRegions("special"), Animation.PlayMode.NORMAL));
        animations.put(BISHAMON_SPECIAL_ATTACK_POST, new Animation<>(BISHAMON_SPECIAL_ATTACK_POST_DURATION, bishamonAtlas.findRegions("special_post"), Animation.PlayMode.NORMAL));
        animations.put(BISHAMON_HIT, new Animation<>(BISHAMON_HIT_DURATION, bishamonAtlas.findRegions("hit"), Animation.PlayMode.NORMAL));
        animations.put(BISHAMON_DEAD, new Animation<>(BISHAMON_DEAD_DURATION, bishamonAtlas.findRegions("knockdown"), Animation.PlayMode.NORMAL));
        animations.put(BISHAMON_VICTORY, new Animation<>(BISHAMON_VICTORY_DURATION, bishamonAtlas.findRegions("victory"), Animation.PlayMode.NORMAL));

        // HSIEN KO
        TextureAtlas hsienKoAtlas = ResourceManager.getAtlas(HSIEN_KO_ATLAS);

        animations.put(HSIEN_KO_IDLE, new Animation<>(HSIEN_KO_IDLE_DURATION, hsienKoAtlas.findRegions("idle"), Animation.PlayMode.LOOP));
        animations.put(HSIEN_KO_WALK, new Animation<>(HSIEN_KO_WALK_DURATION, hsienKoAtlas.findRegions("walk"), Animation.PlayMode.LOOP));
        animations.put(HSIEN_KO_TURN, new Animation<>(HSIEN_KO_TURN_DURATION, hsienKoAtlas.findRegions("turn"), Animation.PlayMode.REVERSED));
        animations.put(HSIEN_KO_ATTACK, new Animation<>(HSIEN_KO_ATTACK_DURATION, hsienKoAtlas.findRegions("attack"), Animation.PlayMode.NORMAL));
        animations.put(HSIEN_KO_HIT, new Animation<>(HSIEN_KO_HIT_DURATION, hsienKoAtlas.findRegions("hit"), Animation.PlayMode.NORMAL));
        animations.put(HSIEN_KO_DEAD, new Animation<>(HSIEN_KO_DEAD_DURATION, hsienKoAtlas.findRegions("knockdown"), Animation.PlayMode.NORMAL));
        animations.put(HSIEN_KO_VICTORY, new Animation<>(HSIEN_KO_VICTORY_DURATION, hsienKoAtlas.findRegions("victory"), Animation.PlayMode.NORMAL));

        // DEATH ADDER
        TextureAtlas deathAdderAtlas = ResourceManager.getAtlas(DEATH_ADDER_ATLAS);

        animations.put(DEATH_ADDER_IDLE, new Animation<>(DEATH_ADDER_IDLE_DURATION, deathAdderAtlas.findRegions("idle"), Animation.PlayMode.LOOP));
        animations.put(DEATH_ADDER_WALK, new Animation<>(DEATH_ADDER_WALK_DURATION, deathAdderAtlas.findRegions("walk"), Animation.PlayMode.LOOP));
        animations.put(DEATH_ADDER_TURN, new Animation<>(DEATH_ADDER_TURN_DURATION, deathAdderAtlas.findRegions("turn"), Animation.PlayMode.REVERSED));
        animations.put(DEATH_ADDER_ATTACK, new Animation<>(DEATH_ADDER_ATTACK_DURATION, deathAdderAtlas.findRegions("attack"), Animation.PlayMode.NORMAL));
        animations.put(DEATH_ADDER_HIT, new Animation<>(DEATH_ADDER_HIT_DURATION, deathAdderAtlas.findRegions("hit"), Animation.PlayMode.NORMAL));
        animations.put(DEATH_ADDER_DEAD, new Animation<>(DEATH_ADDER_DEAD_DURATION, deathAdderAtlas.findRegions("knockdown"), Animation.PlayMode.NORMAL));
        animations.put(DEATH_ADDER_VICTORY, new Animation<>(DEATH_ADDER_VICTORY_DURATION, deathAdderAtlas.findRegions("victory"), Animation.PlayMode.NORMAL));

        // EFFECTS
        TextureAtlas effectsAtlas = ResourceManager.getAtlas(EFFECTS_ATLAS);

        animations.put(BLOOD_SMALL, new Animation<>(BLOOD_SMALL_DURATION, effectsAtlas.findRegions("blood_small"), Animation.PlayMode.NORMAL));
        animations.put(BLOOD_BIG, new Animation<>(BLOOD_BIG_DURATION, effectsAtlas.findRegions("blood_big"), Animation.PlayMode.NORMAL));
        animations.put(BLOCK, new Animation<>(BLOCK_DURATION, effectsAtlas.findRegions("block"), Animation.PlayMode.NORMAL));
    }

    public static Animation<TextureRegion> getAnimation(String key) { return animations.get(key); }

    public static void dispose() { animations.clear(); }
}
