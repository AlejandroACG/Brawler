package com.svalero.brawler.utils;

public class Constants {
    public static final float ASPECT_RATIO = 16f / 9f;
    public static final float GRAVITY = -175f;
    public static final boolean DEBUG_MODE = true;
    public static final long DOUBLE_CLICK_THRESHOLD = 300;
    public static final float WALKING_SOUND_TIMER = 0.20f;
    public static final float RUNNING_SOUND_TIMER = 0.20f;

    // Buttons
    public static final float buttonWidthRatio = 0.4f;
    public static final float buttonHeightRatio = 0.05f;

    // Colliders
    public static final short COLLIDER_CATEGORY_PLAYER = 0x0001;
    public static final short COLLIDER_CATEGORY_ENEMY = 0x0002;
    public static final short COLLIDER_CATEGORY_GROUND = 0x0004;
    public static final short COLLIDER_CATEGORY_BORDER = 0x0008;
    public static final short COLLIDER_CATEGORY_ATTACK_PLAYER = 0X0016;
    public static final short COLLIDER_CATEGORY_ATTACK_ENEMY = 0X0032;

    // Resource Manager
    public static final String UI_SKIN_JSON = "ui/uiskin.json";
    public static final String UI_SKIN_ATLAS = "ui/uiskin.atlas";
    public static final String MAIN_MENU_BACKGROUND = "textures/main-menu.png";
    public static final String TABLE_BACKGROUND = "textures/table-background.png";
    public static final String LEVEL_1_BACKGROUND = "textures/level-1-background.atlas";
    public static final String KAIN_ATLAS = "textures/kain.atlas";
    public static final String BISHAMON_ATLAS = "textures/bishamon.atlas";
    public static final String HSIEN_KO_ATLAS = "textures/hsien-ko.atlas";
    public static final String DEATH_ADDER_ATLAS = "textures/death-adder.atlas";
    public static final String EFFECTS_ATLAS = "textures/effects.atlas";

    // Sounds
    public static final String KAIN_ATTACK_SOUND = "sounds/kain-attack.mp3";
    public static final String KAIN_GRUNT_SOUND = "sounds/kain-grunt.wav";
    public static final String KAIN_BLOCK_MOVE_SOUND = "sounds/kain-block-prep.wav";
    public static final String KAIN_HIT_SOUND = "sounds/kain-hit.mp3";
    public static final String KAIN_DEAD_SOUND = "sounds/kain-dead.mp3";
    public static final String BISHAMON_ATTACK_SOUND = "sounds/bishamon-attack.wav";
    public static final String BISHAMON_HIT_SOUND = "sounds/bishamon-hit.wav";
    public static final String BISHAMON_DEAD_SOUND = "sounds/bishamon-dead.wav";
    public static final String HSIEN_KO_ATTACK_SOUND = "sounds/hsien-ko-attack.wav";
    public static final String HSIEN_KO_HIT_SOUND = "sounds/hsien-ko-hit.wav";
    public static final String HSIEN_KO_DEAD_SOUND = "sounds/hsien-ko-dead.wav";
    public static final String DEATH_ADDER_ATTACK_SOUND = "sounds/death-adder-attack.wav";
    public static final String DEATH_ADDER_HIT_SOUND = "sounds/death-adder-hit.wav";
    public static final String DEATH_ADDER_DEAD_SOUND = "sounds/death-adder-dead.wav";
    public static final String HIT_SOUND = "sounds/hit.mp3";
    public static final String LAND_SOUND = "sounds/land.mp3";
    public static final String WALKING_ON_GRASS_SOUND = "sounds/walking-on-grass.wav";
    public static final String RUNNING_ON_GRASS_SOUND = "sounds/running-on-grass.mp3";

    // Music
    public static final String MENU_MUSIC = "music/menu-music.mp3";
    public static final String LEVEL_1_MUSIC = "music/level-1-music.mp3";

    // Chance
    public static final float TURN_CHANCE = 0.5f;
    public static final float TURN_CHANCE_HARD = 0.9f;
    public static final float WALK_CHANCE = 0.45f;
    public static final float WALK_CHANCE_HARD = 0.9f;
    public static final float STOP_CHANCE = 0.2f;
    public static final float STOP_CHANCE_HARD = 0.9f;
    public static final float ATTACK_CHANCE = 0.8f;
    public static final float ATTACK_CHANCE_HARD = 0.9f;

    // Effects
    // TODO Podría prescindir de pasar tantos datos de los frames entre los dominios y hacer como ya hago aquí
    public static final String BLOOD_SMALL = "blood_small";
    public static final int BLOOD_SMALL_FRAMES = 10;
    public static final float BLOOD_SMALL_DURATION = 0.2f;
    public static final float BLOOD_SMALL_WIDTH = 37;
    public static final float BLOOD_SMALL_HEIGHT = 35;
    public static final String BLOOD_BIG = "blood_big";
    public static final int BLOOD_BIG_FRAMES = 8;
    public static final float BLOOD_BIG_SCALE = 0.7f;
    public static final float BLOOD_BIG_DURATION = 0.2f;
    public static final float BLOOD_BIG_WIDTH = 122 * BLOOD_BIG_SCALE;
    public static final float BLOOD_BIG_HEIGHT = 109 * BLOOD_BIG_SCALE;
    public static final String BLOCK = "block";
    public static final int BLOCK_FRAMES = 4;
    public static final float BLOCK_DURATION = 0.2f;
    public static final float BLOCK_WIDTH = 117;
    public static final float BLOCK_HEIGHT = 114;

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
    public static final float KAIN_WIDTH = 67f * KAIN_SCALE;
    public static final float KAIN_HEIGHT = 99f * KAIN_SCALE;
    public static final float KAIN_FRAME_WIDTH = 256f * KAIN_SCALE;
    public static final float KAIN_FRAME_HEIGHT = 186f * KAIN_SCALE;
    public static final float KAIN_DRAW_CORRECTION_X = (93f * KAIN_SCALE) + KAIN_WIDTH / 2;
    public static final float KAIN_DRAW_CORRECTION_Y = (31f * KAIN_SCALE) + KAIN_HEIGHT / 2;
    public static final float KAIN_SPEED = 70f;
    public static final float KAIN_JUMP_STRENGTH = 2000f;
    public static final int KAIN_HEALTH = 2000;
    public static final int KAIN_HEALTH_HARD = 1000;
    public static final int KAIN_ATTACK_STRENGTH = 250;
    public static final float KAIN_IDLE_DURATION = 0.15f;
    public static final int KAIN_TURN_FRAMES = 3;
    public static final float KAIN_TURN_DURATION = 0.1f;
    public static final float KAIN_WALK_DURATION = 0.15f;
    public static final float KAIN_RUN_DURATION = 0.1f;
    public static final float KAIN_BLOCK_DURATION = 0.1f;
    public static final int KAIN_BLOCK_FRAMES = 2;
    public static final int KAIN_CROUCH_FRAMES = 2;
    public static final float KAIN_CROUCH_DURATION = 0.1f;
    public static final float KAIN_JUMP_UP_DURATION = 0.1f;
    public static final float KAIN_JUMP_DOWN_DURATION = 0.1f;
    public static final int KAIN_LAND_FRAMES = 2;
    public static final float KAIN_LAND_DURATION = 0.1f;
    public static final int KAIN_ATTACK_FRAMES = 8;
    public static final float KAIN_ATTACK_DURATION = 0.1f;
    public static final float KAIN_ATTACK_WIDTH = 96f * KAIN_SCALE;
    public static final float KAIN_ATTACK_HEIGHT = 27f * KAIN_SCALE;
    public static final float KAIN_ATTACK_OFFSET_X = KAIN_WIDTH / 2 + KAIN_ATTACK_WIDTH / 2;
    public static final float KAIN_ATTACK_OFFSET_Y = KAIN_ATTACK_HEIGHT / 2;
    public static final int KAIN_JUMP_ATTACK_FRAMES = 8;
    public static final float KAIN_JUMP_ATTACK_DURATION = 0.1f;
    public static final float KAIN_JUMP_ATTACK_WIDTH = 57f * KAIN_SCALE;
    public static final float KAIN_JUMP_ATTACK_HEIGHT = 37f * KAIN_SCALE;
    public static final float KAIN_JUMP_ATTACK_OFFSET_X = KAIN_WIDTH / 2 + KAIN_JUMP_ATTACK_WIDTH / 2;
    public static final float KAIN_JUMP_ATTACK_OFFSET_Y = -(KAIN_JUMP_ATTACK_HEIGHT / 2) + (9 * KAIN_SCALE);
    public static final int KAIN_HIT_FRAMES = 3;
    public static final float KAIN_HIT_DURATION = 0.16f;
    public static final int KAIN_DEAD_FRAMES = 6;
    public static final float KAIN_DEAD_DURATION = 0.2f;
    public static final String KAIN_IDLE = "kain_idle";
    public static final String KAIN_TURN = "kain_turn";
    public static final String KAIN_WALK = "kain_walk";
    public static final String KAIN_RUN = "kain_run";
    public static final String KAIN_BLOCK_UP = "kain_block_up";
    public static final String KAIN_BLOCK_DOWN = "kain_block_down";
    public static final String KAIN_CROUCH_DOWN = "kain_crouch_down";
    public static final String KAIN_CROUCH_UP = "kain_crouch_up";
    public static final String KAIN_JUMP_UP = "kain_jump_up";
    public static final String KAIN_JUMP_DOWN = "kain_jump_down";
    public static final String KAIN_LAND = "kain_land";
    public static final String KAIN_ATTACK = "kain_attack";
    public static final String KAIN_JUMP_ATTACK = "kain_jump_attack";
    public static final String KAIN_HIT = "kain_hit";
    public static final String KAIN_DEAD = "kain_knockdown";

    // Bishamon
    public static final float BISHAMON_SCALE = 0.55f;
    public static final float BISHAMON_WIDTH = 92f * BISHAMON_SCALE;
    public static final float BISHAMON_HEIGHT = 98f * BISHAMON_SCALE;
    public static final float BISHAMON_FRAME_WIDTH = 306f * BISHAMON_SCALE;
    public static final float BISHAMON_FRAME_HEIGHT = 157f * BISHAMON_SCALE;
    public static final float BISHAMON_DRAW_CORRECTION_X =  (34f * BISHAMON_SCALE) + BISHAMON_WIDTH / 2;;
    public static final float BISHAMON_DRAW_CORRECTION_Y = (16f * BISHAMON_SCALE) + BISHAMON_HEIGHT / 2;
    public static final float BISHAMON_SPEED = 70f;
    public static final int BISHAMON_HEALTH = 1000;
    public static final int BISHAMON_HEALTH_HARD = 2000;
    public static final int BISHAMON_ATTACK_STRENGTH = 250;
    public static final int BISHAMON_ATTACK_STRENGTH_HARD = 500;
    public static final float BISHAMON_IDLE_DURATION = 0.15f;
    public static final float BISHAMON_WALK_DURATION = 0.15f;
    public static final int BISHAMON_HIT_FRAMES = 3;
    public static final float BISHAMON_HIT_DURATION = 0.16f;
    public static final int BISHAMON_DEAD_FRAMES = 9;
    public static final float BISHAMON_DEAD_DURATION = 0.1f;
    public static final int BISHAMON_TURN_FRAMES = 3;
    public static final float BISHAMON_TURN_DURATION = 0.1f;
    public static final int BISHAMON_ATTACK_FRAMES = 7;
    public static final float BISHAMON_ATTACK_DURATION = 0.1f;
    public static final float BISHAMON_ATTACK_WIDTH = 91f * BISHAMON_SCALE;
    public static final float BISHAMON_ATTACK_HEIGHT = 104f * BISHAMON_SCALE;
    public static final float BISHAMON_ATTACK_OFFSET_X = (BISHAMON_WIDTH / 2 + BISHAMON_ATTACK_WIDTH / 2);
    public static final float BISHAMON_ATTACK_OFFSET_Y = (BISHAMON_ATTACK_HEIGHT / 2) - 39 * BISHAMON_SCALE;
//    public static final int KAIN_JUMP_ATTACK_FRAMES = 8;
//    public static final float KAIN_JUMP_ATTACK_DURATION = 0.1f;
//    public static final float KAIN_JUMP_ATTACK_WIDTH = 58f;
//    public static final float KAIN_JUMP_ATTACK_HEIGHT = 39f;
//    public static final float KAIN_JUMP_ATTACK_OFFSET_X = (KAIN_WIDTH / 2 + KAIN_JUMP_ATTACK_WIDTH / 2);
//    public static final float KAIN_JUMP_ATTACK_OFFSET_Y = (-(KAIN_JUMP_ATTACK_HEIGHT / 2) + 9);
    public static final String BISHAMON_IDLE = "bishamon_idle";
    public static final String BISHAMON_TURN = "bishamon_turn";
    public static final String BISHAMON_WALK = "bishamon_walk";
    public static final String BISHAMON_ATTACK = "bishamon_attack";
//    public static final String KAIN_JUMP_ATTACK = "kain_jump_attack";
    public static final String BISHAMON_HIT = "bishamon_hit";
    public static final String BISHAMON_DEAD = "bishamon_knockdown";

    // Hsien Ko
    public static final float HSIEN_KO_SCALE = 0.4f;
    public static final float HSIEN_KO_WIDTH = 57f * HSIEN_KO_SCALE;
    public static final float HSIEN_KO_HEIGHT = 81f * HSIEN_KO_SCALE;
    public static final float HSIEN_KO_FRAME_WIDTH = 204f * HSIEN_KO_SCALE;
    public static final float HSIEN_KO_FRAME_HEIGHT = 117f * HSIEN_KO_SCALE;
    public static final float HSIEN_KO_DRAW_CORRECTION_X =  (70f * HSIEN_KO_SCALE) + HSIEN_KO_WIDTH / 2;;
    public static final float HSIEN_KO_DRAW_CORRECTION_Y = (7f * HSIEN_KO_SCALE) + HSIEN_KO_HEIGHT / 2;
    public static final float HSIEN_KO_SPEED = 70f;
    public static final int HSIEN_KO_HEALTH = 500;
    public static final int HSIEN_KO_HEALTH_HARD = 1000;
    public static final int HSIEN_KO_ATTACK_STRENGTH = 200;
    public static final int HSIEN_KO_ATTACK_STRENGTH_HARD = 400;
    public static final float HSIEN_KO_IDLE_DURATION = 0.2f;
    public static final int HSIEN_KO_HIT_FRAMES = 3;
    public static final float HSIEN_KO_HIT_DURATION = 0.16f;
    public static final int HSIEN_KO_DEAD_FRAMES = 10;
    public static final float HSIEN_KO_DEAD_DURATION = 0.1f;
    public static final int HSIEN_KO_TURN_FRAMES = 3;
    public static final float HSIEN_KO_TURN_DURATION = 0.1f;
    public static final float HSIEN_KO_WALK_DURATION = 0.15f;
    public static final int HSIEN_KO_ATTACK_FRAMES = 7;
    public static final float HSIEN_KO_ATTACK_DURATION = 0.1f;
    public static final float HSIEN_KO_ATTACK_WIDTH = 75f * HSIEN_KO_SCALE;
    public static final float HSIEN_KO_ATTACK_HEIGHT = 29f * HSIEN_KO_SCALE;
    public static final float HSIEN_KO_ATTACK_OFFSET_X = (HSIEN_KO_WIDTH / 2 + HSIEN_KO_ATTACK_WIDTH / 2);
    public static final float HSIEN_KO_ATTACK_OFFSET_Y = (HSIEN_KO_ATTACK_HEIGHT / 2) + 15 * HSIEN_KO_SCALE;
//    public static final int KAIN_JUMP_ATTACK_FRAMES = 8;
//    public static final float KAIN_JUMP_ATTACK_DURATION = 0.1f;
//    public static final float KAIN_JUMP_ATTACK_WIDTH = 58f;
//    public static final float KAIN_JUMP_ATTACK_HEIGHT = 39f;
//    public static final float KAIN_JUMP_ATTACK_OFFSET_X = (KAIN_WIDTH / 2 + KAIN_JUMP_ATTACK_WIDTH / 2);
//    public static final float KAIN_JUMP_ATTACK_OFFSET_Y = (-(KAIN_JUMP_ATTACK_HEIGHT / 2) + 9);
    public static final String HSIEN_KO_IDLE = "hsien_ko_idle";
    public static final String HSIEN_KO_TURN = "hsien_ko_turn";
    public static final String HSIEN_KO_WALK = "hsien_ko_walk";
    public static final String HSIEN_KO_ATTACK = "hsien_ko_attack";
//    public static final String KAIN_JUMP_ATTACK = "kain_jump_attack";
    public static final String HSIEN_KO_HIT = "hsien_ko_hit";
    public static final String HSIEN_KO_DEAD = "hsien_ko_knockdown";

    // Death Adder
    public static final float DEATH_ADDER_SCALE = 0.55f;
    public static final float DEATH_ADDER_WIDTH = 97f * DEATH_ADDER_SCALE;
    public static final float DEATH_ADDER_HEIGHT = 127f * DEATH_ADDER_SCALE;
    public static final float DEATH_ADDER_FRAME_WIDTH = 246f * DEATH_ADDER_SCALE;
    public static final float DEATH_ADDER_FRAME_HEIGHT = 206f * DEATH_ADDER_SCALE;
    public static final float DEATH_ADDER_DRAW_CORRECTION_X =  (73f * DEATH_ADDER_SCALE) + DEATH_ADDER_WIDTH / 2;;
    public static final float DEATH_ADDER_DRAW_CORRECTION_Y = (35f * DEATH_ADDER_SCALE) + DEATH_ADDER_HEIGHT / 2;
    public static final float DEATH_ADDER_SPEED = 70f;
    public static final int DEATH_ADDER_HEALTH = 2000;
    public static final int DEATH_ADDER_HEALTH_HARD = 4000;
    public static final int DEATH_ADDER_ATTACK_STRENGTH = 500;
    public static final int DEATH_ADDER_ATTACK_STRENGTH_HARD = 1000;
    public static final float DEATH_ADDER_IDLE_DURATION = 0.15f;
    public static final int DEATH_ADDER_HIT_FRAMES = 5;
    public static final float DEATH_ADDER_HIT_DURATION = 0.16f;
    public static final int DEATH_ADDER_DEAD_FRAMES = 5;
    public static final float DEATH_ADDER_DEAD_DURATION = 0.2f;
    public static final int DEATH_ADDER_TURN_FRAMES = 5;
    public static final float DEATH_ADDER_TURN_DURATION = 0.1f;
    public static final float DEATH_ADDER_WALK_DURATION = 0.15f;
    public static final int DEATH_ADDER_ATTACK_FRAMES = 8;
    public static final float DEATH_ADDER_ATTACK_DURATION = 0.1f;
    public static final float DEATH_ADDER_ATTACK_WIDTH = 74f * DEATH_ADDER_SCALE;
    public static final float DEATH_ADDER_ATTACK_HEIGHT = 91f * DEATH_ADDER_SCALE;
    public static final float DEATH_ADDER_ATTACK_OFFSET_X = (DEATH_ADDER_WIDTH / 2 + DEATH_ADDER_ATTACK_WIDTH / 2);
    public static final float DEATH_ADDER_ATTACK_OFFSET_Y = (DEATH_ADDER_ATTACK_HEIGHT / 2) - 13 * DEATH_ADDER_SCALE;
//    public static final int KAIN_JUMP_ATTACK_FRAMES = 8;
//    public static final float KAIN_JUMP_ATTACK_DURATION = 0.1f;
//    public static final float KAIN_JUMP_ATTACK_WIDTH = 58f;
//    public static final float KAIN_JUMP_ATTACK_HEIGHT = 39f;
//    public static final float KAIN_JUMP_ATTACK_OFFSET_X = (KAIN_WIDTH / 2 + KAIN_JUMP_ATTACK_WIDTH / 2);
//    public static final float KAIN_JUMP_ATTACK_OFFSET_Y = (-(KAIN_JUMP_ATTACK_HEIGHT / 2) + 9);
    public static final String DEATH_ADDER_IDLE = "death_adder_idle";
    public static final String DEATH_ADDER_TURN = "death_adder_turn";
    public static final String DEATH_ADDER_WALK = "death_adder_walk";
    public static final String DEATH_ADDER_ATTACK = "death_adder_attack";
//    public static final String KAIN_JUMP_ATTACK = "kain_jump_attack";
    public static final String DEATH_ADDER_HIT = "death_adder_hit";
    public static final String DEATH_ADDER_DEAD = "death_adder_knockdown";
}
