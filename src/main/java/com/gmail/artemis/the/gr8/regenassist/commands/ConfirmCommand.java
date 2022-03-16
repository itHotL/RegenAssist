package com.gmail.artemis.the.gr8.regenassist.commands;

import com.gmail.artemis.the.gr8.regenassist.Main;
import com.gmail.artemis.the.gr8.regenassist.utils.MessageWriter;
import com.gmail.artemis.the.gr8.regenassist.utils.MultiverseHandler;
import com.gmail.artemis.the.gr8.regenassist.utils.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class ConfirmCommand implements CommandExecutor {

    private MessageWriter msg;
    private Utilities utils;
    private MultiverseHandler mv;
    private final Main plugin;

    public ConfirmCommand (MessageWriter f, Utilities u, MultiverseHandler m, Main p) {
        msg = f;
        mv = m;
        utils = u;
        plugin = p;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        //if player clicks confirm within 15 seconds, get worldName from HashMap and pass it on to the MultiverseHandler
        if (label.equalsIgnoreCase("regenconfirm")) {

            //args[0] = uniqueCode
            //args[1] = same-seed/random-seed/supply-seed:
            //args[2] = optional reset-gamerules
            if (args.length >= 2 && utils.getWorldCodes().containsKey(UUID.fromString(args[0]))) {

                String worldName = utils.getWorldCodes().remove(UUID.fromString(args[0]));
                boolean useNewSeed = (args[1].equalsIgnoreCase("random-seed") || args[1].startsWith("supply-seed:"));
                boolean randomSeed = (args[1].equalsIgnoreCase("random-seed"));
                String seed = args[1].startsWith("supply-seed:") ? args[1].substring(12) : "";
                boolean keepGameRules = !(args.length == 3 && args[2].equalsIgnoreCase("reset-gamerules"));

                //start the regen
                sender.sendMessage(msg.startRegenerating(worldName));
                mv.mvRegen(sender, worldName, useNewSeed, randomSeed, seed, keepGameRules);

                //check every second if the world has been loaded again after regenerating, and stop + give feedback when the world has been loaded
                new BukkitRunnable() {
                    public void run() {
                        if (!mv.getUnloadedWorlds().contains(worldName)) {
                            sender.sendMessage(msg.doneRegenerating(worldName));
                            this.cancel();
                        }
                    }
                }.runTaskTimer(plugin, 20L, 20L);
            }

            return true;
        }

        return false;
    }
}