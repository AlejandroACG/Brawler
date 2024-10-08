package com.svalero.brawler.domains.characters;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.svalero.brawler.domains.AttackInfo;
import com.svalero.brawler.managers.*;
import com.svalero.brawler.utils.IDGenerator;
import static com.svalero.brawler.managers.AnimationManager.getAnimation;
import static com.svalero.brawler.utils.Constants.*;

public abstract class Character implements Disposable {
    protected LevelManager levelManager;
    protected TextureAtlas textureAtlas;
    protected Vector2 position;
    protected float stateTime = 0;
    protected EffectManager effectManager;
    protected int maxHealth;
    protected int currentHealth;
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
    protected String blockMoveSoundPath;
    protected String jumpSoundPath;
    protected String attackSoundPath;
    protected String hitSoundPath;
    protected String deadKey;
    protected String deadSoundPath;
    protected String victoryKey;
    protected String victorySoundPath;
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
    protected float specialAttackCooldown = 0;
    protected float specialAttackCooldownReset;
    protected float specialAttackDistance;
    protected boolean isOnGround = true;
    protected boolean markToFallDead = false;
    protected float walkingSoundTimer = WALKING_SOUND_TIMER;
    protected float attackDistance;
    protected Fixture standFixture;
    protected Fixture crouchFixture;
    protected float crouchWidth;
    protected float crouchHeight;
    protected String introKey;

    public enum State {
        INTRO,
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
        SPECIAL_ATTACK_PREP,
        SPECIAL_ATTACK,
        SPECIAL_ATTACK_POST,
        JUMP_ATTACK,
        BLOCK_UP,
        BLOCK,
        BLOCK_SUCCESS,
        BLOCK_DOWN,
        HIT,
        DEAD,
        VICTORY
    }

    // For players
    public Character(LevelManager levelManager, World world, Vector2 position,
                     String characterAtlas, int health, int attackStrength, float speed, float width, float height,
                     float frameWidth, float frameHeight, float correctionX, float correctionY, float idleDuration,
                     float jumpUpDuration, float jumpDownDuration, float jumpStrength, String idleKey, String turnKey,
                     String walkKey, String runKey, String blockUpKey, String blockDownKey, String crouchDownKey,
                     String crouchUpKey, String jumpUpKey, String jumpDownKey, String landKey, String attackKey,
                     String jumpAttackKey, String hitKey, String blockMoveSoundPath, String jumpSoundPath,
                     String attackSoundPath, int turnFrames, float turnDuration, int blockFrames, float blockDuration,
                     int crouchFrames, float crouchDuration, int landFrames, float landDuration, int hitFrames,
                     float hitDuration, int attackFrames, float attackDuration, int jumpAttackFrames,
                     float jumpAttackDuration, float attackOffsetX, float attackOffsetY, float attackWidth,
                     float attackHeight, float jumpAttackOffsetX, float jumpAttackOffsetY, float jumpAttackWidth,
                     float jumpAttackHeight, String hitSoundPath, String deadKey, String deadSoundPath, int deadFrames,
                     float deadDuration, String victoryKey, String victorySoundPath, float crouchWidth,
                     float crouchHeight, String introKey) {
        this.levelManager = levelManager;
        this.world = world;
        this.position = position;
        this.maxHealth = health;
        this.currentHealth = health;
        this.attackStrength = attackStrength;
        this.speed = speed;
        this.id = IDGenerator.generateUniqueId();
        this.width = width;
        this.height = height;
        this.currentState = State.INTRO;
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
        this.blockMoveSoundPath = blockMoveSoundPath;
        this.jumpSoundPath = jumpSoundPath;
        this.attackSoundPath = attackSoundPath;
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
        this.hitSoundPath = hitSoundPath;
        this.deadKey = deadKey;
        this.deadSoundPath = deadSoundPath;
        this.deadFrames = deadFrames;
        this.deadDuration = deadDuration;
        this.victoryKey = victoryKey;
        this.victorySoundPath = victorySoundPath;
        this.crouchWidth = crouchWidth;
        this.crouchHeight = crouchHeight;
        this.introKey = introKey;

        createBody(world, characterAtlas);
    }

    // For enemies
    public Character(LevelManager levelManager, World world, Vector2 position,
                     String characterAtlas, int health, int attackStrength, float speed, float width, float height,
                     float frameWidth, float frameHeight, float correctionX, float correctionY, float idleDuration,
                     String idleKey, String hitKey, int hitFrames, float hitDuration, String hitSoundPath,
                     String deadKey, String deadSoundPath, int deadFrames, float deadDuration, String turnKey,
                     int turnFrames, float turnDuration, int attackFrames, float attackDuration, float attackWidth,
                     float attackHeight, float attackOffsetX, float attackOffsetY, String walkKey,
                     String attackSoundPath, String attackKey, String victoryKey, String victorySoundPath,
                     float specialAttackDistance, float attackDistance) {
        this.levelManager = levelManager;
        this.position = position;
        this.maxHealth = health;
        this.currentHealth = health;
        this.attackStrength = attackStrength;
        this.speed = speed;
        this.id = IDGenerator.generateUniqueId();
        this.width = width;
        this.height = height;
        this.currentState = State.INTRO;
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
        this.hitSoundPath = hitSoundPath;
        this.deadKey = deadKey;
        this.deadSoundPath = deadSoundPath;
        this.deadFrames = deadFrames;
        this.deadDuration = deadDuration;
        this.turnKey = turnKey;
        this.turnFrames = turnFrames;
        this.turnDuration = turnDuration;
        this.attackFrames = attackFrames;
        this.attackDuration = attackDuration;
        this.attackWidth = attackWidth;
        this.attackHeight = attackHeight;
        this.attackOffsetX = attackOffsetX;
        this.attackOffsetY = attackOffsetY;
        this.walkKey = walkKey;
        this.attackSoundPath = attackSoundPath;
        this.attackKey = attackKey;
        this.victoryKey = victoryKey;
        this.victorySoundPath = victorySoundPath;
        this.specialAttackCooldownReset = ConfigurationManager.hard ? SPECIAL_ATTACK_COOLDOWN_DURATION_HARD : SPECIAL_ATTACK_COOLDOWN_DURATION;
        this.specialAttackDistance = specialAttackDistance;
        this.attackDistance = attackDistance;

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
        this.effectManager = new EffectManager(body);

        createBodyFixtures();
    }

    protected void createBodyFixtures() {
        // Standing fixture
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((width) / 2f, (height) / 2f);
        FixtureDef standFixtureDef = new FixtureDef();
        standFixtureDef.shape = shape;
        standFixtureDef.density = 1f;
        standFixtureDef.friction = 0f;
        standFixtureDef.restitution = 0.0f;
        if (this instanceof Player) {
            standFixtureDef.filter.categoryBits = COLLIDER_CATEGORY_PLAYER;
            standFixtureDef.filter.maskBits = COLLIDER_CATEGORY_GROUND | COLLIDER_CATEGORY_BORDER | COLLIDER_CATEGORY_ATTACK_ENEMY;
        } else if (this instanceof Enemy) {
            standFixtureDef.filter.categoryBits = COLLIDER_CATEGORY_ENEMY;
            standFixtureDef.filter.maskBits = COLLIDER_CATEGORY_GROUND | COLLIDER_CATEGORY_BORDER | COLLIDER_CATEGORY_ATTACK_PLAYER;
        }
        standFixture = body.createFixture(standFixtureDef);
        standFixture.setUserData(this);
        shape.dispose();

        // TODO Necesario restringir esto a los Player mientras los enemigos no puedan agacharse.
        if (this instanceof Player) {
            // Crouching fixture
            PolygonShape crouchShape = new PolygonShape();
            float heightDifference = (height - crouchHeight) / 2f;

            crouchShape.setAsBox(crouchWidth / 2f, crouchHeight / 2f, new Vector2(0, -heightDifference), 0);

            FixtureDef crouchFixtureDef = new FixtureDef();
            crouchFixtureDef.shape = crouchShape;
            crouchFixtureDef.density = 1f;
            crouchFixtureDef.friction = 0f;
            crouchFixtureDef.restitution = 0.0f;
            crouchFixtureDef.filter.categoryBits = COLLIDER_CATEGORY_PLAYER;
            crouchFixtureDef.filter.maskBits = COLLIDER_CATEGORY_GROUND | COLLIDER_CATEGORY_BORDER | COLLIDER_CATEGORY_ATTACK_ENEMY;
            crouchFixture = body.createFixture(crouchFixtureDef);
            crouchFixture.setUserData(this);
            crouchFixture.setSensor(true);

            crouchShape.dispose();
        }
    }

    // TODO Podría personalizar esto para recibir la boolean de si son bloqueables o no.
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

        attackFixture.setUserData(new AttackInfo(attackStrength, true));

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

    protected void launchAttack() {
        float offsetX = facingLeft ? -attackOffsetX : attackOffsetX;
        createAttackFixture(offsetX, attackOffsetY, attackWidth, attackHeight);
    }

    protected void clearAttackFixture() {
        if (attackFixture != null) {
            body.destroyFixture(attackFixture);
            attackFixture = null;
        }
    }

    public void getHit(int strength, boolean attackFromLeft) {
        currentHealth = currentHealth - strength;
        int previousScore = levelManager.getCurrentScore();
        int newScore;
        if (this instanceof Player) {
            newScore = ConfigurationManager.hard ? previousScore - 10 : previousScore - 20;
            if (newScore <= 0) {
                newScore = 0;
            }
        } else {
            newScore = ConfigurationManager.hard ? previousScore + 60 : previousScore + 40;
        }
        levelManager.setCurrentScore(newScore);
        if (currentHealth <= 0) {
            facingLeft = attackFromLeft;
            setCurrentState(State.DEAD);
            currentAnimation = getAnimation(deadKey);
            SoundManager.playSound(deadSoundPath);

            effectManager.createVisualEffect(position, BLOOD_BIG_WIDTH, BLOOD_BIG_HEIGHT,
                    BLOOD_BIG_DURATION, BLOOD_BIG, attackFromLeft);
        } else {
            currentAnimation = AnimationManager.getAnimation(hitKey);
            SoundManager.playSound(HIT_SOUND);
            SoundManager.playSound(hitSoundPath);
            setCurrentState(State.HIT);

            effectManager.createVisualEffect(position, BLOOD_SMALL_WIDTH, BLOOD_SMALL_HEIGHT,
                    BLOOD_SMALL_DURATION, BLOOD_SMALL, attackFromLeft);
        }
    }

    public void blockAttack() {
        effectManager.createVisualEffect(position, BLOCK_SPARK_WIDTH, BLOCK_SPARK_HEIGHT,
                BLOCK_SPARK_DURATION, BLOCK_SPARK, isFacingLeft());
        SoundManager.playSound(KAIN_BLOCK_SOUND);
        currentState = State.BLOCK;
    }

    public Vector2 stayDead(Vector2 velocity) {
        velocity.y = 0;
        velocity.x = 0;

        return velocity;
    }

    protected Vector2 goIdle(Vector2 velocity) {
        setCurrentStateWithoutReset(State.IDLE);
        currentAnimation = getAnimation(idleKey);
        velocity.x = 0;

        return velocity;
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
        effectManager.dispose();
        if (world != null && body != null) {
            world.destroyBody(body);
            body = null;
        }
    }

    protected Vector2 doVictory (Vector2 velocity) {
        velocity.x = 0;
        if (stateTime == 0) {
            currentAnimation = getAnimation(victoryKey);
            SoundManager.playSound(victorySoundPath);
        }
        return velocity;
    }

    protected Vector2 goAttack(Vector2 velocity) {
        velocity.x = 0;
        currentAnimation = getAnimation(attackKey);
        SoundManager.playSound(attackSoundPath);
        setCurrentState(State.ATTACK);

        // TODO Se puede implementar una lógica adicional para que en casos como Bishamon el fixture de
        // TODO daño solo aparezca a partir de X fotograma.
        launchAttack();
        return velocity;
    }

    protected Vector2 goWalk(Vector2 velocity) {
        setCurrentState(State.WALK);
        currentAnimation = getAnimation(walkKey);
        // TODO De haber más niveles implementar un switch
        SoundManager.playLongSound(levelManager.getCurrentLevel() == 1 ? WALKING_ON_GRASS_SOUND : WALKING_ON_STONE_SOUND, walkKey, true);
        if (levelManager.getCurrentLevel() == 1) { walkingSoundTimer = WALKING_SOUND_TIMER; }
        if (facingLeft) {
            velocity.x = -speed;
        } else {
            velocity.x = speed;
        }
        return velocity;
    }

    protected Vector2 goTurn(Vector2 velocity) {
        setCurrentState(State.TURN);
        velocity.x = 0;
        currentAnimation = getAnimation(turnKey);
        facingLeft = !facingLeft;

        return velocity;
    }

    public EffectManager getEffectManager() { return effectManager; }

    public int getMaxHealth() { return maxHealth; }

    public int getCurrentHealth() { return currentHealth; }

    public boolean isOnGround() { return isOnGround; }

    public void setOnGround(boolean onGround) { isOnGround = onGround; }
}
