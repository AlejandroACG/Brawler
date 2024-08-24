package com.svalero.brawler.managers;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.svalero.brawler.domains.Enemy;
import com.svalero.brawler.utils.ParallaxLayer;

public class RenderManager {
    private SpriteBatch batch;
    private OrthogonalTiledMapRenderer mapRenderer;
    private LevelManager levelManager;
    private CameraManager cameraManager;

    public RenderManager(LevelManager levelManager, CameraManager cameraManager) {
        batch = new SpriteBatch();
        this.levelManager = levelManager;
        mapRenderer = new OrthogonalTiledMapRenderer(levelManager.getMap());
        this.cameraManager = cameraManager;

    }

    public void drawFrame() {
        batch.setProjectionMatrix(cameraManager.getCamera().combined);

        drawBackground();

        mapRenderer.setView(cameraManager.getCamera());
        mapRenderer.render();

        batch.begin();
        levelManager.getPlayer().draw(batch);
        for (Enemy enemy : levelManager.getEnemies().values()) {
            enemy.draw(batch);
        }
        batch.end();
    }

    public void drawBackground() {
        batch.begin();
        for (ParallaxLayer parallaxLayer : levelManager.getParallaxLayers()) {
            parallaxLayer.render(batch);
        }
        batch.end();
    }

    public SpriteBatch getBatch() {
        return batch;
    }
}
