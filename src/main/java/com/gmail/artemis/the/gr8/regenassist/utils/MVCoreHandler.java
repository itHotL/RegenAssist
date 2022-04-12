package com.gmail.artemis.the.gr8.regenassist.utils;

import com.onarandombox.MultiverseCore.api.MVWorldManager;

import java.util.List;

public class MVCoreHandler {

    private final MVWorldManager worldManager;


    public MVCoreHandler(MVWorldManager wm) {

        worldManager = wm;
    }

    //perform the reset with the given parameters
    public boolean mvRegen(String worldName, boolean useNewSeed, boolean randomSeed, String seed, boolean keepGameRules) {
        return worldManager.regenWorld(worldName, useNewSeed, randomSeed, seed, keepGameRules);
    }

    //check if a given world is known to Multiverse
    public boolean isMVWorld(String worldName) {
        return worldManager.isMVWorld(worldName);
    }

    //check which worlds Multiverse knows about, but has not loaded (yet)
    public List<String> getUnloadedWorlds() {
        return worldManager.getUnloadedWorlds();
    }
}
