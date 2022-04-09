package com.gmail.artemis.the.gr8.regenassist.commands;

import com.gmail.artemis.the.gr8.regenassist.Main;
import com.gmail.artemis.the.gr8.regenassist.filehandlers.RegenFileHandler;
import com.gmail.artemis.the.gr8.regenassist.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ConfirmCommand implements CommandExecutor {

    private final MultiverseHandler mv;
    private final RegenFileHandler regenFile;
    private final RegenQueue regenQueue;
    private final Main plugin;
    private String worldName;

    public ConfirmCommand (MultiverseHandler m, RegenFileHandler r, RegenQueue q, Main p) {
        mv = m;
        regenFile = r;
        regenQueue = q;
        plugin = p;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        //if player clicks confirm within 15 seconds, get worldName from HashMap and pass it on to the MultiverseHandler
        //args[0] = uniqueCode
        //args[1] = same-seed/random-seed/supply-seed:
        //args[2] = optional reset-gamerules
        if (sender instanceof Player && args.length >= 2) {

            if (!regenQueue.containsWorldCode(UUID.fromString(args[0]))) {
                sender.sendMessage(MessageWriter.tooSlow());
                return false;
            }

            else {
                boolean regen = startRegen(sender, args[0]);
                if (!regen) {
                    sender.sendMessage(MessageWriter.unknownError(worldName));
                    return false;
                }

                else {
                    return finishedRegen(sender, args[0]);
                }
            }
        }

        //if confirm is typed in console within 15 seconds, get uuid that corresponds to the worldname and start the regen
        else if (sender instanceof ConsoleCommandSender) {
            if (args.length == 1 && regenQueue.containsWorldName(args[0])) {
                return startRegen(sender, regenQueue.getWorldCode(args[0]).toString()) && finishedRegen(sender, args[0]);
            }
        }
        return false;
    }

    private boolean startRegen(CommandSender sender, String uuid) {
        RegenCandidate regenCandidate = regenQueue.removeEntry(UUID.fromString(uuid));
        worldName = regenCandidate.getWorldName();
        String seedOption = regenCandidate.getSeedOption();
        String resetGameRules = regenCandidate.getResetGameRules();

        //send message that regen is starting
        sender.sendMessage(MessageWriter.startRegenerating(worldName));

        boolean useNewSeed = (seedOption.equalsIgnoreCase("random-seed") || seedOption.startsWith("supply-seed:"));
        boolean randomSeed = seedOption.equalsIgnoreCase("random-seed");
        String seed = seedOption.startsWith("supply-seed:") ? seedOption.substring(12) : "";
        boolean keepGameRules = !resetGameRules.equalsIgnoreCase("reset-gamerules");

        //start the regen
        return mv.mvRegen(worldName, useNewSeed, randomSeed, seed, keepGameRules);
    }

    //check every second if the world has been loaded again after regenerating
    //stop + give feedback when the world has been loaded or after 30 seconds
    private boolean finishedRegen(CommandSender sender, String worldName) {
        BukkitTask unloadedWorldsChecker = new BukkitRunnable() {
            public void run() {
                if (!mv.getUnloadedWorlds().contains(worldName)) {
                    regenFile.writeToFile(worldName, TimeHandler.getCurrentTime());
                    sender.sendMessage(MessageWriter.doneRegenerating(worldName));
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 20L, 20L);

        if (!unloadedWorldsChecker.isCancelled()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                if (!unloadedWorldsChecker.isCancelled()) {
                    unloadedWorldsChecker.cancel();
                    plugin.getLogger().warning(MessageWriter.unknownRegenStatus(worldName));
                }
            }, 600L);
        }
        return true;
    }
}
