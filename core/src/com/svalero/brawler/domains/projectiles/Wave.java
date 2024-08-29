package com.svalero.brawler.domains.projectiles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.svalero.brawler.domains.characters.DeathAdder;
import com.svalero.brawler.interfaces.ProjectileInterface;
import com.svalero.brawler.managers.AnimationManager;
import com.svalero.brawler.managers.LevelManager;
import com.svalero.brawler.managers.SoundManager;
import static com.svalero.brawler.utils.Constants.*;

public class Wave extends Projectile implements ProjectileInterface {
    private World world;
    private Body body;
    private Animation<TextureRegion> animation;
    private float stateTime;
    private float timeToLive;
    private boolean facingLeft;
    private DeathAdder deathAdder;
    private LevelManager levelManager;

    public Wave(LevelManager levelManager, Vector2 startPosition, boolean facingLeft, String animationKey,
                float timeToLive, DeathAdder deathAdder) {
        this.world = levelManager.getWorld();
        this.animation = AnimationManager.getAnimation(animationKey);
        this.timeToLive = timeToLive;
        this.stateTime = 0;
        this.facingLeft = facingLeft;
        this.deathAdder = deathAdder;
        this.levelManager = levelManager;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(startPosition);
        bodyDef.fixedRotation = true;
        bodyDef.gravityScale = 0;
        this.body = world.createBody(bodyDef);

        newFixture(48);

        float velocityX = facingLeft ? -DEATH_ADDER_WAVE_SPEED : DEATH_ADDER_WAVE_SPEED;
        body.setLinearVelocity(new Vector2(velocityX, 0));
        // TODO Si hubiese más waves tendría que autogenerarles un ID y usarlo aquí, por ejemplo.
        SoundManager.playLongSound(DEATH_ADDER_SPECIAL_ATTACK_WAVE_SOUND, DEATH_ADDER_WAVE);
    }

    @Override
    public void update(float dt) {
        stateTime += dt;
        timeToLive -= dt;

        if (shouldBeDestroyed()) {
            // TODO En caso de hacer lo del ID, recordar usarlo aquí también.
            SoundManager.stopLongSound(DEATH_ADDER_WAVE);
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

            if (!facingLeft) {
                batch.draw(currentFrame, position.x - DEATH_ADDER_WAVE_WIDTH / 2, position.y - DEATH_ADDER_WAVE_HEIGHT / 2,
                        DEATH_ADDER_WAVE_WIDTH, DEATH_ADDER_WAVE_HEIGHT);
            } else {
                batch.draw(currentFrame, position.x + DEATH_ADDER_WAVE_WIDTH / 2, position.y - DEATH_ADDER_WAVE_HEIGHT / 2,
                        -DEATH_ADDER_WAVE_WIDTH, DEATH_ADDER_WAVE_HEIGHT);
            }
        }
    }

    private void newFixture(float height) {
        PolygonShape shape = new PolygonShape();

        float centerY = -(DEATH_ADDER_WAVE_HEIGHT / 4);

        shape.setAsBox(DEATH_ADDER_WAVE_WIDTH / 2, DEATH_ADDER_WAVE_HEIGHT / 4, new Vector2(0, centerY), 0);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0.5f;
        fixtureDef.filter.categoryBits = COLLIDER_CATEGORY_ATTACK_ENEMY;
        fixtureDef.filter.maskBits = COLLIDER_CATEGORY_PLAYER;

        body.createFixture(fixtureDef).setUserData(deathAdder);
        shape.dispose();
    }

    public void dispose() { world.destroyBody(body); }
}
