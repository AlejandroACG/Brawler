package com.svalero.brawler.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.svalero.brawler.Brawler;
import com.svalero.brawler.managers.ResourceManager;
import static com.svalero.brawler.utils.Constants.*;

public class TutorialScreen implements Screen {
    private final Brawler game;
    private Stage stage;

    public TutorialScreen(Brawler game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();

        Texture backgroundTexture = ResourceManager.getTexture(MAIN_MENU_BACKGROUND);
        Image backgroundImage = new Image(backgroundTexture);
        stage.addActor(backgroundImage);

        Texture tutorialTexture = ResourceManager.getTexture(TUTORIAL_PICTURE);
        Image tutorialImage = new Image(tutorialTexture);

        Table table = new Table(game.getSkin());
        table.setFillParent(true);

        TextButton.TextButtonStyle backButtonStyle = new TextButton.TextButtonStyle(game.getSkin().get(TextButton.TextButtonStyle.class));
        backButtonStyle.font = new BitmapFont(backButtonStyle.font.getData().fontFile); // Crea una nueva instancia de la fuente
        backButtonStyle.font.getData().setScale(1.5f);
        TextButton backButton = new TextButton("BACK", backButtonStyle);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
            }
        });

        float buttonWidth = stage.getWidth() * buttonWidthRatio;
        float buttonHeight = stage.getHeight() * buttonHeightRatio;

        table.add(tutorialImage).center().width(tutorialImage.getWidth() / 1.2f)
                .height(tutorialImage.getHeight() / 1.2f).padBottom(5).row();
        table.add(backButton).center().width(buttonWidth).height(buttonHeight).pad(10).row();

        stage.addActor(table);

        stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1f),
                Actions.run(() -> Gdx.input.setInputProcessor(stage))
        ));
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(dt);
        stage.draw();
    }

    @Override
    public void resize(int w, int h) {
        stage.getViewport().update(w, h, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
