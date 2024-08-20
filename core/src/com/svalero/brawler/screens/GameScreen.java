package com.svalero.brawler.screens;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.svalero.brawler.Brawler;
import com.svalero.brawler.domains.Character;
import com.svalero.brawler.managers.*;

import java.util.Map;

import static com.svalero.brawler.managers.ConfigurationManager.SelectedCharacter.*;

public class GameScreen implements Screen {
    private CameraManager cameraManager;
    private final Brawler game;
    private ActionManager actionManager;
    private LevelManager levelManager;
    private RenderManager renderManager;
    private int currentLevel;
    private Box2DDebugRenderer debugRenderer;

    public GameScreen(Brawler game, int currentLevel) {
        this.game = game;
        this.currentLevel = currentLevel;
        levelManager = new LevelManager(game, currentLevel, KAIN);

        cameraManager = new CameraManager(levelManager);
        levelManager.setCameraManager(cameraManager);
        levelManager.setBackground();
        actionManager = new ActionManager(levelManager);
        renderManager = new RenderManager(levelManager, cameraManager);

        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void show() {}

    @Override
    public void render(float dt) {
        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        actionManager.update(dt);
        cameraManager.handleCamera();
        renderManager.drawFrame();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
        }

        debugRenderer.render(levelManager.getWorld(), cameraManager.getCamera().combined.cpy().scl(1f));
        System.out.println(levelManager.getPlayer().getCurrentState());
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        renderManager.getBatch().dispose();
        for (Map.Entry<Integer, Character> character : levelManager.getCharacters().entrySet()) {
            character.getValue().dispose();
        }
        levelManager.getWorld().dispose();

        debugRenderer.dispose();
    }
}
