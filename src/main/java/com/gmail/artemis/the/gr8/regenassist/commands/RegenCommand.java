package com.gmail.artemis.the.gr8.regenassist.commands;

import com.gmail.artemis.the.gr8.regenassist.RegenManager;
import com.gmail.artemis.the.gr8.regenassist.filehandlers.ConfigHandler;
import com.gmail.artemis.the.gr8.regenassist.regen.MVCoreHandler;
import com.gmail.artemis.the.gr8.regenassist.regen.RegenQueue;
import com.gmail.artemis.the.gr8.regenassist.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class RegenCommand implements CommandExecutor {

    private final ConfigHandler config;
    private final MVCoreHandler mv;
    private final RegenManager regenManager;
    private final RegenQueue regenQueue;

    public RegenCommand(ConfigHandler c, MVCoreHandler m, RegenManager r, RegenQueue q) {
        config = c;
        mv = m;
        regenManager = r;
        regenQueue = q;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        //check if there are worlds listed in the config
        if (config.getWorldList().isEmpty()) {
            sender.sendMessage(MessageWriter.emptyWorldList());
            return true;
        }

        //check if worldName is included
        else if (args.length == 0) {
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

        else {
            //check if to-be-regenerated world is not the main world
            String mainWorld = (config.getMainWorldName()==null) ? "world" : config.getMainWorldName();
            if (args[0].equalsIgnoreCase(mainWorld)) {
                sender.sendMessage(MessageWriter.mainWorldWarning());
                return true;
            }

            //check config to see whether world name is a valid option for regen
            if (!config.getWorldList().contains(args[0])) {
                sender.sendMessage(MessageWriter.wrongName());
                return true;
            }

            else {
                if (!mv.isMVWorld(args[0])) {
                    sender.sendMessage(MessageWriter.unknownWorld());
                    return true;
                }

                //put unique confirm-code on HashMap attached to the worldName (if there is no entry for this name yet)
                else if (regenQueue.containsWorldName(args[0])) {
                    sender.sendMessage(MessageWriter.alreadyRegenerating());
                    return true;
                }

                //get everything ready for confirm prompt (either for player, or console)
                //args[0] = worldName
                //args[1] = same-seed/random-seed/supply-seed:
                //args[2] = optional reset-gamerules
                else {
                    String gamerules = (args.length == 3) ? args[2] : " ";
                    UUID uniqueCode = regenQueue.createEntry(args[0], args[1], gamerules);

                    //give confirm prompt to player
                    if (sender instanceof Player) {
                        String confirmCmd = "tellraw "+sender.getName()+ MessageWriter.confirmCommand(args[0], getUniqueRegenCmd(uniqueCode, args[1], gamerules), regenManager.getTimeSinceLastRegen(args[0]));
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), confirmCmd);
                        return true;
                    }

                    //give confirm prompt in console
                    else if (sender instanceof ConsoleCommandSender) {
                        sender.sendMessage(MessageWriter.confirmCommandConsole(args[0], regenManager.getTimeSinceLastRegen(args[0])));
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
}
