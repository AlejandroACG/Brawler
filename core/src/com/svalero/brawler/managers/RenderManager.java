package com.svalero.brawler.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.svalero.brawler.Brawler;
import com.svalero.brawler.domains.characters.Character;
import com.svalero.brawler.domains.characters.Enemy;
import com.svalero.brawler.domains.projectiles.Projectile;
import com.svalero.brawler.utils.ParallaxLayer;
import static com.svalero.brawler.utils.Constants.*;

public class RenderManager {
    private SpriteBatch batch;
    private BitmapFont font;
    private OrthogonalTiledMapRenderer mapRenderer;
    private LevelManager levelManager;
    private CameraManager cameraManager;
    private TextureRegion healthGlobeBackground;
    private TextureRegion healthGlobeBorder;
    private TextureRegion healthGloBeFill;
    private Brawler game;

    public RenderManager(LevelManager levelManager, CameraManager cameraManager, Brawler game) {
        this.levelManager = levelManager;
        this.cameraManager = cameraManager;
        this.game = game;
        batch = new SpriteBatch();
        font = game.getSkin().getFont(AETHERIUS_FONT);
        font.getData().setScale(1.5f);
        if (levelManager.getCurrentLevel() == 1) {
            font.setColor(Color.BLACK);
        } else if (levelManager.getCurrentLevel() == 2) {
            font.setColor(Color.WHITE);
        }
        font.setUseIntegerPositions(false);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);


        mapRenderer = new OrthogonalTiledMapRenderer(levelManager.getMap());

        TextureAtlas uiAtlas = ResourceManager.getAtlas(UI_ATLAS);
        healthGlobeBackground = uiAtlas.findRegion(HEALTH_GLOBE_BACKGROUND);
        healthGlobeBorder = uiAtlas.findRegion(HEALTH_GLOBE_BORDER);
        healthGloBeFill = uiAtlas.findRegion(HEALTH_GLOBE_FILL);
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

        if (!levelManager.getProjectiles().isEmpty()) {
            for (Projectile projectile : levelManager.getProjectiles()) {
                projectile.render(batch);
            }
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
        float healthPercent = levelManager.getPlayer().getCurrentHealth() / (float) levelManager.getPlayer().getMaxHealth();
        renderHealthGlobe(healthPercent);

        font.draw(batch, "Level: " + levelManager.getCurrentLevel()
                + "\nScore: " + levelManager.getCurrentScore(), 210, Gdx.graphics.getHeight() - 60);
    }

    private void renderHealthGlobe(float healthPercent) {
        float globeX = 20;
        float globeY = Gdx.graphics.getHeight() - 190;
        float globeSize = 170;

        batch.draw(healthGlobeBackground, globeX, globeY, globeSize, globeSize);

        int globeTextureHeight = healthGloBeFill.getRegionHeight();
        int currentHeight = (int) (globeTextureHeight * healthPercent);

        TextureRegion healthGlobeFillCropped = new TextureRegion(healthGloBeFill, 0, globeTextureHeight - currentHeight,
                healthGloBeFill.getRegionWidth(), currentHeight);
        batch.draw(healthGlobeFillCropped, globeX, globeY, globeSize, globeSize * healthPercent);

        batch.draw(healthGlobeBorder, globeX, globeY, globeSize, globeSize);
    }

    public void dispose() {
        batch.dispose();
    }
}
