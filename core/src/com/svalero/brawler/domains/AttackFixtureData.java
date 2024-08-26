package com.svalero.brawler.domains;

import java.util.HashSet;
import java.util.Set;

public class AttackFixtureData {
    private Character attacker;
    private Set<Integer> hitEnemyIds = new HashSet<>();

    public AttackFixtureData(Character attacker) {
        this.attacker = attacker;
    }

    public Character getAttacker() { return attacker; }

    public void setAttacker(Character attacker) { this.attacker = attacker; }

    public Set<Integer> getHitEnemyIds() { return hitEnemyIds; }

    public void setHitEnemyIds(Set<Integer> hitEnemyIds) { this.hitEnemyIds = hitEnemyIds; }
}
