package com.svalero.brawler.utils;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.svalero.brawler.managers.CameraManager;

public class ParallaxLayer {
    private final AtlasRegion atlasRegion;
    private final float parallaxFactor;
    private CameraManager cameraManager;
    private final boolean repeat;
    private int repetitionAmount;
    private int positionX;
    private int positionY;

    public ParallaxLayer(AtlasRegion atlasRegion, float parallaxFactor, CameraManager cameraManager, boolean repeat,
                         int repetitionAmount, int positionX, int positionY) {
        this.atlasRegion = atlasRegion;
        this.parallaxFactor = parallaxFactor;
        this.cameraManager = cameraManager;
        this.repeat = repeat;
        this.repetitionAmount = repetitionAmount;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public void render(SpriteBatch batch) {
        float regionWidth = atlasRegion.getRegionWidth();
        float regionHeigth = atlasRegion.getRegionHeight();
        float positionX;

        if (parallaxFactor != 0) {
            positionX = (cameraManager.getCamera().position.x
                    - cameraManager.getCamera().viewportWidth / 2) * parallaxFactor + this.positionX;
        } else {
            positionX = 0;
        }
        if (repeat) {
            for (int i = 0; i < repetitionAmount; i++) {
                batch.draw(atlasRegion, positionX, positionY, regionWidth, regionHeigth);
                positionX = positionX + regionWidth;
            }
        } else {
            batch.draw(atlasRegion, positionX, positionY, regionWidth, regionHeigth);
        }
    }
}
