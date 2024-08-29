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
import com.svalero.brawler.domains.characters.*;
import com.svalero.brawler.domains.characters.Character;
import com.svalero.brawler.domains.projectiles.Bomb;
import com.svalero.brawler.domains.projectiles.Projectile;
import com.svalero.brawler.screens.GameOverScreen;
import com.svalero.brawler.screens.GameScreen;
import com.svalero.brawler.screens.VictoryScreen;
import com.svalero.brawler.utils.ParallaxLayer;

import java.util.*;

import static com.svalero.brawler.utils.Constants.*;
import com.svalero.brawler.managers.ConfigurationManager.SelectedCharacter;
import com.svalero.brawler.domains.characters.Character.*;

public class LevelManager {
    private Brawler game;
    private int currentLevel;
    private int initialScore;
    private int currentScore;
    private final World world;
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
    private boolean isVictorious = false;
    private float victoryTimer = 5.0f;
    private boolean playerCelebrationTriggered = false;
    private float playerCelebrationTimer = 1.0f;
    private List<Projectile> projectiles;
    private Array<Body> bodiesToDestroy;

    public LevelManager(Brawler game, int currentLevel, SelectedCharacter selectedCharacter, int initialScore) {
        this.game = game;
        this.currentLevel = currentLevel;
        this.initialScore = initialScore;
        this.currentScore = initialScore;
        world = new World(new Vector2(0, GRAVITY), true);
        characters = new HashMap<>();
        enemies = new HashMap<>();
        mapLoader = new TmxMapLoader();
        projectiles = new ArrayList<>();
        bodiesToDestroy = new Array<>();

        switch (currentLevel) {
            case 1:
                map = mapLoader.load(LEVEL_1_MAP);
                mapWidth = LEVEL_1_MAP_WIDTH;
                mapHeight = LEVEL_1_MAP_HEIGHT;
                backgroundMusic = LEVEL_1_MUSIC;
                break;
            case 2:
                map = mapLoader.load(LEVEL_2_MAP);
                mapWidth = LEVEL_2_MAP_WIDTH;
                mapHeight = LEVEL_2_MAP_HEIGHT;
                backgroundMusic = LEVEL_2_MUSIC;
                break;
        }

        MusicManager.stopMusic();
        MusicManager.startMusic(backgroundMusic);
        loadColliders();
        loadCharacters(selectedCharacter);

        setContactListener();

    }

    public void setCameraManager(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }

    public void setBackground() {
        TextureAtlas backgroundAtlas;
        AtlasRegion parallaxTexture1;
        AtlasRegion parallaxTexture2;
        AtlasRegion parallaxTexture3;
        AtlasRegion parallaxTexture4;
        switch (currentLevel) {
            case 1:
                backgroundAtlas = ResourceManager.getAtlas(LEVEL_1_BACKGROUND);
                parallaxTexture1 = backgroundAtlas.findRegion("parallax-1");
                parallaxTexture2 = backgroundAtlas.findRegion("parallax-2");
                parallaxTexture3 = backgroundAtlas.findRegion("parallax-3");
                parallaxTexture4 = backgroundAtlas.findRegion("parallax-4");
                parallaxLayers.add(new ParallaxLayer(parallaxTexture4, LEVEL_1_PARALLAX_FACTOR_4, cameraManager,
                        true, 7, 0, -30));
                parallaxLayers.add(new ParallaxLayer(parallaxTexture3, LEVEL_1_PARALLAX_FACTOR_3, cameraManager,
                        true, 3, 0, -25));
                parallaxLayers.add(new ParallaxLayer(parallaxTexture2, LEVEL_1_PARALLAX_FACTOR_2, cameraManager,
                        true, 7, 0, -25));
                parallaxLayers.add(new ParallaxLayer(parallaxTexture1, LEVEL_1_PARALLAX_FACTOR_1, cameraManager,
                        false, 1, 186, 27));
                break;
            case 2:
                backgroundAtlas = ResourceManager.getAtlas(LEVEL_2_BACKGROUND);
                parallaxTexture1 = backgroundAtlas.findRegion("parallax-1");
                parallaxTexture2 = backgroundAtlas.findRegion("parallax-2");
                parallaxTexture3 = backgroundAtlas.findRegion("parallax-3");
                parallaxTexture4 = backgroundAtlas.findRegion("parallax-4");
                parallaxLayers.add(new ParallaxLayer(parallaxTexture4, LEVEL_2_PARALLAX_FACTOR_4, cameraManager,
                        true, 7, 0, -30));
                parallaxLayers.add(new ParallaxLayer(parallaxTexture3, LEVEL_2_PARALLAX_FACTOR_3, cameraManager,
                        true, 3, 0, -25));
                parallaxLayers.add(new ParallaxLayer(parallaxTexture2, LEVEL_2_PARALLAX_FACTOR_2, cameraManager,
                        true, 7, 0, -25));
                parallaxLayers.add(new ParallaxLayer(parallaxTexture1, LEVEL_2_PARALLAX_FACTOR_1, cameraManager,
                        false, 1, -500, -45));
                break;
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
                    character.setOnGround(true);
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

                // Detección de bombas
                if ((fixtureA.getFilterData().categoryBits == COLLIDER_CATEGORY_BOMB_IDLE && userDataB instanceof Player) ||
                        (fixtureB.getFilterData().categoryBits == COLLIDER_CATEGORY_BOMB_IDLE && userDataA instanceof Player)) {
                    Bomb bomb = (Bomb) (userDataA instanceof Bomb ? userDataA : userDataB);
                    System.out.println("BOOM");
                    bomb.collision();
                }
            }

            private void handleAttackHit(Character attacker, Character victim, boolean attackFromLeft, Vector2 contactPoint) {
                if (victim.getCurrentState() != State.HIT && victim.getCurrentState() != State.DEAD) {
                    victim.getHit(attacker.getAttackStrength(), attackFromLeft, contactPoint);
                }
            }

            @Override
            public void endContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();
                Object userDataA = fixtureA.getUserData();
                Object userDataB = fixtureB.getUserData();

                if ((fixtureA.getFilterData().categoryBits == COLLIDER_CATEGORY_GROUND && userDataB instanceof Character) ||
                        (fixtureB.getFilterData().categoryBits == COLLIDER_CATEGORY_GROUND && userDataA instanceof Character)) {
                    Character character = (Character) (userDataA instanceof Character ? userDataA : userDataB);
                    character.setOnGround(false);
                }
            }

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
        }
    }

    public void queueBodyForDestruction(Body body) {
        if (!bodiesToDestroy.contains(body, true)) { // Verifica si ya está en la lista
            bodiesToDestroy.add(body);
        }
    }

    public void cleanUpBodies() {
        for (Body body : bodiesToDestroy) {
            if (body != null) {
                world.destroyBody(body);
            }
        }
        bodiesToDestroy.clear();
    }

    public void checkVictoryCondition(float dt) {
        if (!isVictorious) {
            boolean allEnemiesDead = true;

            for (Enemy enemy : enemies.values()) {
                if (enemy.getCurrentState() != State.DEAD) {
                    allEnemiesDead = false;
                    break;
                }
            }

            if (allEnemiesDead && player.isOnGround()) {
                isVictorious = true;
            }
        }

        if (isVictorious && !playerCelebrationTriggered) {
            playerCelebrationTimer -= dt;
            if (playerCelebrationTimer <= 0) {
                playerCelebrationTriggered = true;
                player.setCurrentState(State.VICTORY);
                MusicManager.stopMusic();
            }
        }

        if (playerCelebrationTriggered) {
            victoryTimer -= dt;
            if (victoryTimer <= 0) {
                triggerNextLevel();
            }
        }
    }

    private void triggerNextLevel() {
        if (currentLevel < NUMBER_OF_LEVELS) {
            game.setScreen(new GameScreen(game, ++currentLevel, currentScore));
        } else {
            game.setScreen(new VictoryScreen(game, currentScore));
        }
    }

    public void addProjectile(Projectile projectile) { projectiles.add(projectile); }

    public List<Projectile> getProjectiles() { return projectiles; }

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

    public int getCurrentLevel() { return currentLevel; }
}
