package com.svalero.brawler.utils;

public class FixtureData {
    public EntityType entityType;
    public int id;
    public SensorType sensorType;

    public enum EntityType {
        PLAYER,
        ENEMY,
        GROUND,
        BORDER
    }

    public enum SensorType {
        BODY,
        FOOT,
        GROUND,
        BORDER
    }

    public FixtureData(EntityType entityType, int id, SensorType sensorType) {
        this.entityType = entityType;
        this.id = id;
        this.sensorType = sensorType;
    }
}
