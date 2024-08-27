package com.svalero.brawler.managers;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.svalero.brawler.Brawler;
import com.svalero.brawler.domains.*;
import com.svalero.brawler.domains.Character;
import com.svalero.brawler.screens.GameOverScreen;
import com.svalero.brawler.utils.ParallaxLayer;
import java.util.HashMap;
import java.util.Map;
import static com.svalero.brawler.utils.Constants.*;
import com.svalero.brawler.managers.ConfigurationManager.SelectedCharacter;
import com.svalero.brawler.domains.Character.*;

public class LevelManager {
    private Brawler game;
    private int currentLevel;
    private int initialScore;
    private int currentScore;
    private World world;
    private Map<Integer, Character> characters;
    private Map<Integer, Enemy> enemies;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private Player player;
    private float mapWidth;
    private float mapHeight;
    private CameraManager cameraManager;
    private final Array<ParallaxLayer> parallaxLayers = new Array<>();
    private String backgroundMusic;
    private boolean isDefeated = false;
    private float defeatTimer = 10.0f;
    private boolean enemyCelebrationTriggered = false;
    private float enemyCelebrationTimer = 1.0f;

    public LevelManager(Brawler game, int currentLevel, SelectedCharacter selectedCharacter, int initialScore) {
        this.game = game;
        this.currentLevel = currentLevel;
        this.initialScore = initialScore;
        this.currentScore = initialScore;
        world = new World(new Vector2(0, GRAVITY), true);
        characters = new HashMap<>();
        enemies = new HashMap<>();
        mapLoader = new TmxMapLoader();

        switch (currentLevel) {
            case 1:
                map = mapLoader.load(LEVEL_1_MAP);
                mapWidth = LEVEL_1_MAP_WIDTH;
                mapHeight = LEVEL_1_MAP_HEIGHT;
                backgroundMusic = LEVEL_1_MUSIC;
        }

        // TODO loadCurrentLevel
        // TODO restartCurrentLevel (retry/restart/exit)
        MusicManager.startMusic(backgroundMusic);
        loadColliders();
        loadCharacters(selectedCharacter);

        setContactListener();


    }

    public void setCameraManager(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }

    public void setBackground() {
        switch (currentLevel) {
            case 1:
                TextureAtlas backgroundAtlas = ResourceManager.getAtlas(LEVEL_1_BACKGROUND);
                AtlasRegion parallaxTexture1 = backgroundAtlas.findRegion("parallax-1");
                AtlasRegion parallaxTexture2 = backgroundAtlas.findRegion("parallax-2");
                AtlasRegion parallaxTexture3 = backgroundAtlas.findRegion("parallax-3");
                AtlasRegion parallaxTexture4 = backgroundAtlas.findRegion("parallax-4");
                parallaxLayers.add(new ParallaxLayer(parallaxTexture4, LEVEL_1_PARALLAX_FACTOR_4, cameraManager,
                        true, 7, 0, -30));
                parallaxLayers.add(new ParallaxLayer(parallaxTexture3, LEVEL_1_PARALLAX_FACTOR_3, cameraManager,
                        true, 3, 0, -25));
                parallaxLayers.add(new ParallaxLayer(parallaxTexture2, LEVEL_1_PARALLAX_FACTOR_2, cameraManager,
                        true, 7, 0, -25));
                parallaxLayers.add(new ParallaxLayer(parallaxTexture1, LEVEL_1_PARALLAX_FACTOR_1, cameraManager,
                        false, 1, 186, 27));
        }
    }

    private void loadColliders() {
        for (MapObject object : map.getLayers().get("Colliders").getObjects()) {
            if (object.getProperties().containsKey("tag")) {
                if (object instanceof RectangleMapObject) {
                    if (object.getProperties().get("tag", String.class).equals("ground")) {
                        createScenarioBody(object, COLLIDER_CATEGORY_GROUND);
                    } else if (object.getProperties().get("tag", String.class).equals("border")) {
                        createScenarioBody(object, COLLIDER_CATEGORY_BORDER);
                    }
                }
            }
        }
    }

    public void createScenarioBody(MapObject object, short category) {
        Rectangle rect = ((RectangleMapObject) object).getRectangle();

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set((rect.x + rect.width / 2), (rect.y + rect.height / 2));
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(rect.width / 2, rect.height / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = category;
        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public void loadCharacters(SelectedCharacter selectedCharacter) {
        for (MapObject object : map.getLayers().get("Characters").getObjects()) {
            float x, y;
            if (object.getProperties().containsKey("tag")) {
                x = object.getProperties().get("x", float.class);
                y = object.getProperties().get("y", float.class);

                if (object.getProperties().get("tag", String.class).equals("player")) {
                    switch (selectedCharacter) {
                        case KAIN:
                            player = new Kain(this, world, new Vector2(x, y));
                    }
                    characters.put(player.getId(), player);
                }

                if (object.getProperties().get("tag", String.class).equals("bishamon")) {
                    Enemy enemy = new Bishamon(this, world, new Vector2(x, y));
                    characters.put(enemy.getId(), enemy);
                    enemies.put(enemy.getId(), enemy);
                }

                if (object.getProperties().get("tag", String.class).equals("hsien-ko")) {
                    Enemy enemy = new HsienKo(this, world, new Vector2(x, y));
                    characters.put(enemy.getId(), enemy);
                    enemies.put(enemy.getId(), enemy);
                }

                if (object.getProperties().get("tag", String.class).equals("death-adder")) {
                    Enemy enemy = new DeathAdder(this, world, new Vector2(x, y));
                    characters.put(enemy.getId(), enemy);
                    enemies.put(enemy.getId(), enemy);
                }
            }
        }
    }

    public void setContactListener() {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();
                Object userDataA = fixtureA.getUserData();
                Object userDataB = fixtureB.getUserData();

                // Detección de colisión con el suelo al caer de un salto
                if ((fixtureA.getFilterData().categoryBits == COLLIDER_CATEGORY_GROUND && userDataB instanceof Character) ||
                        (fixtureB.getFilterData().categoryBits == COLLIDER_CATEGORY_GROUND && userDataA instanceof Character)) {

                    Character character = (Character) (fixtureA.getUserData() instanceof Character ? fixtureA.getUserData() : fixtureB.getUserData());
                    if (character.getCurrentState() == State.JUMP_DOWN || character.getCurrentState() == State.JUMP_UP ||
                            character.getCurrentState() == State.JUMP_ATTACK) {
                        character.setStateTime(0);
                        character.setCurrentState(State.LAND);
                        character.setHasAttackedThisJump(false);
                    }
                }

                // Detección de colisión con el suelo al caer derrotado
                if ((fixtureA.getFilterData().categoryBits == COLLIDER_CATEGORY_GROUND && userDataB instanceof Character) ||
                        (fixtureB.getFilterData().categoryBits == COLLIDER_CATEGORY_GROUND && userDataA instanceof Character)) {

                    Character character = (Character) (fixtureA.getUserData() instanceof Character ? fixtureA.getUserData() : fixtureB.getUserData());
                    if (character.getCurrentState() == State.DEAD) {
                        character.stayDead();
                    }
                }

                // Detección de colisión de ataques
                if ((fixtureA.getFilterData().categoryBits == COLLIDER_CATEGORY_ATTACK_PLAYER && fixtureB.getUserData() instanceof Enemy) ||
                        (fixtureA.getFilterData().categoryBits == COLLIDER_CATEGORY_ATTACK_ENEMY && fixtureB.getUserData() instanceof Player)) {

                    boolean attackFromLeft = fixtureA.getBody().getPosition().x < fixtureB.getBody().getPosition().x;
                    handleAttackHit((Character) fixtureA.getUserData(), (Character) fixtureB.getUserData(),
                            attackFromLeft, contact.getWorldManifold().getPoints()[0]);

                } else if ((fixtureB.getFilterData().categoryBits == COLLIDER_CATEGORY_ATTACK_PLAYER && fixtureA.getUserData() instanceof Enemy) ||
                        (fixtureB.getFilterData().categoryBits == COLLIDER_CATEGORY_ATTACK_ENEMY && fixtureA.getUserData() instanceof Player)) {

                    boolean attackFromLeft = fixtureB.getBody().getPosition().x < fixtureA.getBody().getPosition().x;
                    handleAttackHit((Character) fixtureB.getUserData(), (Character) fixtureA.getUserData(),
                            attackFromLeft, contact.getWorldManifold().getPoints()[0]);
                }
            }

            private void handleAttackHit(Character attacker, Character victim, boolean attackFromLeft, Vector2 contactPoint) {
                if (victim.getCurrentState() != State.HIT && victim.getCurrentState() != State.DEAD) {
                    victim.getHit(attacker.getAttackStrength(), attackFromLeft, contactPoint);
                }
            }

            @Override
            public void endContact(Contact contact) {}

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                if (fixtureA.getFilterData().categoryBits == COLLIDER_CATEGORY_ATTACK_PLAYER ||
                        fixtureA.getFilterData().categoryBits == COLLIDER_CATEGORY_ATTACK_ENEMY ||
                        fixtureB.getFilterData().categoryBits == COLLIDER_CATEGORY_ATTACK_PLAYER ||
                        fixtureB.getFilterData().categoryBits == COLLIDER_CATEGORY_ATTACK_ENEMY) {
                    contact.setEnabled(false);
                }
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {}
        });
    }

    public void checkDefeatCondition(float dt) {
        if (player.getCurrentState() == State.DEAD && !isDefeated) {
            isDefeated = true;
        }

        if (isDefeated && !enemyCelebrationTriggered) {
            enemyCelebrationTimer -= dt;
            if (enemyCelebrationTimer <= 0) {
                enemyCelebrationTriggered = true;
                for (Enemy enemy : getEnemies().values()) {
                    if (enemy.getCurrentState() != State.DEAD) {
                        enemy.setCurrentState(State.VICTORY);
                    }
                }
                MusicManager.stopMusic();
                MusicManager.startMusic(GAME_OVER_MUSIC);
            }
        }

        if (enemyCelebrationTriggered) {
            defeatTimer -= dt;
            if (defeatTimer <= 0) {
                triggerGameOver();
            }
        }    }

    private void triggerGameOver() { game.setScreen(new GameOverScreen(game, currentLevel, initialScore, currentScore)); }

    public float getMapWidth() { return mapWidth; }

    public float getMapHeight() { return mapHeight; }

    public Map<Integer, Character> getCharacters() { return characters; }

    public Map<Integer, Enemy> getEnemies() { return enemies; }

    public Player getPlayer() { return player; }

    public World getWorld() { return world; }

    public TiledMap getMap() { return map; }

    public Array<ParallaxLayer> getParallaxLayers() { return parallaxLayers; }

    public int getCurrentScore() { return currentScore; }

    public void setCurrentScore(int currentScore) { this.currentScore = currentScore; }
}
