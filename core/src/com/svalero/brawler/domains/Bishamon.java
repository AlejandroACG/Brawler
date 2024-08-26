package com.svalero.brawler.domains;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.svalero.brawler.managers.LevelManager;

import static com.svalero.brawler.utils.Constants.*;

public class Bishamon extends Enemy {
    public Bishamon(LevelManager levelManager, World world, Vector2 position) {
        super(levelManager, world, position, BISHAMON_ATLAS, BISHAMON_HEALTH, BISHAMON_STRENGTH, BISHAMON_SPEED,
                BISHAMON_WIDTH, BISHAMON_HEIGHT, BISHAMON_FRAME_WIDTH, BISHAMON_FRAME_HEIGHT,
                BISHAMON_DRAW_CORRECTION_X, BISHAMON_DRAW_CORRECTION_Y, BISHAMON_IDLE_DURATION, BISHAMON_IDLE,
                BISHAMON_HIT, BISHAMON_HIT_FRAMES, BISHAMON_HIT_DURATION, BISHAMON_HIT_SOUND, BISHAMON_DEAD,
                BISHAMON_DEAD_SOUND, BISHAMON_DEAD_FRAMES, BISHAMON_DEAD_DURATION);
    }
}
