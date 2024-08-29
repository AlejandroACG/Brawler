package com.svalero.brawler.screens;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.svalero.brawler.Brawler;
import com.svalero.brawler.domains.characters.Character;
import com.svalero.brawler.managers.*;
import java.util.Map;
import static com.svalero.brawler.managers.ConfigurationManager.SelectedCharacter.*;
import static com.svalero.brawler.utils.Constants.DEBUG_MODE;

public class GameScreen implements Screen {
    private CameraManager cameraManager;
    private final Brawler game;
    private ActionManager actionManager;
    private LevelManager levelManager;
    private RenderManager renderManager;
    private int currentLevel;
    private int initialScore = 0;
    private Box2DDebugRenderer debugRenderer;

    public GameScreen(Brawler game, int currentLevel, int initialScore) {
        this.game = game;
        this.currentLevel = currentLevel;
        levelManager = new LevelManager(game, currentLevel, KAIN, initialScore);
        cameraManager = new CameraManager(levelManager);
        levelManager.setCameraManager(cameraManager);
        levelManager.setBackground();
        actionManager = new ActionManager(levelManager);
        renderManager = new RenderManager(levelManager, cameraManager, game);
        if (DEBUG_MODE) {
            debugRenderer = new Box2DDebugRenderer();
        }
    }

    @Override
    public void render(float dt) {
        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        levelManager.checkDefeatCondition(dt);
        levelManager.checkVictoryCondition(dt);
        actionManager.update(dt);
        cameraManager.handleCamera();
        renderManager.drawFrame();
        levelManager.cleanUpBodies();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            MusicManager.stopMusic();
            SoundManager.stopAllLongSounds();
            Gdx.app.postRunnable(() -> ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game)));
        }

        if (DEBUG_MODE) {
            debugRenderer.render(levelManager.getWorld(), cameraManager.getCamera().combined.cpy().scl(1f));
            System.out.println(levelManager.getPlayer().getCurrentState());
        }
    }

    @Override
    public void dispose() {
        renderManager.dispose();
        for (Map.Entry<Integer, Character> character : levelManager.getCharacters().entrySet()) {
            character.getValue().dispose();
        }
        levelManager.getWorld().dispose();

        if (DEBUG_MODE) {
            debugRenderer.dispose();
        }
    }

    @Override
    public void show() {}

    @Override
    public void resize(int i, int i1) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}
}
