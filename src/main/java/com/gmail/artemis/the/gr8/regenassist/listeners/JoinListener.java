package com.gmail.artemis.the.gr8.regenassist.listeners;

import com.gmail.artemis.the.gr8.regenassist.Main;
import com.gmail.artemis.the.gr8.regenassist.filehandlers.PlayerFileHandler;
import com.gmail.artemis.the.gr8.regenassist.filehandlers.RegenFileHandler;
import com.gmail.artemis.the.gr8.regenassist.utils.MessageWriter;
import com.gmail.artemis.the.gr8.regenassist.utils.TimeHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final PlayerFileHandler playerFile;
    private final RegenFileHandler regenFile;
    private final Main plugin;

    public JoinListener(PlayerFileHandler pl, RegenFileHandler r, Main p) {
        playerFile = pl;
        regenFile = r;
        plugin = p;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent joinEvent) {

        Player player = joinEvent.getPlayer();

        //check if there are logout records of player
        if (playerFile.hasEntry(player.getName())) {
            String worldName = playerFile.getLogoutWorld(player.getName());

            //check if player is in a world that has last-regenerated records in the datafile
            if (regenFile.hasEntry(worldName)) {

                //check if lastPlayed is before a reset (param: unix time lastPlayed, datafile String lastRegen)
                if (TimeHandler.lastPlayedBeforeReset(player.getLastPlayed(), regenFile.getLastRegenTime(worldName))) {

                    //teleport player to spawn, and let them know what happened
                    boolean teleport = player.teleport(joinEvent.getPlayer().getWorld().getSpawnLocation());
                    if (teleport) {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + MessageWriter.teleportMessage());
                            }
                        }, 20L);
                    }
                }
            }
        }
    }
}
