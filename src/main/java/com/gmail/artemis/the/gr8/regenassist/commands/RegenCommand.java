package com.gmail.artemis.the.gr8.regenassist.commands;

import com.gmail.artemis.the.gr8.regenassist.Main;
import com.gmail.artemis.the.gr8.regenassist.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class RegenCommand implements CommandExecutor {

    private final Utilities utils;
    private final ConfigHandler conf;
    private final DataFileHandler data;
    private final MultiverseHandler mv;
    private final Main plugin;

    public RegenCommand (Utilities u, ConfigHandler c, DataFileHandler d, MultiverseHandler m, Main p) {
        utils = u;
        conf = c;
        data = d;
        mv = m;
        plugin = p;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        //the main command
        if(label.equalsIgnoreCase("regen")) {

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

                if(!conf.getWorldList().contains(args[0])) {
                    sender.sendMessage(MessageWriter.wrongName());
                    return true;
                }

                else {

                    if(!mv.isMVWorld(args[0])) {
                        sender.sendMessage(MessageWriter.unknownWorld());
                        return true;
                    }

                    //put unique confirm-code on HashMap attached to the worldName (if there is no entry for this name yet)
                    else if(utils.getWorldCodes().containsValue(args[0])) {
                        sender.sendMessage(MessageWriter.alreadyRegenerating());
                        return true;
                    }

                    else {
                        UUID uniqueCode = UUID.randomUUID();
                        utils.getWorldCodes().put(uniqueCode, args[0]);

                        //start 15-second timer that removes unique code from the HashMap if it is still there
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                if(utils.getWorldCodes().containsKey(uniqueCode)) {
                                    utils.getWorldCodes().remove(uniqueCode);
                                }
                            }
                        }, 300L);

                        //give confirm prompt to player
                        //args[0] = uniqueCode
                        //args[1] = same-seed/random-seed/supply-seed:
                        //args[2] = optional reset-gamerules
                        String gamerules = (args.length == 3) ? args[2] : " ";
                        String confirmCmd = "tellraw "+sender.getName()+ MessageWriter.confirmCommand(args[0], getUniqueRegenCmd(uniqueCode, args[1], gamerules), getTimeSinceLastRegen(args[0]));
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), confirmCmd);
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private String getUniqueRegenCmd(UUID uuid, String seedOption, String gamerules) {
        return "/regenconfirm "+uuid+" "+seedOption+" "+gamerules;
    }

    //calculate the time since this world has last been reset
    private String getTimeSinceLastRegen(String worldName) {
        String timestamp = data.getLastRegenTime(worldName);
        if (!timestamp.equalsIgnoreCase("")) {
            return TimeHandler.getStringTimeSinceLastRegen(timestamp);
        }

        else {
            return timestamp;
        }
    }
}
