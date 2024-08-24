package com.svalero.brawler.domains;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import static com.svalero.brawler.utils.Constants.*;

public class Kain extends Player {
    public Kain(World world, Vector2 position) {
        super(world, position, KAIN_SCALE, KAIN_ATLAS, KAIN_SPEED, KAIN_WIDTH, KAIN_HEIGHT,
                KAIN_SPRITE_WIDTH, KAIN_SPRITE_HEIGHT, KAIN_DRAW_CORRECTION_X, KAIN_DRAW_CORRECTION_Y, KAIN_IDLE_DURATION,
                KAIN_WALK_DURATION, KAIN_JUMP_UP_DURATION, KAIN_JUMP_DOWN_DURATION, KAIN_JUMP_STRENGTH, KAIN_IDLE);
    }
}
