package com.svalero.brawler.managers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static com.svalero.brawler.utils.Constants.BLOOD_BIG;
import static com.svalero.brawler.utils.Constants.BLOOD_SMALL;

public class EffectManager implements Disposable {
    private final List<VisualEffect> effects = new ArrayList<>();
    private final Body body;

    public EffectManager(Body body) {
        this.body = body;
    }

    public void createVisualEffect(Vector2 position, float characterWidth, float characterHeight,
                                   float duration, String effectType, boolean facingLeft) {
        Vector2 effectPosition = new Vector2(
                position.x + (characterWidth / 2),
                position.y + (characterHeight / 2)
        );

        VisualEffect effect = new VisualEffect(effectPosition, characterWidth, characterHeight, duration, effectType, facingLeft);
        effects.add(effect);
    }

    public void update(float dt) {
        Iterator<VisualEffect> iterator = effects.iterator();
        while (iterator.hasNext()) {
            VisualEffect effect = iterator.next();
            effect.update(dt);
            if (effect.isFinished()) {
                iterator.remove();
            }
        }
    }

    public void drawEffects(SpriteBatch batch) {
        for (VisualEffect effect : effects) {
            effect.draw(batch);
        }
    }

    @Override
    public void dispose() {}

    private class VisualEffect {
        private final Vector2 position;
        private final float width;
        private final float height;
        private final String effectType;
        private boolean facingLeft;
        private float stateTime = 0;
        private final Animation<TextureRegion> animation;

        public VisualEffect(Vector2 position, float width, float height, float duration, String effectType, boolean facingLeft) {
            this.position = new Vector2(position);
            this.width = width;
            this.height = height;
            this.effectType = effectType;
            this.facingLeft = facingLeft;

            this.animation = AnimationManager.getAnimation(effectType);
            animation.setFrameDuration(duration / animation.getKeyFrames().length);
        }

        public void update(float dt) {
            stateTime += dt;
        }

        public boolean isFinished() {
            return animation.isAnimationFinished(stateTime);
        }

        public void draw(SpriteBatch batch) {
            TextureRegion currentFrame = animation.getKeyFrame(stateTime, false);

            float adjustedX = position.x;
            float adjustedY = position.y;
            switch(effectType) {
                case BLOOD_SMALL:
                    if (facingLeft) {
                        adjustedX = position.x - width - width / 2;
                    } else {
                        adjustedX = position.x + width / 2;
                    }
                    break;
                case BLOOD_BIG:
                    if (facingLeft) {
                        adjustedX = position.x - width;
                    }
                    adjustedY = position.y - (height / 2);
                    break;
            }

            if (facingLeft) {
                batch.draw(currentFrame, adjustedX, adjustedY, width, height);
            } else {
                batch.draw(currentFrame, adjustedX, adjustedY, -width, height);
            }
        }
    }
}
