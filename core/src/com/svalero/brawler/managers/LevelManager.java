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
import com.svalero.brawler.domains.Character;
import com.svalero.brawler.domains.Enemy;
import com.svalero.brawler.domains.Kain;
import com.svalero.brawler.domains.Player;
import com.svalero.brawler.utils.FixtureData;
import com.svalero.brawler.utils.ParallaxLayer;
import java.util.HashMap;
import java.util.Map;
import static com.svalero.brawler.utils.Constants.*;
import com.svalero.brawler.managers.ConfigurationManager.SelectedCharacter;
import com.svalero.brawler.utils.FixtureData.*;
import com.svalero.brawler.domains.Character.*;

public class LevelManager {
    private Brawler game;
    private int currentLevel;
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

    public LevelManager(Brawler game, int currentLevel, SelectedCharacter selectedCharacter) {
        this.game = game;
        this.currentLevel = currentLevel;
        world = new World(new Vector2(0, GRAVITY), true);
        characters = new HashMap<>();
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
                        createScenarioBody(object, EntityType.GROUND, SensorType.GROUND, COLLIDER_CATEGORY_GROUND);
                    } else if (object.getProperties().get("tag", String.class).equals("border")) {
                        createScenarioBody(object, EntityType.BORDER, SensorType.BORDER, COLLIDER_CATEGORY_BORDER);
                    }
                }
            }
        }
    }

    public void createScenarioBody(MapObject object, EntityType entityType, SensorType sensorType, short category) {
        Rectangle rect = ((RectangleMapObject) object).getRectangle();

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set((rect.x + rect.width / 2), (rect.y + rect.height / 2));
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(rect.width / 2, rect.height / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.filter.categoryBits = category;
        fixtureDef.filter.maskBits = COLLIDER_CATEGORY_PLAYER | COLLIDER_CATEGORY_ENEMY;
        if (category == COLLIDER_CATEGORY_BORDER) {
            fixtureDef.filter.maskBits = COLLIDER_CATEGORY_PLAYER;
        }
        Fixture fixture = body.createFixture(fixtureDef);
        FixtureData groundData = new FixtureData(entityType, -2, sensorType);
        fixture.setUserData(groundData);
        shape.dispose();
    }

    public void loadCharacters(SelectedCharacter selectedCharacter) {
        for (MapObject object : map.getLayers().get("Characters").getObjects()) {
            float x, y;
            if (object.getProperties().containsKey("tag")) {
                if (object.getProperties().get("tag", String.class).equals("player")) {
                    x = object.getProperties().get("x", float.class);
                    y = object.getProperties().get("y", float.class);

                    switch (selectedCharacter) {
                        case KAIN:
                            player = new Kain(world, new Vector2(x, y));
                    }
                    characters.put(player.getId(), player);
                }

            }
        }
    }

    public void setContactListener() {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                FixtureData fixtureDataA = (FixtureData) contact.getFixtureA().getUserData();
                FixtureData fixtureDataB = (FixtureData) contact.getFixtureB().getUserData();

                // Landing
                if (fixtureDataA.entityType == EntityType.GROUND || fixtureDataB.entityType == EntityType.GROUND) {
                    FixtureData characterData = fixtureDataA.entityType == EntityType.GROUND ? fixtureDataB : fixtureDataA;
                    Character character = characters.get(characterData.id);
                    if (character.getCurrentState() == State.JUMP_DOWN || character.getCurrentState() == State.JUMP_UP) {
                        character.setStateTime(0);
                        character.setCurrentState(State.LAND);
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {
                FixtureData fixtureDataA = (FixtureData) contact.getFixtureA().getUserData();
                FixtureData fixtureDataB = (FixtureData) contact.getFixtureB().getUserData();

                // Jumping
//                if (fixtureDataA.entityType == EntityType.GROUND || fixtureDataB.entityType == EntityType.GROUND) {
//                    FixtureData characterData = fixtureDataA.entityType == EntityType.GROUND ? fixtureDataB : fixtureDataA;
//                    if(characters.get(characterData.id).body.getLinearVelocity().y >= 0) {
//                        characters.get(characterData.id).setCurrentState(State.JUMP_UP);
//                    } else {
//                        characters.get(characterData.id).setCurrentState(State.JUMP_DOWN);
//                    }
//                }
            }

            @Override
            public void preSolve(Contact contact, Manifold manifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse contactImpulse) {
            }
        });
    }

    public float getMapWidth() {
        return mapWidth;
    }

    public float getMapHeight() {
        return mapHeight;
    }

    public Map<Integer, Character> getCharacters() { return characters; }

    public Player getPlayer() { return player; }

    public World getWorld() {
        return world;
    }

    public TiledMap getMap() {
        return map;
    }

    public Array<ParallaxLayer> getParallaxLayers() {
        return parallaxLayers;
    }
}
