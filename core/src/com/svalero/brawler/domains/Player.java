package com.svalero.brawler.domains;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import static com.svalero.brawler.utils.Constants.*;
import com.svalero.brawler.utils.FixtureData.*;

public abstract class Player extends Character {
    public Player(World world, Vector2 position, float scale, String characterAtlas, float speed, float width, float height,
                  float spriteWidth, float spriteHeight, float correctionX, float correctionY, float idleDuration,
                  float walkDuration, float jumpUpDuration, float jumpDownDuration, float jumpStrength) {
        super(world, position, scale, characterAtlas, speed, width, height, EntityType.PLAYER, COLLIDER_CATEGORY_PLAYER,
                spriteWidth, spriteHeight, correctionX, correctionY, idleDuration, walkDuration, jumpUpDuration, jumpDownDuration,
                jumpStrength);
    }

    public void update(float dt) {
        manageInput(dt);
        Vector2 bodyPosition = body.getPosition();
        position.set(bodyPosition.x, bodyPosition.y);
        stateTime += dt;
    }

    // TODO ¿Puede que la fuerza de salto no funcione por algún motivo?
    // TODO Que cuando salta tarde un segundo de instante, sincronizado con la animación
    // TODO Limpiar aquí y en Character todo donde ponga Kain en vez de ser un atributo genérico
    // TODO Limpiar también los idle placeholders
    // TODO Cuando se agacha, la mitad superior del body no deberia recibir daños, igual puedo crear un Fixture para cuando está
    // TODO agachado y alternarlos?
    // TODO Faltan ataques (idle, jump, crouch) y el movimiento especial
    // TODO Faltan bloqueos
    // TODO Cambiar en el atlas landing por land
    // TODO Enemigos
    // TODO Daño
    // TODO Pause
    // TODO Sonidos con mute

    public void manageInput(float dt) {
        Vector2 velocity = body.getLinearVelocity();
        if (currentState == State.WALK) {
            currentState = State.IDLE;
        }

        if (currentState == State.IDLE) {
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                velocity.x = speed;
                currentState = State.WALK;
                currentAnimation = getWalkAnimation();
                facingLeft = false;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                velocity.x = -speed;
                currentState = State.WALK;
                currentAnimation = getWalkAnimation();
                facingLeft = true;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                currentAnimation = getCrouchDownAnimation();
                velocity.x = 0;
                currentState = State.CROUCH_DOWN;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
                velocity.y = jumpStrength;
                currentState = State.JUMP_UP;
            }
        } else if (currentState == State.CROUCH_DOWN) {
            if (stateTime >= KAIN_CROUCH_FRAMES * KAIN_CROUCH_DURATION) {
                currentState = State.CROUCH;
            }
            if (!Gdx.input.isKeyPressed(Input.Keys.S)) {
                currentAnimation = getCrouchUpAnimation();
                currentState = State.CROUCH_UP;
            }
        } else if (currentState == State.CROUCH) {
            currentAnimation = getCrouchAnimation();
            if (!Gdx.input.isKeyPressed(Input.Keys.S)) {
                currentAnimation = getCrouchUpAnimation();
                currentState = State.CROUCH_UP;
            }
        } else if (currentState == State.CROUCH_UP) {
            if (stateTime >= KAIN_CROUCH_FRAMES * KAIN_CROUCH_DURATION) {
                currentState = State.IDLE;
            }
        } else if (currentState == State.JUMP_UP || currentState == State.JUMP_DOWN) {
            if (velocity.y > 0) {
                currentAnimation = getJumpUpAnimation();
                currentState = State.JUMP_UP;
            } else if (velocity.y < 0) {
                currentAnimation = getJumpDownAnimation();
                currentState = State.JUMP_DOWN;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                velocity.x = body.getLinearVelocity().x + speed * 0.05f;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                velocity.x = body.getLinearVelocity().x - speed * 0.05f;
            }
        } else if (currentState == State.LAND) {
            currentAnimation = getLandAnimation();
            velocity.x = 0;
            if (stateTime >= KAIN_LAND_FRAMES * KAIN_LAND_DURATION) {
                currentState = State.IDLE;
                if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.D)) {
                    currentState = State.WALK;
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                currentAnimation = getCrouchDownAnimation();
                currentState = State.CROUCH_DOWN;
            }
        }

        if (currentState == State.IDLE) {
            velocity.x = 0;
            currentAnimation = getIdleAnimation();
        }

        body.setLinearVelocity(velocity.x, velocity.y);

//            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
//                position.y -= speed * dt;
//            }
    }
}
