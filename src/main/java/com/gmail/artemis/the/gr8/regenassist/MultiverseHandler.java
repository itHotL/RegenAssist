package com.gmail.artemis.the.gr8.regenassist;

import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class MultiverseHandler {

    private MVWorldManager worldManager;
    private Fancifier fancy;

    public MultiverseHandler (MVWorldManager wm, Fancifier f) {
        worldManager = wm;
        fancy = f;
    }

    public boolean mvRegen(CommandSender sender, String worldName) {

        //if worldname = world, panic and cancel
        if(worldName.equalsIgnoreCase("world")) {
            sender.sendMessage(fancy.mainWorld());
            return true;
        }

        //otherwise, perform the reset
        else {
            boolean useNewSeed = true;
            boolean keepGameRules = true;

            //randomseed = false when seed is provided, true if seed is not provided
            boolean randomSeed = false;
            String seed = "420";

            return worldManager.regenWorld(worldName, useNewSeed, randomSeed, seed, keepGameRules);
        }
    }


}
