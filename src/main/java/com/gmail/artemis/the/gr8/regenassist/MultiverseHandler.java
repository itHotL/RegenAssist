package com.gmail.artemis.the.gr8.regenassist;

import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.command.CommandSender;

public class MultiverseHandler {

    private MVWorldManager worldManager;
    private MessageHandler msg;

    public MultiverseHandler (MVWorldManager wm, MessageHandler f) {
        worldManager = wm;
        msg = f;
    }

    public boolean mvRegen(CommandSender sender, String worldName) {

        //if worldname = world, panic and cancel
        if(worldName.equalsIgnoreCase("world")) {
            sender.sendMessage(msg.mainWorldWarning());
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
