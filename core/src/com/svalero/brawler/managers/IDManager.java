package com.svalero.brawler.managers;

public class IDManager {
    // TODO Debería generar un ID único para todo lo que tenga sonidos (Enemy, Player y Bomb) e implementarlo en ellos
    //  igual que lo he implementado en Wave.
    private static int waveId = 0;

    public static synchronized int getNewWaveId() { return waveId++; }

    public static synchronized void reset() { waveId = 0; }
}
