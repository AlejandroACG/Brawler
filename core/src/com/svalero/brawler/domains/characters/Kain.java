package com.svalero.brawler.domains.characters;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.svalero.brawler.managers.ConfigurationManager;
import com.svalero.brawler.managers.LevelManager;

import static com.svalero.brawler.utils.Constants.*;

public class Kain extends Player {
    public Kain(LevelManager levelManager, World world, Vector2 position) {
        super(levelManager, world, position, KAIN_ATLAS,
                ConfigurationManager.hard ? KAIN_HEALTH_HARD : KAIN_HEALTH,
                KAIN_ATTACK_STRENGTH, KAIN_SPEED, KAIN_WIDTH,
                KAIN_HEIGHT, KAIN_FRAME_WIDTH, KAIN_FRAME_HEIGHT, KAIN_DRAW_CORRECTION_X, KAIN_DRAW_CORRECTION_Y,
                KAIN_IDLE_DURATION, KAIN_WALK_DURATION, KAIN_JUMP_UP_DURATION, KAIN_JUMP_STRENGTH, KAIN_IDLE,
                KAIN_TURN, KAIN_WALK, KAIN_RUN, KAIN_BLOCK_UP, KAIN_BLOCK_DOWN, KAIN_CROUCH_DOWN, KAIN_CROUCH_UP,
                KAIN_JUMP_UP, KAIN_JUMP_DOWN, KAIN_LAND, KAIN_ATTACK, KAIN_JUMP_ATTACK, KAIN_HIT, KAIN_BLOCK_MOVE_SOUND,
                KAIN_GRUNT_SOUND, KAIN_ATTACK_SOUND, KAIN_TURN_FRAMES, KAIN_TURN_DURATION, KAIN_BLOCK_FRAMES,
                KAIN_BLOCK_DURATION, KAIN_CROUCH_FRAMES, KAIN_CROUCH_DURATION, KAIN_LAND_FRAMES, KAIN_LAND_DURATION,
                KAIN_HIT_FRAMES, KAIN_HIT_DURATION, KAIN_ATTACK_FRAMES, KAIN_ATTACK_DURATION, KAIN_JUMP_ATTACK_FRAMES,
                KAIN_JUMP_ATTACK_DURATION, KAIN_ATTACK_OFFSET_X, KAIN_ATTACK_OFFSET_Y, KAIN_ATTACK_WIDTH,
                KAIN_ATTACK_HEIGHT, KAIN_JUMP_ATTACK_OFFSET_X, KAIN_JUMP_ATTACK_OFFSET_Y, KAIN_JUMP_ATTACK_WIDTH,
                KAIN_JUMP_ATTACK_HEIGHT, KAIN_HIT_SOUND, KAIN_DEAD, KAIN_DEAD_SOUND, KAIN_DEAD_FRAMES,
                KAIN_DEAD_DURATION, KAIN_VICTORY, KAIN_VICTORY_SOUND, KAIN_CROUCH_WIDTH, KAIN_CROUCH_HEIGHT);
    }
}
