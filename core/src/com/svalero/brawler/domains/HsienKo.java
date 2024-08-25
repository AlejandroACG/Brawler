package com.svalero.brawler.domains;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import static com.svalero.brawler.utils.Constants.*;

public class HsienKo extends Enemy {
    public HsienKo(World world, Vector2 position) {
        super(world, position, HSIEN_KO_ATLAS, HSIEN_KO_HEALTH, HSIEN_KO_SPEED, HSIEN_KO_WIDTH, HSIEN_KO_HEIGHT,
                HSIEN_KO_FRAME_WIDTH, HSIEN_KO_FRAME_HEIGHT, HSIEN_KO_DRAW_CORRECTION_X, HSIEN_KO_DRAW_CORRECTION_Y,
                HSIEN_KO_IDLE_DURATION, HSIEN_KO_IDLE);
    }
}
