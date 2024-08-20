package com.svalero.brawler.domains;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.svalero.brawler.utils.FixtureData;
import com.svalero.brawler.utils.IDGenerator;
import static com.svalero.brawler.utils.Constants.*;
import com.svalero.brawler.utils.FixtureData.*;

public abstract class Character implements Disposable {
    protected TextureAtlas textureAtlas;
    protected Vector2 position;
    protected float stateTime = 0;
    protected int life;
    protected float speed;
    protected Body body;
    protected float width;
    protected float height;
    protected float scale;
    protected Animation<TextureRegion> currentAnimation;
    protected int id;
    protected State currentState;
    protected boolean animationLoop = true;
    protected final float spriteWidth;
    protected final float spriteHeight;
    protected final float correctionX;
    protected final float correctionY;
    protected boolean facingLeft;
    protected final float idleDuration;
    protected final float walkDuration;
    protected final float jumpUpDuration;
    protected final float jumpDownDuration;
    protected final float jumpStrength;

    public enum State {
        IDLE,
        WALK,
        ATTACK,
        JUMP_UP,
        JUMP_DOWN,
        LAND,
        CROUCH_DOWN,
        CROUCH,
        CROUCH_UP,
        BLOCK,
        HURT
    }

    public Character(World world, Vector2 position, float scale,
                     String characterAtlas, float speed, float width, float height, EntityType entityType, short category,
                     float spriteWidth, float spriteHeight, float correctionX, float correctionY, float idleDuration,
                     float walkDuration, float jumpUpDuration, float jumpDownDuration, float jumpStrength) {
        this.position = position;
        this.scale = scale;
        this.speed = speed;
        this.id = IDGenerator.generateUniqueId();
        this.width = width;
        this.height = height;
        this.currentState = State.IDLE;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.correctionX = correctionX;
        this.correctionY = correctionY;
        this.idleDuration = idleDuration;
        this.walkDuration = walkDuration;
        this.jumpUpDuration = jumpUpDuration;
        this.jumpDownDuration = jumpDownDuration;
        this.jumpStrength = jumpStrength;
        createBody(world, characterAtlas, entityType, category);
    }

    protected void createBody(World world, String characterAtlas, EntityType entityType, short category) {
        textureAtlas = new TextureAtlas(Gdx.files.internal(characterAtlas));
        currentAnimation = getIdleAnimation();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x, position.y + height * scale / 2);

        body = world.createBody(bodyDef);

        createBodyFixtures(entityType, scale, category);
    }

    protected void createBodyFixtures(FixtureData.EntityType entityType, float scale, short category) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2f * scale, height / 2f * scale);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0.0f;
        fixtureDef.filter.categoryBits = category;
        if (category == COLLIDER_CATEGORY_PLAYER) {
            fixtureDef.filter.maskBits = COLLIDER_CATEGORY_GROUND | COLLIDER_CATEGORY_ENEMY | COLLIDER_CATEGORY_BORDER;
        } else {
            fixtureDef.filter.maskBits = COLLIDER_CATEGORY_GROUND | COLLIDER_CATEGORY_PLAYER;
        }
        FixtureData bodyData = new FixtureData(entityType, id, FixtureData.SensorType.BODY);
        body.createFixture(fixtureDef).setUserData(bodyData);
        shape.dispose();

        PolygonShape sensorShape = new PolygonShape();
        Vector2 sensorPosition = new Vector2(0f, -height / 2f * scale);
        sensorShape.setAsBox(width / 2f * scale, 0.1f, sensorPosition, 0f);
        FixtureDef fixtureSensorDef = new FixtureDef();
        fixtureSensorDef.shape = sensorShape;
        fixtureSensorDef.isSensor = true;
        FixtureData footData = new FixtureData(entityType, id, FixtureData.SensorType.FOOT);
        body.createFixture(fixtureSensorDef).setUserData(footData);
        sensorShape.dispose();
    }

    public Animation<TextureRegion> getIdleAnimation() {
        Array<AtlasRegion> idleFrames = textureAtlas.findRegions("idle");
        animationLoop = true;
        if (currentState != State.IDLE) {
            stateTime = 0;
        }
        return new Animation<>(idleDuration, idleFrames, Animation.PlayMode.LOOP);
    }

    public Animation<TextureRegion> getWalkAnimation() {
        Array<AtlasRegion> walkFrames = textureAtlas.findRegions("walk");
        animationLoop = true;
        if (currentState != State.WALK) {
            stateTime = 0;
        }
        return new Animation<>(walkDuration, walkFrames, Animation.PlayMode.LOOP);
    }

    public Animation<TextureRegion> getCrouchDownAnimation() {
        Array<AtlasRegion> crouchFrames = textureAtlas.findRegions("crouch");
        animationLoop = false;
        if (currentState != State.CROUCH_DOWN) {
            stateTime = 0;
        }
        return new Animation<>(KAIN_CROUCH_DURATION, crouchFrames, Animation.PlayMode.NORMAL);
    }

    public Animation<TextureRegion> getCrouchAnimation() {
        Array<AtlasRegion> crouchFrames = textureAtlas.findRegions("crouch");
        animationLoop = false;
        return new Animation<>(KAIN_CROUCH_DURATION, crouchFrames, Animation.PlayMode.NORMAL);
    }

    public Animation<TextureRegion> getCrouchUpAnimation() {
        Array<AtlasRegion> crouchFrames = textureAtlas.findRegions("crouch");
        crouchFrames.reverse();
        animationLoop = false;
        if (currentState != State.CROUCH_UP) {
            stateTime = 0;
        }
        return new Animation<>(KAIN_CROUCH_DURATION, crouchFrames, Animation.PlayMode.NORMAL);
    }

    public Animation<TextureRegion> getJumpUpAnimation() {
        Array<AtlasRegion> jumpUpFrames = textureAtlas.findRegions("jump_up");
        animationLoop = false;
        if (currentState != State.JUMP_UP) {
            stateTime = 0;
        }
        return new Animation<>(jumpUpDuration, jumpUpFrames, Animation.PlayMode.NORMAL);
    }

    public Animation<TextureRegion> getJumpDownAnimation() {
        Array<AtlasRegion> jumpDownFrames = textureAtlas.findRegions("jump_down");
        animationLoop = false;
        if (currentState != State.JUMP_DOWN) {
            stateTime = 0;
        }
        return new Animation<>(jumpDownDuration, jumpDownFrames, Animation.PlayMode.NORMAL);
    }

    public Animation<TextureRegion> getLandAnimation() {
        Array<AtlasRegion> landFrames = textureAtlas.findRegions("landing");
        animationLoop = false;
        return new Animation<>(KAIN_LAND_DURATION, landFrames, Animation.PlayMode.NORMAL);
    }

    public void draw(SpriteBatch batch) {
        if (!facingLeft) {
            batch.draw(getCurrentFrame(),
                    (position.x - correctionX),
                    (position.y - correctionY),
                    spriteWidth * scale,
                    spriteHeight * scale);
        } else {
            batch.draw(getCurrentFrame(),
                    (position.x + correctionX),
                    (position.y - correctionY),
                    -(spriteWidth * scale),
                    spriteHeight * scale);
        }
    }

    protected TextureRegion getCurrentFrame() {
        return currentAnimation.getKeyFrame(stateTime, animationLoop);
    }

    public void setCurrentState(State state) {
        currentState = state;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setStateTime(int stateTime) {
        this.stateTime = stateTime;
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getId() {
        return id;
    }

    @Override
    public void dispose() {
        textureAtlas.dispose();
    }
}
