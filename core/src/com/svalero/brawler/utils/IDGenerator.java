package com.svalero.brawler.utils;

public class IDGenerator {
    private static int nextId = 0;

    public static int generateUniqueId() {
        return nextId++;
    }
}
