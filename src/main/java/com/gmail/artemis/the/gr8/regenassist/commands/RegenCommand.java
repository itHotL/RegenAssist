package com.gmail.artemis.the.gr8.regenassist.commands;

import com.gmail.artemis.the.gr8.regenassist.Main;
import com.gmail.artemis.the.gr8.regenassist.utils.MessageHandler;
import com.gmail.artemis.the.gr8.regenassist.utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class RegenCommand implements CommandExecutor {

    private MessageHandler msg;
    private Utilities utils;
    private final Main plugin;

    public RegenCommand (MessageHandler f, Utilities u, Main p) {
        msg = f;
        plugin = p;
        utils = u;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        //the main command
        if(label.equalsIgnoreCase("regen")) {

            //check if a worldname is included
            if(args.length == 0) {
                sender.sendMessage(msg.missingName());
                return true;
            }

            //check if option for seed was chosen
            else if (args.length == 1) {
                sender.sendMessage(msg.missingSeedOption());
                return false;
            }

            //check if seed was supplied if supply-seed was chosen
            else if (args.length >= 2 && args[1].equalsIgnoreCase("supply-seed:")) {
                sender.sendMessage(msg.missingSeed());
                return false;
            }

            //check whether world name is a valid option for regen
            //---> check config
            else {

                if(!utils.getWorldList().contains(args[0])) {
                    sender.sendMessage(msg.wrongName());
                    return true;
                }

                if(utils.getWorldList().contains(args[0])) {
                    long time = Bukkit.getWorld(args[0]).getGameTime();

                    //put unique confirm-code on HashMap attached to the worldName (if there is no entry for this name yet)
                    if(utils.getWorldCodes().containsValue(args[0])) {
                        sender.sendMessage(msg.alreadyRegenerating());
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
                        String gamerules = (args.length == 3) ? args[2] : " ";
                        String uniqueRegenCmd = "/regenconfirm "+uniqueCode+" "+args[1]+" "+gamerules;
                        String confirmCommand = "tellraw "+sender.getName()+ msg.confirm(args[0], uniqueRegenCmd, time);
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), confirmCommand);
                    }

                    return true;
                }
            }
        }

        return false;
    }
}
