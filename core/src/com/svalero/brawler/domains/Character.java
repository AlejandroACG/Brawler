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
import com.svalero.brawler.managers.LevelManager;
import com.svalero.brawler.managers.SoundManager;
import com.svalero.brawler.utils.IDGenerator;
import static com.svalero.brawler.managers.AnimationManager.getAnimation;
import static com.svalero.brawler.utils.Constants.*;

public abstract class Character implements Disposable {
    protected LevelManager levelManager;
    protected TextureAtlas textureAtlas;
    protected Vector2 position;
    protected float stateTime = 0;
    protected int health;
    protected int attackStrength;
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
    protected Fixture attackFixture;
    protected boolean hasAttackedThisJump = false;
    protected World world;
    protected String idleKey;
    protected String turnKey;
    protected String walkKey;
    protected String runKey;
    protected String blockUpKey;
    protected String blockDownKey;
    protected String crouchDownKey;
    protected String crouchUpKey;
    protected String jumpUpKey;
    protected String jumpDownKey;
    protected String landKey;
    protected String attackKey;
    protected String jumpAttackKey;
    protected String hitKey;
    protected String blockMoveSoundKey;
    protected String jumpSoundKey;
    protected String attackSoundKey;
    protected String hitSoundKey;
    protected String deadKey;
    protected String deadSoundKey;
    protected int turnFrames;
    protected float turnDuration;
    protected int blockFrames;
    protected float blockDuration;
    protected int crouchFrames;
    protected float crouchDuration;
    protected int landFrames;
    protected float landDuration;
    protected int hitFrames;
    protected float hitDuration;
    protected int deadFrames;
    protected float deadDuration;
    protected int attackFrames;
    protected float attackDuration;
    protected int jumpAttackFrames;
    protected float jumpAttackDuration;
    protected float attackOffsetX;
    protected float attackOffsetY;
    protected float attackWidth;
    protected float attackHeight;
    protected float jumpAttackOffsetX;
    protected float jumpAttackOffsetY;
    protected float jumpAttackWidth;
    protected float jumpAttackHeight;

    public enum State {
        IDLE,
        TURN,
        WALK,
        RUN,
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
        HIT,
        DEAD,
        VICTORY
    }

    public Character(LevelManager levelManager, World world, Vector2 position,
                     String characterAtlas, int health, int attackStrength, float speed, float width, float height,
                     float frameWidth, float frameHeight, float correctionX, float correctionY, float idleDuration,
                     float jumpUpDuration, float jumpDownDuration, float jumpStrength, String idleKey, String turnKey,
                     String walkKey, String runKey, String blockUpKey, String blockDownKey, String crouchDownKey,
                     String crouchUpKey, String jumpUpKey, String jumpDownKey, String landKey, String attackKey,
                     String jumpAttackKey, String hitKey, String blockMoveSoundKey, String jumpSoundKey,
                     String attackSoundKey, int turnFrames, float turnDuration, int blockFrames, float blockDuration,
                     int crouchFrames, float crouchDuration, int landFrames, float landDuration, int hitFrames,
                     float hitDuration, int attackFrames, float attackDuration, int jumpAttackFrames,
                     float jumpAttackDuration, float attackOffsetX, float attackOffsetY, float attackWidth,
                     float attackHeight, float jumpAttackOffsetX, float jumpAttackOffsetY, float jumpAttackWidth,
                     float jumpAttackHeight, String hitSoundKey, String deadKey, String deadSoundKey, int deadFrames,
                     float deadDuration) {
        this.levelManager = levelManager;
        this.world = world;
        this.position = position;
        this.health = health;
        this.attackStrength = attackStrength;
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
        this.idleKey = idleKey;
        this.jumpUpDuration = jumpUpDuration;
        this.jumpDownDuration = jumpDownDuration;
        this.jumpStrength = jumpStrength;
        this.turnKey = turnKey;
        this.walkKey = walkKey;
        this.runKey = runKey;
        this.blockUpKey = blockUpKey;
        this.blockDownKey = blockDownKey;
        this.crouchDownKey = crouchDownKey;
        this.crouchUpKey = crouchUpKey;
        this.jumpUpKey = jumpUpKey;
        this.jumpDownKey = jumpDownKey;
        this.landKey = landKey;
        this.attackKey = attackKey;
        this.jumpAttackKey = jumpAttackKey;
        this.hitKey = hitKey;
        this.blockMoveSoundKey = blockMoveSoundKey;
        this.jumpSoundKey = jumpSoundKey;
        this.attackSoundKey = attackSoundKey;
        this.turnFrames = turnFrames;
        this.turnDuration = turnDuration;
        this.blockFrames = blockFrames;
        this.blockDuration = blockDuration;
        this.crouchFrames = crouchFrames;
        this.crouchDuration = crouchDuration;
        this.landFrames = landFrames;
        this.landDuration = landDuration;
        this.hitFrames = hitFrames;
        this.hitDuration = hitDuration;
        this.attackFrames = attackFrames;
        this.attackDuration = attackDuration;
        this.jumpAttackFrames = jumpAttackFrames;
        this.jumpAttackDuration = jumpAttackDuration;
        this.attackOffsetX = attackOffsetX;
        this.attackOffsetY = attackOffsetY;
        this.attackWidth = attackWidth;
        this.attackHeight = attackHeight;
        this.jumpAttackOffsetX = jumpAttackOffsetX;
        this.jumpAttackOffsetY = jumpAttackOffsetY;
        this.jumpAttackWidth = jumpAttackWidth;
        this.jumpAttackHeight = jumpAttackHeight;
        this.hitSoundKey = hitSoundKey;
        this.deadKey = deadKey;
        this.deadSoundKey = deadSoundKey;
        this.deadFrames = deadFrames;
        this.deadDuration = deadDuration;

        createBody(world, characterAtlas);
    }

    public Character(LevelManager levelManager, World world, Vector2 position,
                     String characterAtlas, int health, int attackStrength, float speed, float width, float height,
                     float frameWidth, float frameHeight, float correctionX, float correctionY, float idleDuration,
                     String idleKey, String hitKey, int hitFrames, float hitDuration, String hitSoundKey,
                     String deadKey, String deadSoundKey, int deadFrames, float deadDuration, String turnKey,
                     int turnFrames, float turnDuration) {
        this.levelManager = levelManager;
        this.position = position;
        this.health = health;
        this.attackStrength = attackStrength;
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
        this.currentAnimation = AnimationManager.getAnimation(idleKey);
        this.idleKey = idleKey;
        this.hitKey = hitKey;
        this.hitFrames = hitFrames;
        this.hitDuration = hitDuration;
        this.hitSoundKey = hitSoundKey;
        this.deadKey = deadKey;
        this.deadSoundKey = deadSoundKey;
        this.deadFrames = deadFrames;
        this.deadDuration = deadDuration;
        this.turnKey = turnKey;
        this.turnFrames = turnFrames;
        this.turnDuration = turnDuration;

        createBody(world, characterAtlas);
    }

    protected void createBody(World world, String characterAtlas) {
        textureAtlas = new TextureAtlas(Gdx.files.internal(characterAtlas));
        currentAnimation = getAnimation(idleKey);

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
        if (this instanceof Player) {
            fixtureDef.filter.categoryBits = COLLIDER_CATEGORY_PLAYER;
            fixtureDef.filter.maskBits = COLLIDER_CATEGORY_GROUND | COLLIDER_CATEGORY_BORDER | COLLIDER_CATEGORY_ATTACK_ENEMY;
        } else if (this instanceof Enemy) {
            fixtureDef.filter.categoryBits = COLLIDER_CATEGORY_ENEMY;
            fixtureDef.filter.maskBits = COLLIDER_CATEGORY_GROUND | COLLIDER_CATEGORY_BORDER | COLLIDER_CATEGORY_ATTACK_PLAYER;
        }
        body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();
    }

    public void createAttackFixture(float offsetX, float offsetY, float attackWidth, float attackHeight) {
        PolygonShape attackShape = new PolygonShape();
        attackShape.setAsBox(attackWidth / 2, attackHeight / 2, new Vector2(offsetX, offsetY), 0);

        FixtureDef attackFixtureDef = new FixtureDef();
        attackFixtureDef.shape = attackShape;
        attackFixtureDef.isSensor = false;
        if (this instanceof Player) {
            attackFixtureDef.filter.categoryBits = COLLIDER_CATEGORY_ATTACK_PLAYER;
            attackFixtureDef.filter.maskBits = COLLIDER_CATEGORY_ENEMY;
        } else if (this instanceof Enemy) {
            attackFixtureDef.filter.categoryBits = COLLIDER_CATEGORY_ATTACK_ENEMY;
            attackFixtureDef.filter.maskBits = COLLIDER_CATEGORY_PLAYER;
        }

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

    public void getHit(int strength, boolean attackFromLeft) {
        health = health - strength;
        if (health <= 0) {
            facingLeft = attackFromLeft;
            setCurrentState(State.DEAD);
            currentAnimation = getAnimation(deadKey);
            SoundManager.playSound(deadSoundKey);
        } else {
            currentAnimation = AnimationManager.getAnimation(hitKey);
            SoundManager.playSound(HIT_SOUND);
            SoundManager.playSound(hitSoundKey);
            setCurrentState(State.HIT);
        }
    }

    public void stayDead() {
        Vector2 velocity = body.getLinearVelocity();
        velocity.y = 0;
        velocity.x = 0;

        body.setLinearVelocity(velocity.x, velocity.y);
    }

    public void setHasAttackedThisJump(boolean hasAttackedThisJump) { this.hasAttackedThisJump = hasAttackedThisJump; }

    public boolean isFacingLeft() { return facingLeft; }

    public State getCurrentState() { return currentState; }

    public void setStateTime(int stateTime) { this.stateTime = stateTime; }

    public Vector2 getPosition() { return position; }

    public int getId() { return id; }

    public int getAttackStrength() { return attackStrength; }

    @Override
    public void dispose() {
        if (world != null && body != null) {
            world.destroyBody(body);
            body = null;
        }
    }
}
