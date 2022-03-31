package com.gmail.artemis.the.gr8.regenassist.listeners;

import com.gmail.artemis.the.gr8.regenassist.Main;
import com.gmail.artemis.the.gr8.regenassist.filehandlers.ConfigHandler;
import com.gmail.artemis.the.gr8.regenassist.filehandlers.PlayerFileHandler;
import com.gmail.artemis.the.gr8.regenassist.filehandlers.RegenFileHandler;
import com.gmail.artemis.the.gr8.regenassist.utils.MessageWriter;
import com.gmail.artemis.the.gr8.regenassist.utils.MultiverseHandler;
import com.gmail.artemis.the.gr8.regenassist.utils.TimeHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final ConfigHandler config;
    private final MultiverseHandler mv;
    private final PlayerFileHandler playerFile;
    private final RegenFileHandler regenFile;
    private final Main plugin;

    public JoinListener(ConfigHandler c, MultiverseHandler m, PlayerFileHandler pl, RegenFileHandler r, Main p) {
        config = c;
        mv = m;
        playerFile = pl;
        regenFile = r;
        plugin = p;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent joinEvent) {

        Player player = joinEvent.getPlayer();

        //check if there are logout records of player
        if (playerFile.hasEntry(player.getName())) {
            String logoutWorldName = playerFile.getLogoutWorld(player.getName());

            //check if player is in a world that has last-regenerated records in the datafile
            if (regenFile.hasEntry(logoutWorldName)) {

                //check if lastPlayed is before a reset (param: unix time lastPlayed, datafile String lastRegen)
                if (TimeHandler.lastPlayedBeforeReset(player.getLastPlayed(), regenFile.getLastRegenTime(logoutWorldName))) {

                    //teleport player to spawn of a specified or the default world, and let them know what happened
                    String spawnWorldName = config.getSpawnWorldName();
                    Location spawnLocation = mv.isMVWorld(spawnWorldName) ? Bukkit.getServer().getWorld(spawnWorldName).getSpawnLocation() : joinEvent.getPlayer().getWorld().getSpawnLocation();
                    boolean teleport = player.teleport(spawnLocation);
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
