package com.svalero.brawler.domains.characters;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.svalero.brawler.interfaces.SpecialAttackableInterface;
import com.svalero.brawler.managers.ConfigurationManager;
import com.svalero.brawler.managers.LevelManager;
import com.svalero.brawler.managers.SoundManager;
import static com.svalero.brawler.domains.characters.Character.State.*;
import static com.svalero.brawler.managers.AnimationManager.getAnimation;
import static com.svalero.brawler.utils.Constants.*;

public class Bishamon extends Enemy implements SpecialAttackableInterface {
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
                BISHAMON_ATTACK_SOUND, BISHAMON_ATTACK, BISHAMON_VICTORY, BISHAMON_VICTORY_SOUND,
                BISHAMON_SPECIAL_ATTACK_DISTANCE, BISHAMON_ATTACK_DISTANCE);
    }

    @Override
    public void goSpecialAttack() {
        setCurrentState(SPECIAL_ATTACK_PREP);
        // TODO Igual que con el resto de long sounds, si fuese a haber más de una instancia del mismo personaje al mismo tiempo,
        //  aquí tendría que añadir un identificador por ID de cada entidad, y lo mismo en cada stop.
        // TODO Una manera de hacer mejor esto sería añadir también una boolean que indique si se va a reproducir en bucle o no,
        //  porque por ejemplo, en este caso en particular, sería mejor que no se reprodujese en bucle.
        SoundManager.playLongSound(BISHAMON_SPECIAL_ATTACK_PREP_SOUND, BISHAMON_SPECIAL_ATTACK_PREP, false);
        currentAnimation = getAnimation(BISHAMON_SPECIAL_ATTACK_PREP);
    }

    @Override
    public Vector2 handleSpecialAttack(float dt, Vector2 velocity) {
        if (currentState == SPECIAL_ATTACK_PREP) {
            velocity.x = 0;
            if (stateTime >= BISHAMON_SPECIAL_ATTACK_PREP_FRAMES * BISHAMON_SPECIAL_ATTACK_PREP_DURATION) {

                setCurrentState(SPECIAL_ATTACK);
                currentAnimation = getAnimation(BISHAMON_SPECIAL_ATTACK);
                SoundManager.playSound(BISHAMON_SPECIAL_ATTACK_SOUND);
                SoundManager.playSound(BISHAMON_ATTACK_SOUND);

                velocity.x = isFacingLeft() ? -BISHAMON_SPECIAL_ATTACK_SPEED : BISHAMON_SPECIAL_ATTACK_SPEED;
                // TODO Compensando por la escala
                body.setGravityScale(0.0f);
                Fixture baseFixture = body.getFixtureList().get(0);
                baseFixture.setDensity(0.01f);
                body.resetMassData();

                // TODO El launchAttack() podría modificarse para incluir variaciones como ésta. Ahora mismo es excesivamente
                //  específico
                float offsetX = facingLeft ? -BISHAMON_SPECIAL_ATTACK_OFFSET_X : BISHAMON_SPECIAL_ATTACK_OFFSET_X;
                createAttackFixture(offsetX, BISHAMON_SPECIAL_ATTACK_OFFSET_Y, BISHAMON_SPECIAL_ATTACK_WIDTH, BISHAMON_SPECIAL_ATTACK_HEIGHT);
            }
        }

        if (currentState == SPECIAL_ATTACK) {
            velocity.x = isFacingLeft() ? -BISHAMON_SPECIAL_ATTACK_SPEED : BISHAMON_SPECIAL_ATTACK_SPEED;
            if (stateTime >= BISHAMON_SPECIAL_ATTACK_FRAMES * BISHAMON_SPECIAL_ATTACK_DURATION) {
                // TODO Compensando por la escala
                body.setGravityScale(1.0f);
                Fixture baseFixture = body.getFixtureList().get(0);
                baseFixture.setDensity(1f);
                body.resetMassData();
                velocity.x = 0;
                setCurrentState(SPECIAL_ATTACK_POST);
                currentAnimation = getAnimation(BISHAMON_SPECIAL_ATTACK_POST);
            }
        }

        if (currentState == SPECIAL_ATTACK_POST) {
            if (attackFixture != null) {
                clearAttackFixture();
            }
            if (stateTime >= BISHAMON_SPECIAL_ATTACK_POST_FRAMES * BISHAMON_SPECIAL_ATTACK_POST_DURATION) {
                setCurrentState(IDLE);
                currentAnimation = getAnimation(idleKey);
            }
        }
        return velocity;
    }
}
