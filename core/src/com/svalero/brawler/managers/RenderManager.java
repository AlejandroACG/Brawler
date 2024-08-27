package com.svalero.brawler.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.svalero.brawler.domains.Character;
import com.svalero.brawler.domains.Enemy;
import com.svalero.brawler.utils.ParallaxLayer;

public class RenderManager {
    private SpriteBatch batch;
    private BitmapFont font;
    private OrthogonalTiledMapRenderer mapRenderer;
    private LevelManager levelManager;
    private CameraManager cameraManager;

    public RenderManager(LevelManager levelManager, CameraManager cameraManager) {
        this.levelManager = levelManager;
        this.cameraManager = cameraManager;
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2);
        mapRenderer = new OrthogonalTiledMapRenderer(levelManager.getMap());
    }

    public void drawFrame() {
        batch.setProjectionMatrix(cameraManager.getCamera().combined);

        drawBackground();

        mapRenderer.setView(cameraManager.getCamera());
        mapRenderer.render();

        batch.begin();
        for (Enemy enemy : levelManager.getEnemies().values()) {
            enemy.draw(batch);
        }
        levelManager.getPlayer().draw(batch);
        for (Character character : levelManager.getCharacters().values()) {
            character.getEffectManager().drawEffects(batch);
        }
        batch.end();

        batch.setProjectionMatrix(cameraManager.getUICamera().combined);
        batch.begin();
        renderUI(levelManager);
        batch.end();
    }

    public void drawBackground() {
        batch.begin();
        for (ParallaxLayer parallaxLayer : levelManager.getParallaxLayers()) {
            parallaxLayer.render(batch);
        }
        batch.end();
    }

    private void renderUI(LevelManager levelManager) {
        font.getData().setScale(1);
        font.draw(batch, "Health: " + levelManager.getPlayer().getHealth(), 20, Gdx.graphics.getHeight() - 20);
        font.draw(batch, "Level: " + levelManager.getCurrentLevel(), 20, Gdx.graphics.getHeight() - 50);
        font.draw(batch, "Score: " + levelManager.getCurrentScore(), 20, Gdx.graphics.getHeight() - 80);
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
