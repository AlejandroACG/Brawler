package com.svalero.brawler.managers;

import com.svalero.brawler.domains.Enemy;

public class ActionManager {
    LevelManager levelManager;

    public ActionManager(LevelManager levelManager) {
        this.levelManager = levelManager;
    }

    public void update(float dt) {
        levelManager.getWorld().step(1/60f, 6, 2);
        levelManager.getPlayer().update(dt);
        for (Enemy enemy : levelManager.getEnemies().values()) {
            enemy.update(dt);
        }
    }
}
