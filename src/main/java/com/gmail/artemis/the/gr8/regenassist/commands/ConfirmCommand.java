package com.gmail.artemis.the.gr8.regenassist.commands;

import com.gmail.artemis.the.gr8.regenassist.Main;
import com.gmail.artemis.the.gr8.regenassist.utils.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class ConfirmCommand implements CommandExecutor {

    private final Utilities utils;
    private final MultiverseHandler mv;
    private final DataFileHandler data;
    private final Main plugin;

    public ConfirmCommand (Utilities u, MultiverseHandler m, DataFileHandler d, Main p) {
        utils = u;
        mv = m;
        data = d;
        plugin = p;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        //if player clicks confirm within 15 seconds, get worldName from HashMap and pass it on to the MultiverseHandler
        if (label.equalsIgnoreCase("regenconfirm") && args.length >= 2) {

            if (!utils.getWorldCodes().containsKey(UUID.fromString(args[0]))) {
                sender.sendMessage(MessageWriter.tooSlow());
                return false;
            }

            else {
                //args[0] = uniqueCode
                //args[1] = same-seed/random-seed/supply-seed:
                //args[2] = optional reset-gamerules
                String worldName = utils.getWorldCodes().remove(UUID.fromString(args[0]));
                boolean useNewSeed = (args[1].equalsIgnoreCase("random-seed") || args[1].startsWith("supply-seed:"));
                boolean randomSeed = (args[1].equalsIgnoreCase("random-seed"));
                String seed = args[1].startsWith("supply-seed:") ? args[1].substring(12) : "";
                boolean keepGameRules = !(args.length == 3 && args[2].equalsIgnoreCase("reset-gamerules"));

                //another failsafe in case worldname is world
                if (worldName.equalsIgnoreCase("world")) {
                    sender.sendMessage(MessageWriter.mainWorldWarning());
                    return false;
                }

                //start the regen
                else {
                    sender.sendMessage(MessageWriter.startRegenerating(worldName));
                    boolean regen = mv.mvRegen(sender, worldName, useNewSeed, randomSeed, seed, keepGameRules);

                    if (!regen) {
                        sender.sendMessage(MessageWriter.unknownError(worldName));
                        return false;
                    }

                    else {
                        //check every second if the world has been loaded again after regenerating, and stop + give feedback when the world has been loaded
                        new BukkitRunnable() {
                            public void run() {
                                if (!mv.getUnloadedWorlds().contains(worldName)) {
                                    data.writeToDataFile(worldName, TimeHandler.getCurrentTime());
                                    sender.sendMessage(MessageWriter.doneRegenerating(worldName));
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(plugin, 20L, 20L);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
