package com.gmail.artemis.the.gr8.regenassist.commands;

import com.gmail.artemis.the.gr8.regenassist.RegenManager;
import com.gmail.artemis.the.gr8.regenassist.regen.RegenQueue;
import com.gmail.artemis.the.gr8.regenassist.utils.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ConfirmCommand implements CommandExecutor {

    private final RegenManager regenManager;
    private final RegenQueue regenQueue;

    public ConfirmCommand (RegenManager rm, RegenQueue q) {

        regenManager = rm;
        regenQueue = q;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        //if player clicks confirm within 15 seconds, start the regen
        //args[0] = uniqueCode
        //args[1] = same-seed/random-seed/supply-seed:
        //args[2] = optional reset-gamerules
        if (sender instanceof Player && args.length >= 2) {

            if (!regenQueue.containsWorldCode(UUID.fromString(args[0]))) {
                sender.sendMessage(MessageWriter.tooSlow());
                return false;
            }

            else {
                return regenManager.performRegen(sender, args);
            }
        }

        //if confirm is typed in console within 15 seconds, start the regen
        //args[0] = worldName
        else if (sender instanceof ConsoleCommandSender) {
            if (args.length == 1 && regenQueue.containsWorldName(args[0])) {
                return regenManager.performRegen(sender, args);
            }
        }
        return false;
    }
}
