package com.svalero.brawler.domains;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import static com.svalero.brawler.utils.Constants.*;
import com.svalero.brawler.managers.SoundManager;

public abstract class Player extends Character {
    private float walkingSoundTimer = KAIN_WALKING_SOUND_TIMER;

    public Player(World world, Vector2 position, float scale, String characterAtlas, float speed, float width, float height,
                  float spriteWidth, float spriteHeight, float correctionX, float correctionY, float idleDuration,
                  float walkDuration, float jumpUpDuration, float jumpDownDuration, float jumpStrength, String idleAnimationKey) {
        super(world, position, scale, characterAtlas, speed, width, height, COLLIDER_CATEGORY_PLAYER,
                spriteWidth, spriteHeight, correctionX, correctionY, idleDuration, walkDuration, jumpUpDuration, jumpDownDuration,
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

    public void manageInput(float dt) {
        Vector2 velocity = body.getLinearVelocity();

        // WALK
        if (currentState == State.WALK) {
            setCurrentStateWithoutReset(State.IDLE);
        }

        // IDLE
        if (currentState == State.IDLE) {
            velocity.x = 0;
            currentAnimation = getIdleAnimation(KAIN_IDLE_ANIMATION);

            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                velocity.x = speed;
                setCurrentStateWithoutReset(State.WALK);
                currentAnimation = getWalkAnimation(KAIN_WALK_ANIMATION);
                facingLeft = false;
                SoundManager.playLongSound(WALKING_ON_GRASS_SOUND, "kain_walk");
                walkingSoundTimer = KAIN_WALKING_SOUND_TIMER;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                velocity.x = -speed;
                setCurrentStateWithoutReset(State.WALK);
                currentAnimation = getWalkAnimation(KAIN_WALK_ANIMATION);
                facingLeft = true;
                SoundManager.playLongSound(WALKING_ON_GRASS_SOUND, "kain_walk");
                walkingSoundTimer = KAIN_WALKING_SOUND_TIMER;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                currentAnimation = getCrouchDownAnimation(KAIN_CROUCH_DOWN_ANIMATION);
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
                currentAnimation = getAttackAnimation(KAIN_ATTACK_ANIMATION);
                SoundManager.playSound(KAIN_ATTACK_SOUND);
                setCurrentState(State.ATTACK);
            }

        // CROUCH DOWN
        } else if (currentState == State.CROUCH_DOWN) {
            if (stateTime >= KAIN_CROUCH_FRAMES * KAIN_CROUCH_DURATION) {
                setCurrentState(State.CROUCH);
            }
            if (!Gdx.input.isKeyPressed(Input.Keys.S)) {
                currentAnimation = getCrouchUpAnimation(KAIN_CROUCH_UP_ANIMATION);
                setCurrentState(State.CROUCH_UP);
            }

        // CROUCH
        } else if (currentState == State.CROUCH) {
            currentAnimation = getCrouchAnimation(KAIN_CROUCH_DOWN_ANIMATION);
            if (!Gdx.input.isKeyPressed(Input.Keys.S)) {
                currentAnimation = getCrouchUpAnimation(KAIN_CROUCH_UP_ANIMATION);
                setCurrentState(State.CROUCH_UP);
            }

        // CROUCH UP
        } else if (currentState == State.CROUCH_UP) {
            if (stateTime >= KAIN_CROUCH_FRAMES * KAIN_CROUCH_DURATION) {
                setCurrentStateWithoutReset(State.IDLE);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                currentAnimation = getCrouchDownAnimation(KAIN_CROUCH_DOWN_ANIMATION);
                setCurrentState(State.CROUCH_DOWN);
            }

        // JUMP UP
        } else if (currentState == State.JUMP_UP || currentState == State.JUMP_DOWN) {
            if (velocity.y > 0) {
                currentAnimation = getJumpUpAnimation(KAIN_JUMP_UP_ANIMATION);
                setCurrentState(State.JUMP_UP);
            } else if (velocity.y < 0) {
                currentAnimation = getJumpDownAnimation(KAIN_JUMP_DOWN_ANIMATION);
                setCurrentState(State.JUMP_DOWN);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                velocity.x = body.getLinearVelocity().x + speed * 0.05f;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                velocity.x = body.getLinearVelocity().x - speed * 0.05f;
            }

        // LAND
        } else if (currentState == State.LAND) {
            currentAnimation = getLandAnimation(KAIN_LAND_ANIMATION);
            velocity.x = 0;
            SoundManager.playLongSound(LAND_SOUND, "kain_land");
            if (stateTime >= KAIN_LAND_FRAMES * KAIN_LAND_DURATION) {
                SoundManager.stopLongSound(LAND_SOUND, "kain_land");
                setCurrentState(State.IDLE);
                if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.D)) {
                    setCurrentState(State.WALK);
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                currentAnimation = getCrouchDownAnimation(KAIN_CROUCH_DOWN_ANIMATION);
                setCurrentState(State.CROUCH_DOWN);
            }
        }

        // ATTACK
        if (currentState == State.ATTACK) {
            if (stateTime >= KAIN_ATTACK_FRAMES * KAIN_ATTACK_DURATION) {
                setCurrentState(State.IDLE);
            }
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
}
