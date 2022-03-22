package com.gmail.artemis.the.gr8.regenassist.utils;

import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.command.CommandSender;

import java.util.List;

public class MultiverseHandler {

    private final MVWorldManager worldManager;


    public MultiverseHandler (MVWorldManager wm) {
        worldManager = wm;
    }


    public boolean mvRegen(CommandSender sender, String worldName, boolean useNewSeed, boolean randomSeed, String seed, boolean keepGameRules) {

        //if worldname = world, panic and cancel
        if(worldName.equalsIgnoreCase("world")) {
            sender.sendMessage(MessageWriter.mainWorldWarning());
            return false;
        }

        //otherwise, perform the reset
        else {
            return worldManager.regenWorld(worldName, useNewSeed, randomSeed, seed, keepGameRules);
        }
    }

    public boolean isMVWorld(String worldName) {
        return worldManager.isMVWorld(worldName);
    }

    public List<String> getUnloadedWorlds() {
        return worldManager.getUnloadedWorlds();
    }
}
