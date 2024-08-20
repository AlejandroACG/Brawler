package com.svalero.brawler.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.svalero.brawler.Brawler;
import com.svalero.brawler.managers.MusicManager;
import com.svalero.brawler.managers.ResourceManager;

import static com.svalero.brawler.utils.Constants.MENU_MUSIC;

public class SplashScreen implements Screen {
    private Texture splashTexture;
    private Image splashImage;
    private Stage stage;
    private boolean splashDone = false;
    private final Brawler game;

    public SplashScreen(Brawler game) {
        this.game = game;
    }

    @Override
    public void show() {
        splashTexture = new Texture(Gdx.files.internal("ui/splash.png"));
        splashImage = new Image(splashTexture);
        stage = new Stage(new FitViewport(1920, 1080));

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        table.row().height(splashTexture.getHeight());
        table.add(splashImage).center();
        Label aboutLabel = new Label("by Alejandro Asterio Cristóbal Garcés", game.getSkin(), "system");
        aboutLabel.setWrap(true);
        aboutLabel.setAlignment(Align.center);
        table.row();
        table.add(aboutLabel).center().expandX().fillX().pad(5);
        stage.addActor(table);
        stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(2f), Actions.delay(2f),
                Actions.run(() -> stage.addAction(Actions.sequence(Actions.fadeOut(2f), Actions.delay(0f),
                                Actions.run(() -> splashDone = true )
                        ))
                )
        ));
        MusicManager.startMusic(MENU_MUSIC);
        ResourceManager.loadAllResources();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();

        if (ResourceManager.update()) {
            if (splashDone) {
                game.setScreen(new MainMenuScreen(game));
            }
        }
    }

    @Override
    public void resize(int w, int h) {
        stage.getViewport().update(w, h, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        splashTexture.dispose();
        stage.dispose();
    }
}
