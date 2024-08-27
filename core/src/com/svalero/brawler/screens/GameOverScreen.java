package com.svalero.brawler.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.svalero.brawler.Brawler;
import com.svalero.brawler.managers.MusicManager;
import com.svalero.brawler.managers.ResourceManager;
import static com.svalero.brawler.utils.Constants.*;

public class GameOverScreen implements Screen {
    private final Brawler game;
    private Stage stage;
    private int currentLevel;
    private int initialScore;
    private int currentScore;

    public GameOverScreen(Brawler game, int currentLevel, int initialScore, int currentScore) {
        this.game = game;
        this.currentLevel = currentLevel;
        this.initialScore = initialScore;
        this.currentScore = currentScore;
    }

    @Override
    public void show() {
        stage = new Stage();

        Texture backgroundTexture = ResourceManager.getTexture(MAIN_MENU_BACKGROUND);
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        Table table = new Table(game.getSkin());
        table.setBackground(ResourceManager.getTableBackground());

        Label gameOverLabel = new Label("GAME OVER", game.getSkin());
        gameOverLabel.setAlignment(Align.center);
        Label scoreLabel = new Label(String.valueOf(currentScore), game.getSkin(), "red-label");
        scoreLabel.setAlignment(Align.center);

        TextButton retryButton = new TextButton("RETRY", game.getSkin());
        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                MusicManager.stopMusic();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(game, currentLevel, initialScore));
            }
        });

        TextButton mainMenuButton = new TextButton("GIVE UP", game.getSkin());
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                MusicManager.stopMusic();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
            }
        });

        float buttonWidth = stage.getWidth() * buttonWidthRatio;
        float buttonHeight = stage.getHeight() * buttonHeightRatio;

        table.add().center().width(buttonWidth).height(buttonHeight).pad(5).row();
        table.add(gameOverLabel).center().width(buttonWidth).height(buttonHeight).pad(5).row();
        table.add(scoreLabel).center().width(buttonWidth).height(buttonHeight).pad(5).row();
        table.add(retryButton).center().width(buttonWidth).height(buttonHeight).pad(5).row();
        table.add(mainMenuButton).center().width(buttonWidth).height(buttonHeight).pad(5).row();
        table.add().center().width(buttonWidth).height(buttonHeight).pad(5).row();

        table.pad(100);
        table.pack();
        float x = (stage.getWidth() - table.getWidth()) / 2;
        float y = (stage.getHeight() - table.getHeight()) / 2;
        table.setPosition(x, y);

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
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
