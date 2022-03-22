package com.gmail.artemis.the.gr8.regenassist.listeners;

import com.gmail.artemis.the.gr8.regenassist.Main;
import com.gmail.artemis.the.gr8.regenassist.utils.DataFileHandler;
import com.gmail.artemis.the.gr8.regenassist.utils.TimeHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final DataFileHandler data;
    private final Main plugin;

    public JoinListener(DataFileHandler d, Main p) {
        data = d;
        plugin = p;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent joinEvent) {
        String worldName = joinEvent.getPlayer().getWorld().getName();
        long lastPlayed = joinEvent.getPlayer().getLastPlayed();
        plugin.getLogger().info("Last played at: "+ TimeHandler.getLastPlayedTime(lastPlayed));
        plugin.getLogger().info("Time now is: "+ TimeHandler.getCurrentTime());
        String command = "[\"\",{\"text\":\"[\",\"color\":\"gray\"},{\"text\":\"RegenAssist\",\"color\":\"gold\"},{\"text\":\"] \",\"color\":\"gray\"},{\"text\":\"You are at: \",\"color\":\"white\"},{\"text\":\"[" + worldName + "]\",\"color\":\"gold\"}]";

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + joinEvent.getPlayer().getName() + " " + command);
            }
        }, 20L);
    }

}
