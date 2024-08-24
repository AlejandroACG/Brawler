package com.svalero.brawler.domains;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.svalero.brawler.managers.AnimationManager;
import com.svalero.brawler.utils.IDGenerator;

import static com.svalero.brawler.managers.AnimationManager.getAnimation;
import static com.svalero.brawler.utils.Constants.*;

public abstract class Character implements Disposable {
    protected TextureAtlas textureAtlas;
    protected Vector2 position;
    protected float stateTime = 0;
    protected int health;
    protected float speed;
    protected Body body;
    protected float width;
    protected float height;
    protected Animation<TextureRegion> currentAnimation;
    protected int id;
    protected State currentState;
    protected final float frameWidth;
    protected final float frameHeight;
    protected final float correctionX;
    protected final float correctionY;
    protected boolean facingLeft;
    protected final float idleDuration;
    protected float jumpUpDuration;
    protected float jumpDownDuration;
    protected float jumpStrength;
    protected Animation<TextureRegion> getCurrentAnimation;
    protected Fixture attackFixture;
    protected boolean hasAttackedThisJump = false;

    public enum State {
        IDLE,
        TURN,
        WALK,
        JUMP_UP,
        JUMP_DOWN,
        LAND,
        CROUCH_DOWN,
        CROUCH,
        CROUCH_UP,
        ATTACK,
        JUMP_ATTACK,
        BLOCK_UP,
        BLOCK,
        BLOCK_DOWN,
        HIT
    }

    public Character(World world, Vector2 position,
                     String characterAtlas, float speed, float width, float height,
                     float frameWidth, float frameHeight, float correctionX, float correctionY, float idleDuration,
                     float jumpUpDuration, float jumpDownDuration, float jumpStrength, String idleAnimationKey) {
        this.position = position;
        this.speed = speed;
        this.id = IDGenerator.generateUniqueId();
        this.width = width;
        this.height = height;
        this.currentState = State.IDLE;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.correctionX = correctionX;
        this.correctionY = correctionY;
        this.idleDuration = idleDuration;
        this.jumpUpDuration = jumpUpDuration;
        this.jumpDownDuration = jumpDownDuration;
        this.jumpStrength = jumpStrength;
        createBody(world, characterAtlas, idleAnimationKey);
    }

    public Character(World world, Vector2 position,
                     String characterAtlas, float speed, float width, float height,
                     float frameWidth, float frameHeight, float correctionX, float correctionY, float idleDuration,
                     String idleAnimationKey) {
        this.position = position;
        this.speed = speed;
        this.id = IDGenerator.generateUniqueId();
        this.width = width;
        this.height = height;
        this.currentState = State.IDLE;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.correctionX = correctionX;
        this.correctionY = correctionY;
        this.idleDuration = idleDuration;
        this.currentAnimation = AnimationManager.getAnimation(idleAnimationKey);
        createBody(world, characterAtlas, idleAnimationKey);
    }

    protected void createBody(World world, String characterAtlas, String idleAnimationKey) {
        textureAtlas = new TextureAtlas(Gdx.files.internal(characterAtlas));
        currentAnimation = getAnimation(idleAnimationKey);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x, position.y + (height) / 2);
        bodyDef.fixedRotation = true;

        body = world.createBody(bodyDef);

        createBodyFixtures();
    }

    protected void createBodyFixtures() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((width) / 2f, (height) / 2f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0.0f;
        fixtureDef.filter.categoryBits = COLLIDER_CATEGORY_BODY;
        fixtureDef.filter.maskBits = COLLIDER_CATEGORY_GROUND | COLLIDER_CATEGORY_BORDER;
        body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();

        PolygonShape sensorShape = new PolygonShape();
        Vector2 sensorPosition = new Vector2(0f, -height / 2f);
        sensorShape.setAsBox(width / 2f, 0.1f, sensorPosition, 0f);
        FixtureDef sensorFixtureDef = new FixtureDef();
        sensorFixtureDef.shape = sensorShape;
        sensorFixtureDef.isSensor = true;
        sensorFixtureDef.filter.categoryBits = COLLIDER_CATEGORY_BODY;
        sensorFixtureDef.filter.maskBits = COLLIDER_CATEGORY_ATTACK;
        body.createFixture(sensorFixtureDef).setUserData(this);
        sensorShape.dispose();
    }

    public void createAttackFixture(float offsetX, float offsetY, float attackWidth, float attackHeight) {
        PolygonShape attackShape = new PolygonShape();

        attackShape.setAsBox(attackWidth / 2, attackHeight / 2, new Vector2(offsetX, offsetY), 0);

        FixtureDef attackFixtureDef = new FixtureDef();
        attackFixtureDef.shape = attackShape;
        attackFixtureDef.isSensor = true;
        attackFixtureDef.filter.categoryBits = COLLIDER_CATEGORY_ATTACK;
        attackFixtureDef.filter.maskBits = COLLIDER_CATEGORY_BODY;

        if (attackFixture != null) {
            body.destroyFixture(attackFixture);
        }

        attackFixture = body.createFixture(attackFixtureDef);
        attackFixture.setUserData(this);
        attackShape.dispose();
    }

    public void setCurrentState(State state) {
        if (this.currentState != state) {
            this.currentState = state;
            stateTime = 0;
            // Para manejar el crouch que carece de animación propia
            // TODO Podría darle una propia.
            if (this.currentState == State.CROUCH) {
                stateTime = 3;
            }
        }
    }

    public void setCurrentStateWithoutReset(State state) {
        if (this.currentState != state) {
            this.currentState = state;
        }
    }

    public void draw(SpriteBatch batch) {
        if (!facingLeft) {
            batch.draw(getCurrentFrame(),
                    (position.x - correctionX),
                    (position.y - correctionY),
                    frameWidth,
                    frameHeight);
        } else {
            batch.draw(getCurrentFrame(),
                    (position.x + correctionX),
                    (position.y - correctionY),
                    -(frameWidth),
                    frameHeight);
        }
    }

    protected TextureRegion getCurrentFrame() {
        return currentAnimation.getKeyFrame(stateTime);
    }

    protected void clearAttackFixture() {
        if (attackFixture != null) {
            body.destroyFixture(attackFixture);
            attackFixture = null;
        }
    }

    public void setHasAttackedThisJump(boolean hasAttackedThisJump) { this.hasAttackedThisJump = hasAttackedThisJump; }

    public boolean isFacingLeft() { return facingLeft; }

    public State getCurrentState() { return currentState; }

    public void setStateTime(int stateTime) { this.stateTime = stateTime; }

    public Vector2 getPosition() { return position; }

    public int getId() { return id; }

    @Override
    public void dispose() { textureAtlas.dispose(); }
}
