package com.svalero.brawler.utils;

public class Constants {
    public static final float ASPECT_RATIO = 16f / 9f;
    public static final float GRAVITY = -175f;

    // Buttons
    public static final float buttonWidthRatio = 0.4f;
    public static final float buttonHeightRatio = 0.05f;

    // Colliders
    public static final short COLLIDER_CATEGORY_PLAYER = 0x0001;
    public static final short COLLIDER_CATEGORY_ENEMY = 0x0002;
    public static final short COLLIDER_CATEGORY_GROUND = 0x0003;
    public static final short COLLIDER_CATEGORY_BORDER = 0x0004;

    // Resource Manager
    public static final String UI_SKIN_JSON = "ui/uiskin.json";
    public static final String UI_SKIN_ATLAS = "ui/uiskin.atlas";
    public static final String MAIN_MENU_BACKGROUND = "textures/main-menu.png";
    public static final String TABLE_BACKGROUND = "textures/table-background.png";
    public static final String LEVEL_1_BACKGROUND = "textures/level-1-background.atlas";
    public static final String KAIN_ATLAS = "textures/kain.atlas";

    // Music and Sounds
    public static final String MENU_MUSIC = "music/menu-music.mp3";

    // Level 1
    public static final String LEVEL_1_MAP = "maps/level-1.tmx";
    public static final float LEVEL_1_MAP_WIDTH = 960f;
    public static final float LEVEL_1_MAP_HEIGHT = 272f;
    public static final float LEVEL_1_PARALLAX_FACTOR_1 = -0.075f;
    public static final float LEVEL_1_PARALLAX_FACTOR_2 = 0.5f;
    public static final float LEVEL_1_PARALLAX_FACTOR_3 = 0.8f;
    public static final float LEVEL_1_PARALLAX_FACTOR_4 = 1f;

    // Kain
    public static final float KAIN_SCALE = 0.5f;
    public static final float KAIN_WIDTH = 69f;
    public static final float KAIN_HEIGHT = 101f;
    public static final float KAIN_SPRITE_WIDTH = 297f;
    public static final float KAIN_SPRITE_HEIGHT = 186f;
    public static final float KAIN_DRAW_CORRECTION_X = 83f;
    public static final float KAIN_DRAW_CORRECTION_Y = 41f;
    public static final float KAIN_SPEED = 70f;
    public static final float KAIN_JUMP_STRENGTH = 10000000000000f;
    public static final float KAIN_IDLE_DURATION = 0.15f;
    public static final int KAIN_CROUCH_FRAMES = 2;
    public static final float KAIN_CROUCH_DURATION = 0.1f;
    public static final float KAIN_JUMP_UP_DURATION = 0.5f;
    public static final float KAIN_WALK_DURATION = 0.15f;
    public static final int KAIN_LAND_FRAMES = 2;
    public static final float KAIN_LAND_DURATION = 0.1f;
    public static final float KAIN_JUMP_DOWN_DURATION = 0.15f;
}
