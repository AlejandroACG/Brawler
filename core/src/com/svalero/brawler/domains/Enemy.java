package com.svalero.brawler.domains;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.svalero.brawler.managers.ConfigurationManager;
import com.svalero.brawler.managers.LevelManager;
import com.svalero.brawler.managers.SoundManager;
import static com.svalero.brawler.managers.AnimationManager.getAnimation;
import static com.svalero.brawler.utils.Constants.*;

public class Enemy extends Character {
    boolean turnChance = false;
    boolean walkChance = false;
    boolean randomStopChance = false;
    boolean specialStopChance = false;
    boolean attackStopChance = false;
    boolean attackChance = false;
    private float turnTimer = 0.0f;
    private float walkTimer = 0.0f;
    private float stopTimer = 0.0f;
    private float specialStopTimer = 0.0f;
    private float attackTimer = 0.0f;
    protected float timeSinceLastSpecial = 0.0f;
    private float walkingSoundTimer = WALKING_SOUND_TIMER;

    public Enemy(LevelManager levelManager, World world, Vector2 position, String characterAtlas, int health,
                 int attackStrength, float speed, float width, float height, float frameWidth, float frameHeight,
                 float correctionX, float correctionY, float idleDuration, String idleKey, String hitKey,
                 int hitFrames, float hitDuration, String hitSoundPath, String deadKey, String deadSoundPath,
                 int deadFrames, float deadDuration, String turnKey, int turnFrames, float turnDuration,
                 int attackFrames, float attackDuration, float attackWidth, float attackHeight, float attackOffsetX,
                 float attackOffsetY, String walkKey, String attackSoundPath, String attackKey, String victoryKey,
                 String victorySoundPath, float specialAttackDistance) {
        super(levelManager, world, position, characterAtlas, health, attackStrength, speed, width, height, frameWidth,
                frameHeight, correctionX, correctionY, idleDuration, idleKey, hitKey, hitFrames, hitDuration,
                hitSoundPath, deadKey, deadSoundPath, deadFrames, deadDuration, turnKey, turnFrames, turnDuration,
                attackFrames, attackDuration, attackWidth, attackHeight, attackOffsetX, attackOffsetY, walkKey,
                attackSoundPath, attackKey, victoryKey, victorySoundPath, specialAttackDistance);
    }

    public void update(float dt) {
        manageAI(dt);
        effectManager.update(dt);
        resetChances();

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
            shouldTurn(isPlayerLeft, dt);
            if (!facingLeft && isPlayerLeft && turnChance) {
                velocity = goTurn(velocity, true);
            } else if (facingLeft && !isPlayerLeft && turnChance) {
                velocity = goTurn(velocity, false);
            }
            if (facingLeft == isPlayerLeft) {
                if (currentState == State.IDLE && distanceX >= attackWidth) {
                    shouldWalk(dt);
                    if (walkChance) {
                        velocity = goWalk(velocity);
                    }
                } else if (currentState == State.WALK && distanceX > attackWidth) {
                    shouldRandomStop(dt);
                    if (randomStopChance) {
                        velocity = goIdle(velocity);
                    }
                } else if (currentState == State.WALK && timeSinceLastSpecial >= specialAttackCooldown
                        && distanceX <= specialAttackDistance) {
                    shouldSpecialStop(dt);
                    if (specialStopChance) {
                        velocity = goIdle(velocity);
                    }
                } else if (currentState == State.WALK && distanceX < attackWidth) {
                    velocity = goIdle(velocity);
                } else if (currentState == State.IDLE && distanceX < attackWidth) {
                    shouldAttack(dt);
                    if (attackChance) {
                        velocity = goAttack(velocity);
                    }
                }
            }

        // TURN
        } else if (currentState == State.TURN) {
            if (stateTime >= turnFrames * turnDuration) {
                velocity = goIdle(velocity);
            }

        // ATTACK
        } else if (currentState == State.ATTACK) {
            if (stateTime >= attackFrames * attackDuration) {
                velocity = goIdle(velocity);
                if (attackFixture != null) {
                    clearAttackFixture();
                }
            }

        // HIT
        } else if (currentState == State.HIT) {
            velocity.x = 0;
            if (stateTime >= hitFrames * hitDuration) {
                velocity = goIdle(velocity);
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

    protected void shouldTurn(boolean isPlayerLeft, float dt) {
        if (facingLeft != isPlayerLeft && turnTimer >= CHANCE_TIMERS_MARK) {
            turnChance = Math.random() < (ConfigurationManager.hard ? TURN_CHANCE_HARD : TURN_CHANCE);
            turnTimer = 0.0f;
        } else {
            turnTimer += dt;
        }
    }

    protected Vector2 goTurn(Vector2 velocity, boolean left) {
        setCurrentState(State.TURN);
        velocity.x = 0;
        currentAnimation = getAnimation(turnKey);
        facingLeft = left;
        turnTimer = 0.0f;
        turnChance = false;

        return velocity;
    }

    protected void shouldWalk(float dt) {
        if (walkTimer >= CHANCE_TIMERS_MARK) {
            walkChance = Math.random() < (ConfigurationManager.hard ? WALK_CHANCE_HARD : WALK_CHANCE);
            walkTimer = 0.0f;
        } else {
            walkTimer += dt;
        }
    }

    protected Vector2 goWalk(Vector2 velocity) {
        setCurrentState(State.WALK);
        currentAnimation = getAnimation(walkKey);
        SoundManager.playLongSound(WALKING_ON_GRASS_SOUND, walkKey);
        walkingSoundTimer = WALKING_SOUND_TIMER;

        if (facingLeft) {
            velocity.x = -speed;
        } else {
            velocity.x = speed;
        }

        return velocity;
    }

    protected void shouldRandomStop(float dt) {
        if (stopTimer >= CHANCE_TIMERS_MARK) {
            randomStopChance = Math.random() < (ConfigurationManager.hard ? RANDOM_STOP_CHANCE_HARD : RANDOM_STOP_CHANCE);
            stopTimer = 0.0f;
        } else {
            stopTimer += dt;
        }
    }

    protected void shouldSpecialStop(float dt) {
        if (specialStopTimer >= CHANCE_TIMERS_MARK) {
            specialStopChance = Math.random() < (ConfigurationManager.hard ? SPECIAL_STOP_CHANCE_HARD : SPECIAL_STOP_CHANCE);
            specialStopTimer = 0.0f;
        } else {
            specialStopTimer += dt;
        }
    }

    protected Vector2 goIdle(Vector2 velocity) {
        setCurrentStateWithoutReset(State.IDLE);
        currentAnimation = getAnimation(idleKey);
        velocity.x = 0;

        return velocity;
    }

    protected void shouldAttack(float dt) {
        if (attackTimer >= CHANCE_TIMERS_MARK) {
            attackChance = Math.random() < (ConfigurationManager.hard ? ATTACK_CHANCE_HARD : ATTACK_CHANCE);
            attackTimer = 0.0f;
        } else {
            attackTimer += dt;
        }
    }

    protected Vector2 goAttack(Vector2 velocity) {
        velocity.x = 0;
        currentAnimation = getAnimation(attackKey);
        SoundManager.playSound(attackSoundPath);
        setCurrentState(State.ATTACK);

        // TODO Se puede implementar una lógica adicional para que en casos como Bishamon el fixture de
        // TODO daño solo aparezca a partir de X fotograma.
        launchAttack();
        return velocity;
    }

    protected void resetChances() {
        turnChance = false;
        walkChance = false;
        randomStopChance = false;
        specialStopChance = false;
        attackStopChance = false;
        attackChance = false;
    }
}
