package com.svalero.brawler.domains;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.svalero.brawler.managers.ConfigurationManager;
import com.svalero.brawler.managers.LevelManager;

import static com.svalero.brawler.utils.Constants.*;

public class HsienKo extends Enemy {
    public HsienKo(LevelManager levelManager, World world, Vector2 position) {
        super(levelManager, world, position, HSIEN_KO_ATLAS,
                ConfigurationManager.hard ? HSIEN_KO_HEALTH_HARD : HSIEN_KO_HEALTH,
                ConfigurationManager.hard ? HSIEN_KO_ATTACK_STRENGTH_HARD : HSIEN_KO_ATTACK_STRENGTH,
                HSIEN_KO_SPEED, HSIEN_KO_WIDTH, HSIEN_KO_HEIGHT, HSIEN_KO_FRAME_WIDTH, HSIEN_KO_FRAME_HEIGHT,
                HSIEN_KO_DRAW_CORRECTION_X, HSIEN_KO_DRAW_CORRECTION_Y, HSIEN_KO_IDLE_DURATION, HSIEN_KO_IDLE,
                HSIEN_KO_HIT, HSIEN_KO_HIT_FRAMES, HSIEN_KO_HIT_DURATION, HSIEN_KO_HIT_SOUND, HSIEN_KO_DEAD,
                HSIEN_KO_DEAD_SOUND, HSIEN_KO_DEAD_FRAMES, HSIEN_KO_DEAD_DURATION, HSIEN_KO_TURN, HSIEN_KO_TURN_FRAMES,
                HSIEN_KO_TURN_DURATION, HSIEN_KO_ATTACK_FRAMES, HSIEN_KO_ATTACK_DURATION, HSIEN_KO_ATTACK_WIDTH,
                HSIEN_KO_ATTACK_HEIGHT, HSIEN_KO_ATTACK_OFFSET_X, HSIEN_KO_ATTACK_OFFSET_Y, HSIEN_KO_WALK,
                HSIEN_KO_ATTACK_SOUND, HSIEN_KO_ATTACK, HSIEN_KO_VICTORY, HSIEN_KO_VICTORY_SOUND);
    }
}
