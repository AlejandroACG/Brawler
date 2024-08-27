package com.svalero.brawler.domains;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.svalero.brawler.managers.ConfigurationManager;
import com.svalero.brawler.managers.LevelManager;

import static com.svalero.brawler.utils.Constants.*;

public class DeathAdder extends Enemy {
    public DeathAdder(LevelManager levelManager, World world, Vector2 position) {
        super(levelManager, world, position, DEATH_ADDER_ATLAS,
                ConfigurationManager.hard ? DEATH_ADDER_HEALTH_HARD : DEATH_ADDER_HEALTH,
                ConfigurationManager.hard ? DEATH_ADDER_ATTACK_STRENGTH_HARD : DEATH_ADDER_ATTACK_STRENGTH,
                DEATH_ADDER_SPEED, DEATH_ADDER_WIDTH, DEATH_ADDER_HEIGHT, DEATH_ADDER_FRAME_WIDTH,
                DEATH_ADDER_FRAME_HEIGHT, DEATH_ADDER_DRAW_CORRECTION_X, DEATH_ADDER_DRAW_CORRECTION_Y,
                DEATH_ADDER_IDLE_DURATION, DEATH_ADDER_IDLE, DEATH_ADDER_HIT, DEATH_ADDER_HIT_FRAMES,
                DEATH_ADDER_HIT_DURATION, DEATH_ADDER_HIT_SOUND, DEATH_ADDER_DEAD, DEATH_ADDER_DEAD_SOUND,
                DEATH_ADDER_DEAD_FRAMES, DEATH_ADDER_DEAD_DURATION, DEATH_ADDER_TURN, DEATH_ADDER_TURN_FRAMES,
                DEATH_ADDER_TURN_DURATION, DEATH_ADDER_ATTACK_FRAMES, DEATH_ADDER_ATTACK_DURATION,
                DEATH_ADDER_ATTACK_WIDTH, DEATH_ADDER_ATTACK_HEIGHT, DEATH_ADDER_ATTACK_OFFSET_X,
                DEATH_ADDER_ATTACK_OFFSET_Y, DEATH_ADDER_WALK, DEATH_ADDER_ATTACK_SOUND, DEATH_ADDER_ATTACK);
    }
}
