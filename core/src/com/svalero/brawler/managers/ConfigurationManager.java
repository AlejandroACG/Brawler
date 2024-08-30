package com.svalero.brawler.managers;

public class ConfigurationManager {
    public static boolean mute = false;
    public static boolean hard = false;
    public static SelectedCharacter selectedCharacter;

    public static void switchMute() {
        mute = !mute;
    }
    public static void switchHard() { hard = !hard; }

    public enum SelectedCharacter {
        KAIN
    }
}
