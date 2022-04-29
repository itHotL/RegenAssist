package com.gmail.artemis.the.gr8.regenassist;

import com.gmail.artemis.the.gr8.regenassist.filehandlers.ConfigHandler;
import com.gmail.artemis.the.gr8.regenassist.filehandlers.RegenFileHandler;
import com.gmail.artemis.the.gr8.regenassist.portal.MyPortalManager;
import com.gmail.artemis.the.gr8.regenassist.portal.PortalResult;
import com.gmail.artemis.the.gr8.regenassist.regen.MVCoreHandler;
import com.gmail.artemis.the.gr8.regenassist.regen.RegenCandidate;
import com.gmail.artemis.the.gr8.regenassist.regen.RegenQueue;
import com.gmail.artemis.the.gr8.regenassist.utils.MessageWriter;
import com.gmail.artemis.the.gr8.regenassist.utils.TimeHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class RegenManager {

    private final ConfigHandler config;
    private final MVCoreHandler mv;
    private final MyPortalManager myPortalManager;
    private final RegenFileHandler regenFile;
    private final RegenQueue regenQueue;
    private final Main plugin;

    public RegenManager(ConfigHandler c, MVCoreHandler m, MyPortalManager pm, RegenFileHandler r, RegenQueue rq, Main p) {
        config = c;
        mv = m;
        myPortalManager = pm;
        regenFile = r;
        regenQueue = rq;
        plugin = p;
    }

    //calculate the time since the given world has last been reset
    public String getTimeSinceLastRegen(String worldName) {
        String timestamp = regenFile.getLastRegenTime(worldName);
        if (!timestamp.equalsIgnoreCase("")) {
            return TimeHandler.getStringTimeSinceLastRegen(timestamp);
        }

        else {
            return timestamp;
        }
    }

    //if commandsender is console, args[0] will be worldName
    //if commandsender is player, args[0] will be uuid (String)
    public boolean performRegen(CommandSender sender, String[] args) {
        UUID worldCode = null;
        if (sender instanceof Player) worldCode = UUID.fromString(args[0]);
        else if (sender instanceof ConsoleCommandSender) worldCode = regenQueue.getWorldCode(args[0]);

        RegenCandidate regenCandidate = regenQueue.removeEntry(worldCode);
        sender.sendMessage(MessageWriter.startRegenerating());

        if (!startRegen(regenCandidate)) {
            sender.sendMessage(MessageWriter.unknownError());
            return false;
        }

        else {
            finishRegen(sender, regenCandidate.getWorldName());
            return true;
        }
    }

    private boolean startRegen(RegenCandidate regenCandidate) {
        String worldName = regenCandidate.getWorldName();
        String seedOption = regenCandidate.getSeedOption();
        String resetGameRules = regenCandidate.getResetGameRules();

        boolean useNewSeed = (seedOption.equalsIgnoreCase("random-seed") || seedOption.startsWith("supply-seed:"));
        boolean randomSeed = seedOption.equalsIgnoreCase("random-seed");
        String seed = seedOption.startsWith("supply-seed:") ? seedOption.substring(12) : "";
        boolean keepGameRules = !resetGameRules.equalsIgnoreCase("reset-gamerules");

        //start the regen
        return mv.mvRegen(worldName, useNewSeed, randomSeed, seed, keepGameRules);
    }

    private void finishRegen(CommandSender sender, String worldName) {
        //check every second if the world has been loaded again after regenerating
        //if so: stop, relocate portal if there is any, and give feedback when the world has been loaded
        BukkitTask unloadedWorldsChecker = new BukkitRunnable() {
            public void run() {
                if (!mv.getUnloadedWorlds().contains(worldName)) {

                    this.cancel();
                    regenFile.writeToFile(worldName, TimeHandler.getCurrentTime());

                    boolean fixedPortal = false;
                    String portalName = null;
                    if (config.restorePortal()) {
                        if (myPortalManager.hasMVPortal(worldName)) {
                            sender.sendMessage(MessageWriter.portalFound());
                            PortalResult portal = myPortalManager.fixPotentialPortal(worldName);
                            if (portal != null) {
                                fixedPortal = true;
                                portalName = portal.getPortalName();
                                mv.setSpawn(worldName, portal.getSpawnLocation());
                            }
                        }
                    }
                    sender.sendMessage(MessageWriter.doneRegenerating(worldName, fixedPortal, portalName));
                }
            }
        }.runTaskTimer(plugin, 20L, 20L);

        //cancel the check if the world has not loaded yet after 30 seconds
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            if (!unloadedWorldsChecker.isCancelled()) {
                unloadedWorldsChecker.cancel();
                plugin.getLogger().info(MessageWriter.unknownRegenStatus(worldName));
            }
        }, 600L);
    }
}
