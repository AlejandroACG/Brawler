package com.svalero.brawler.domains.projectiles;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.svalero.brawler.interfaces.ProjectileInterface;

import static com.svalero.brawler.utils.Constants.*;

public class Bomb extends Projectile implements ProjectileInterface {
    private World world;
    private Body body;
    private float stateTime;
    private boolean exploded;

    public Bomb(World world, Vector2 startPosition, Vector2 velocity) {
        this.world = world;
        this.stateTime = 0;
        this.exploded = false;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(startPosition);
        bodyDef.fixedRotation = true;
        this.body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(5f); // Ajusta el tamaño de la bomba

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0.3f; // Hace que rebote un poco
        fixtureDef.filter.categoryBits = COLLIDER_CATEGORY_ATTACK_ENEMY;
        fixtureDef.filter.maskBits = COLLIDER_CATEGORY_PLAYER | COLLIDER_CATEGORY_GROUND;
        body.createFixture(fixtureDef).setUserData(this);

        shape.dispose();

        body.setLinearVelocity(velocity);
    }

    public void update(float dt) {
        stateTime += dt;

        if (exploded || body.getPosition().y <= 0) {
            explode();
        }
    }

    public void render(SpriteBatch batch) {
        // Aquí dibujas la bomba, usando su posición y estado actual
        // Por ejemplo, batch.draw(texture, position.x, position.y);
    }

    public void explode() {
        if (!exploded) {
            exploded = true;
            // Lógica de explosión
            world.destroyBody(body);
        }
    }

    public boolean isExploded() {
        return exploded;
    }
}
