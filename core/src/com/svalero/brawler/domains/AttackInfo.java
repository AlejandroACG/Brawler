package com.svalero.brawler.domains;

public class AttackInfo {
    private int damage;
    private boolean isBlockable;

    public AttackInfo(int damage, boolean isBlockable) {
        this.damage = damage;
        this.isBlockable = isBlockable;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isBlockable() {
        return isBlockable;
    }
}
