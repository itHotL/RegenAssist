package com.gmail.artemis.the.gr8.regenassist.listeners;

import com.gmail.artemis.the.gr8.regenassist.Main;
import com.gmail.artemis.the.gr8.regenassist.filehandlers.RegenFileHandler;
import com.gmail.artemis.the.gr8.regenassist.utils.MessageWriter;
import com.gmail.artemis.the.gr8.regenassist.utils.TimeHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final RegenFileHandler data;
    private final Main plugin;

    public JoinListener(RegenFileHandler d, Main p) {
        data = d;
        plugin = p;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent joinEvent) {
        String worldName = joinEvent.getPlayer().getWorld().getName();
        Location location = joinEvent.getPlayer().getLocation();
        plugin.getLogger().info("Player is in: "+worldName+" with location: "+location);

        //check if player is in a world that has last-regenerated records in the datafile
        if (data.hasDataEntry(worldName)) {

            //check if lastPlayed is before a reset (param: unix time lastPlayed, datafile String lastRegen)
            if (TimeHandler.lastPlayedBeforeReset(joinEvent.getPlayer().getLastPlayed(), data.getLastRegenTime(worldName))) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + joinEvent.getPlayer().getName() + MessageWriter.teleportMessage());
                    }
                }, 20L);
            }
        }
    }
}
