package com.svalero.brawler.domains;

import com.badlogic.gdx.math.Vector2;

public interface SpecialAttackable {
    void goSpecialAttack();
    Vector2 handleSpecialAttack(float dt, Vector2 velocity);
}
