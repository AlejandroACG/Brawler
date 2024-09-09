package com.svalero.brawler.domains.characters;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
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
    protected float specialAttackTimer = 0.0f;
    protected boolean turnChance = false;
    protected boolean walkChance = false;
    protected boolean randomStopChance = false;
    protected boolean specialAttackStopChance = false;
    protected boolean attackStopChance = false;
    protected boolean attackChance = false;
    protected boolean specialAttackChance = false;

    public Enemy(LevelManager levelManager, World world, Vector2 position, String characterAtlas, int health,
                 int attackStrength, float speed, float width, float height, float frameWidth, float frameHeight,
                 float correctionX, float correctionY, float idleDuration, String idleKey, String hitKey,
                 int hitFrames, float hitDuration, String hitSoundPath, String deadKey, String deadSoundPath,
                 int deadFrames, float deadDuration, String turnKey, int turnFrames, float turnDuration,
                 int attackFrames, float attackDuration, float attackWidth, float attackHeight, float attackOffsetX,
                 float attackOffsetY, String walkKey, String attackSoundPath, String attackKey, String victoryKey,
                 String victorySoundPath, float specialAttackDistance, float attackDistance) {
        super(levelManager, world, position, characterAtlas, health, attackStrength, speed, width, height, frameWidth,
                frameHeight, correctionX, correctionY, idleDuration, idleKey, hitKey, hitFrames, hitDuration,
                hitSoundPath, deadKey, deadSoundPath, deadFrames, deadDuration, turnKey, turnFrames, turnDuration,
                attackFrames, attackDuration, attackWidth, attackHeight, attackOffsetX, attackOffsetY, walkKey,
                attackSoundPath, attackKey, victoryKey, victorySoundPath, specialAttackDistance, attackDistance);
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

        if (specialAttackCooldown > 0) { specialAttackCooldown -= dt; }

        // Reset things after a special move
        if (currentState != State.SPECIAL_ATTACK_PREP && currentState != State.SPECIAL_ATTACK && currentState
                != State.SPECIAL_ATTACK_POST) {
            // TODO Compensando por la escala
            body.setGravityScale(1.0f);
            Fixture baseFixture = body.getFixtureList().get(0);
            baseFixture.setDensity(1f);
            body.resetMassData();
        }

        // INTRO
        if (currentState == State.INTRO) {
            velocity.x = 0;
            return;
        }

        // IDLE / WALK
        if (currentState == State.IDLE || currentState == State.WALK) {
            if (currentState == State.IDLE) {
                velocity = goIdle(velocity);
            }
            if (currentState == State.WALK && facingLeft != isPlayerLeft) {
                velocity = goIdle(velocity);
            }
            shouldTurn(isPlayerLeft, dt);
            if (!facingLeft && isPlayerLeft && turnChance) {
                velocity = goTurn(velocity);
                turnChance = false;
            } else if (facingLeft && !isPlayerLeft && turnChance) {
                velocity = goTurn(velocity);
                turnChance = false;
            }
            if (facingLeft == isPlayerLeft) {
                if (currentState == State.WALK && distanceX > attackDistance) {
                    shouldRandomStop(dt);
                    if (randomStopChance) {
                        velocity = goIdle(velocity);
                    }
                }
                if (currentState == State.WALK && specialAttackCooldown <= 0 && distanceX <= specialAttackDistance) {
                    shouldSpecialStop(dt);
                    if (specialAttackStopChance) {
                        velocity = goIdle(velocity);
                    }
                }
                if (currentState == State.IDLE && specialAttackCooldown <= 0 && distanceX < specialAttackDistance) {
                    shouldSpecialAttack(dt);
                    if (specialAttackChance) {
                        goSpecialAttack();
                        specialAttackCooldown = specialAttackCooldownReset;
                    }
                }
                if (currentState == State.WALK && distanceX < attackDistance) {
                    velocity = goIdle(velocity);
                }
                if (currentState == State.IDLE && distanceX < attackDistance) {
                    shouldAttack(dt);
                    if (attackChance) {
                        velocity = goAttack(velocity);
                    }
                }
                if (currentState == State.IDLE && distanceX >= attackDistance) {
                    shouldWalk(dt);
                    if (walkChance) {
                        velocity = goWalk(velocity);
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

            // SPECIAL ATTACK
        } else if (currentState == State.SPECIAL_ATTACK || currentState == State.SPECIAL_ATTACK_PREP
                || currentState == State.SPECIAL_ATTACK_POST) {
            velocity = handleSpecialAttack(dt, velocity);

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
            if (markToFallDead || stateTime == deadFrames * deadDuration) {
                if (isOnGround || stateTime == deadFrames * deadDuration) {
                    velocity = stayDead(velocity);
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
        if (currentState != State.ATTACK && currentState != State.SPECIAL_ATTACK) { clearAttackFixture(); }

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
            specialAttackStopChance = Math.random() < (ConfigurationManager.hard ? SPECIAL_STOP_CHANCE_HARD : SPECIAL_ATTACK_STOP_CHANCE);
            specialStopTimer = 0.0f;
        } else {
            specialStopTimer += dt;
        }
    }

    protected void shouldSpecialAttack(float dt) {
        if (specialAttackTimer >= CHANCE_TIMERS_MARK) {
            specialAttackChance = Math.random() < (ConfigurationManager.hard ? SPECIAL_ATTACK_CHANCE_HARD : SPECIAL_ATTACK_CHANCE);
            specialAttackTimer = 0.0f;
        } else {
            specialAttackTimer += dt;
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

    protected void goSpecialAttack() {}

    protected Vector2 handleSpecialAttack(float dt, Vector2 velocity) { return velocity; }

    protected void resetChances() {
        turnChance = false;
        walkChance = false;
        randomStopChance = false;
        specialAttackStopChance = false;
        attackStopChance = false;
        specialAttackChance = false;
        attackChance = false;
    }
}
