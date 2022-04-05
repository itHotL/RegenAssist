package com.gmail.artemis.the.gr8.regenassist.commands;

import com.gmail.artemis.the.gr8.regenassist.Main;
import com.gmail.artemis.the.gr8.regenassist.filehandlers.ConfigHandler;
import com.gmail.artemis.the.gr8.regenassist.filehandlers.RegenFileHandler;
import com.gmail.artemis.the.gr8.regenassist.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.Console;
import java.util.UUID;

public class RegenCommand implements CommandExecutor {

    private final RegenQueue regenQueue;
    private final ConfigHandler config;
    private final RegenFileHandler regenFile;
    private final MultiverseHandler mv;
    private final Main plugin;

    public RegenCommand (ConfigHandler c, MultiverseHandler m, RegenFileHandler r, RegenQueue q, Main p) {

        config = c;
        mv = m;
        regenFile = r;
        regenQueue = q;
        plugin = p;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        //check if a worldname is included
        if(args.length == 0) {
            sender.sendMessage(MessageWriter.missingName());
            return false;
        }

        //check if option for seed was chosen
        else if (args.length == 1) {
            sender.sendMessage(MessageWriter.missingSeedOption());
            return false;
        }

        //check if seed was supplied if supply-seed was chosen
        else if (args[1].equalsIgnoreCase("supply-seed:")) {
            sender.sendMessage(MessageWriter.missingSeed());
            return false;
        }

        //check config to see whether world name is a valid option for regen
        else {

            //check if to-be-regenerated world is not the main world
            //get name of the main world from config, or assume 'world' if path is empty
            String mainWorld = (config.getMainWorldName()==null) ? "world" : config.getMainWorldName();
            if (args[0].equalsIgnoreCase(mainWorld)) {
                sender.sendMessage(MessageWriter.mainWorldWarning());
                return true;
            }

            if(!config.getWorldList().contains(args[0])) {
                sender.sendMessage(MessageWriter.wrongName());
                return true;
            }

            else {

                if(!mv.isMVWorld(args[0])) {
                    sender.sendMessage(MessageWriter.unknownWorld());
                    return true;
                }

                //put unique confirm-code on HashMap attached to the worldName (if there is no entry for this name yet)
                else if(regenQueue.containsWorldName(args[0])) {
                    sender.sendMessage(MessageWriter.alreadyRegenerating());
                    return true;
                }

                else {
                    UUID uniqueCode = regenQueue.createWorldCode(args[0]);

                    //start 15-second timer that removes unique code from the HashMap if it is still there
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            if(regenQueue.containsWorldCode(uniqueCode)) {
                                regenQueue.removeEntry(uniqueCode);
                            }
                        }
                    }, 300L);

                    //get everything ready for confirm prompt (either for player, or console)
                    //args[0] = worldName
                    //args[1] = same-seed/random-seed/supply-seed:
                    //args[2] = optional reset-gamerules
                    String gamerules = (args.length == 3) ? args[2] : " ";

                    //give confirm prompt to player
                    if (sender instanceof Player) {
                        String confirmCmd = "tellraw "+sender.getName()+ MessageWriter.confirmCommand(args[0], getUniqueRegenCmd(uniqueCode, args[1], gamerules), getTimeSinceLastRegen(args[0]));
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), confirmCmd);
                        return true;
                    }

                    //give confirm prompt in console
                    else if (sender instanceof ConsoleCommandSender) {
                        sender.sendMessage(MessageWriter.confirmCommandConsole(args[0], getTimeSinceLastRegen(args[0])));
                        return true;
                    }

                    else {
                        return false;
                    }
                }
            }
        }
    }


    private String getUniqueRegenCmd(UUID uuid, String seedOption, String gamerules) {
        return "/regenconfirm "+uuid+" "+seedOption+" "+gamerules;
    }

    //calculate the time since this world has last been reset
    private String getTimeSinceLastRegen(String worldName) {
        String timestamp = regenFile.getLastRegenTime(worldName);
        if (!timestamp.equalsIgnoreCase("")) {
            return TimeHandler.getStringTimeSinceLastRegen(timestamp);
        }

        else {
            return timestamp;
        }
    }
}
