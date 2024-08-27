package com.svalero.brawler.domains;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.svalero.brawler.managers.ConfigurationManager;
import com.svalero.brawler.managers.LevelManager;

import static com.svalero.brawler.utils.Constants.*;

public class Bishamon extends Enemy {
    public Bishamon(LevelManager levelManager, World world, Vector2 position) {
        super(levelManager, world, position, BISHAMON_ATLAS,
                ConfigurationManager.hard ? BISHAMON_HEALTH_HARD : BISHAMON_HEALTH,
                ConfigurationManager.hard ? BISHAMON_ATTACK_STRENGTH_HARD : BISHAMON_ATTACK_STRENGTH,
                BISHAMON_SPEED, BISHAMON_WIDTH, BISHAMON_HEIGHT, BISHAMON_FRAME_WIDTH, BISHAMON_FRAME_HEIGHT,
                BISHAMON_DRAW_CORRECTION_X, BISHAMON_DRAW_CORRECTION_Y, BISHAMON_IDLE_DURATION, BISHAMON_IDLE,
                BISHAMON_HIT, BISHAMON_HIT_FRAMES, BISHAMON_HIT_DURATION, BISHAMON_HIT_SOUND, BISHAMON_DEAD,
                BISHAMON_DEAD_SOUND, BISHAMON_DEAD_FRAMES, BISHAMON_DEAD_DURATION, BISHAMON_TURN, BISHAMON_TURN_FRAMES,
                BISHAMON_TURN_DURATION, BISHAMON_ATTACK_FRAMES, BISHAMON_ATTACK_DURATION, BISHAMON_ATTACK_WIDTH,
                BISHAMON_ATTACK_HEIGHT, BISHAMON_ATTACK_OFFSET_X, BISHAMON_ATTACK_OFFSET_Y, BISHAMON_WALK,
                BISHAMON_ATTACK_SOUND, BISHAMON_ATTACK, BISHAMON_VICTORY, BISHAMON_VICTORY_SOUND);
    }
}
