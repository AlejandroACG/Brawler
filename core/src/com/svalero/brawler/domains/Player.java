package com.svalero.brawler.domains;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import static com.svalero.brawler.managers.AnimationManager.getAnimation;
import static com.svalero.brawler.utils.Constants.*;
import com.svalero.brawler.managers.SoundManager;

public abstract class Player extends Character {
    private float walkingSoundTimer = KAIN_WALKING_SOUND_TIMER;

    public Player(World world, Vector2 position, String characterAtlas, float speed, float width, float height,
                  float frameWidth, float frameHeight, float correctionX, float correctionY, float idleDuration,
                  float jumpUpDuration, float jumpDownDuration, float jumpStrength, String idleAnimationKey) {
        super(world, position, characterAtlas, speed, width, height,
                frameWidth, frameHeight, correctionX, correctionY, idleDuration, jumpUpDuration, jumpDownDuration,
                jumpStrength, idleAnimationKey);
    }

    public void update(float dt) {
        manageInput(dt);
        Vector2 bodyPosition = body.getPosition();
        position.set(bodyPosition.x, bodyPosition.y);
        stateTime += dt;
    }

    // TODO De meter más personajes, limpiar aquí y en Character todo donde ponga Kain en vez de ser un atributo genérico
    // TODO Cuando se agacha, la mitad superior del body no deberia recibir daños, igual puedo crear un Fixture para cuando está
    // TODO agachado y alternarlos?
    // TODO Faltan ataques (idle, jump, crouch) y el movimiento especial
    // TODO Faltan bloqueos
    // TODO Enemigos
    // TODO Daño
    // TODO Pause
    // TODO Todo el tema de agacharse
    // TODO Ataque especial
    // TODO Darle ángulo al fixture del ataque aéreo para mejorar la hitbox

    public void manageInput(float dt) {
        Vector2 velocity = body.getLinearVelocity();

        // WALK
        if (currentState == State.WALK) {
            setCurrentStateWithoutReset(State.IDLE);
        }

        // IDLE
        if (currentState == State.IDLE) {
            velocity.x = 0;
            currentAnimation = getAnimation(KAIN_IDLE);

            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                if (isFacingLeft()) {
                    setCurrentState(State.TURN);
                    // TODO Sound
                    currentAnimation = getAnimation(KAIN_TURN);
                    facingLeft = false;
                } else {
                    velocity.x = speed;
                    setCurrentStateWithoutReset(State.WALK);
                    currentAnimation = getAnimation(KAIN_WALK);
                    SoundManager.playLongSound(WALKING_ON_GRASS_SOUND, "kain_walk");
                    walkingSoundTimer = KAIN_WALKING_SOUND_TIMER;
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                if (!isFacingLeft()) {
                    setCurrentState(State.TURN);
                    // TODO Sound
                    currentAnimation = getAnimation(KAIN_TURN);
                    facingLeft = true;
                } else {
                    velocity.x = -speed;
                    setCurrentStateWithoutReset(State.WALK);
                    currentAnimation = getAnimation(KAIN_WALK);
                    SoundManager.playLongSound(WALKING_ON_GRASS_SOUND, "kain_walk");
                    walkingSoundTimer = KAIN_WALKING_SOUND_TIMER;
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.K)) {
                currentAnimation = getAnimation(KAIN_BLOCK_UP);
                velocity.x = 0;
                setCurrentState(State.BLOCK_UP);
                // TODO Sound
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                currentAnimation = getAnimation(KAIN_CROUCH_DOWN);
                velocity.x = 0;
                setCurrentState(State.CROUCH_DOWN);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
                velocity.y = jumpStrength;
                SoundManager.playSound(KAIN_GRUNT_SOUND);
                setCurrentState(State.JUMP_UP);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
                velocity.x = 0;
                currentAnimation = getAnimation(KAIN_ATTACK);
                SoundManager.playSound(KAIN_ATTACK_SOUND);
                setCurrentState(State.ATTACK);

                launchAttack();
            }

        // TURN
        } else if (currentState == State.TURN) {
            if (stateTime >= KAIN_TURN_FRAMES * KAIN_TURN_DURATION) {
                setCurrentStateWithoutReset(State.WALK);
            }

        // BLOCK
        } else if (currentState == State.BLOCK_UP) {
            if (stateTime >= KAIN_BLOCK_FRAMES * KAIN_BLOCK_DURATION) {
                setCurrentStateWithoutReset(State.BLOCK);
            }
        } else if (currentState == State.BLOCK) {
            if (!Gdx.input.isKeyPressed(Input.Keys.K)) {
                currentAnimation = getAnimation(KAIN_BLOCK_DOWN);
                setCurrentState(State.BLOCK_DOWN);
            }
        } else if (currentState == State.BLOCK_DOWN) {
            if (stateTime >= KAIN_BLOCK_FRAMES * KAIN_BLOCK_DURATION) {
                setCurrentStateWithoutReset(State.IDLE);
            }

        // CROUCH DOWN
        } else if (currentState == State.CROUCH_DOWN) {
            if (stateTime >= KAIN_CROUCH_FRAMES * KAIN_CROUCH_DURATION) {
                setCurrentState(State.CROUCH);
            }
            if (!Gdx.input.isKeyPressed(Input.Keys.S)) {
                currentAnimation = getAnimation(KAIN_CROUCH_UP);
                setCurrentState(State.CROUCH_UP);
            }

        // CROUCH
        } else if (currentState == State.CROUCH) {
            currentAnimation = getAnimation(KAIN_CROUCH_DOWN);
            if (!Gdx.input.isKeyPressed(Input.Keys.S)) {
                currentAnimation = getAnimation(KAIN_CROUCH_UP);
                setCurrentState(State.CROUCH_UP);
            }

        // CROUCH UP
        } else if (currentState == State.CROUCH_UP) {
            if (stateTime >= KAIN_CROUCH_FRAMES * KAIN_CROUCH_DURATION) {
                setCurrentStateWithoutReset(State.IDLE);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                currentAnimation = getAnimation(KAIN_CROUCH_DOWN);
                setCurrentState(State.CROUCH_DOWN);
            }

        // JUMP UP
        } else if (currentState == State.JUMP_UP || currentState == State.JUMP_DOWN) {
            if (velocity.y > 0) {
                currentAnimation = getAnimation(KAIN_JUMP_UP);
                setCurrentState(State.JUMP_UP);
            } else if (velocity.y <= 0) {
                currentAnimation = getAnimation(KAIN_JUMP);
                setCurrentState(State.JUMP_DOWN);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                velocity.x = body.getLinearVelocity().x + speed * 0.05f;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                velocity.x = body.getLinearVelocity().x - speed * 0.05f;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.J) && !hasAttackedThisJump) {
                currentAnimation = getAnimation(KAIN_JUMP_ATTACK);
                SoundManager.playSound(KAIN_ATTACK_SOUND);
                setCurrentState(State.JUMP_ATTACK);
                setHasAttackedThisJump(true);

                launchJumpAttack();
            }

        // LAND
        } else if (currentState == State.LAND) {
            currentAnimation = getAnimation(KAIN_LAND);
            velocity.x = 0;
            if (stateTime == 0) {
                SoundManager.playSound(LAND_SOUND);
            }
            if (stateTime >= KAIN_LAND_FRAMES * KAIN_LAND_DURATION) {
                setCurrentState(State.IDLE);
                if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.D)) {
                    setCurrentState(State.WALK);
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                currentAnimation = getAnimation(KAIN_CROUCH_DOWN);
                setCurrentState(State.CROUCH_DOWN);
            }
        }

        // ATTACK
        if (currentState == State.ATTACK) {
            if (stateTime >= KAIN_ATTACK_FRAMES * KAIN_ATTACK_DURATION) {
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
            if (stateTime >= KAIN_JUMP_ATTACK_FRAMES * KAIN_JUMP_ATTACK_DURATION) {
                setCurrentState(State.JUMP_DOWN);
                body.setGravityScale(1);
                stateTime = KAIN_JUMP_DOWN_DURATION;
                velocity.y = -1;
                if (attackFixture != null) {
                    clearAttackFixture();
                }
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
                SoundManager.stopLongSound(WALKING_ON_GRASS_SOUND, "kain_walk");
            }
        }

        body.setLinearVelocity(velocity.x, velocity.y);
    }

    protected void launchAttack() {
        if (this instanceof Kain) {
            float offsetX = facingLeft ? -KAIN_ATTACK_OFFSET_X : KAIN_ATTACK_OFFSET_X;
            createAttackFixture(offsetX, KAIN_ATTACK_OFFSET_Y, KAIN_ATTACK_WIDTH, KAIN_ATTACK_HEIGHT);
        }
    }

    protected void launchJumpAttack() {
        if (this instanceof Kain) {
            float offsetX = facingLeft ? -KAIN_JUMP_ATTACK_OFFSET_X : KAIN_JUMP_ATTACK_OFFSET_X;
            createAttackFixture(offsetX, KAIN_JUMP_ATTACK_OFFSET_Y, KAIN_JUMP_ATTACK_WIDTH, KAIN_JUMP_ATTACK_HEIGHT);
        }
    }
}
