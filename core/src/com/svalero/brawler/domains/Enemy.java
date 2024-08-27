package com.svalero.brawler.domains;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.svalero.brawler.managers.AnimationManager;
import com.svalero.brawler.managers.ConfigurationManager;
import com.svalero.brawler.managers.LevelManager;
import com.svalero.brawler.managers.SoundManager;
import static com.svalero.brawler.managers.AnimationManager.getAnimation;
import static com.svalero.brawler.utils.Constants.*;

public class Enemy extends Character {
    boolean chance;
    private final float timerMark = 1.75f;
    private float turnTimer = 0.0f;
    private float walkTimer = 0.0f;
    private float stopTimer = 0.0f;
    private float attackTimer = 0.0f;
    private float walkingSoundTimer = WALKING_SOUND_TIMER;

    public Enemy(LevelManager levelManager, World world, Vector2 position, String characterAtlas, int health,
                 int attackStrength, float speed, float width, float height, float frameWidth, float frameHeight,
                 float correctionX, float correctionY, float idleDuration, String idleKey, String hitKey,
                 int hitFrames, float hitDuration, String hitSoundPath, String deadKey, String deadSoundPath,
                 int deadFrames, float deadDuration, String turnKey, int turnFrames, float turnDuration,
                 int attackFrames, float attackDuration, float attackWidth, float attackHeight, float attackOffsetX,
                 float attackOffsetY, String walkKey, String attackSoundPath, String attackKey, String victoryKey,
                 String victorySoundPath) {
        super(levelManager, world, position, characterAtlas, health, attackStrength, speed, width, height, frameWidth,
                frameHeight, correctionX, correctionY, idleDuration, idleKey, hitKey, hitFrames, hitDuration,
                hitSoundPath, deadKey, deadSoundPath, deadFrames, deadDuration, turnKey, turnFrames, turnDuration,
                attackFrames, attackDuration, attackWidth, attackHeight, attackOffsetX, attackOffsetY, walkKey,
                attackSoundPath, attackKey, victoryKey, victorySoundPath);
    }

    public void update(float dt) {
        manageAI(dt);
        effectManager.update(dt);
        chance = false;

        Vector2 bodyPosition = body.getPosition();
        position.set(bodyPosition.x, bodyPosition.y);
        stateTime += dt;
    }

    public void manageAI(float dt) {
        Vector2 velocity = body.getLinearVelocity();
        Vector2 playerPosition = levelManager.getPlayer().getPosition();
        Vector2 thisPosition = this.getPosition();

        boolean isPlayerLeft = playerPosition.x < thisPosition.x;
        float distanceX = Math.abs((playerPosition.x - thisPosition.x)) - (levelManager.getPlayer().width / 2 + this.width / 2);

        // IDLE / WALK
        if (currentState == State.IDLE || currentState == State.WALK) {

            // Chance of turning
            if (facingLeft != isPlayerLeft && turnTimer >= timerMark) {
                chance = Math.random() < (ConfigurationManager.hard ? TURN_CHANCE_HARD : TURN_CHANCE);
                turnTimer = 0.0f;
            } else {
                turnTimer += dt;
            }
            // Turn
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
            if (facingLeft == isPlayerLeft) {
                // WALK
                if (currentState == State.IDLE && distanceX >= attackWidth) {
                    // Chance to start walking
                    if (walkTimer >= timerMark) {
                        chance = Math.random() < (ConfigurationManager.hard ? WALK_CHANCE_HARD : WALK_CHANCE);
                        walkTimer = 0.0f;
                    } else {
                        walkTimer += dt;
                    }
                    // Start walking
                    if (chance) {
                        setCurrentState(State.WALK);
                        currentAnimation = getAnimation(walkKey);
                        SoundManager.playLongSound(WALKING_ON_GRASS_SOUND, walkKey);
                        walkingSoundTimer = WALKING_SOUND_TIMER;

                        if (facingLeft) {
                            velocity.x = -speed;
                        } else {
                            velocity.x = speed;
                        }
                    }
                // Chance of random stop
                } else if (currentState == State.WALK && distanceX > attackWidth) {
                    if (stopTimer >= timerMark) {
                        chance = Math.random() < (ConfigurationManager.hard ? STOP_CHANCE_HARD : STOP_CHANCE);
                        stopTimer = 0.0f;
                    } else {
                        stopTimer += dt;
                    }
                    if (chance) {
                        setCurrentStateWithoutReset(State.IDLE);
                        currentAnimation = AnimationManager.getAnimation(idleKey);
                        velocity.x = 0;
                    }
                // Stop on attack range
                } else if (currentState == State.WALK && distanceX < attackWidth) {
                    setCurrentStateWithoutReset(State.IDLE);
                    currentAnimation = AnimationManager.getAnimation(idleKey);
                    velocity.x = 0;
                // Chance of attack
                } else if (currentState == State.IDLE && distanceX < attackWidth) {
                    if (attackTimer >= timerMark) {
                        chance = Math.random() < (ConfigurationManager.hard ? ATTACK_CHANCE_HARD : ATTACK_CHANCE);
                        attackTimer = 0.0f;
                    } else {
                        attackTimer += dt;
                    }
                    // Attack
                    if (chance) {
                        velocity.x = 0;
                        currentAnimation = getAnimation(attackKey);
                        SoundManager.playSound(attackSoundPath);
                        setCurrentState(State.ATTACK);

                        // TODO Se puede implementar una lógica adicional para que en casos como Bishamon el fixture de
                        // TODO daño solo aparezca a partir de X fotograma.
                        launchAttack();
                    }
                }
            }

            // TURN
        } else if (currentState == State.TURN) {
            if (stateTime >= turnFrames * turnDuration) {
                currentAnimation = AnimationManager.getAnimation(idleKey);
                setCurrentStateWithoutReset(State.IDLE);
            }

        // ATTACK
        } else if (currentState == State.ATTACK) {
            if (stateTime >= attackFrames * attackDuration) {
                setCurrentState(State.IDLE);
                currentAnimation = AnimationManager.getAnimation(idleKey);
                if (attackFixture != null) {
                    clearAttackFixture();
                }
            }

        // HIT
        } else if (currentState == State.HIT) {
            velocity.x = 0;
            if (stateTime >= hitFrames * hitDuration) {
                currentAnimation = getAnimation(idleKey);
                setCurrentStateWithoutReset(State.IDLE);
            }

        // DEAD
        } else if (currentState == State.DEAD) {
            if (stateTime == 0) {
                velocity.y = 100f;
                if (facingLeft) {
                    velocity.x = 140f;
                } else {
                    velocity.x = -140f;
                }
            }
        }

        // Stop walking sound
        if (currentState != State.WALK) {
            if (walkingSoundTimer > 0) {
                walkingSoundTimer -= dt;
            } else {
                SoundManager.stopLongSound(walkKey);
            }
        }

        // Erase unused attack fixtures
        if (currentState != State.ATTACK) {
            clearAttackFixture();
        }

        // VICTORY
        if (currentState == State.VICTORY) {
            if (stateTime == 0) {
                currentAnimation = getAnimation(victoryKey);
                SoundManager.playSound(victorySoundPath);
            }
        }

        body.setLinearVelocity(velocity.x, velocity.y);
    }
}
