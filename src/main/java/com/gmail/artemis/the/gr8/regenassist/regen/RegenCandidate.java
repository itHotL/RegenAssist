package com.gmail.artemis.the.gr8.regenassist.regen;

public class RegenCandidate {

    private final String worldName;
    private final String seedOption;
    private final String resetGameRules;

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
