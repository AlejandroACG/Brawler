package com.svalero.brawler.domains;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import static com.svalero.brawler.utils.Constants.*;

public class Bishamon extends Enemy {
    public Bishamon(World world, Vector2 position) {
        super(world, position, BISHAMON_ATLAS, BISHAMON_HEALTH, BISHAMON_SPEED, BISHAMON_WIDTH, BISHAMON_HEIGHT,
                BISHAMON_FRAME_WIDTH, BISHAMON_FRAME_HEIGHT, BISHAMON_DRAW_CORRECTION_X, BISHAMON_DRAW_CORRECTION_Y,
                BISHAMON_IDLE_DURATION, BISHAMON_IDLE);
    }
}
