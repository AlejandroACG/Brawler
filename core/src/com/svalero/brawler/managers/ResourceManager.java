package com.svalero.brawler.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import static com.svalero.brawler.utils.Constants.*;
import static com.svalero.brawler.utils.Constants.HSIEN_KO_ATTACK_SOUND;

public class ResourceManager {
    public static AssetManager assets = new AssetManager();

    public static void loadAllResources() {
        // Cargar sonidos
        assets.load(KAIN_ATTACK_SOUND, Sound.class);
        assets.load(KAIN_GRUNT_SOUND, Sound.class);
        assets.load(KAIN_BLOCK_MOVE_SOUND, Sound.class);
        assets.load(KAIN_HIT_SOUND, Sound.class);
        assets.load(KAIN_DEAD_SOUND, Sound.class);
        assets.load(KAIN_VICTORY_SOUND, Sound.class);
        assets.load(BISHAMON_ATTACK_SOUND, Sound.class);
        assets.load(BISHAMON_HIT_SOUND, Sound.class);
        assets.load(BISHAMON_DEAD_SOUND, Sound.class);
        assets.load(BISHAMON_VICTORY_SOUND, Sound.class);
        assets.load(HSIEN_KO_ATTACK_SOUND, Sound.class);
        assets.load(HSIEN_KO_HIT_SOUND, Sound.class);
        assets.load(HSIEN_KO_DEAD_SOUND, Sound.class);
        assets.load(HSIEN_KO_VICTORY_SOUND, Sound.class);
        assets.load(DEATH_ADDER_ATTACK_SOUND, Sound.class);
        assets.load(DEATH_ADDER_HIT_SOUND, Sound.class);
        assets.load(DEATH_ADDER_DEAD_SOUND, Sound.class);
        assets.load(DEATH_ADDER_VICTORY_SOUND, Sound.class);
        assets.load(HIT_SOUND, Sound.class);
        assets.load(LAND_SOUND, Sound.class);
        assets.load(WALKING_ON_GRASS_SOUND, Sound.class);
        assets.load(RUNNING_ON_GRASS_SOUND, Sound.class);

        // Cargar música
        assets.load(MENU_MUSIC, Music.class);
        assets.load(LEVEL_1_MUSIC, Music.class);
        assets.load(GAME_OVER_MUSIC, Music.class);

        // Cargar texturas
        assets.load(MAIN_MENU_BACKGROUND, Texture.class);
        assets.load(TABLE_BACKGROUND, Texture.class);
        assets.load(LEVEL_1_BACKGROUND, TextureAtlas.class);
        assets.load(KAIN_ATLAS, TextureAtlas.class);
        assets.load(BISHAMON_ATLAS, TextureAtlas.class);
        assets.load(HSIEN_KO_ATLAS, TextureAtlas.class);
        assets.load(DEATH_ADDER_ATLAS, TextureAtlas.class);
        assets.load(EFFECTS_ATLAS, TextureAtlas.class);
    }

    public static void finishLoading() {
        assets.finishLoading();
    }

    public static boolean update() { return assets.update(); }

    public static Texture getTexture(String path) {
        return assets.get(path, Texture.class);
    }

    public static Drawable getTableBackground() { return new TextureRegionDrawable(new TextureRegion(getTexture(TABLE_BACKGROUND))); }

    public static TextureAtlas getAtlas(String path) {
        return assets.get(path, TextureAtlas.class);
    }

    public static Sound getSound(String path) { return assets.get(path, Sound.class); }

    public static Music getMusic(String path) { return assets.get(path, Music.class); }

    public static void dispose() {
        assets.dispose();
    }
}
