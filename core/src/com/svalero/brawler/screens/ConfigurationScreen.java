package com.svalero.brawler.screens;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.svalero.brawler.Brawler;
import com.svalero.brawler.managers.ConfigurationManager;
import com.svalero.brawler.managers.MusicManager;
import com.svalero.brawler.managers.ResourceManager;

import static com.svalero.brawler.utils.Constants.*;

public class ConfigurationScreen implements Screen {
    private final Brawler game;
    private Stage stage;

    public ConfigurationScreen(Brawler game) { this.game = game; }

    @Override
    public void show() {
        stage = new Stage();

        Texture backgroundTexture = ResourceManager.getTexture(MAIN_MENU_BACKGROUND);
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        Table table = new Table(game.getSkin());
        table.setBackground(ResourceManager.getTableBackground());

        CheckBox muteCheckBox = new CheckBox("MUTE", game.getSkin());
        if (ConfigurationManager.mute) {
            muteCheckBox.setChecked(true);
        }
        muteCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ConfigurationManager.switchMute();
                if (ConfigurationManager.mute) {
                    MusicManager.stopMusic();
                } else {
                    MusicManager.startMusic(MENU_MUSIC);
                }
                System.out.println(ConfigurationManager.mute);
            }
        });

        CheckBox hardCheckBox = new CheckBox("HARD", game.getSkin());
        if (ConfigurationManager.hard) {
            hardCheckBox.setChecked(true);
        }
        hardCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ConfigurationManager.switchHard();
                System.out.println(ConfigurationManager.hard);
            }
        });

        TextButton returnButton = new TextButton("RETURN", game.getSkin());
        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
            }
        });

        float buttonWidth = stage.getWidth() * buttonWidthRatio;
        float buttonHeight = stage.getHeight() * buttonHeightRatio;

        table.add().center().width(buttonWidth).height(buttonHeight).pad(5).row();
        table.add(muteCheckBox).center().width(buttonWidth).height(buttonHeight).pad(5).row();
        table.add().center().width(buttonWidth).height(buttonHeight).pad(5).row();
        table.add(hardCheckBox).center().width(buttonWidth).height(buttonHeight).pad(5).row();
        table.add().center().width(buttonWidth).height(buttonHeight).pad(5).row();
        table.add(returnButton).center().width(buttonWidth).height(buttonHeight).pad(5).row();
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

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
