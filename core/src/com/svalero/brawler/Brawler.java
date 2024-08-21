package com.svalero.brawler;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.svalero.brawler.managers.ResourceManager;
import com.svalero.brawler.screens.SplashScreen;
import static com.svalero.brawler.utils.Constants.UI_SKIN_ATLAS;
import static com.svalero.brawler.utils.Constants.UI_SKIN_JSON;

public class Brawler extends Game {
	private Skin skin;

	@Override
	public void create () {
//		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		skin = new Skin (Gdx.files.internal(UI_SKIN_JSON), new TextureAtlas(Gdx.files.internal(UI_SKIN_ATLAS)));
		((Game) Gdx.app.getApplicationListener()).setScreen(new SplashScreen(this));
	}

	public Skin getSkin() {
		if (skin == null) skin = new Skin(Gdx.files.internal(UI_SKIN_JSON), new TextureAtlas(Gdx.files.internal(UI_SKIN_ATLAS)));
		return skin;
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		skin.dispose();
		ResourceManager.dispose();
		super.dispose();
	}
}
