//package com.svalero.brawler.domain;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Input;
//import com.badlogic.gdx.graphics.g2d.Animation;
//import com.badlogic.gdx.graphics.g2d.TextureAtlas;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.physics.box2d.World;
//import com.badlogic.gdx.utils.Array;
//
//public class DeathAdder extends Character {
//    public Animation<TextureRegion> idleAnimation;
//
//    public DeathAdder(World world, Vector2 position) {
//        super(world, position, placeHolderAtlas);
//        textureAtlas = new TextureAtlas(Gdx.files.internal("./textures/death_adder.atlas"));
//
//        Array<TextureAtlas.AtlasRegion> idleFrames = textureAtlas.findRegions("idle");
//        idleAnimation = new Animation<>(0.1f, idleFrames, Animation.PlayMode.LOOP);
//    }
//
//    public void update(float dt) {
//        manageInput();
//        stateTime += dt;
//    }
//
//    public TextureRegion getCurrentIdleFrame() {
//        return idleAnimation.getKeyFrame(stateTime, true);
//    }
//
//    public void manageInput() {
//        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//            position.x += 20;
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
//            position.x -= 20;
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
//            position.y += 20;
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
//            position.y -= 20;
//        }
//    }
//}
