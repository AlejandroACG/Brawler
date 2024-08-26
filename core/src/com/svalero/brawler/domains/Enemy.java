package com.svalero.brawler.domains;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.svalero.brawler.managers.AnimationManager;
import com.svalero.brawler.managers.LevelManager;
import com.svalero.brawler.managers.SoundManager;

import static com.svalero.brawler.managers.AnimationManager.getAnimation;
import static com.svalero.brawler.utils.Constants.*;

public class Enemy extends Character {
    boolean chance;
    private float turnTimerMark = 2.0f;
    private float turnTimer = 0.0f;
    private float walkingSoundTimer = WALKING_SOUND_TIMER;

    public Enemy(LevelManager levelManager, World world, Vector2 position, String characterAtlas, int health,
                 int attackStrength, float speed, float width, float height, float frameWidth, float frameHeight,
                 float correctionX, float correctionY, float idleDuration, String idleKey, String hitKey,
                 int hitFrames, float hitDuration, String hitSoundPath, String deadKey, String deadSoundPath,
                 int deadFrames, float deadDuration, String turnKey, int turnFrames, float turnDuration) {
        super(levelManager, world, position, characterAtlas, health, attackStrength, speed, width, height, frameWidth,
                frameHeight, correctionX, correctionY, idleDuration, idleKey, hitKey, hitFrames, hitDuration,
                hitSoundPath, deadKey, deadSoundPath, deadFrames, deadDuration, turnKey, turnFrames, turnDuration);
    }

    public void update(float dt) {
        manageAI(dt);

        Vector2 bodyPosition = body.getPosition();
        position.set(bodyPosition.x, bodyPosition.y);
        stateTime += dt;
    }

    public void manageAI(float dt) {
        Vector2 velocity = body.getLinearVelocity();

        // IDLE / WALK
        if (currentState == State.IDLE || currentState == State.WALK || currentState == State.RUN) {
            boolean isPlayerLeft = levelManager.getPlayer().getPosition().x < this.getPosition().x;

            // Turn
            if (facingLeft != isPlayerLeft && turnTimer >= turnTimerMark) {
                chance = Math.random() < 0.5;
            } else {
                turnTimer += dt;
            }
            if (!facingLeft && isPlayerLeft && chance) {
                setCurrentState(State.TURN);
                velocity.x = 0;
                currentAnimation = getAnimation(turnKey);
                facingLeft = true;
                turnTimer = 0.0f;
                chance = false;
            } else if (facingLeft && !isPlayerLeft && chance) {
                setCurrentState(State.TURN);
                velocity.x = 0;
                currentAnimation = getAnimation(turnKey);
                facingLeft = false;
                turnTimer = 0.0f;
                chance = false;
            }

            // Walk

        // TURN
        } else if (currentState == State.TURN) {
            if (stateTime >= turnFrames * turnDuration) {
                currentAnimation = AnimationManager.getAnimation(idleKey);
                setCurrentStateWithoutReset(State.IDLE);
            }
        // HIT
        } else if (currentState == State.HIT) {
            velocity.x = 0;
            if (stateTime >= hitFrames * hitDuration) {
                currentAnimation = getAnimation(idleKey);
                setCurrentStateWithoutReset(State.IDLE);
            }
        }

        // DEAD
        if (currentState == State.DEAD) {
            if (stateTime == 0) {
                velocity.y = 100f;
                if (facingLeft) {
                    velocity.x = 140f;
                } else {
                    velocity.x = -140f;
                }
            }
        }

        body.setLinearVelocity(velocity.x, velocity.y);
    }
}
