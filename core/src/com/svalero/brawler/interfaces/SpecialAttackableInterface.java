package com.svalero.brawler.interfaces;

import com.badlogic.gdx.math.Vector2;

public interface SpecialAttackableInterface {
    void goSpecialAttack();
    Vector2 handleSpecialAttack(float dt, Vector2 velocity);
}
