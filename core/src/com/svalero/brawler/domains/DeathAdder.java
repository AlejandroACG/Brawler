package com.svalero.brawler.domains;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import static com.svalero.brawler.utils.Constants.*;

public class DeathAdder extends Enemy {
    public DeathAdder(World world, Vector2 position) {
        super(world, position, DEATH_ADDER_ATLAS, DEATH_ADDER_HEALTH, DEATH_ADDER_STRENGTH, DEATH_ADDER_SPEED, DEATH_ADDER_WIDTH, DEATH_ADDER_HEIGHT,
                DEATH_ADDER_FRAME_WIDTH, DEATH_ADDER_FRAME_HEIGHT, DEATH_ADDER_DRAW_CORRECTION_X, DEATH_ADDER_DRAW_CORRECTION_Y,
                DEATH_ADDER_IDLE_DURATION, DEATH_ADDER_IDLE, DEATH_ADDER_HIT);
    }
}
