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
import static com.svalero.brawler.utils.Constants.*;

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
    protected Animation<TextureRegion> getCurrentAnimation;

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
        HIT
    }

    public Character(World world, Vector2 position, float scale,
                     String characterAtlas, float speed, float width, float height, short category,
                     float spriteWidth, float spriteHeight, float correctionX, float correctionY, float idleDuration,
                     float walkDuration, float jumpUpDuration, float jumpDownDuration, float jumpStrength, String idleAnimationKey) {
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
        this.currentAnimation = AnimationManager.getAnimation("kain_idle");
        createBody(world, characterAtlas, category, idleAnimationKey);
    }

    protected void createBody(World world, String characterAtlas, short category, String idleAnimationKey) {
        textureAtlas = new TextureAtlas(Gdx.files.internal(characterAtlas));
        currentAnimation = getIdleAnimation(idleAnimationKey);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x, position.y + height * scale / 2);

        body = world.createBody(bodyDef);

        createBodyFixtures(scale, category);
    }

    protected void createBodyFixtures(float scale, short category) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2f * scale, height / 2f * scale);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0.0f;
        fixtureDef.filter.categoryBits = category;
        fixtureDef.filter.maskBits = COLLIDER_CATEGORY_GROUND | COLLIDER_CATEGORY_BORDER;
        body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();

        PolygonShape sensorShape = new PolygonShape();
        Vector2 sensorPosition = new Vector2(0f, -height / 2f * scale);
        sensorShape.setAsBox(width / 2f * scale, 0.1f, sensorPosition, 0f);
        FixtureDef sensorFixtureDef = new FixtureDef();
        sensorFixtureDef.shape = sensorShape;
        sensorFixtureDef.isSensor = true;
        sensorFixtureDef.filter.categoryBits = category;
        sensorFixtureDef.filter.maskBits = COLLIDER_CATEGORY_ENEMY /*| COLLIDER_CATEGORY_ATTACK*/;
        body.createFixture(sensorFixtureDef).setUserData(this);
        sensorShape.dispose();
    }

    public void setCurrentState(State state) {
        if (this.currentState != state) {
            this.currentState = state;
            stateTime = 0;
            // Para manejar el crouch que carece de animación propia
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

    public Animation<TextureRegion> getIdleAnimation(String animationKey) {
        Animation<TextureRegion> idleAnimation = AnimationManager.getAnimation(animationKey);
        animationLoop = true;
        return idleAnimation;
    }

    public Animation<TextureRegion> getWalkAnimation(String animationKey) {
        Animation<TextureRegion> walkAnimation = AnimationManager.getAnimation(animationKey);
        animationLoop = true;
        return walkAnimation;
    }

    public Animation<TextureRegion> getCrouchDownAnimation(String animationKey) {
        Animation<TextureRegion> crouchDownAnimation = AnimationManager.getAnimation(animationKey);
        animationLoop = false;
        return crouchDownAnimation;
    }

    public Animation<TextureRegion> getCrouchAnimation(String animationKey) {
        Animation<TextureRegion> crouchAnimation = AnimationManager.getAnimation(animationKey);
        animationLoop = false;
        return crouchAnimation;
    }

    public Animation<TextureRegion> getCrouchUpAnimation(String animationKey) {
        Animation<TextureRegion> crouchUpAnimation = AnimationManager.getAnimation(animationKey);
        animationLoop = false;
        return crouchUpAnimation;
    }

    public Animation<TextureRegion> getJumpUpAnimation(String animationKey) {
        Animation<TextureRegion> jumpUpAnimation = AnimationManager.getAnimation(animationKey);
        animationLoop = false;
        return jumpUpAnimation;
    }

    public Animation<TextureRegion> getJumpDownAnimation(String animationKey) {
        Animation<TextureRegion> jumpDownAnimation = AnimationManager.getAnimation(animationKey);
        animationLoop = false;
        return jumpDownAnimation;
    }

    public Animation<TextureRegion> getLandAnimation(String animationKey) {
        Animation<TextureRegion> landAnimation = AnimationManager.getAnimation(animationKey);
        animationLoop = false;
        return landAnimation;
    }

    public Animation<TextureRegion> getAttackAnimation(String animationKey) {
        Animation<TextureRegion> attackAnimation = AnimationManager.getAnimation(animationKey);
        animationLoop = false;
        return attackAnimation;
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
