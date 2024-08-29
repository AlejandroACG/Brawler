package com.svalero.brawler.domains;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.svalero.brawler.managers.ConfigurationManager;
import com.svalero.brawler.managers.LevelManager;
import com.svalero.brawler.managers.SoundManager;

import static com.svalero.brawler.domains.Character.State.*;
import static com.svalero.brawler.managers.AnimationManager.getAnimation;
import static com.svalero.brawler.utils.Constants.*;

public class DeathAdder extends Enemy implements SpecialAttackable {
    public DeathAdder(LevelManager levelManager, World world, Vector2 position) {
        super(levelManager, world, position, DEATH_ADDER_ATLAS,
                ConfigurationManager.hard ? DEATH_ADDER_HEALTH_HARD : DEATH_ADDER_HEALTH,
                ConfigurationManager.hard ? DEATH_ADDER_ATTACK_STRENGTH_HARD : DEATH_ADDER_ATTACK_STRENGTH,
                DEATH_ADDER_SPEED, DEATH_ADDER_WIDTH, DEATH_ADDER_HEIGHT, DEATH_ADDER_FRAME_WIDTH,
                DEATH_ADDER_FRAME_HEIGHT, DEATH_ADDER_DRAW_CORRECTION_X, DEATH_ADDER_DRAW_CORRECTION_Y,
                DEATH_ADDER_IDLE_DURATION, DEATH_ADDER_IDLE, DEATH_ADDER_HIT, DEATH_ADDER_HIT_FRAMES,
                DEATH_ADDER_HIT_DURATION, DEATH_ADDER_HIT_SOUND, DEATH_ADDER_DEAD, DEATH_ADDER_DEAD_SOUND,
                DEATH_ADDER_DEAD_FRAMES, DEATH_ADDER_DEAD_DURATION, DEATH_ADDER_TURN, DEATH_ADDER_TURN_FRAMES,
                DEATH_ADDER_TURN_DURATION, DEATH_ADDER_ATTACK_FRAMES, DEATH_ADDER_ATTACK_DURATION,
                DEATH_ADDER_ATTACK_WIDTH, DEATH_ADDER_ATTACK_HEIGHT, DEATH_ADDER_ATTACK_OFFSET_X,
                DEATH_ADDER_ATTACK_OFFSET_Y, DEATH_ADDER_WALK, DEATH_ADDER_ATTACK_SOUND, DEATH_ADDER_ATTACK,
                DEATH_ADDER_VICTORY, DEATH_ADDER_VICTORY_SOUND, DEATH_ADDER_SPECIAL_ATTACK_DISTANCE,
                DEATH_ADDER_ATTACK_DISTANCE);
    }

    @Override
    public void goSpecialAttack() {
        setCurrentState(SPECIAL_ATTACK_PREP);
        currentAnimation = getAnimation(DEATH_ADDER_SPECIAL_ATTACK);
        SoundManager.playSound(DEATH_ADDER_SPECIAL_ATTACK_SOUND);
    }

    private void shootProjectile() {
        Vector2 position = body.getPosition().cpy();
        position.x += facingLeft ? -DEATH_ADDER_WAVE_OFFSET_X : DEATH_ADDER_WAVE_OFFSET_X;
        position.y += DEATH_ADDER_WAVE_OFFSET_Y;

        Wave wave = new Wave(levelManager, position, facingLeft, DEATH_ADDER_WAVE,
                DEATH_ADDER_WAVE_FRAMES * DEATH_ADDER_WAVE_DURATION, this);
        levelManager.addProjectile(wave);
    }

    @Override
    public Vector2 handleSpecialAttack(float dt, Vector2 velocity) {
        if (currentState == SPECIAL_ATTACK_PREP) {
            if (stateTime >= 2 * DEATH_ADDER_ATTACK_DURATION) {
                shootProjectile();
                setCurrentStateWithoutReset(SPECIAL_ATTACK);
            }
        }

        if (currentState == SPECIAL_ATTACK) {
            if (stateTime >= DEATH_ADDER_SPECIAL_ATTACK_FRAMES * DEATH_ADDER_SPECIAL_ATTACK_DURATION) {
                setCurrentState(IDLE);
                currentAnimation = getAnimation(idleKey);
            }
        }

        return velocity;
    }
}
