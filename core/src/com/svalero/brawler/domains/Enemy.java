package com.svalero.brawler.domains;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import static com.svalero.brawler.managers.AnimationManager.getAnimation;
import static com.svalero.brawler.utils.Constants.*;

public class Enemy extends Character {
    private float walkingSoundTimer = WALKING_SOUND_TIMER;

    public Enemy(World world, Vector2 position, String characterAtlas, float speed, float width, float height,
                  float frameWidth, float frameHeight, float correctionX, float correctionY, float idleDuration,
                  String idleAnimationKey) {
        super(world, position, characterAtlas, speed, width, height,
                frameWidth, frameHeight, correctionX, correctionY, idleDuration, idleAnimationKey);
    }

    public void update(float dt) {
//        manageInput(dt);

        Vector2 velocity = body.getLinearVelocity();
        setCurrentStateWithoutReset(State.WALK);
        currentAnimation = getAnimation(BISHAMON_WALK);
        velocity.x = speed;
        body.setLinearVelocity(velocity.x, velocity.y);

        Vector2 bodyPosition = body.getPosition();
        position.set(bodyPosition.x, bodyPosition.y);
        stateTime += dt;
    }

//    public void manageInput(float dt) {
//        Vector2 velocity = body.getLinearVelocity();
//
//        // WALK
//        if (currentState == Character.State.WALK) {
//            setCurrentStateWithoutReset(Character.State.IDLE);
//        }
//
//        // IDLE
//        if (currentState == Character.State.IDLE) {
//            velocity.x = 0;
//            currentAnimation = getAnimation(KAIN_IDLE);
//
//            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
//                if (isFacingLeft()) {
//                    setCurrentState(Character.State.TURN);
//                    // TODO Sound
//                    currentAnimation = getAnimation(KAIN_TURN);
//                    facingLeft = false;
//                } else {
//                    velocity.x = speed;
//                    setCurrentStateWithoutReset(Character.State.WALK);
//                    currentAnimation = getAnimation(KAIN_WALK);
//                    SoundManager.playLongSound(WALKING_ON_GRASS_SOUND, "kain_walk");
//                    walkingSoundTimer = KAIN_WALKING_SOUND_TIMER;
//                }
//            }
//            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
//                if (!isFacingLeft()) {
//                    setCurrentState(Character.State.TURN);
//                    // TODO Sound
//                    currentAnimation = getAnimation(KAIN_TURN);
//                    facingLeft = true;
//                } else {
//                    velocity.x = -speed;
//                    setCurrentStateWithoutReset(Character.State.WALK);
//                    currentAnimation = getAnimation(KAIN_WALK);
//                    SoundManager.playLongSound(WALKING_ON_GRASS_SOUND, "kain_walk");
//                    walkingSoundTimer = KAIN_WALKING_SOUND_TIMER;
//                }
//            }
//            if (Gdx.input.isKeyPressed(Input.Keys.K)) {
//                currentAnimation = getAnimation(KAIN_BLOCK_UP);
//                velocity.x = 0;
//                setCurrentState(Character.State.BLOCK_UP);
//                // TODO Sound
//            }
//            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
//                currentAnimation = getAnimation(KAIN_CROUCH_DOWN);
//                velocity.x = 0;
//                setCurrentState(Character.State.CROUCH_DOWN);
//            }
//            if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
//                velocity.y = jumpStrength;
//                SoundManager.playSound(KAIN_GRUNT_SOUND);
//                setCurrentState(Character.State.JUMP_UP);
//            }
//            if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
//                velocity.x = 0;
//                currentAnimation = getAnimation(KAIN_ATTACK);
//                SoundManager.playSound(KAIN_ATTACK_SOUND);
//                setCurrentState(Character.State.ATTACK);
//
//                launchAttack();
//            }
//
//            // TURN
//        } else if (currentState == Character.State.TURN) {
//            if (stateTime >= KAIN_TURN_FRAMES * KAIN_TURN_DURATION) {
//                setCurrentStateWithoutReset(Character.State.WALK);
//            }
//
//            // BLOCK
//        } else if (currentState == Character.State.BLOCK_UP) {
//            if (stateTime >= KAIN_BLOCK_FRAMES * KAIN_BLOCK_DURATION) {
//                setCurrentStateWithoutReset(Character.State.BLOCK);
//            }
//        } else if (currentState == Character.State.BLOCK) {
//            if (!Gdx.input.isKeyPressed(Input.Keys.K)) {
//                currentAnimation = getAnimation(KAIN_BLOCK_DOWN);
//                setCurrentState(Character.State.BLOCK_DOWN);
//            }
//        } else if (currentState == Character.State.BLOCK_DOWN) {
//            if (stateTime >= KAIN_BLOCK_FRAMES * KAIN_BLOCK_DURATION) {
//                setCurrentStateWithoutReset(Character.State.IDLE);
//            }
//
//            // CROUCH DOWN
//        } else if (currentState == Character.State.CROUCH_DOWN) {
//            if (stateTime >= KAIN_CROUCH_FRAMES * KAIN_CROUCH_DURATION) {
//                setCurrentState(Character.State.CROUCH);
//            }
//            if (!Gdx.input.isKeyPressed(Input.Keys.S)) {
//                currentAnimation = getAnimation(KAIN_CROUCH_UP);
//                setCurrentState(Character.State.CROUCH_UP);
//            }
//
//            // CROUCH
//        } else if (currentState == Character.State.CROUCH) {
//            currentAnimation = getAnimation(KAIN_CROUCH_DOWN);
//            if (!Gdx.input.isKeyPressed(Input.Keys.S)) {
//                currentAnimation = getAnimation(KAIN_CROUCH_UP);
//                setCurrentState(Character.State.CROUCH_UP);
//            }
//
//            // CROUCH UP
//        } else if (currentState == Character.State.CROUCH_UP) {
//            if (stateTime >= KAIN_CROUCH_FRAMES * KAIN_CROUCH_DURATION) {
//                setCurrentStateWithoutReset(Character.State.IDLE);
//            }
//            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
//                currentAnimation = getAnimation(KAIN_CROUCH_DOWN);
//                setCurrentState(Character.State.CROUCH_DOWN);
//            }
//
//            // JUMP UP
//        } else if (currentState == Character.State.JUMP_UP || currentState == Character.State.JUMP_DOWN) {
//            if (velocity.y > 0) {
//                currentAnimation = getAnimation(KAIN_JUMP_UP);
//                setCurrentState(Character.State.JUMP_UP);
//            } else if (velocity.y <= 0) {
//                currentAnimation = getAnimation(KAIN_JUMP);
//                setCurrentState(Character.State.JUMP_DOWN);
//            }
//            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
//                velocity.x = body.getLinearVelocity().x + speed * 0.05f;
//            }
//            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
//                velocity.x = body.getLinearVelocity().x - speed * 0.05f;
//            }
//            if (Gdx.input.isKeyJustPressed(Input.Keys.J) && !hasAttackedThisJump) {
//                currentAnimation = getAnimation(KAIN_JUMP_ATTACK);
//                SoundManager.playSound(KAIN_ATTACK_SOUND);
//                setCurrentState(Character.State.JUMP_ATTACK);
//                setHasAttackedThisJump(true);
//
//                launchJumpAttack();
//            }
//
//            // LAND
//        } else if (currentState == Character.State.LAND) {
//            currentAnimation = getAnimation(KAIN_LAND);
//            velocity.x = 0;
//            if (stateTime == 0) {
//                SoundManager.playSound(LAND_SOUND);
//            }
//            if (stateTime >= KAIN_LAND_FRAMES * KAIN_LAND_DURATION) {
//                setCurrentState(Character.State.IDLE);
//                if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.D)) {
//                    setCurrentState(Character.State.WALK);
//                }
//            }
//            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
//                currentAnimation = getAnimation(KAIN_CROUCH_DOWN);
//                setCurrentState(Character.State.CROUCH_DOWN);
//            }
//        }
//
//        // ATTACK
//        if (currentState == Character.State.ATTACK) {
//            if (stateTime >= KAIN_ATTACK_FRAMES * KAIN_ATTACK_DURATION) {
//                setCurrentState(Character.State.IDLE);
//                if (attackFixture != null) {
//                    clearAttackFixture();
//                }
//            }
//        }
//
//        // JUMP ATTACK
//        if (currentState == Character.State.JUMP_ATTACK) {
//            velocity.y = 0;
//            velocity.x = 0;
//            body.setGravityScale(0);
//            if (stateTime >= KAIN_JUMP_ATTACK_FRAMES * KAIN_JUMP_ATTACK_DURATION) {
//                setCurrentState(Character.State.JUMP_DOWN);
//                body.setGravityScale(1);
//                stateTime = KAIN_JUMP_DOWN_DURATION;
//                velocity.y = -1;
//                if (attackFixture != null) {
//                    clearAttackFixture();
//                }
//            }
//        }
//
//        // Erase unused attack fixtures
//        if (currentState != Character.State.ATTACK && currentState != Character.State.JUMP_ATTACK) {
//            clearAttackFixture();
//        }
//
//        // Stop walking sound
//        if (currentState != Character.State.WALK) {
//            if (walkingSoundTimer > 0) {
//                walkingSoundTimer -= dt;
//            } else {
//                SoundManager.stopLongSound(WALKING_ON_GRASS_SOUND, "kain_walk");
//            }
//        }
//
//        body.setLinearVelocity(velocity.x, velocity.y);
//    }
//
//    protected void launchAttack() {
//        if (this instanceof Kain) {
//            float offsetX = facingLeft ? -KAIN_ATTACK_OFFSET_X : KAIN_ATTACK_OFFSET_X;
//            createAttackFixture(offsetX, KAIN_ATTACK_OFFSET_Y, KAIN_ATTACK_WIDTH, KAIN_ATTACK_HEIGHT);
//        }
//    }
//
//    protected void launchJumpAttack() {
//        if (this instanceof Kain) {
//            float offsetX = facingLeft ? -KAIN_JUMP_ATTACK_OFFSET_X : KAIN_JUMP_ATTACK_OFFSET_X;
//            createAttackFixture(offsetX, KAIN_JUMP_ATTACK_OFFSET_Y, KAIN_JUMP_ATTACK_WIDTH, KAIN_JUMP_ATTACK_HEIGHT);
//        }
//    }
}
