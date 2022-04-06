package com.gmail.artemis.the.gr8.regenassist.utils;

public class RegenCandidate {

    private String worldName;
    private String seedOption;
    private String resetGameRules;


    public RegenCandidate(String name, String seed, String gamerules) {
        worldName = name;
        seedOption = seed;
        resetGameRules = gamerules;
    }

    public String getWorldName() {
        return worldName;
    }

    public String getSeedOption() {
        return seedOption;
    }

    public String getResetGameRules() {
        return resetGameRules;
    }
}
