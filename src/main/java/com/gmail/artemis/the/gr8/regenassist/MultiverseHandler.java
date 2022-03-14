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

    public boolean mvRegen(CommandSender sender, String worldName, boolean useNewSeed, boolean randomSeed, String seed, boolean keepGameRules) {

        //if worldname = world, panic and cancel
        if(worldName.equalsIgnoreCase("world")) {
            sender.sendMessage(msg.mainWorldWarning());
            return true;
        }

        //otherwise, perform the reset
        else {
        /*  System.out.println("worldname: "+worldName);
            System.out.println("useNewSeed: "+useNewSeed);
            System.out.println("randomSeed: "+randomSeed);
            System.out.println("seed: "+seed);
            System.out.println("keepGameRules: "+keepGameRules);  */

            return worldManager.regenWorld(worldName, useNewSeed, randomSeed, seed, keepGameRules);
        }
    }




}
