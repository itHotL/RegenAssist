package com.gmail.artemis.the.gr8.regenassist.regen;

import com.gmail.artemis.the.gr8.regenassist.Main;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegenQueue {

    //stores a unique code for worlds that are queued to be regenerated
    //key = UUID, value = object containing worldname, seed-option and gamerules-option
    private final HashMap<UUID, RegenCandidate> regenQueue = new HashMap<>();
    private final Main plugin;

    public RegenQueue(Main p) {
        plugin = p;
    }

    //create and return a unique identifying key for the world and store relevant properties in the entry for 15 seconds
    public UUID createEntry(String worldName, String seedOption, String resetGameRules) {
        RegenCandidate regenCandidate = new RegenCandidate(worldName, seedOption, resetGameRules);
        UUID uniqueCode = UUID.randomUUID();

        regenQueue.put(uniqueCode, regenCandidate);
        startTimer(uniqueCode);
        return uniqueCode;
    }

    public boolean containsWorldName(String worldName) {
        return regenQueue.entrySet().stream().anyMatch(element -> element.getValue().getWorldName().equalsIgnoreCase(worldName));
    }

    public boolean containsWorldCode(UUID uuid) {
        return regenQueue.containsKey(uuid);
    }

    public UUID getWorldCode(String worldName) {
        Map.Entry<UUID, RegenCandidate> entry = regenQueue.entrySet().stream().filter(element ->
                element.getValue().getWorldName().equalsIgnoreCase(worldName)).findFirst().orElse(null);
        return !(entry == null) ? entry.getKey() : null;
    }

    //retrieve and remove an entry from the list
    public RegenCandidate removeEntry(UUID uuid) {
        return regenQueue.remove(uuid);
    }

    //start 15-second timer that removes unique code from the HashMap if it is still there
    private void startTimer(UUID uuid) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (containsWorldCode(uuid)) {
                RegenCandidate canceledCandidate = removeEntry(uuid);
                plugin.getLogger().info("15 seconds have passed, so " + canceledCandidate.getWorldName() + " has been removed from the regen queue.");
            }
        }, 300L);
    }
}



