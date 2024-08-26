package com.svalero.brawler.domains;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import static com.svalero.brawler.managers.AnimationManager.getAnimation;
import static com.svalero.brawler.utils.Constants.*;

import com.svalero.brawler.managers.LevelManager;
import com.svalero.brawler.managers.SoundManager;

public abstract class Player extends Character {
    private float walkingSoundTimer = WALKING_SOUND_TIMER;
    private float runningSoundTimer = RUNNING_SOUND_TIMER;
    private long lastKeyPressTimeA = 0;
    private long lastKeyPressTimeD = 0;
    private boolean isRunning = false;

    public Player(LevelManager levelManager, World world, Vector2 position, String characterAtlas, int health,
                  int attackStrength, float speed, float width, float height, float frameWidth, float frameHeight,
                  float correctionX, float correctionY, float idleDuration, float jumpUpDuration,
                  float jumpDownDuration, float jumpStrength, String idleKey, String turnKey, String walkKey,
                  String runKey, String blockUpKey, String blockDownKey, String crouchDownKey, String crouchUpKey,
                  String jumpUpKey, String jumpDownKey, String landKey, String attackKey, String jumpAttackKey,
                  String hitKey, String blockMoveSoundKey, String jumpSoundKey, String attackSoundKey, int turnFrames,
                  float turnDuration, int blockFrames, float blockDuration, int crouchFrames, float crouchDuration,
                  int landFrames, float landDuration, int hitFrames, float hitDuration, int attackFrames,
                  float attackDuration, int jumpAttackFrames, float jumpAttackDuration, float attackOffsetX,
                  float attackOffsetY, float attackWidth, float attackHeight, float jumpAttackOffsetX,
                  float jumpAttackOffsetY, float jumpAttackWidth, float jumpAttackHeight, String hitSoundKey,
                  String deadKey, String deadSoundKey, int deadFrames, float deadDuration) {
        super(levelManager, world, position, characterAtlas, health, attackStrength, speed, width, height, frameWidth, frameHeight,
                correctionX, correctionY, idleDuration, jumpUpDuration, jumpDownDuration, jumpStrength, idleKey,
                turnKey, walkKey, runKey, blockUpKey, blockDownKey, crouchDownKey, crouchUpKey, jumpUpKey, jumpDownKey,
                landKey, attackKey, jumpAttackKey, hitKey, blockMoveSoundKey, jumpSoundKey, attackSoundKey, turnFrames,
                turnDuration, blockFrames, blockDuration, crouchFrames, crouchDuration, landFrames, landDuration,
                hitFrames, hitDuration, attackFrames, attackDuration, jumpAttackFrames, jumpAttackDuration,
                attackOffsetX, attackOffsetY, attackWidth, attackHeight, jumpAttackOffsetX, jumpAttackOffsetY,
                jumpAttackWidth, jumpAttackHeight, hitSoundKey, deadKey, deadSoundKey, deadFrames, deadDuration);
    }

    public void update(float dt) {
        manageInput(dt);
        Vector2 bodyPosition = body.getPosition();
        position.set(bodyPosition.x, bodyPosition.y);
        stateTime += dt;
    }

    // TODO Cuando se agacha, la mitad superior del body no deberia recibir daños, igual puedo crear un Fixture para cuando está
    // TODO agachado y alternarlos?
    // TODO Faltan ataques crouch y el movimiento especial
    // TODO Faltan bloqueos
    // TODO Daño
    // TODO Pause
    // TODO Todo el tema de agacharse
    // TODO Darle ángulo al fixture del ataque aéreo para mejorar la hitbox
    // TODO Mejorar la lógica de mantener A y D a la vez

    public void manageInput(float dt) {
        Vector2 velocity = body.getLinearVelocity();
        long currentTime = System.currentTimeMillis();

        // Stop WALK / RUN
        if ((currentState == State.WALK || currentState == State.RUN) &&
                (!Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D))) {
            setCurrentStateWithoutReset(State.IDLE);
            isRunning = true;
        }

        // IDLE / WALK / RUN
        if (currentState == State.IDLE || currentState == State.WALK || currentState == State.RUN) {
            currentAnimation = getAnimation(idleKey);

            // Walking / running left
            if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                if (currentTime - lastKeyPressTimeA < DOUBLE_CLICK_THRESHOLD || currentState == State.RUN) {
                    velocity.x = -speed * 2;
                    setCurrentState(State.RUN);
                    isRunning = true;
                    currentAnimation = getAnimation(runKey);
                    SoundManager.playLongSound(RUNNING_ON_GRASS_SOUND, runKey);
                    runningSoundTimer = RUNNING_SOUND_TIMER;
                }
                lastKeyPressTimeA = currentTime;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                if (!isFacingLeft()) {
                    setCurrentState(State.TURN);
                    velocity.x = 0;
                    currentAnimation = getAnimation(turnKey);
                    facingLeft = true;
                } else if (currentState == State.RUN) {
                    velocity.x = -speed * 2;
                    setCurrentState(State.RUN);
                    currentAnimation = getAnimation(runKey);
                    SoundManager.playLongSound(RUNNING_ON_GRASS_SOUND, runKey);
                    runningSoundTimer = RUNNING_SOUND_TIMER;
                } else {
                    velocity.x = -speed;
                    setCurrentState(State.WALK);
                    currentAnimation = getAnimation(walkKey);
                    SoundManager.playLongSound(WALKING_ON_GRASS_SOUND, walkKey);
                    walkingSoundTimer = WALKING_SOUND_TIMER;
                }
            }
            // Walking / running right
            if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                if (currentTime - lastKeyPressTimeD < DOUBLE_CLICK_THRESHOLD || currentState == State.RUN) {
                    velocity.x = speed * 2;
                    isRunning = true;
                    setCurrentState(State.RUN);
                    currentAnimation = getAnimation(runKey);
                    SoundManager.playLongSound(RUNNING_ON_GRASS_SOUND, runKey);
                    runningSoundTimer = RUNNING_SOUND_TIMER;
                }
                lastKeyPressTimeD = currentTime;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                if (isFacingLeft()) {
                    setCurrentState(State.TURN);
                    velocity.x = 0;
                    currentAnimation = getAnimation(turnKey);
                    facingLeft = false;
                } else if (currentState == State.RUN) {
                    velocity.x = speed * 2;
                    setCurrentState(State.RUN);
                    currentAnimation = getAnimation(runKey);
                    SoundManager.playLongSound(RUNNING_ON_GRASS_SOUND, runKey);
                    runningSoundTimer = RUNNING_SOUND_TIMER;
                } else {
                    velocity.x = speed;
                    setCurrentState(State.WALK);
                    currentAnimation = getAnimation(walkKey);
                    SoundManager.playLongSound(WALKING_ON_GRASS_SOUND, walkKey);
                    walkingSoundTimer = WALKING_SOUND_TIMER;
                }
            }

            // Block
            if (Gdx.input.isKeyPressed(Input.Keys.K)) {
                currentAnimation = getAnimation(blockUpKey);
                velocity.x = 0;
                setCurrentState(State.BLOCK_UP);
                SoundManager.playSound(blockMoveSoundKey);
            }

            // Crouch
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                currentAnimation = getAnimation(crouchDownKey);
                velocity.x = 0;
                setCurrentState(State.CROUCH_DOWN);
            }

            // Jump
            if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
                velocity.y = jumpStrength;
                SoundManager.playSound(jumpSoundKey);
                setCurrentState(State.JUMP_UP);
            }

            // Attack
            if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
                velocity.x = 0;
                currentAnimation = getAnimation(attackKey);
                SoundManager.playSound(attackSoundKey);
                setCurrentState(State.ATTACK);

                launchAttack();
            }

            // Idle
            if (currentState == State.IDLE) {
                isRunning = false;
                velocity.x = 0;
            }

        // TURN
        } else if (currentState == State.TURN) {
            if ((Gdx.input.isKeyJustPressed(Input.Keys.A) && currentTime - lastKeyPressTimeA < DOUBLE_CLICK_THRESHOLD)
                    || (Gdx.input.isKeyJustPressed(Input.Keys.D) && currentTime - lastKeyPressTimeD < DOUBLE_CLICK_THRESHOLD)) {
                isRunning = true;
            }
            if (stateTime >= turnFrames * turnDuration) {
                if (isRunning) {
                    setCurrentStateWithoutReset(State.RUN);
                } else {
                    setCurrentStateWithoutReset(State.WALK);
                }
            }

        // BLOCK
        } else if (currentState == State.BLOCK_UP) {
            if (stateTime >= blockFrames * blockDuration) {
                setCurrentStateWithoutReset(State.BLOCK);
            }
        } else if (currentState == State.BLOCK) {
            if (!Gdx.input.isKeyPressed(Input.Keys.K)) {
                SoundManager.playSound(blockMoveSoundKey);
                currentAnimation = getAnimation(blockDownKey);
                setCurrentState(State.BLOCK_DOWN);
            }
        } else if (currentState == State.BLOCK_DOWN) {
            if (stateTime >= blockFrames * blockDuration) {
                setCurrentStateWithoutReset(State.IDLE);
            }

        // CROUCH DOWN
        } else if (currentState == State.CROUCH_DOWN) {
            if (stateTime >= crouchFrames * crouchDuration) {
                setCurrentState(State.CROUCH);
            }
            if (!Gdx.input.isKeyPressed(Input.Keys.S)) {
                currentAnimation = getAnimation(crouchUpKey);
                setCurrentState(State.CROUCH_UP);
            }

        // CROUCH
        } else if (currentState == State.CROUCH) {
            currentAnimation = getAnimation(crouchDownKey);
            if (!Gdx.input.isKeyPressed(Input.Keys.S)) {
                currentAnimation = getAnimation(crouchUpKey);
                setCurrentState(State.CROUCH_UP);
            }

        // CROUCH UP
        } else if (currentState == State.CROUCH_UP) {
            if (stateTime >= crouchFrames * crouchDuration) {
                setCurrentStateWithoutReset(State.IDLE);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                currentAnimation = getAnimation(crouchDownKey);
                setCurrentState(State.CROUCH_DOWN);
            }

        // JUMP UP
        } else if (currentState == State.JUMP_UP || currentState == State.JUMP_DOWN) {
            if (velocity.y > 0) {
                currentAnimation = getAnimation(jumpUpKey);
                setCurrentState(State.JUMP_UP);
            } else if (velocity.y <= 0) {
                currentAnimation = getAnimation(jumpDownKey);
                setCurrentState(State.JUMP_DOWN);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                velocity.x = body.getLinearVelocity().x + speed * 0.05f;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                velocity.x = body.getLinearVelocity().x - speed * 0.05f;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.J) && !hasAttackedThisJump) {
                currentAnimation = getAnimation(jumpAttackKey);
                SoundManager.playSound(attackSoundKey);
                setCurrentState(State.JUMP_ATTACK);
                setHasAttackedThisJump(true);

                launchJumpAttack();
            }

        // LAND
        } else if (currentState == State.LAND) {
            currentAnimation = getAnimation(landKey);
            velocity.x = 0;
            if (stateTime == 0) {
                SoundManager.playSound(LAND_SOUND);
            }
            if (stateTime >= landFrames * landDuration) {
                setCurrentState(State.IDLE);
                if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.D)) {
                    setCurrentState(State.WALK);
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                currentAnimation = getAnimation(crouchDownKey);
                setCurrentState(State.CROUCH_DOWN);
            }
        }

        // ATTACK
        if (currentState == State.ATTACK) {
            if (stateTime >= attackFrames * attackDuration) {
                setCurrentState(State.IDLE);
                if (attackFixture != null) {
                    clearAttackFixture();
                }
            }
        }

        // JUMP ATTACK
        if (currentState == State.JUMP_ATTACK) {
            velocity.y = 0;
            velocity.x = 0;
            body.setGravityScale(0);
            if (stateTime >= jumpAttackFrames * jumpAttackDuration) {
                setCurrentState(State.JUMP_DOWN);
                body.setGravityScale(1);
                stateTime = jumpDownDuration;
                velocity.y = -1;
                if (attackFixture != null) {
                    clearAttackFixture();
                }
            }
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
            if (stateTime >= (deadFrames - 1) * deadDuration) {
                velocity.y = 0;
                velocity.x = 0;
            }
        }

        // Erase unused attack fixtures
        if (currentState != State.ATTACK && currentState != State.JUMP_ATTACK) {
            clearAttackFixture();
        }

        // Stop walking sound
        if (currentState != State.WALK) {
            if (walkingSoundTimer > 0) {
                walkingSoundTimer -= dt;
            } else {
                SoundManager.stopLongSound(WALKING_ON_GRASS_SOUND, walkKey);
            }
        }

        // Stop running sound
        if (currentState != State.RUN) {
            if (runningSoundTimer > 0) {
                runningSoundTimer -= dt;
            } else {
                SoundManager.stopLongSound(WALKING_ON_GRASS_SOUND, runKey);
            }
        }


        body.setLinearVelocity(velocity.x, velocity.y);
    }

    protected void launchAttack() {
        float offsetX = facingLeft ? -attackOffsetX : attackOffsetX;
        createAttackFixture(offsetX, attackOffsetY, attackWidth, attackHeight);
    }

    protected void launchJumpAttack() {
        float offsetX = facingLeft ? -jumpAttackOffsetX : jumpAttackOffsetX;
        createAttackFixture(offsetX, jumpAttackOffsetY, jumpAttackWidth, jumpAttackHeight);
    }
}
