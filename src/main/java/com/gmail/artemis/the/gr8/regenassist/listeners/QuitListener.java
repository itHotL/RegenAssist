package com.gmail.artemis.the.gr8.regenassist.listeners;

import com.gmail.artemis.the.gr8.regenassist.filehandlers.PlayerFileHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    private final PlayerFileHandler playerFile;

    public QuitListener(PlayerFileHandler p) {
        playerFile = p;
    }

    @EventHandler
    public void OnPlayerQuit(PlayerQuitEvent quitEvent) {
        String playerName = quitEvent.getPlayer().getName();
        String worldName = quitEvent.getPlayer().getWorld().getName();
        playerFile.writeToFile(playerName, worldName);
    }
}
