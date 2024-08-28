package com.svalero.brawler.domains;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.svalero.brawler.managers.ConfigurationManager;
import com.svalero.brawler.managers.LevelManager;
import com.svalero.brawler.managers.SoundManager;
import static com.svalero.brawler.utils.Constants.*;

public class Enemy extends Character {
    protected float turnTimer = 0.0f;
    protected float walkTimer = 0.0f;
    protected float stopTimer = 0.0f;
    protected float specialStopTimer = 0.0f;
    protected float attackTimer = 0.0f;
    protected float timeSinceLastSpecial = 0.0f;
    protected boolean turnChance = false;
    protected boolean walkChance = false;
    protected boolean randomStopChance = false;
    protected boolean specialStopChance = false;
    protected boolean attackStopChance = false;
    protected boolean attackChance = false;

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
                velocity = goTurn(velocity);
                turnChance = false;
            } else if (facingLeft && !isPlayerLeft && turnChance) {
                velocity = goTurn(velocity);
                turnChance = false;
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
            if (!isOnGround) {
                markToFallDead = true;
            }
            if (markToFallDead) {
                if (isOnGround) {
                    stayDead();
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
        if (currentState != State.ATTACK) { clearAttackFixture(); }

        // VICTORY
        if (currentState == State.VICTORY) { velocity = doVictory(velocity); }

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

    protected void shouldWalk(float dt) {
        if (walkTimer >= CHANCE_TIMERS_MARK) {
            walkChance = Math.random() < (ConfigurationManager.hard ? WALK_CHANCE_HARD : WALK_CHANCE);
            walkTimer = 0.0f;
        } else {
            walkTimer += dt;
        }
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

    protected void shouldAttack(float dt) {
        if (attackTimer >= CHANCE_TIMERS_MARK) {
            attackChance = Math.random() < (ConfigurationManager.hard ? ATTACK_CHANCE_HARD : ATTACK_CHANCE);
            attackTimer = 0.0f;
        } else {
            attackTimer += dt;
        }
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
