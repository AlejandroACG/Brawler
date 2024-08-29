package com.svalero.brawler.domains.projectiles;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.svalero.brawler.domains.characters.HsienKo;
import com.svalero.brawler.interfaces.ProjectileInterface;
import com.svalero.brawler.managers.AnimationManager;
import com.svalero.brawler.managers.LevelManager;
import static com.svalero.brawler.utils.Constants.*;

public class Bomb extends Projectile implements ProjectileInterface {
    private Body body;
    private float stateTime;
    private float timeToLive;
    private boolean exploded;
    protected State currentState;
    private Animation<TextureRegion> animation;
    private LevelManager levelManager;

    public enum State {
        IDLE,
        EXPLOSION
    }

    public Bomb(Vector2 startPosition, Vector2 velocity, HsienKo hsienKo, float timeToLive,
                String animationKey, LevelManager levelManager) {
        this.timeToLive = timeToLive;
        this.animation = AnimationManager.getAnimation(animationKey);
        this.stateTime = 0;
        this.exploded = false;
        this.levelManager = levelManager;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(startPosition);
        bodyDef.fixedRotation = true;
        this.body = levelManager.getWorld().createBody(bodyDef);

        // TODO Podría ser CircleShape si no estuviese usando las medidas como he decidido hacerlo.
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(HSIEN_KO_BOMB_WIDTH / 2f, HSIEN_KO_BOMB_HEIGHT / 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 1f;
        fixtureDef.filter.categoryBits = COLLIDER_CATEGORY_BOMB_IDLE;
        fixtureDef.filter.maskBits = COLLIDER_CATEGORY_GROUND;
        body.createFixture(fixtureDef).setUserData(hsienKo);

        shape.dispose();

        currentState = State.IDLE;
        animation = AnimationManager.getAnimation(HSIEN_KO_BOMB);
        body.setLinearVelocity(velocity);
//        SoundManager.playLongSound(DEATH_ADDER_SPECIAL_ATTACK_WAVE_SOUND, DEATH_ADDER_WAVE);
    }

    @Override
    public void update(float dt) {
        stateTime += dt;
        timeToLive -= dt;

        if (shouldBeDestroyed()) {
            levelManager.queueBodyForDestruction(body);
            body = null;
        }
    }

    public boolean shouldBeDestroyed() { return timeToLive <= 0 || animation.isAnimationFinished(stateTime); }

    @Override
    public void render(SpriteBatch batch) {
        if (body != null) {
            TextureRegion currentFrame = animation.getKeyFrame(stateTime, false);
            Vector2 position = body.getPosition();

            batch.draw(currentFrame, position.x + HSIEN_KO_BOMB_OFFSET_X, position.y + HSIEN_KO_BOMB_OFFSET_Y,
                    HSIEN_KO_BOMB_FRAME_WIDTH, HSIEN_KO_BOMB_FRAME_HEIGHT);
        }
    }

    // TODO Investigar por qué si no accedo al world así de manera directa desde levelmanager sale null.
    @Override
    public void dispose() { levelManager.getWorld().destroyBody(body); }
}
