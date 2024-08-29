package com.svalero.brawler.domains.characters;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.svalero.brawler.domains.projectiles.Bomb;
import com.svalero.brawler.interfaces.SpecialAttackableInterface;
import com.svalero.brawler.managers.ConfigurationManager;
import com.svalero.brawler.managers.LevelManager;
import static com.svalero.brawler.domains.characters.Character.State.*;
import static com.svalero.brawler.managers.AnimationManager.getAnimation;
import static com.svalero.brawler.utils.Constants.*;

public class HsienKo extends Enemy implements SpecialAttackableInterface {
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
                HSIEN_KO_ATTACK_SOUND, HSIEN_KO_ATTACK, HSIEN_KO_VICTORY, HSIEN_KO_VICTORY_SOUND,
                HSIEN_KO_SPECIAL_ATTACK_DISTANCE, HSIEN_KO_ATTACK_DISTANCE);
    }

    @Override
    public void goSpecialAttack() {
        setCurrentState(SPECIAL_ATTACK_PREP);
        currentAnimation = getAnimation(HSIEN_KO_SPECIAL_ATTACK);
    }

    @Override
    public Vector2 handleSpecialAttack(float dt, Vector2 velocity) {
        if (currentState == SPECIAL_ATTACK_PREP) {
            if (stateTime >= 10 * HSIEN_KO_SPECIAL_ATTACK_DURATION) {

                setCurrentStateWithoutReset(SPECIAL_ATTACK);

                Vector2 hsienKoPosition = this.getPosition();

                hsienKoPosition.y = hsienKoPosition.y + HSIEN_KO_HEIGHT;

                Bomb bomb = new Bomb(hsienKoPosition, new Vector2(isFacingLeft() ? - HSIEN_KO_BOMB_SPEED : HSIEN_KO_BOMB_SPEED,
                        HSIEN_KO_BOMB_SPEED), this, HSIEN_KO_BOMB_FRAMES * HSIEN_KO_BOMB_DURATION,
                        HSIEN_KO_BOMB, levelManager);

                levelManager.addProjectile(bomb);
            }
        }

        if (currentState == SPECIAL_ATTACK) {
            if (stateTime >= HSIEN_KO_SPECIAL_ATTACK_FRAMES * HSIEN_KO_SPECIAL_ATTACK_DURATION) {
                setCurrentState(IDLE);
                currentAnimation = getAnimation(idleKey);
            }
        }
        return velocity;
    }
}
