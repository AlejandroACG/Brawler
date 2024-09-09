package com.svalero.brawler.domains.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import static com.svalero.brawler.managers.AnimationManager.getAnimation;
import static com.svalero.brawler.utils.Constants.*;

import com.svalero.brawler.managers.AnimationManager;
import com.svalero.brawler.managers.LevelManager;
import com.svalero.brawler.managers.SoundManager;

public abstract class Player extends Character {
    protected long lastKeyPressTimeA = 0;
    protected long lastKeyPressTimeD = 0;
    protected boolean isRunning = false;
    private boolean turnBlocked = false;

    public Player(LevelManager levelManager, World world, Vector2 position, String characterAtlas, int health,
                  int attackStrength, float speed, float width, float height, float frameWidth, float frameHeight,
                  float correctionX, float correctionY, float idleDuration, float jumpUpDuration,
                  float jumpDownDuration, float jumpStrength, String idleKey, String turnKey, String walkKey,
                  String runKey, String blockUpKey, String blockDownKey, String crouchDownKey, String crouchUpKey,
                  String jumpUpKey, String jumpDownKey, String landKey, String attackKey, String jumpAttackKey,
                  String hitKey, String blockMoveSoundPath, String jumpSoundPath, String attackSoundPath,
                  int turnFrames, float turnDuration, int blockFrames, float blockDuration, int crouchFrames,
                  float crouchDuration, int landFrames, float landDuration, int hitFrames, float hitDuration,
                  int attackFrames, float attackDuration, int jumpAttackFrames, float jumpAttackDuration,
                  float attackOffsetX, float attackOffsetY, float attackWidth, float attackHeight,
                  float jumpAttackOffsetX, float jumpAttackOffsetY, float jumpAttackWidth, float jumpAttackHeight,
                  String hitSoundPath, String deadKey, String deadSoundPath, int deadFrames, float deadDuration,
                  String victoryKey, String victorySoundPath, float crouchWidth, float crouchHeight, String introKey) {
        super(levelManager, world, position, characterAtlas, health, attackStrength, speed, width, height, frameWidth, frameHeight,
                correctionX, correctionY, idleDuration, jumpUpDuration, jumpDownDuration, jumpStrength, idleKey,
                turnKey, walkKey, runKey, blockUpKey, blockDownKey, crouchDownKey, crouchUpKey, jumpUpKey, jumpDownKey,
                landKey, attackKey, jumpAttackKey, hitKey, blockMoveSoundPath, jumpSoundPath, attackSoundPath, turnFrames,
                turnDuration, blockFrames, blockDuration, crouchFrames, crouchDuration, landFrames, landDuration,
                hitFrames, hitDuration, attackFrames, attackDuration, jumpAttackFrames, jumpAttackDuration,
                attackOffsetX, attackOffsetY, attackWidth, attackHeight, jumpAttackOffsetX, jumpAttackOffsetY,
                jumpAttackWidth, jumpAttackHeight, hitSoundPath, deadKey, deadSoundPath, deadFrames, deadDuration,
                victoryKey, victorySoundPath, crouchWidth, crouchHeight, introKey);
    }

    public void update(float dt) {
        manageInput(dt);
        effectManager.update(dt);
        Vector2 bodyPosition = body.getPosition();
        position.set(bodyPosition.x, bodyPosition.y);
        stateTime += dt;
    }

    // TODO Cuando se agacha, la mitad superior del body no deberia recibir daños, igual puedo crear un Fixture para cuando está
    // TODO agachado y alternarlos?
    // TODO Faltan ataques crouch y el movimiento especial
    // TODO Faltan bloqueos
    // TODO Pause
    // TODO Todo el tema de agacharse
    // TODO Darle ángulo al fixture del ataque aéreo para mejorar la hitbox
    // TODO Que algunos hitboxes o sigan las armas o aparezcan en un momento más apropiado.
    // TODO Se puede reducir código en la lógica de A y D
    // TODO Seguir encapsulando de ser necesario
    // TODO Escalar de píxeles a metros para mayor control
    // TODO Podría implementar daños diferentes para cada tipo de ataque pero sus fixtures tendrías que llevar esa información

    public void manageInput(float dt) {
        Vector2 velocity = body.getLinearVelocity();
        long currentTime = System.currentTimeMillis();

        // INTRO
        if (currentState == State.INTRO) {
            velocity.x = 0;
            currentAnimation = getAnimation(introKey);
            return;
        }

        // Stop WALK / RUN
        if ((currentState == State.WALK || currentState == State.RUN) &&
                (!Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D))) {
            velocity = goIdle(velocity);
            isRunning = false;
        }

        // IDLE / WALK / RUN
        if (currentState == State.IDLE || currentState == State.WALK || currentState == State.RUN) {
            if (currentState == State.IDLE) {
                velocity = goIdle(velocity);
            }

            turnBlocked = Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.D);

            // Walking / running left
            if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                if (currentTime - lastKeyPressTimeA < DOUBLE_CLICK_THRESHOLD || currentState == State.RUN) {
                    velocity = goRun(velocity);
                }
                lastKeyPressTimeA = currentTime;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                if (!isFacingLeft()) {
                    if (!turnBlocked) {
                        velocity = goTurn(velocity);
                    }
                    turnBlocked = false;
                } else if (currentState == State.RUN) {
                    velocity = goRun(velocity);
                } else {
                    velocity = goWalk(velocity);
                }
            }
            // Walking / running right
            if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                if (currentTime - lastKeyPressTimeD < DOUBLE_CLICK_THRESHOLD || currentState == State.RUN) {
                    velocity = goRun(velocity);
                }
                lastKeyPressTimeD = currentTime;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                if (isFacingLeft()) {
                    if (!turnBlocked) {
                        velocity = goTurn(velocity);
                    }
                    turnBlocked = false;
                } else if (currentState == State.RUN) {
                    velocity = goRun(velocity);
                } else {
                    velocity = goWalk(velocity);
                }
            }

            // Block
            if (Gdx.input.isKeyPressed(Input.Keys.K)) {
                currentAnimation = getAnimation(blockUpKey);
                velocity.x = 0;
                setCurrentState(State.BLOCK_UP);
                SoundManager.playSound(blockMoveSoundPath);
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
                SoundManager.playSound(jumpSoundPath);
                setCurrentState(State.JUMP_UP);
            }

            // Attack
            if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
                velocity = goAttack(velocity);
            }

            // TURN
        } else if (currentState == State.TURN) {
            if ((Gdx.input.isKeyJustPressed(Input.Keys.A) && currentTime - lastKeyPressTimeA < DOUBLE_CLICK_THRESHOLD
                    && !(Gdx.input.isKeyJustPressed(Input.Keys.D)))
                    || (Gdx.input.isKeyJustPressed(Input.Keys.D) && (currentTime - lastKeyPressTimeD < DOUBLE_CLICK_THRESHOLD
                    && !Gdx.input.isKeyJustPressed(Input.Keys.A)))) {
                isRunning = true;
            }
            if (stateTime >= turnFrames * turnDuration) {
                if (Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.D)) {
                    velocity = goIdle(velocity);
                } else if (((facingLeft && Gdx.input.isKeyPressed(Input.Keys.A)) ||
                        (!facingLeft && Gdx.input.isKeyPressed(Input.Keys.D)))) {
                    if (isRunning) {
                        velocity = goRun(velocity);
                    } else {
                        velocity = goWalk(velocity);
                    }
                } else {
                    velocity = goIdle(velocity);
                }
            }

            // BLOCK
        } else if (currentState == State.BLOCK_UP) {
            if (stateTime >= blockFrames * blockDuration) {
                setCurrentStateWithoutReset(State.BLOCK);
            }
        } else if (currentState == State.BLOCK) {
            if (!Gdx.input.isKeyPressed(Input.Keys.K)) {
                SoundManager.playSound(blockMoveSoundPath);
                currentAnimation = getAnimation(blockDownKey);
                setCurrentState(State.BLOCK_DOWN);
            }
        } else if (currentState == State.BLOCK_DOWN) {
            if (stateTime >= blockFrames * blockDuration) {
                velocity = goIdle(velocity);
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
            standFixture.setSensor(true);
            crouchFixture.setSensor(false);
            if (!Gdx.input.isKeyPressed(Input.Keys.S)) {
                currentAnimation = getAnimation(crouchUpKey);
                setCurrentState(State.CROUCH_UP);
            }

            // CROUCH UP
        } else if (currentState == State.CROUCH_UP) {
            if (stateTime >= crouchFrames * crouchDuration) {
                velocity = goIdle(velocity);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                currentAnimation = getAnimation(crouchDownKey);
                setCurrentState(State.CROUCH_DOWN);
            }

            // JUMP
        } else if (currentState == State.JUMP_UP || currentState == State.JUMP_DOWN) {
            if (currentState == State.JUMP_DOWN && isOnGround) {
                goLand();
            } else {
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
                    SoundManager.playSound(attackSoundPath);
                    setCurrentState(State.JUMP_ATTACK);
                    setHasAttackedThisJump(true);

                    launchJumpAttack();
                }
            }

            // LAND
        } else if (currentState == State.LAND) {
            currentAnimation = getAnimation(landKey);
            velocity.x = 0;
            if (stateTime == 0) {
                SoundManager.playSound(LAND_SOUND);
            }
            if (stateTime >= landFrames * landDuration) {
                velocity = goIdle(velocity);
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
                velocity = goIdle(velocity);
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
                body.setGravityScale(1);
                if (isOnGround) {
                    goLand();
                } else {
                    setCurrentState(State.JUMP_DOWN);
                    stateTime = jumpDownDuration;
                    velocity.y = -1;
                }
                if (attackFixture != null) {
                    clearAttackFixture();
                }
            }
        }

        // HIT
        if (currentState == State.HIT) {
            body.setGravityScale(0);
            velocity.x = 0;
            velocity.y = 0;
            if (stateTime >= hitFrames * hitDuration) {
                body.setGravityScale(1);
                if (isOnGround) {
                    currentAnimation = getAnimation(idleKey);
                    velocity = goIdle(velocity);
                } else {
                    velocity.x = -1;
                    currentAnimation = getAnimation(jumpDownKey);
                    setCurrentStateWithoutReset(State.JUMP_DOWN);
                }
            }
        }

        // BLOCK SUCCESS
        if (currentState == State.BLOCK_SUCCESS) {
            body.setGravityScale(0);
            velocity.x = 0;
            velocity.y = 0;
            if (stateTime >= BLOCK_SPARK_FRAMES * BLOCK_SPARK_DURATION) {
                body.setGravityScale(1);
                velocity.x = -1;
                setCurrentStateWithoutReset(State.BLOCK);
            }
        }

        // DEAD
        if (currentState == State.DEAD) {
            if (stateTime == 0) {
                // TODO El espaldarazo de la muerte podria ser una animacion diferente que se active cuando cae al suelo,
                //  y para eso se podría usar la posición en Y.
                body.setGravityScale(1);
                if (isOnGround) {
                    velocity.y = 200f;
                } else {
                    velocity.y = 0f;
                }
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

        // Erase unused attack fixtures
        if (currentState != State.ATTACK && currentState != State.JUMP_ATTACK) {
            clearAttackFixture();
        }

        if(currentState != State.CROUCH) {
            standFixture.setSensor(false);
            crouchFixture.setSensor(true);
        }

        // TODO Estaría bien también un timer antes de empezar, para que no suene en el frame antes de que se active State.RUN
        // Stop walking sound
        if (currentState != State.WALK) {
            if (walkingSoundTimer > 0) {
                walkingSoundTimer -= dt;
            } else {
                SoundManager.stopLongSound(walkKey);
            }
        }

        // Stop running sound
        if (currentState != State.RUN) { SoundManager.stopLongSound(runKey); }

        // VICTORY
        if (currentState == State.VICTORY) { velocity = doVictory(velocity); }

        body.setLinearVelocity(velocity.x, velocity.y);
    }

    protected void launchJumpAttack() {
        float offsetX = facingLeft ? -jumpAttackOffsetX : jumpAttackOffsetX;
        createAttackFixture(offsetX, jumpAttackOffsetY, jumpAttackWidth, jumpAttackHeight);
    }

    protected void goLand() {
        setStateTime(0);
        setCurrentState(State.LAND);
        setHasAttackedThisJump(false);
        if (stateTime == 0) {
            SoundManager.playSound(LAND_SOUND);
        }
    }

    protected Vector2 goRun(Vector2 velocity) {
        velocity.x = facingLeft ? -speed * 2 : speed * 2;
        setCurrentState(State.RUN);
        isRunning = true;
        currentAnimation = getAnimation(runKey);
        SoundManager.playLongSound(levelManager.getCurrentLevel() == 1 ? RUNNING_ON_GRASS_SOUND : RUNNING_ON_STONE_SOUND, runKey);
        return velocity;
    }
}
