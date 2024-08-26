package com.svalero.brawler.domains;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.svalero.brawler.managers.LevelManager;
import static com.svalero.brawler.managers.AnimationManager.getAnimation;
import static com.svalero.brawler.utils.Constants.*;

public class Enemy extends Character {
    private float walkingSoundTimer = WALKING_SOUND_TIMER;

    public Enemy(LevelManager levelManager, World world, Vector2 position, String characterAtlas, int health,
                 int attackStrength, float speed, float width, float height, float frameWidth, float frameHeight,
                 float correctionX, float correctionY, float idleDuration, String idleKey, String hitKey,
                 int hitFrames, float hitDuration, String hitSoundKey, String deadKey, String deadSoundKey,
                 int deadFrames, float deadDuration) {
        super(levelManager, world, position, characterAtlas, health, attackStrength, speed, width, height, frameWidth,
                frameHeight, correctionX, correctionY, idleDuration, idleKey, hitKey, hitFrames, hitDuration,
                hitSoundKey, deadKey, deadSoundKey, deadFrames, deadDuration);
    }

    public void update(float dt) {
        manageAI(dt);

//        Vector2 velocity = body.getLinearVelocity();
//        setCurrentStateWithoutReset(State.WALK);
//        currentAnimation = getAnimation(BISHAMON_WALK);
//        velocity.x = speed;
//        body.setLinearVelocity(velocity.x, velocity.y);
//
        Vector2 bodyPosition = body.getPosition();
        position.set(bodyPosition.x, bodyPosition.y);
        stateTime += dt;
    }

    public void manageAI(float dt) {
        Vector2 velocity = body.getLinearVelocity();

        // IDLE / WALK
        if (currentState == State.IDLE || currentState == State.WALK || currentState == State.RUN) {
            boolean isPlayerLeft = levelManager.getPlayer().getPosition().x < this.getPosition().x;


        }

        // HIT
        if (currentState == State.HIT) {
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
